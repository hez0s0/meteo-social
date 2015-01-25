package es.uned.grc.pfc.meteo.server.collector;

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
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.util.IServerConstants;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

@Component
public class QualityJob {

   protected static Logger logger = LoggerFactory.getLogger (QualityJob.class);

   @Autowired
   private IStationPersistence stationPersistence = null;
   @Autowired
   private IObservationPersistence observationPersistence = null;

   /**
    * To be executed periodically
    */
   @Transactional (propagation = Propagation.REQUIRED)
   @Scheduled (fixedRate = IServerConstants.QUALITY_POLLING_TIME)
   public synchronized void timeout () {
      Station station = null;
      List <Observation> observations = null;
      
      logger.info ("Executing task {}", getClass ().getSimpleName ());
      try {
         station = stationPersistence.getOwnStation ();
         if (station == null) {
            throw new RuntimeException ("unable to obtain own station, please review the system configuration");
         }
         observations = observationPersistence.getUncontrolled ();
         
         if (observations != null && !observations.isEmpty ()) {
            processQuality (station, observations);
         }
         logger.info ("Quality processed for {} observations", observations != null ? observations.size () : 0);
      } catch (Exception e) {
         logger.error ("Error computing quality control of observations", e);
      }
   }

   @Transactional (propagation = Propagation.REQUIRED)
   private void processQuality (Station station, List <Observation> observations) {
      boolean ok = false;
      StringBuffer warning = null;
      Date now = new Date ();
      
      try {
         logger.info ("Quality to be checked on {} observations", observations.size ());

         for (Observation observation : observations) {
            ok = false;
            warning = new StringBuffer ();
            
            if (!StringUtils.isEmpty (observation.getValue ())) {
               if (observation.getVariable ().getMaximum () != null || observation.getVariable ().getMinimum () != null) {
                  //use provided config for control
                  ok = processQuality (observation.getValue (), 
                                       observation.getVariable ().getMinimum (), 
                                       observation.getVariable ().getMaximum (),
                                       observation.getVariable ().getPhysicalMinimum (), 
                                       observation.getVariable ().getPhysicalMaximum (),
                                       warning);
               } else if (observation.getVariable ().getDefaultMaximum () != null || observation.getVariable ().getDefaultMinimum () != null) {
                  //use the default values
                  ok = processQuality (observation.getValue (), 
                                       observation.getVariable ().getDefaultMinimum (), 
                                       observation.getVariable ().getDefaultMaximum (),
                                       observation.getVariable ().getPhysicalMinimum (), 
                                       observation.getVariable ().getPhysicalMaximum (),
                                       warning);
               } else if (observation.getVariable ().getPhysicalMaximum () != null || observation.getVariable ().getPhysicalMinimum () != null) {
                  //use the physical values only
                  ok = processQuality (observation.getValue (), 
                                       null, 
                                       null,
                                       observation.getVariable ().getPhysicalMinimum (), 
                                       observation.getVariable ().getPhysicalMaximum (),
                                       warning);
               } else {
                  //nothing configured, the variable simply requires no quality control
                  ok = true;
               }
            } else {
               //empty observations are suspicious (sensor errors)
               ok = false;
               addWarning ("Value is empty", warning);
            }
            
            //the quality control was conducted, store it
            observation.setControlled (now);
            observation.setQuality (ok);
            if (!ok) {
               observation.setWarning (warning.toString ());
            }
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
            addWarning (String.format ("Value '%s' should not be less than '%s'", value, minimum), warning);
         }
         if (maximum != null && value > maximum) {
            ok = false;
            addWarning (String.format ("Value '%s' should not be more than '%s'", value, maximum), warning);
         }
         if (minimum != null && value < physicalMinimum) {
            ok = false;
            addWarning (String.format ("Value '%s' cannot be less than '%s'", value, physicalMinimum), warning);
         }
         if (maximum != null && value > physicalMaximum) {
            ok = false;
            addWarning (String.format ("Value '%s' cannot be more than '%s'", value, physicalMaximum), warning);
         }
      } catch (Exception e) {
         //if not a double, it should not have limits, so this is suspicious (sensor data corruption)
         ok = false;
         addWarning (String.format ("Value '%s' is not a number", value, physicalMaximum), warning);
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
