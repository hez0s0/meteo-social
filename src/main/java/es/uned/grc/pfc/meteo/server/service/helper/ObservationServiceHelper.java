package es.uned.grc.pfc.meteo.server.service.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.grc.pfc.meteo.client.util.DerivedUtils;
import es.uned.grc.pfc.meteo.server.dto.DerivedRangeDTO;
import es.uned.grc.pfc.meteo.server.dto.DerivedVariableDTO;
import es.uned.grc.pfc.meteo.server.dto.ObservationBlockDTO;
import es.uned.grc.pfc.meteo.server.dto.VariableObservationsDTO;
import es.uned.grc.pfc.meteo.server.job.station.IStationPlugin;
import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.RequestParam;
import es.uned.grc.pfc.meteo.server.model.RequestParamFilter;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.model.Variable;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IVariablePersistence;
import es.uned.grc.pfc.meteo.server.util.IServerConstants;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;
import es.uned.grc.pfc.meteo.shared.ISharedConstants.DerivedRangeType;

/**
 * Supports non-client invokable actions of the ObservationService module
 */
@Service
public class ObservationServiceHelper {

   @SuppressWarnings ("unused")
   private static Logger logger = LoggerFactory.getLogger (ObservationServiceHelper.class);
   
   @Autowired
   private IObservationPersistence observationPersistence = null;
   @Autowired
   private IStationPersistence stationPersistence = null;
   @Autowired
   private IVariablePersistence variablePersistence = null;
   
   @Autowired
   private IStationPlugin stationPlugin = null;
   
   public List <Observation> fillGaps (List <Observation> observations, RequestParam requestParam) throws ParseException {
      Observation  observation = null;
      List <Observation> result = new ArrayList <Observation> ();
      Date startDate = getStartDate (requestParam);
      Date endDate = getEndDate (requestParam);
      List <Variable> variables = null;
      Station station = null;
      
      if (startDate != null && endDate != null) {
         //iterate all the periods and variables to fill the gaps with "empty" observations
         station = getStation (requestParam, observations);
         variables = getVariables (station, requestParam);
         
         long stationPeriod = (stationPlugin.getObservationPeriod () * IServerConstants.ONE_MINUTE);
         
         for (long observed = startDate.getTime (); observed <= endDate.getTime (); observed += stationPeriod) {
            for (Variable variable : variables) {
               if (!variable.getInternal ()) {
                  observation = findObservation (observations, observed, variable.getId ());
                  if (observation == null) {
                     observation = createEmptyObservation (observed, variable, station);
                  }
                  result.add (observation);
               }
            }
         }
      } else {
         //just get the results as-is
         result.addAll (observations);
      }
      
      return result;
   }
   
   private List <Variable> getVariables (Station station, RequestParam requestParam) {
      List <Variable> result = null;
      String filterValue = findFilterValue (requestParam, ISharedConstants.ObservationFilter.VARIABLE_IDS);
      
      if (!StringUtils.isEmpty (filterValue)) {
         //a list of variables was provided
         result = variablePersistence.findByIdList (variablePersistence.parseIntegerList (filterValue));
      } else {
         //use the variables of the station
         result = new ArrayList <Variable> (variablePersistence.getStationVariables (null, station.getId (), true, false));
      }
      
      return result;
   }

   private Station getStation (RequestParam requestParam, List <Observation> observations) {
      Station result = null;
      String filterValue = null;
      // if there is any observation at all, it must contain the station
      if (!observations.isEmpty ()) {
         result = observations.get (0).getStation ();
      } else {
         //get it from the filter, if no observation at all was found
         filterValue = findFilterValue (requestParam, ISharedConstants.ObservationFilter.OWN);
         if (!StringUtils.isEmpty (filterValue)) {
            //own station
            result = stationPersistence.getOwnStation ();
            if (result == null) {
               throw new RuntimeException ("unable to obtain own station, please review the system configuration");
            }
         } else {
            //id-specified station
            filterValue = findFilterValue (requestParam, ISharedConstants.ObservationFilter.STATION_ID);
            if (!StringUtils.isEmpty (filterValue)) {
               result = stationPersistence.findById (Integer.valueOf (filterValue));
            }
         }
      }
      
      if (result == null) {
         throw new RuntimeException ("No station found, but it is mandatory");
      }
      
      return result;
   }

