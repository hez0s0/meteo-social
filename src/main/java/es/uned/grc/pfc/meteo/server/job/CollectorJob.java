package es.uned.grc.pfc.meteo.server.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.uned.grc.pfc.meteo.server.job.station.ICollector;
import es.uned.grc.pfc.meteo.server.job.station.IParser;
import es.uned.grc.pfc.meteo.server.job.station.IStationPlugin;
import es.uned.grc.pfc.meteo.server.job.station.RawObservation;
import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.Parameter;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.model.Variable;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IParameterPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.util.IServerConstants;

@Component
public class CollectorJob {
   
   private static final long TOO_OLD_MINUTES = 7 * 24 * 60 * IServerConstants.ONE_MINUTE;

   protected static Logger logger = LoggerFactory.getLogger (CollectorJob.class);
   
   @Autowired
   private IStationPersistence stationPersistence = null;
   @Autowired
   private IParameterPersistence parameterPersistence = null;
   @Autowired
   private IObservationPersistence observationPersistence = null;

   /**
    * To be executed periodically
    */
   @Transactional (propagation = Propagation.REQUIRED)
   @Scheduled (fixedRate = IServerConstants.COLLECTION_POLLING_TIME)
   public synchronized void timeout () {
      logger.info ("Executing task {}", getClass ().getSimpleName ());
      try {
         List <Station> stations = stationPersistence.findAll ();
         for (Station station : stations) {
            collect (station.getId ());
         }
      } catch (Exception e) {
         logger.error ("Error collecting observations", e);
      }
   }
   
   /**
    * Collects the next bulk of observations from the local station,
    * stores them in the database and pings the qualityJob for their processing
    */
   @Transactional (propagation = Propagation.REQUIRED)
   public void collect (int stationId) {
      Station station = null;
      ICollector collector = null;
      IParser parser = null;
      IStationPlugin stationPlugin = null;
      Map <String, String> configuredParameters = null;
      List <Observation> result = new ArrayList <Observation> ();
      
      synchronized (IJobConstants.STATION_ACCESS) {
         station = stationPersistence.findById (stationId);
         if (station.getStationModel () == null) {
            logger.info ("Station {} not configured (no stationModel)", station.getId ());
         } else {
            stationPlugin = stationPersistence.getStationPlugin (station);
            if (stationPlugin == null) {
               throw new RuntimeException ("unable to obtain station plugin, please review the system configuration");
            }
            if (stationPlugin.getStationModelDescriptor () == null) {
               throw new RuntimeException (String.format ("unable to obtain a descriptor for '%s' station, please review the station plugin code", stationPlugin));
            }
            
            collector = stationPlugin.getCollector ();
            if (collector == null) {
               throw new RuntimeException (String.format ("unable to obtain collector for '%s' station, please review the station plugin code", stationPlugin.getStationModelDescriptor ().getName ()));
            }
            parser = stationPlugin.getParser ();
            if (parser == null) {
               throw new RuntimeException (String.format ("unable to obtain parser for '%s' station, please review the station plugin code", stationPlugin.getStationModelDescriptor ().getName ()));
            }
            
            configuredParameters = asMap (station.getParameters ());
            
            //iterate until every pending observation has been obtained
            Date nextObservationPeriod = getNextObservationPeriod (station, stationPlugin);
            while (nextObservationPeriod.getTime () < new Date ().getTime ()) {
               if (collect (station, nextObservationPeriod, configuredParameters, collector, parser, result)) {
                  logger.debug ("Block of observations appended. Aggregated results {}", result.size ());
                  
                  station.setLastCollectedPeriod (nextObservationPeriod);
                  station = stationPersistence.merge (station);
               }
               
               //true if there are more observations
               nextObservationPeriod.setTime (nextObservationPeriod.getTime () + (stationPlugin.getObservationPeriod () * IServerConstants.ONE_MINUTE));
            }
            logger.info ("End of collection. Aggregated results {}", result.size ());
            logger.info ("{} observations sent for station", result.size (), station.getId ());
         }
      }
   }
   
