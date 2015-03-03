package es.uned.grc.pfc.meteo.server.collector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.model.Variable;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IVariablePersistence;
import es.uned.grc.pfc.meteo.server.util.IServerConstants;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

@Component
public class DerivedJob {

   protected static Logger logger = LoggerFactory.getLogger (DerivedJob.class);

   private enum DeriveType {MINIMUM, MAXIMUM, AVERAGE};
   
   @Autowired
   private IStationPersistence stationPersistence = null;
   @Autowired
   private IObservationPersistence observationPersistence = null;
   @Autowired
   private IVariablePersistence variablePersistence = null;

   /**
    * To be executed periodically
    */
   @Transactional (propagation = Propagation.REQUIRED)
   @Scheduled (fixedRate = IServerConstants.DERIVED_POLLING_TIME)
   public synchronized void timeout () {
      Station station = null;
      List <Observation> observations = null;
      
      logger.info ("Executing task {}", getClass ().getSimpleName ());
      try {
         synchronized (IJobConstants.STATION_ACCESS) {
            station = stationPersistence.getOwnStation ();
            if (station == null) {
               throw new RuntimeException ("unable to obtain own station, please review the system configuration");
            }
            station = checkInternalVariables (station);
            
            observations = observationPersistence.getUnderived ();
            
            if (observations != null && !observations.isEmpty ()) {
               createUpdateDerived (station, observations);
            }
            logger.info ("Derived calculation processed for {} observations", observations != null ? observations.size () : 0);
         }
      } catch (Exception e) {
         logger.error ("Error calculation derived observations", e);
      }
   }

   /**
    * Generates (or updates) the derived variables
    */
   @Transactional (propagation = Propagation.REQUIRED)
   private void createUpdateDerived (Station station, List <Observation> observations) {
      DeriveType deriveType = null;
      Date now = new Date ();
      Date [] range = new Date [2];
      Map <String, List <Observation>> existingObservationsMap = new HashMap <String, List <Observation>> ();
      
      //iterate all observations
      for (Observation observation : observations) {
         //for every observation, derive a new observation for every internal variable
         for (Variable variable : station.getVariables ()) {
            if (variable.getInternal ()) {
               range [0] = null;
               range [1] = null;
               //depending on the variable, calculate the deriveType and date range
               switch (variable.getAcronym ()) {
                  case IServerConstants.HOUR_MINIMUM:
                     deriveType = DeriveType.MINIMUM;
                     range [0] = getHourIni (observation.getObserved ());
                     range [1] = getHourEnd (observation.getObserved ());
                     break;
                  case IServerConstants.HOUR_MAXIMUM:
                     deriveType = DeriveType.MAXIMUM;
                     range [0] = getHourIni (observation.getObserved ());
                     range [1] = getHourEnd (observation.getObserved ());
                     break;
                  case IServerConstants.HOUR_AVERAGE:
                     deriveType = DeriveType.AVERAGE;
                     range [0] = getHourIni (observation.getObserved ());
                     range [1] = getHourEnd (observation.getObserved ());
                     break;
                  case IServerConstants.MORNING_MINIMUM:
                     deriveType = DeriveType.MINIMUM;
                     range [0] = getMorningIni (observation.getObserved ());
                     range [1] = getMorningEnd (observation.getObserved ());
                     break;
                  case IServerConstants.MORNING_MAXIMUM:
                     deriveType = DeriveType.MAXIMUM;
                     range [0] = getMorningIni (observation.getObserved ());
                     range [1] = getMorningEnd (observation.getObserved ());
                     break;
                  case IServerConstants.MORNING_AVERAGE:
                     deriveType = DeriveType.AVERAGE;
                     range [0] = getMorningIni (observation.getObserved ());
                     range [1] = getMorningEnd (observation.getObserved ());
                     break;
                  case IServerConstants.AFTERNOON_MINIMUM:
                     deriveType = DeriveType.MINIMUM;
                     range [0] = getAfternoonIni (observation.getObserved ());
                     range [1] = getAfternoonEnd (observation.getObserved ());
                     break;
                  case IServerConstants.AFTERNOON_MAXIMUM:
                     deriveType = DeriveType.MAXIMUM;
                     range [0] = getAfternoonIni (observation.getObserved ());
                     range [1] = getAfternoonEnd (observation.getObserved ());
                     break;
                  case IServerConstants.AFTERNOON_AVERAGE:
                     deriveType = DeriveType.AVERAGE;
                     range [0] = getAfternoonIni (observation.getObserved ());
                     range [1] = getAfternoonEnd (observation.getObserved ());
                     break;
                  case IServerConstants.NIGHT_MINIMUM:
                     deriveType = DeriveType.MINIMUM;
                     range [0] = getNightIni (observation.getObserved ());
                     range [1] = getNightEnd (observation.getObserved ());
                     break;
                  case IServerConstants.NIGHT_MAXIMUM:
                     deriveType = DeriveType.MAXIMUM;
                     range [0] = getNightIni (observation.getObserved ());
                     range [1] = getNightEnd (observation.getObserved ());
                     break;
                  case IServerConstants.NIGHT_AVERAGE:
                     deriveType = DeriveType.AVERAGE;
                     range [0] = getNightIni (observation.getObserved ());
                     range [1] = getNightEnd (observation.getObserved ());
                     break;
                  case IServerConstants.DAY_MINIMUM:
                     deriveType = DeriveType.MINIMUM;
                     range [0] = getDayIni (observation.getObserved ());
                     range [1] = getDayEnd (observation.getObserved ());
                     break;
                  case IServerConstants.DAY_MAXIMUM:
                     deriveType = DeriveType.MAXIMUM;
                     range [0] = getDayIni (observation.getObserved ());
                     range [1] = getDayEnd (observation.getObserved ());
                     break;
                  case IServerConstants.DAY_AVERAGE:
                     deriveType = DeriveType.AVERAGE;
                     range [0] = getDayIni (observation.getObserved ());
                     range [1] = getDayEnd (observation.getObserved ());
                     break;
                  case IServerConstants.MONTH_MINIMUM:
                     deriveType = DeriveType.MINIMUM;
                     range [0] = getMonthIni (observation.getObserved ());
                     range [1] = getMonthEnd (observation.getObserved ());
                     break;
                  case IServerConstants.MONTH_MAXIMUM:
                     deriveType = DeriveType.MAXIMUM;
                     range [0] = getMonthIni (observation.getObserved ());
                     range [1] = getMonthEnd (observation.getObserved ());
                     break;
                  case IServerConstants.MONTH_AVERAGE:
                     deriveType = DeriveType.AVERAGE;
                     range [0] = getMonthIni (observation.getObserved ());
                     range [1] = getMonthEnd (observation.getObserved ());
                     break;
               }
            }
            
            //actually create and save the derived observation
            if (range [0] != null && range [1] != null) {
               derive (observation, variable, range, deriveType, existingObservationsMap, now);
            }
         }
      }
   }