   private Observation createEmptyObservation (long observed, Variable variable, Station station) {
      Observation observation = new Observation ();
      
      observation.setStation (station);
      observation.setVariable (variable);
      observation.setObserved (new Date (observed));
      
      return observation;
   }

   private Observation createEmptyObservation (Date rangeIni, Date rangeEnd, Variable variable, Variable derivedVariable, Station station) {
      Observation observation = new Observation ();
      
      observation.setStation (station);
      observation.setVariable (variable);
      observation.setRangeIni (rangeIni);
      observation.setRangeEnd (rangeEnd);
      observation.setDerivedVariable (derivedVariable);
      
      return observation;
   }

   private Observation findObservation (List <Observation> observations, long observed, int variableId) {
      for (Observation observation : observations) {
         if (observation.getObserved ().getTime () == observed && observation.getVariable ().getId () == variableId) {
            return observation;
         }
      }
      return null;
   }

   private Observation findDerivedObservation (Date [] range, List <Observation> observations, int variableId, int originalVariableId) {
      for (Observation observation : observations) {
         if (observation.getVariable ().getId () == variableId 
               && observation.getDerivedVariable () != null 
               && observation.getDerivedVariable ().getId () == originalVariableId
               && observation.getRangeIni ().getTime () == range [0].getTime ()
               && observation.getRangeEnd ().getTime () == range [1].getTime ()) {
            return observation;
         }
      }
      return null;
   }
   
   private Date getStartDate (RequestParam requestParam) throws ParseException {
      String value = findFilterValue (requestParam, ISharedConstants.ObservationFilter.START_DATE);
      return !StringUtils.isEmpty (value) ? new SimpleDateFormat (ISharedConstants.SHARED_SHORT_DATE_FORMAT).parse (value) : null;
   }

   private Date getEndDate (RequestParam requestParam) throws ParseException {
      String value = findFilterValue (requestParam, ISharedConstants.ObservationFilter.END_DATE);
      return !StringUtils.isEmpty (value) ? new SimpleDateFormat (ISharedConstants.SHARED_SHORT_DATE_FORMAT).parse (value) : null;
   }
   
   public String findFilterValue (RequestParam requestParam, ISharedConstants.ObservationFilter searchedFilter) {
      for (RequestParamFilter filter : requestParam.getFilters ()) {
         if (filter.getParam () != null) {
            if (ISharedConstants.ObservationFilter.valueOf (filter.getParam ()).equals (searchedFilter)) {
               return filter.getValue ();
            }
         }
      }
      return null;
   }

   /**
    * Groups the observations per blocks and sorts them
    */
   public List <ObservationBlockDTO> groupBlocks (List <Observation> observations) {
      ObservationBlockDTO block = null;
      Map <Date, List <Observation>> observationMap = new HashMap <Date, List <Observation>> ();
      List <ObservationBlockDTO> result = new ArrayList <ObservationBlockDTO> ();
      
      //group them into a map
      for (Observation observation : observations) {
         if (!observationMap.containsKey (observation.getObserved ())) {
            //create a new block
            observationMap.put (observation.getObserved (), new ArrayList <Observation> ());
         }
         //append the observation to eht block
         observationMap.get (observation.getObserved ()).add (observation);
      }
      
      //dump the map into the resulting list
      for (Map.Entry <Date, List <Observation>> entry : observationMap.entrySet ()) {
         block = new ObservationBlockDTO ();
         block.setObserved (entry.getKey ());
         block.setStation (entry.getValue ().get (0).getStation ());
         block.setObservations (entry.getValue ());
         
         result.add (block);
         
         Collections.sort (block.getObservations (), new ObservationComparator ());
      }

      Collections.sort (result, new ObservationBlockComparator ());
      
      return result;
   }

