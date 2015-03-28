package es.uned.grc.pfc.meteo.server.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.grc.pfc.meteo.server.dto.DerivedRangeDTO;
import es.uned.grc.pfc.meteo.server.dto.ObservationBlockDTO;
import es.uned.grc.pfc.meteo.server.dto.VariableObservationsDTO;
import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.RequestParam;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.model.Variable;
import es.uned.grc.pfc.meteo.server.model.paged.PagedList;
import es.uned.grc.pfc.meteo.server.model.paged.VariablePagedList;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IVariablePersistence;
import es.uned.grc.pfc.meteo.server.service.helper.ObservationServiceHelper;
import es.uned.grc.pfc.meteo.server.util.IServerConstants;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;
import es.uned.grc.pfc.meteo.shared.ISharedConstants.DerivedRangeType;

/**
 * Service to manage observations.
 * To be wrapped and invoked through RequestFactory via a twin context.
 */
@Service
public class ObservationService {

   private static Logger logger = LoggerFactory.getLogger (ObservationService.class);
   
   @Autowired
   private IObservationPersistence observationPersistence = null;
   @Autowired
   private IStationPersistence stationPersistence = null;
   @Autowired
   private IVariablePersistence variablePersistence = null;
   
   @Autowired
   private ObservationServiceHelper observationServiceHelper = null;

   /**
    * Get the variables that a given station is able to measure.
    * Specify null as the stationId to get the variables of your own station
    */
   public VariablePagedList getStationVariables (String filter, Integer stationId, boolean measuredOnly, boolean derivedOnly) {
      try {
         if (stationId == null) {
            stationId = stationPersistence.getOwnStation ().getId ();
         }
         return new VariablePagedList (variablePersistence.getStationVariables (filter, stationId, measuredOnly, derivedOnly));
      } catch (Exception e) {
         logger.error ("Error listing station variable", e);
         throw new RuntimeException ("Could not list variables of a station. See server logs.");
      }
   }
   
   /**
    * Obtains a list of observations for the given filter. If a start and end date
    * is provided, it fills all the possible gaps by pushing Observations with null values
    * for every period within the range and every variable.
    * IMPORTANT: this method assumes that 1 station is provided as filter, either by id
    * or by marking the own station flag !!
    * The result is formed as a list of blocks of observations
    */
   public List <ObservationBlockDTO> getObservationBlocks (RequestParam requestParam) {
      PagedList <Integer, Observation> observationsPagedList =  null;
      List <Observation> observations = null;
      try {
         if ( (StringUtils.isEmpty (observationServiceHelper.findFilterValue (requestParam, ISharedConstants.ObservationFilter.OWN)))
               && (StringUtils.isEmpty (observationServiceHelper.findFilterValue (requestParam, ISharedConstants.ObservationFilter.STATION_ID))) ) {
            throw new RuntimeException ("Either the 'own' flag must be active, or the station id must contain a value in order to run this method ");
         }
         observationsPagedList = observationPersistence.getList (requestParam);
         
         observations = observationServiceHelper.fillGaps (observationsPagedList.getList (), requestParam);
         
         return observationServiceHelper.groupBlocks (observations);
      } catch (Exception e) {
         logger.error ("Error listing observations", e);
         throw new RuntimeException ("Could not list observations. See server logs.");
      }
   }

   /**
    * Obtains a map of observations for the given filter, grouped by variable, fit 
    * to be displayed, for example, in graphics. If a start and end date
    * is provided, it fills all the possible gaps by pushing Observations with null values
    * for every period within the range and every variable.
    * IMPORTANT: this method assumes that 1 station is provided as filter, either by id
    * or by marking the own station flag !! 
    */
   public List <VariableObservationsDTO> getVariableObservations (RequestParam requestParam) {
      PagedList <Integer, Observation> observationsPagedList =  null;
      List <Observation> observations = null;
      try {

         if ( (StringUtils.isEmpty (observationServiceHelper.findFilterValue (requestParam, ISharedConstants.ObservationFilter.OWN)))
               && (StringUtils.isEmpty (observationServiceHelper.findFilterValue (requestParam, ISharedConstants.ObservationFilter.STATION_ID))) ) {
            throw new RuntimeException ("Either the 'own' flag must be active, or the station id must contain a value in order to run this method ");
         }
         observationsPagedList = observationPersistence.getList (requestParam);

         observations = observationServiceHelper.fillGaps (observationsPagedList.getList (), requestParam);
         
         return observationServiceHelper.groupByVariable (observations, requestParam);
      } catch (Exception e) {
         logger.error ("Error listing observations", e);
         throw new RuntimeException ("Could not list observations. See server logs.");
      }
   }