   /**
    * Collects and stores one specific block
    */
   @Transactional (propagation = Propagation.REQUIRED)
   private boolean collect (Station station, Date nextObservationPeriod, Map <String, String> configuredParameters, ICollector collector, IParser parser, List <Observation> result) {
      List <Observation> observations = null;
      byte [] block = null;
      Date now = null;
      
      now = new Date ();
      logger.info ("Obtaining observations for station {} at time {}", station.getName (), nextObservationPeriod);
      
      //read a raw block ob observations from the station at the given timestamp
      block = collector.getObservationBlock (nextObservationPeriod, configuredParameters);
      
      //parse them into a well-known observation structure
      observations = toObservations (parser.parseBlock (block), station, now);
      
      if (!observations.isEmpty ()) {
         //persist the collected info and update the station
         logger.info ("Storing {} observations for station {} at time {}", observations.size (), station.getName (), nextObservationPeriod);
         store (observations);
         
         result.addAll (observations);
      }
      
      return !observations.isEmpty ();
   }

   private void store (List <Observation> observations) {
      for (Observation observation : observations) {
         observationPersistence.save (observation);
      }
   }

   private List <Observation> toObservations (List <RawObservation> parseBlock, Station station, Date now) {
      List <Observation> observations = new ArrayList <Observation> (parseBlock != null ? parseBlock.size () : 0);
      
      if (parseBlock != null) {
         for (RawObservation rawObservation : parseBlock) {
            if (rawObservation != null) {
               observations.add (toObservation (rawObservation, station, now));
            }
         }
      }
      return observations;
   }

   private Observation toObservation (RawObservation rawObservation, Station station, Date now) {
      Observation observation = new Observation ();

      observation.setObserved (rawObservation.getObserved ());
      observation.setReceived (now);
      observation.setStation (station);
      observation.setValue (rawObservation.getValue ());
      observation.setVariable (findByAcronyn (rawObservation.getVariableName (), station.getVariables ()));
      
      return observation;
   }

   private Variable findByAcronyn (String acronym, Set <Variable> variables) {
      for (Variable variable : variables) {
         if (variable.getAcronym ().equalsIgnoreCase (acronym)) {
            return variable;
         }
      }
      throw new RuntimeException (String.format ("Variable '%s' not found in the station", acronym));
   }

   private Date getNextObservationPeriod (Station station, IStationPlugin stationPlugin) {
      Calendar nextPeriod = null;
      if (station.getLastCollectedPeriod () == null || isTooOld (station.getLastCollectedPeriod ())) {
         //there was never any collection (or the last one happened a too long time ago) let us start with X minutes ago
         nextPeriod = Calendar.getInstance ();
         nextPeriod.set(Calendar.MINUTE, (nextPeriod.get(Calendar.MINUTE) / stationPlugin.getObservationPeriod ()) * stationPlugin.getObservationPeriod ());
         nextPeriod.set(Calendar.SECOND, 0);
         nextPeriod.set(Calendar.MILLISECOND, 0);
      } else {
         //there was a previous collection: start there
         nextPeriod = Calendar.getInstance ();
         nextPeriod.setTimeInMillis (station.getLastCollectedPeriod ().getTime () + stationPlugin.getObservationPeriod () * IServerConstants.ONE_MINUTE);
      }
      return nextPeriod.getTime ();
   }
   
   private boolean isTooOld (Date lastCollectedPeriod) {
      return (new Date ().getTime () - lastCollectedPeriod.getTime () > TOO_OLD_MINUTES);
   }

   @Transactional (propagation = Propagation.REQUIRED)
   private Map <String, String> asMap (Set <Parameter> parameters) {
      Map <String, String> configuredParameters = new HashMap <String, String> (parameters.size ());
      
      for (Parameter parameter : parameters) {
         if (parameter.getValue () != null) {
            configuredParameters.put (parameter.getName (), parameter.getValue ());
         } else {
            configuredParameters.put (parameter.getName (), parameter.getDefaultValue ());
         }
      }
      
      return configuredParameters;
   }
}