   /**
    * Group observations by variable
    */
   public List <VariableObservationsDTO> groupByVariable (List <Observation> observations, RequestParam requestParam) {
      Station station = null;
      List <Variable> variables = null;
      List <VariableObservationsDTO> variableObservations = null;
      VariableObservationsDTO variableObservation = null;
      Map <Variable, List <Observation>> observationsMap = new HashMap <Variable, List <Observation>> ();
      
      station = getStation (requestParam, observations);
      variables = getVariables (station, requestParam);
      
      for (Variable variable : variables) {
         observationsMap.put (variable, new ArrayList <Observation> ());
      }
      for (Observation observation : observations) {
         observationsMap.get (observation.getVariable ()).add (observation);
      }
      for (Map.Entry <Variable, List <Observation>> entry : observationsMap.entrySet ()) {
         Collections.sort (entry.getValue (), new ObservationComparator ());
      }
      
      variableObservations = new ArrayList <VariableObservationsDTO> (observationsMap.size ());
      for (Map.Entry <Variable, List <Observation>> entry : observationsMap.entrySet ()) {
         variableObservation = new VariableObservationsDTO ();
         variableObservation.setStation (station);
         variableObservation.setVariable (entry.getKey ());
         variableObservation.setObservations (entry.getValue ());
         variableObservations.add (variableObservation);
      }
      Collections.sort (variableObservations, new VariableObservationsComparator ());
      
      return variableObservations;
   }
   
   private class VariableObservationsComparator implements Comparator <VariableObservationsDTO> {
      @Override
      public int compare (VariableObservationsDTO o1, VariableObservationsDTO o2) {
         return Integer.valueOf (o1.getVariable ().getPosition ()).compareTo (Integer.valueOf (o2.getVariable ().getPosition ()));
      }
   }
   
   private class ObservationBlockComparator implements Comparator <ObservationBlockDTO> {
      @Override
      public int compare (ObservationBlockDTO o1, ObservationBlockDTO o2) {
         return o1.getObserved ().compareTo (o2.getObserved ());
      }
   }

   private class ObservationComparator implements Comparator <Observation> {
      @Override
      public int compare (Observation o1, Observation o2) {
         return new Integer (o1.getVariable ().getPosition ()).compareTo (new Integer (o2.getVariable ().getPosition ()));
      }
   }
   
   public DerivedRangeDTO fillAndGroupAsRange (Station station, 
                                               List <Observation> observations, 
                                               Date [] range, 
                                               Variable minimum,
                                               Variable average,
                                               Variable maximum,
                                               List <Variable> stationVariables) {
      DerivedVariableDTO derivedVariableDTO = null;
      List <DerivedVariableDTO> derivedVariableDTOs = null;
      DerivedRangeDTO result = null;
      
      //build the results for every observed variable
      derivedVariableDTOs = new ArrayList <DerivedVariableDTO> (stationVariables.size ());
      for (Variable variable : stationVariables) {
         derivedVariableDTO = new DerivedVariableDTO ();
         derivedVariableDTO.setVariable (variable);
         derivedVariableDTO.setMinimum (getDerivedObservation (range, station, observations, variable, minimum).getValue ());
         derivedVariableDTO.setMinimumDeriveBase (getDerivedObservation (range, station, observations, variable, minimum).getDeriveBase ());
         derivedVariableDTO.setMinimumDeriveIgnored (getDerivedObservation (range, station, observations, variable, minimum).getDeriveIgnored ());
         derivedVariableDTO.setMinimumDeriveExpected (getDerivedObservation (range, station, observations, variable, minimum).getDeriveExpected ());
         derivedVariableDTO.setAverage (getDerivedObservation (range, station, observations, variable, average).getValue ());
         derivedVariableDTO.setAverageDeriveBase (getDerivedObservation (range, station, observations, variable, average).getDeriveBase ());
         derivedVariableDTO.setAverageDeriveIgnored (getDerivedObservation (range, station, observations, variable, average).getDeriveIgnored ());
         derivedVariableDTO.setAverageDeriveExpected (getDerivedObservation (range, station, observations, variable, average).getDeriveExpected ());
         derivedVariableDTO.setMaximum (getDerivedObservation (range, station, observations, variable, maximum).getValue ());
         derivedVariableDTO.setMaximumDeriveBase (getDerivedObservation (range, station, observations, variable, maximum).getDeriveBase ());
         derivedVariableDTO.setMaximumDeriveIgnored (getDerivedObservation (range, station, observations, variable, maximum).getDeriveIgnored ());
         derivedVariableDTO.setMaximumDeriveExpected (getDerivedObservation (range, station, observations, variable, maximum).getDeriveExpected ());
         derivedVariableDTO.setDisplayDate (range [0]);
         derivedVariableDTOs.add (derivedVariableDTO);
      }
      
      //build the result object
      result = new DerivedRangeDTO ();
      result.setStation (station);
      result.setIni (range [0]);
      result.setEnd (range [1]);
      result.setDerivedVariables (derivedVariableDTOs);
      
      return result;
   }