   /**
    * Obtains a list of derivedRange objects of given type referred to the given date 
    */
   public DerivedRangeDTO getDerivedInRange (ISharedConstants.DerivedRangeType derivedRangeType, Date searched, Integer stationId) {
      Date [] range = null;
      List <Observation> observations = null;
      Station station = null;
      Variable minimum = null;
      Variable average = null;
      Variable maximum = null;

      try {
         station = stationId == null ? stationPersistence.getOwnStation () : stationPersistence.findById (stationId);

         range = observationServiceHelper.getRange (derivedRangeType, searched);
         switch (derivedRangeType) {
            case AFTERNOON:
               minimum = variablePersistence.getByAcronym (IServerConstants.AFTERNOON_MINIMUM);
               average = variablePersistence.getByAcronym (IServerConstants.AFTERNOON_AVERAGE);
               maximum = variablePersistence.getByAcronym (IServerConstants.AFTERNOON_MAXIMUM);
               break;
            case DAY:
               minimum = variablePersistence.getByAcronym (IServerConstants.DAY_MINIMUM);
               average = variablePersistence.getByAcronym (IServerConstants.DAY_AVERAGE);
               maximum = variablePersistence.getByAcronym (IServerConstants.DAY_MAXIMUM);
               break;
            case EVENING:
               minimum = variablePersistence.getByAcronym (IServerConstants.EVENING_MINIMUM);
               average = variablePersistence.getByAcronym (IServerConstants.EVENING_AVERAGE);
               maximum = variablePersistence.getByAcronym (IServerConstants.EVENING_MAXIMUM);
               break;
            case MONTH:
               minimum = variablePersistence.getByAcronym (IServerConstants.MONTH_MINIMUM);
               average = variablePersistence.getByAcronym (IServerConstants.MONTH_AVERAGE);
               maximum = variablePersistence.getByAcronym (IServerConstants.MONTH_MAXIMUM);
               break;
            case MORNING:
               minimum = variablePersistence.getByAcronym (IServerConstants.MORNING_MINIMUM);
               average = variablePersistence.getByAcronym (IServerConstants.MORNING_AVERAGE);
               maximum = variablePersistence.getByAcronym (IServerConstants.MORNING_MAXIMUM);
               break;
            case NIGHT:
               minimum = variablePersistence.getByAcronym (IServerConstants.NIGHT_MINIMUM);
               average = variablePersistence.getByAcronym (IServerConstants.NIGHT_AVERAGE);
               maximum = variablePersistence.getByAcronym (IServerConstants.NIGHT_MAXIMUM);
               break;
         }
         observations = observationPersistence.getDerivedInRange (range [0], range [1], minimum, average, maximum);
         
         return observationServiceHelper.fillAndGroupAsRange (station, observations, range, minimum, average, maximum);
      } catch (Exception e) {
         logger.error ("Error listing derived observations in range", e);
         throw new RuntimeException ("Could not list derived observations in range. See server logs.");
      }
   }

