package es.uned.grc.pfc.meteo.server.job;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.model.StationVariable;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationVariablePersistence;
import es.uned.grc.pfc.meteo.server.util.IServerConstants;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

@Component
public class QualityJob {

   private static final int UNCONTROLLED_BLOCK = 100;

   protected static Logger logger = LoggerFactory.getLogger (QualityJob.class);

   @Autowired
   private IStationPersistence stationPersistence = null;
   @Autowired
   private IStationVariablePersistence stationVariablePersistence = null;
   @Autowired
   private IObservationPersistence observationPersistence = null;

   /**
    * To be executed periodically
    */
   @Transactional (propagation = Propagation.REQUIRED)
   @Scheduled (fixedRate = IServerConstants.QUALITY_POLLING_TIME)
   public synchronized void timeout () {
      List <Observation> observations = null;
      
      logger.info ("Executing task {}", getClass ().getSimpleName ());
      try {
         synchronized (IJobConstants.STATION_ACCESS) {
            List <Station> stations = stationPersistence.findAll ();
            for (Station station : stations) {
               observations = observationPersistence.getUncontrolled (station.getId (), UNCONTROLLED_BLOCK);
               
               if (observations != null && !observations.isEmpty ()) {
                  processQuality (station, observations);
               }
               logger.info ("Quality processed for {} observations of station {}", observations != null ? observations.size () : 0, station.getId ());
            }
         }
      } catch (Exception e) {
         logger.error ("Error computing quality control of observations", e);
      }
   }

   @Transactional (propagation = Propagation.REQUIRED)
   private void processQuality (Station station, List <Observation> observations) {
      boolean ok = false;
      StringBuffer warning = null;
      Date now = new Date ();
      StationVariable stationVariable = null;
      
      try {
         logger.info ("Quality to be checked on {} observations", observations.size ());

         for (Observation observation : observations) {
            ok = false;
            warning = new StringBuffer ();
            stationVariable = stationVariablePersistence.findStationVariable (station.getId (), observation.getVariable ().getId (), station.getStationVariables ());
            
            if (!StringUtils.isEmpty (observation.getValue ())) {
               if (stationVariable.getMaximum () != null || stationVariable.getMinimum () != null) {
                  //use provided config for control
                  ok = processQuality (observation.getValue (), 
                                       stationVariable.getMinimum (), 
                                       stationVariable.getMaximum (),
                                       stationVariable.getPhysicalMinimum (), 
                                       stationVariable.getPhysicalMaximum (),
                                       warning);
               } else if (stationVariable.getDefaultMaximum () != null || stationVariable.getDefaultMinimum () != null) {
                  //use the default values
                  ok = processQuality (observation.getValue (), 
                                       stationVariable.getDefaultMinimum (), 
                                       stationVariable.getDefaultMaximum (),
                                       stationVariable.getPhysicalMinimum (), 
                                       stationVariable.getPhysicalMaximum (),
                                       warning);
               } else if (stationVariable.getPhysicalMaximum () != null || stationVariable.getPhysicalMinimum () != null) {
                  //use the physical values only
                  ok = processQuality (observation.getValue (), 
                                       null, 
                                       null,
                                       stationVariable.getPhysicalMinimum (), 
                                       stationVariable.getPhysicalMaximum (),
                                       warning);
               } else {
                  //nothing configured, the variable simply requires no quality control
                  ok = true;
               }
            } else {
               //empty observations are suspicious (sensor errors)
               ok = false;
               addWarning ("El valor está vacío", warning);
            }
            
            //the quality control was conducted, store it
            observation.setControlled (now);
            observation.setQuality (ok);
            observation.setWarning (warning.toString ());
            observationPersistence.merge (observation);
            logger.info ("Quality result for observation {} is {}", observation.getId (), observation.getQuality ());
         }
      } catch (Exception e) {
         logger.error ("Error collecting observations", e);
      }
   }

   private boolean processQuality (String stringValue, Double minimum, Double maximum, Double physicalMinimum, Double physicalMaximum, StringBuffer warning) {
      Double value = null;
      boolean ok = true;
      
      try {
         value = Double.valueOf (stringValue);
         if (minimum != null && value < minimum) {
            ok = false;
            addWarning (String.format ("El valor '%s' no debería ser menor de '%s'", value, minimum), warning);
         }
         if (maximum != null && value > maximum) {
            ok = false;
            addWarning (String.format ("El valor '%s' no debería ser mayor de '%s'", value, maximum), warning);
         }
         if (physicalMinimum != null && value < physicalMinimum) {
            ok = false;
            addWarning (String.format ("El valor '%s' no debería ser menor de '%s'", value, physicalMinimum), warning);
         }
         if (physicalMaximum != null && value > physicalMaximum) {
            ok = false;
            addWarning (String.format ("El valor '%s' no debería ser mayor de '%s'", value, physicalMaximum), warning);
         }
      } catch (Exception e) {
         //if not a double, it should not have limits, so this is suspicious (sensor data corruption)
         ok = false;
         addWarning (String.format ("El valor '%s' no es numérico", value), warning);
      }
      if (ok) {
         warning.append (String.format ("mínimo físico=%s; máximo físico=%s, mínimo=%s; máximo=%s", 
                                        physicalMinimum != null ? physicalMinimum : "ninguno", 
                                        physicalMaximum != null ? physicalMaximum : "ninguno",
                                        minimum != null ? minimum : "ninguno", 
                                        maximum != null ? maximum : "ninguno"));
      }
      
      return ok;
   }

   private void addWarning (String message, StringBuffer warning) {
      if (warning.length () > 0) {
         warning.append (ISharedConstants.WORD_LIST_SEPARATOR);
         warning.append (ISharedConstants.WORD_SEPARATOR);
      }
      warning.append (message);
   }
}