   private Observation getDerivedObservation (Date [] range, Station station, List<Observation> observations, Variable variable, Variable internalVariable) {
      Observation observation = findDerivedObservation (range, observations, internalVariable.getId (), variable.getId ());
      if (observation == null) {
         //this derivation did not exist, we must create a dummy version
         observation = createEmptyObservation (range [0], range [1], internalVariable, variable, station);
      }
      return observation;
   }

   /**
    * For a given variable and time, calculate the overall range
    * required to obtain observations for graphical display
    */
   public Date [] getWideRange (DerivedRangeType derivedRangeType, Date searched) {
      Calendar ini = Calendar.getInstance ();
      Calendar end = Calendar.getInstance ();
      if (derivedRangeType.equals (DerivedRangeType.MONTH)) {
         ini.setTime (searched);
         ini.set (Calendar.HOUR_OF_DAY, 0);
         ini.set (Calendar.MINUTE, 0);
         ini.set (Calendar.SECOND, 0);
         end.setTimeInMillis (ini.getTimeInMillis ());
         
         ini.set (Calendar.DAY_OF_YEAR, 1);
         ini.set (Calendar.MONTH, 0);

         end.set (Calendar.DAY_OF_YEAR, end.getActualMaximum (Calendar.DAY_OF_YEAR));
         end.set (Calendar.MONTH, end.getActualMaximum (Calendar.MONTH));
      } else {
         ini.setTimeInMillis (DerivedUtils.getMonthIni (searched).getTime ());
         end.setTimeInMillis (DerivedUtils.getMonthEnd (searched).getTime ());
      }
      return new Date [] {ini.getTime (), end.getTime ()};
   }

   public Date [] getRange (DerivedRangeType derivedRangeType, Date searched) {
      Date [] range = new Date [2];
      switch (derivedRangeType) {
         case AFTERNOON:
            range [0] = DerivedUtils.getAfternoonIni (searched);
            range [1] = DerivedUtils.getAfternoonEnd (searched);
            break;
         case DAY:
            range [0] = DerivedUtils.getDayIni (searched);
            range [1] = DerivedUtils.getDayEnd (searched);
            break;
         case EVENING:
            range [0] = DerivedUtils.getEveningIni (searched);
            range [1] = DerivedUtils.getEveningEnd (searched);
            break;
         case MONTH:
            range [0] = DerivedUtils.getMonthIni (searched);
            range [1] = DerivedUtils.getMonthEnd (searched);
            break;
         case MORNING:
            range [0] = DerivedUtils.getMorningIni (searched);
            range [1] = DerivedUtils.getMorningEnd (searched);
            break;
         case NIGHT:
            range [0] = DerivedUtils.getNightIni (searched);
            range [1] = DerivedUtils.getNightEnd (searched);
            break;
      }
      return range;
   }
}