   /**
    * Obtains a list of derivedRange objects of given type referred to the given date for graphic display
    * The difference is basically, that a wider range of observations is obtained: for example, for graphical
    * display of a month derivation, the month derived variables from the whole year shall be obtained
    */
   public List <DerivedRangeDTO> getDerivedInRangeForGraphics (ISharedConstants.DerivedRangeType derivedRangeType, Date searched, Integer stationId) {
      Date [] range = null;
      List <Observation> observations = null;
      Station station = null;
      Variable minimum = null;
      Variable average = null;
      Variable maximum = null;
      Calendar current = null;
      Date [] wideRange = new Date [2];
      List <DerivedRangeDTO> result = new ArrayList <DerivedRangeDTO> ();
      
      try {
         station = stationId == null ? stationPersistence.getOwnStation () : stationPersistence.findById (stationId);
         
         wideRange = observationServiceHelper.getWideRange (derivedRangeType, searched);
         logger.info ("Wide range for {}: {}-{}: ", derivedRangeType, wideRange [0], wideRange [1]);
         current = Calendar.getInstance ();
         current.setTime (wideRange [0]);
         
         while (current.getTimeInMillis () < wideRange [1].getTime ()) {
            range = observationServiceHelper.getRange (derivedRangeType, current.getTime ());
            switch (derivedRangeType) {
               case AFTERNOON:
                  minimum = variablePersistence.getByAcronym (IServerConstants.AFTERNOON_MINIMUM);
                  average = variablePersistence.getByAcronym (IServerConstants.AFTERNOON_AVERAGE);
                  maximum = variablePersistence.getByAcronym (IServerConstants.AFTERNOON_MAXIMUM);
                  break;
               case DAY:
                  minimum = variablePersistence.getByAcronym (IServerConstants.DAY_MINIMUM);
                  average = variablePersistence.getByAcronym (IServerConstants.DAY_AVERAGE);
                  maximum = variablePersistence.getByAcronym (IServerConstants.DAY_MAXIMUM);
                  break;
               case EVENING:
                  minimum = variablePersistence.getByAcronym (IServerConstants.EVENING_MINIMUM);
                  average = variablePersistence.getByAcronym (IServerConstants.EVENING_AVERAGE);
                  maximum = variablePersistence.getByAcronym (IServerConstants.EVENING_MAXIMUM);
                  break;
               case MONTH:
                  minimum = variablePersistence.getByAcronym (IServerConstants.MONTH_MINIMUM);
                  average = variablePersistence.getByAcronym (IServerConstants.MONTH_AVERAGE);
                  maximum = variablePersistence.getByAcronym (IServerConstants.MONTH_MAXIMUM);
                  break;
               case MORNING:
                  minimum = variablePersistence.getByAcronym (IServerConstants.MORNING_MINIMUM);
                  average = variablePersistence.getByAcronym (IServerConstants.MORNING_AVERAGE);
                  maximum = variablePersistence.getByAcronym (IServerConstants.MORNING_MAXIMUM);
                  break;
               case NIGHT:
                  minimum = variablePersistence.getByAcronym (IServerConstants.NIGHT_MINIMUM);
                  average = variablePersistence.getByAcronym (IServerConstants.NIGHT_AVERAGE);
                  maximum = variablePersistence.getByAcronym (IServerConstants.NIGHT_MAXIMUM);
                  break;
            }
            observations = observationPersistence.getDerivedInRange (range [0], range [1], minimum, average, maximum);
            
            result.add (observationServiceHelper.fillAndGroupAsRange (station, observations, range, minimum, average, maximum));
            
            logger.info ("Obtained derived for: " + current.getTime ());
            if (derivedRangeType.equals (DerivedRangeType.MONTH)) {
               current.add (Calendar.MONTH, 1);
            } else {
               current.add (Calendar.DAY_OF_YEAR, 1);
            }
         }
      } catch (Exception e) {
         logger.error ("Error listing derived observations in range", e);
         throw new RuntimeException ("Could not list derived observations in range. See server logs.");
      }
      return result;
   }
   
   /**
    * Obtain the start of the current day
    */
   public Date getTodayStart () {
      Calendar today = Calendar.getInstance ();
      
      today.set (Calendar.HOUR_OF_DAY, 0);
      today.set (Calendar.MINUTE, 0);
      today.set (Calendar.SECOND, 0);
      today.set (Calendar.MILLISECOND, 0);
      
      return today.getTime ();
   }
}
