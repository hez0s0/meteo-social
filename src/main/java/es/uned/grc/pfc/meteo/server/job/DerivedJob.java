package es.uned.grc.pfc.meteo.server.job;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.util.Calendar;

import es.uned.grc.pfc.meteo.client.util.DerivedUtils;
import es.uned.grc.pfc.meteo.server.job.station.IStationPlugin;
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

   private static final int UNDERIVED_BLOCK = 100;
   
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
      List <Observation> observations = null;
      
      logger.info ("Executing task {}", getClass ().getSimpleName ());
      try {
         synchronized (IJobConstants.STATION_ACCESS) {
            List <Station> stations = stationPersistence.findAll ();
            for (Station station : stations) {
               station = checkInternalVariables (station);
               
               observations = observationPersistence.getUnderived (station.getId (), UNDERIVED_BLOCK);
               
               if (observations != null && !observations.isEmpty ()) {
                  createUpdateDerived (station, observations, new Date ());
               }
               logger.info ("Derived calculation processed for {} observations of station {}", observations != null ? observations.size () : 0, station.getId ());
            }
         }
      } catch (Exception e) {
         logger.error ("Error calculation derived observations", e);
      }
   }

   /**
    * Generates (or updates) the derived variables
    */
   @Transactional (propagation = Propagation.REQUIRED)
   private void createUpdateDerived (Station station, List <Observation> observations, Date now) {
      IStationPlugin stationPlugin = null;
      DeriveType deriveType = null;
      int deriveExpected = -1;
      int monthDays = -1;
      Calendar calendar = null;
      Date [] range = new Date [2];
      Map <String, List <Observation>> existingObservationsMap = new HashMap <String, List <Observation>> ();
      
      stationPlugin = stationPersistence.getStationPlugin (station);
      //iterate all observations
      for (Observation observation : observations) {
         //for every observation, derive a new observation for every internal variable
         for (Variable variable : station.getVariables ()) {
            if (variable.getInternal ()) {
               range [0] = null;
               range [1] = null;
               //depending on the variable, calculate the deriveType and date range
               switch (variable.getAcronym ()) {
                  case IServerConstants.NIGHT_MINIMUM:
                     deriveType = DeriveType.MINIMUM;
                     range [0] = DerivedUtils.getNightIni (observation.getObserved ());
                     range [1] = DerivedUtils.getNightEnd (observation.getObserved ());
                     deriveExpected = getExpected (Integer.valueOf (IServerConstants.NIGHT_END_HOUR) - Integer.valueOf (IServerConstants.NIGHT_START_HOUR), stationPlugin);
                     break;
                  case IServerConstants.NIGHT_MAXIMUM:
                     deriveType = DeriveType.MAXIMUM;
                     range [0] = DerivedUtils.getNightIni (observation.getObserved ());
                     range [1] = DerivedUtils.getNightEnd (observation.getObserved ());
                     deriveExpected = getExpected (Integer.valueOf (IServerConstants.NIGHT_END_HOUR) - Integer.valueOf (IServerConstants.NIGHT_START_HOUR), stationPlugin);
                     break;
                  case IServerConstants.NIGHT_AVERAGE:
                     deriveType = DeriveType.AVERAGE;
                     range [0] = DerivedUtils.getNightIni (observation.getObserved ());
                     range [1] = DerivedUtils.getNightEnd (observation.getObserved ());
                     deriveExpected = getExpected (Integer.valueOf (IServerConstants.NIGHT_END_HOUR) - Integer.valueOf (IServerConstants.NIGHT_START_HOUR), stationPlugin);
                     break;
                  case IServerConstants.MORNING_MINIMUM:
                     deriveType = DeriveType.MINIMUM;
                     range [0] = DerivedUtils.getMorningIni (observation.getObserved ());
                     range [1] = DerivedUtils.getMorningEnd (observation.getObserved ());
                     deriveExpected = getExpected (Integer.valueOf (IServerConstants.MORNING_END_HOUR) - Integer.valueOf (IServerConstants.MORNING_START_HOUR), stationPlugin);
                     break;
                  case IServerConstants.MORNING_MAXIMUM:
                     deriveType = DeriveType.MAXIMUM;
                     range [0] = DerivedUtils.getMorningIni (observation.getObserved ());
                     range [1] = DerivedUtils.getMorningEnd (observation.getObserved ());
                     deriveExpected = getExpected (Integer.valueOf (IServerConstants.MORNING_END_HOUR) - Integer.valueOf (IServerConstants.MORNING_START_HOUR), stationPlugin);
                     break;
                  case IServerConstants.MORNING_AVERAGE:
                     deriveType = DeriveType.AVERAGE;
                     range [0] = DerivedUtils.getMorningIni (observation.getObserved ());
                     range [1] = DerivedUtils.getMorningEnd (observation.getObserved ());
                     deriveExpected = getExpected (Integer.valueOf (IServerConstants.MORNING_END_HOUR) - Integer.valueOf (IServerConstants.MORNING_START_HOUR), stationPlugin);
                     break;
                  case IServerConstants.AFTERNOON_MINIMUM:
                     deriveType = DeriveType.MINIMUM;
                     range [0] = DerivedUtils.getAfternoonIni (observation.getObserved ());
                     range [1] = DerivedUtils.getAfternoonEnd (observation.getObserved ());
                     deriveExpected = getExpected (Integer.valueOf (IServerConstants.AFTERNOON_END_HOUR) - Integer.valueOf (IServerConstants.AFTERNOON_START_HOUR), stationPlugin);
                     break;
                  case IServerConstants.AFTERNOON_MAXIMUM:
                     deriveType = DeriveType.MAXIMUM;
                     range [0] = DerivedUtils.getAfternoonIni (observation.getObserved ());
                     range [1] = DerivedUtils.getAfternoonEnd (observation.getObserved ());
                     deriveExpected = getExpected (Integer.valueOf (IServerConstants.AFTERNOON_END_HOUR) - Integer.valueOf (IServerConstants.AFTERNOON_START_HOUR), stationPlugin);
                     break;
                  case IServerConstants.AFTERNOON_AVERAGE:
                     deriveType = DeriveType.AVERAGE;
                     range [0] = DerivedUtils.getAfternoonIni (observation.getObserved ());
                     range [1] = DerivedUtils.getAfternoonEnd (observation.getObserved ());
                     deriveExpected = getExpected (Integer.valueOf (IServerConstants.AFTERNOON_END_HOUR) - Integer.valueOf (IServerConstants.AFTERNOON_START_HOUR), stationPlugin);
                     break;
                  case IServerConstants.EVENING_MINIMUM:
                     deriveType = DeriveType.MINIMUM;
                     range [0] = DerivedUtils.getEveningIni (observation.getObserved ());
                     range [1] = DerivedUtils.getEveningEnd (observation.getObserved ());
                     deriveExpected = getExpected (Integer.valueOf (IServerConstants.EVENING_END_HOUR) - Integer.valueOf (IServerConstants.EVENING_START_HOUR), stationPlugin);
                     break;
                  case IServerConstants.EVENING_MAXIMUM:
                     deriveType = DeriveType.MAXIMUM;
                     range [0] = DerivedUtils.getEveningIni (observation.getObserved ());
                     range [1] = DerivedUtils.getEveningEnd (observation.getObserved ());
                     deriveExpected = getExpected (Integer.valueOf (IServerConstants.EVENING_END_HOUR) - Integer.valueOf (IServerConstants.EVENING_START_HOUR), stationPlugin);
                     break;
                  case IServerConstants.EVENING_AVERAGE:
                     deriveType = DeriveType.AVERAGE;
                     range [0] = DerivedUtils.getEveningIni (observation.getObserved ());
                     range [1] = DerivedUtils.getEveningEnd (observation.getObserved ());
                     deriveExpected = getExpected (Integer.valueOf (IServerConstants.EVENING_END_HOUR) - Integer.valueOf (IServerConstants.EVENING_START_HOUR), stationPlugin);
                     break;
                  case IServerConstants.DAY_MINIMUM:
                     deriveType = DeriveType.MINIMUM;
                     range [0] = DerivedUtils.getDayIni (observation.getObserved ());
                     range [1] = DerivedUtils.getDayEnd (observation.getObserved ());
                     deriveExpected = getExpected (IServerConstants.ONE_DAY_HOURS, stationPlugin);
                     break;
                  case IServerConstants.DAY_MAXIMUM:
                     deriveType = DeriveType.MAXIMUM;
                     range [0] = DerivedUtils.getDayIni (observation.getObserved ());
                     range [1] = DerivedUtils.getDayEnd (observation.getObserved ());
                     deriveExpected = getExpected (IServerConstants.ONE_DAY_HOURS, stationPlugin);
                     break;
                  case IServerConstants.DAY_AVERAGE:
                     deriveType = DeriveType.AVERAGE;
                     range [0] = DerivedUtils.getDayIni (observation.getObserved ());
                     range [1] = DerivedUtils.getDayEnd (observation.getObserved ());
                     deriveExpected = getExpected (IServerConstants.ONE_DAY_HOURS, stationPlugin);
                     break;
                  case IServerConstants.MONTH_MINIMUM:
                     deriveType = DeriveType.MINIMUM;
                     range [0] = DerivedUtils.getMonthIni (observation.getObserved ());
                     range [1] = DerivedUtils.getMonthEnd (observation.getObserved ());
                     calendar = Calendar.getInstance ();
                     calendar.setTime (range [1]);
                     monthDays = calendar.get (Calendar.DAY_OF_MONTH);
                     deriveExpected = getExpected (monthDays * IServerConstants.ONE_DAY_HOURS, stationPlugin);
                     break;
                  case IServerConstants.MONTH_MAXIMUM:
                     deriveType = DeriveType.MAXIMUM;
                     range [0] = DerivedUtils.getMonthIni (observation.getObserved ());
                     range [1] = DerivedUtils.getMonthEnd (observation.getObserved ());
                     calendar = Calendar.getInstance ();
                     calendar.setTime (range [1]);
                     monthDays = calendar.get (Calendar.DAY_OF_MONTH);
                     deriveExpected = getExpected (monthDays * IServerConstants.ONE_DAY_HOURS, stationPlugin);
                     break;
                  case IServerConstants.MONTH_AVERAGE:
                     deriveType = DeriveType.AVERAGE;
                     range [0] = DerivedUtils.getMonthIni (observation.getObserved ());
                     range [1] = DerivedUtils.getMonthEnd (observation.getObserved ());
                     calendar = Calendar.getInstance ();
                     calendar.setTime (range [1]);
                     monthDays = calendar.get (Calendar.DAY_OF_MONTH);
                     deriveExpected = getExpected (monthDays * IServerConstants.ONE_DAY_HOURS, stationPlugin);
                     break;
               }
               
               //actually create and save the derived observation
               if (range [0] != null && range [1] != null && isInRange (observation, range)) {
                  derive (station, observation, variable, range, deriveType, deriveExpected, existingObservationsMap, now);
               }
            }
         }
         observation.setDerived (now);
         observation = observationPersistence.saveOrMerge (observation);
      }
   }

   private int getExpected (int hours, IStationPlugin stationPlugin) {
      return (IServerConstants.ONE_HOUR_MINUTES / stationPlugin.getObservationPeriod ()) * hours;
   }

   /**
    * Checks if the internal (derived) variables already exist in the database or not
    */
   @Transactional (propagation = Propagation.REQUIRED)
   private Station checkInternalVariables (Station station) {
      checkInternalVariable (station, IServerConstants.NIGHT_MINIMUM, "Mínima noche", "Valor mínimo durante la noche (00-06)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.NIGHT_AVERAGE, "Media noche", "Valor medio durante la noche (00-06)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.NIGHT_MAXIMUM, "Máxima noche", "Valor máximo durante la noche (00-06)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.MORNING_MINIMUM, "Mínima mañana", "Valor mínimo durante la mañana (06-12)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.MORNING_AVERAGE, "Media mañana", "Valor medio durante la mañana (06-12)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.MORNING_MAXIMUM, "Máxima mañana", "Valor máximo durante la mañana (06-12)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.AFTERNOON_MINIMUM, "Mínima tarde", "Valor mínimo durante la tarde (12-18)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.AFTERNOON_AVERAGE, "Media tarde", "Valor medio durante la tarde (12-18)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.AFTERNOON_MAXIMUM, "Máxima tarde", "Valor máximo durante la tarde (12-18)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.EVENING_MINIMUM, "Mínima tarde-noche", "Valor mínimo durante la tarde-noche (18-00)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.EVENING_AVERAGE, "Media tarde-noche", "Valor medio durante la tarde-noche (18-00)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.EVENING_MAXIMUM, "Máxima tarde-noche", "Valor máximo durante la tarde-noche (18-00)", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.DAY_MINIMUM, "Mínima del día", "Valor mínimo durante el día", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.DAY_AVERAGE, "Media del día", "Valor medio durante el día", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.DAY_MAXIMUM, "Máxima del día", "Valor máximo durante el día", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.MONTH_MINIMUM, "Mínima del mes", "Valor mínimo durante el mes", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.MONTH_AVERAGE, "Media del mes", "Valor medio durante el mes", 0, 0, ISharedConstants.GraphType.NONE);
      checkInternalVariable (station, IServerConstants.MONTH_MAXIMUM, "Máxima del mes", "Valor máximo durante el mes", 0, 0, ISharedConstants.GraphType.NONE);
      return stationPersistence.merge (station);
   }

   @Transactional (propagation = Propagation.REQUIRED)
   private void checkInternalVariable (Station station, String acronym, String name, String description, int position, int displayGroup, ISharedConstants.GraphType graphType) {
      Variable stationVariable = null;
      Variable internalVariable = variablePersistence.getByAcronym (acronym);
      
      if (internalVariable == null) {
         //it not in DB yet, create it new
         internalVariable = new Variable ();
         internalVariable.setAcronym (acronym);
         internalVariable.setName (name);
         internalVariable.setDescription (description);
         internalVariable.setInternal (true);
         internalVariable.setPosition (position);
         internalVariable.setDisplayGroup (displayGroup);
         internalVariable.setGraphType (graphType);
         variablePersistence.save (internalVariable);
         
         logger.info ("Automatically creating internal variable {}", internalVariable.getAcronym ());
      } else {
         internalVariable.setName (name);
         internalVariable.setDescription (description);
         variablePersistence.save (internalVariable);
      }
      
      stationVariable = findInternalByAcronyn (acronym, station.getVariables ());
      if (stationVariable == null) {
         //if not assigned to this station, add it there too
         station.getVariables ().add (internalVariable);
         stationPersistence.save (station);
      }
   }

   @Transactional (propagation = Propagation.REQUIRED)
   private void derive (Station station,
                        Observation observation, 
                        Variable variable, 
                        Date [] range, 
                        DeriveType deriveType,
                        int deriveExpected,
                        Map <String, List <Observation>> existingObservationsMap, 
                        Date now) {
      List <Observation> rangeObservations = null;
      Observation derivedObservation = null;
      MutableInt deriveBase = new MutableInt ();
      MutableInt deriveIgnored = new MutableInt ();
      SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMdd");
      String monthObservedKey = sdf.format (observation.getObserved ());
      String monthDerivedKey = sdf.format (observation.getObserved ()) + "_range";
      List <Observation> monthObservations = existingObservationsMap.get (monthObservedKey);
      List <Observation> monthDerivedObservations = existingObservationsMap.get (monthDerivedKey);
      
      if (monthObservations == null) {
         monthObservations = observationPersistence.getObservedInRange (station.getId (), DerivedUtils.getMonthIni (observation.getObserved ()), DerivedUtils.getMonthEnd (observation.getObserved ()), true);
         existingObservationsMap.put (monthObservedKey, monthObservations);
      }
      if (monthDerivedObservations == null) {
         monthDerivedObservations = observationPersistence.getDerivedInRange (station.getId (), DerivedUtils.getMonthIni (observation.getObserved ()), DerivedUtils.getMonthEnd (observation.getObserved ()));
         existingObservationsMap.put (monthDerivedKey, monthDerivedObservations);
      }
      
      rangeObservations = getSubRange (observation, monthObservations, range);
      derivedObservation = findDerived (monthDerivedObservations, observation, variable, range);
      
      if (derivedObservation == null) {
         derivedObservation = new Observation ();
         derivedObservation.setVariable (variable);
         derivedObservation.setStation (observation.getStation ());
         derivedObservation.setRangeIni (range [0]);
         derivedObservation.setRangeEnd (range [1]);
         derivedObservation.setDerivedVariable (observation.getVariable ());
         derivedObservation.setReceived (now);
      }
      derivedObservation.setObserved (now);
      derivedObservation.setValue (calculate (rangeObservations, deriveType, deriveBase, deriveIgnored));
      derivedObservation.setDeriveBase (deriveBase.intValue ());
      derivedObservation.setDeriveIgnored (deriveIgnored.intValue ());
      derivedObservation.setDeriveExpected (deriveExpected);
      derivedObservation = observationPersistence.saveOrMerge (derivedObservation);
   }
   
   private String calculate (List <Observation> rangeObservations, DeriveType deriveType, MutableInt deriveBase, MutableInt deriveIgnored) {
      Double result = null;
      Double value = null;
      double total = 0.0;
      for (Observation observation : rangeObservations) {
         if (observation.getQuality ()) {
            //if the quality is passed, consider it
            deriveBase.setValue (deriveBase.intValue () + 1);
            value = getAsDouble (observation.getValue ());
            switch (deriveType) {
               case MINIMUM:
                  if ( (value != null) && (result == null || value < result) ) {
                     result = value;
                  }
                  break;
               case MAXIMUM:
                  if ( (value != null) && (result == null || value > result) ) {
                     result = value;
                  }
                  break;
               case AVERAGE:
                  if (value != null) {
                     total += value;
                     result = total / rangeObservations.size ();
                  }
                  break;
            }
         } else {
            //if the quality is not passed, ignore it
            deriveIgnored.setValue (deriveIgnored.intValue () + 1);
         }
      }
      return result != null ?  new DecimalFormat ("#.##").format (result).replace (",", ".") : null;
   }

   private Double getAsDouble (String value) {
      try {
         return Double.parseDouble (value);
      } catch (Exception e) {
         return null;
      }
   }

   private Observation findDerived (List <Observation> monthObservations, Observation observation, Variable variable, Date [] range) {
      for (Observation monthObservation : monthObservations) {
         if (monthObservation.getDerivedVariable () != null
               && monthObservation.getVariable ().getId ().equals (variable.getId ())
               && monthObservation.getRangeIni ().getTime () == range [0].getTime () 
               && monthObservation.getRangeEnd ().getTime () == range [1].getTime ()
               && monthObservation.getVariable ().getId ().equals (variable.getId ()) 
               && monthObservation.getDerivedVariable ().getId ().equals (observation.getVariable ().getId ())  ) {
            return monthObservation;
         }
      }
      return null;
   }

   private List <Observation> getSubRange (Observation observation, List <Observation> monthObservations, Date [] range) {
      List <Observation> result = new ArrayList <Observation> ();
      
      for (Observation monthObservation : monthObservations) {
         if (observation.getVariable ().getId ().equals (monthObservation.getVariable ().getId ()) && isInRange (monthObservation, range)) {
            result.add (monthObservation);
         }
      }
      
      return result;
   }

   private Variable findInternalByAcronyn (String acronym, Set <Variable> variables) {
      for (Variable variable : variables) {
         if (variable.getAcronym ().equalsIgnoreCase (acronym) && variable.getInternal ()) {
            return variable;
         }
      }
      return null;
   }

   private boolean isInRange (Observation observation, Date [] range) {
      return (observation.getDerivedVariable () == null 
              && observation.getObserved ().getTime () >= range [0].getTime () 
              && observation.getObserved ().getTime () <= range [1].getTime ());
   }
}