   /**
    * Checks if the internal (derived) variables already exist in the database or not
    */
   @Transactional (propagation = Propagation.REQUIRED)
   private Station checkInternalVariables (Station station) {
      checkInternalVariable (station, IServerConstants.HOUR_MINIMUM, "Hour minimum", "Minimum value within the hour", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.HOUR_AVERAGE, "Hour average", "Average value within the hour", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.HOUR_MAXIMUM, "Hour maximum", "Maximum value within the hour", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.MORNING_MINIMUM, "Morning minimum", "Minimum value within the morning (06-14)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.MORNING_AVERAGE, "Morning average", "Average value within the morning (06-14)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.MORNING_MAXIMUM, "Morning maximum", "Maximum value within the morning (06-14)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.AFTERNOON_MINIMUM, "Afternoon minimum", "Minimum value within the afternoon (14-22)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.AFTERNOON_AVERAGE, "Afternoon average", "Average value within the afternoon (14-22)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.AFTERNOON_MAXIMUM, "Afternoon maximum", "Maximum value within the afternoon (14-22)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.NIGHT_MINIMUM, "Night minimum", "Minimum value within the night (22-06)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.NIGHT_AVERAGE, "Night average", "Average value within the night (22-06)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.NIGHT_MAXIMUM, "Night maximum", "Maximum value within the night (22-06)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.DAY_MINIMUM, "Day minimum", "Minimum value within the day", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.DAY_AVERAGE, "Day average", "Average value within the day", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.DAY_MAXIMUM, "Day maximum", "Maximum value within the day", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.MONTH_MINIMUM, "Month minimum", "Minimum value within the month", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.MONTH_AVERAGE, "Month average", "Average value within the month", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.MONTH_MAXIMUM, "Month maximum", "Maximum value within the month", 0, 0, ISharedConstants.GraphType.NONE);
      return stationPersistence.merge (station);
   }

   @Transactional (propagation = Propagation.REQUIRED)
   private void checkInternalVariable (Station station, String acronym, String name, String description, int position, int displayGroup, ISharedConstants.GraphType graphType) {
      Variable internalVariable = findInternalByAcronyn (acronym, station.getVariables ());
      
      if (internalVariable == null) {
         //it not in DB yet, create it new
         internalVariable = new Variable ();
         internalVariable.setAcronym (acronym);
         internalVariable.setName (name);
         internalVariable.setDescription (description);
         internalVariable.setInternal (true);
         internalVariable.setStation (station);
         internalVariable.setPosition (position);
         internalVariable.setDisplayGroup (displayGroup);
         internalVariable.setGraphType (graphType);
         variablePersistence.save (internalVariable);
         
         logger.info ("Automatically creating internal variable {}", internalVariable.getAcronym ());
         
         station.getVariables ().add (internalVariable);
      }
   }

   @Transactional (propagation = Propagation.REQUIRED)
   private void derive (Observation observation, Variable variable, Date [] range, DeriveType deriveType, Map <String, List <Observation>> existingObservationsMap, Date now) {
      List <Observation> rangeObservations = null;
      Observation derivedObservation = null;
      SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMdd");
      String monthKey = sdf.format (observation.getObserved ());
      List <Observation> monthObservations = existingObservationsMap.get (monthKey);
      
      if (monthObservations == null) {
         monthObservations = getObservations (getMonthIni (observation.getObserved ()), getMonthEnd (observation.getObserved ()));
         existingObservationsMap.put (monthKey, monthObservations);
      }
      rangeObservations = getSubRange (observation, monthObservations, range);
      
      derivedObservation = findDerived (observation, variable, range);
      
      if (derivedObservation == null) {
         derivedObservation = new Observation ();
         //TODO all other attributes
      }
      derivedObservation.setValue (calculate (rangeObservations, deriveType));
      derivedObservation = observationPersistence.saveOrMerge (derivedObservation);

      observation.setDerived (now);
      observation = observationPersistence.saveOrMerge (observation);
   }
   
   private String calculate (List <Observation> rangeObservations, DeriveType deriveType) {
      // TODO Auto-generated method stub
      return null;
   }

   private Observation findDerived (Observation observation, Variable variable, Date [] range) {
      // TODO Auto-generated method stub
      return null;
   }

   private List <Observation> getSubRange (Observation observation, List <Observation> monthObservations, Date [] range) {
      // TODO Auto-generated method stub
      return null;
   }

   private List <Observation> getObservations (Date monthIni, Date monthEnd) {
      // TODO Auto-generated method stub
      return new ArrayList <Observation> (0);
   }

   private Variable findInternalByAcronyn (String acronym, Set <Variable> variables) {
      for (Variable variable : variables) {
         if (variable.getAcronym ().equalsIgnoreCase (acronym) && variable.getInternal ()) {
            return variable;
         }
      }
      return null;
   }

   private Date getHourEnd (Date observed) {
      // TODO Auto-generated method stub
      return null;
   }

   private Date getHourIni (Date observed) {
      // TODO Auto-generated method stub
      return null;
   }

   private Date getMorningEnd (Date observed) {
      // TODO Auto-generated method stub
      return null;
   }

   private Date getMorningIni (Date observed) {
      // TODO Auto-generated method stub
      return null;
   }

   private Date getAfternoonEnd (Date observed) {
      // TODO Auto-generated method stub
      return null;
   }

   private Date getAfternoonIni (Date observed) {
      // TODO Auto-generated method stub
      return null;
   }

   private Date getNightEnd (Date observed) {
      // TODO Auto-generated method stub
      return null;
   }

   private Date getNightIni (Date observed) {
      // TODO Auto-generated method stub
      return null;
   }

   private Date getDayEnd (Date observed) {
      // TODO Auto-generated method stub
      return null;
   }

   private Date getDayIni (Date observed) {
      // TODO Auto-generated method stub
      return null;
   }

   private Date getMonthEnd (Date observed) {
      // TODO Auto-generated method stub
      return null;
   }

   private Date getMonthIni (Date observed) {
      // TODO Auto-generated method stub
      return null;
   }
}
