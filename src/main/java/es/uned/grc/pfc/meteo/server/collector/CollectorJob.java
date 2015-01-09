package es.uned.grc.pfc.meteo.server.collector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import es.uned.grc.pfc.meteo.server.collector.station.ICollector;
import es.uned.grc.pfc.meteo.server.collector.station.IParser;
import es.uned.grc.pfc.meteo.server.collector.station.IStationPlugin;
import es.uned.grc.pfc.meteo.server.collector.station.RawObservation;
import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.Parameter;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.model.Variable;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IParameterPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.util.IServerConstants;

//@Stateless
public class CollectorJob {
   
   private static final long ONE_MINUTE = 60 * 1000;

   private static final long TOO_OLD_MINUTES = 7 * 24 * 60 * ONE_MINUTE;

   protected static Logger logger = LoggerFactory.getLogger (CollectorJob.class);   
   
   @Resource (mappedName = IServerConstants.OBSERVATIONS_QUEUE_NAME)
   private Queue observationQueue = null;

   @Autowired
   private JmsTemplate jmsTemplate = null;
   
   @Autowired
   private IStationPersistence stationPersistence = null;
   @Autowired
   private IParameterPersistence parameterPersistence = null;
   @Autowired
   private IObservationPersistence observationPersistence = null;
   @Autowired
   private IStationPlugin stationPlugin = null;

   @Scheduled (fixedRate = 60000)
   public void timeout () {
      List <Observation> observations = null;
      try {
         observations = collect ();
         
         if (observations != null && !observations.isEmpty ()) {
            jmsTemplate.convertAndSend (observations);
            logger.info ("%s observations sent", observations.size ());
         }
      } catch (Exception e) {
         logger.error ("Error collecting observations", e);
      }
   }
   
   public List <Observation> collect () {
      Station station = null;
      Date nextObservationPeriod = null;
      ICollector collector = null;
      IParser parser = null;
      Map <String, String> configuredParameters = null;
      List <Observation> observations = null;
      byte [] block = null;
      boolean done = false;
      Date now = null;
      List <Observation> result = new ArrayList <Observation> ();
      
      station = stationPersistence.getOwnStation ();
      if (station == null) {
         throw new RuntimeException ("unable to obtain own station, please review the system configuration");
      }
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
      
      while (!done) {
         now = new Date ();
         nextObservationPeriod = getNextObservationPeriod (station);
         logger.info ("Obtaining observations for station %s at time %s", station.getName (), nextObservationPeriod);
         
         block = collector.getObservationBlock (nextObservationPeriod, configuredParameters);
         
         observations = toObservations (parser.parseBlock (block), station, now);
         
         logger.info ("Storing %s observations for station %s at time %s", observations.size (), station.getName (), nextObservationPeriod);
         store (observations);
         
         result.addAll (observations);
         
         station.setLastCollectedPeriod (nextObservationPeriod);
         stationPersistence.merge (station);
         
         done = nextObservationPeriod.getTime () + stationPlugin.getObservationPeriod () * ONE_MINUTE >= new Date ().getTime ();
      }
      
      return result;
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
      return null;
   }

   private Observation toObservation (RawObservation rawObservation, Station station, Date now) {
      Observation observation = new Observation ();

      observation.setObserved (rawObservation.getObserved ());
      observation.setReceived (now);
      observation.setStation (station);
      observation.setValue (rawObservation.getValue ());
      observation.setVariable (findByName (rawObservation.getVariableName (), station.getVariables ()));
      
      return observation;
   }

   private Variable findByName (String variableName, List <Variable> variables) {
      for (Variable variable : variables) {
         if (variable.getName ().equalsIgnoreCase (variableName)) {
            return variable;
         }
      }
      throw new RuntimeException (String.format ("Variable '%' not found in the station", variableName));
   }

   private Date getNextObservationPeriod (Station station) {
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
         nextPeriod.setTimeInMillis (station.getLastCollectedPeriod ().getTime () + stationPlugin.getObservationPeriod () * ONE_MINUTE);
      }
      return nextPeriod.getTime ();
   }
   
   private boolean isTooOld (Date lastCollectedPeriod) {
      return (new Date ().getTime () - lastCollectedPeriod.getTime () > TOO_OLD_MINUTES);
   }

   private Map <String, String> asMap (List <Parameter> parameters) {
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
