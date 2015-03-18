package es.uned.grc.pfc.meteo.server.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.grc.pfc.meteo.client.util.DerivedUtils;
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
      Date [] range = new Date [2];
      List <Observation> observations = null;
      Station station = null;
      Variable minimum = null;
      Variable average = null;
      Variable maximum = null;

      try {
         station = stationId == null ? stationPersistence.getOwnStation () : stationPersistence.findById (stationId);
         
         switch (derivedRangeType) {
            case AFTERNOON:
               range [0] = DerivedUtils.getAfternoonIni (searched);
               range [1] = DerivedUtils.getAfternoonEnd (searched);
               minimum = variablePersistence.getByAcronym (IServerConstants.AFTERNOON_MINIMUM);
               average = variablePersistence.getByAcronym (IServerConstants.AFTERNOON_AVERAGE);
               maximum = variablePersistence.getByAcronym (IServerConstants.AFTERNOON_MAXIMUM);
               break;
            case DAY:
               range [0] = DerivedUtils.getDayIni (searched);
               range [1] = DerivedUtils.getDayEnd (searched);
               minimum = variablePersistence.getByAcronym (IServerConstants.DAY_MINIMUM);
               average = variablePersistence.getByAcronym (IServerConstants.DAY_AVERAGE);
               maximum = variablePersistence.getByAcronym (IServerConstants.DAY_MAXIMUM);
               break;
            case EVENING:
               range [0] = DerivedUtils.getEveningIni (searched);
               range [1] = DerivedUtils.getEveningEnd (searched);
               minimum = variablePersistence.getByAcronym (IServerConstants.EVENING_MINIMUM);
               average = variablePersistence.getByAcronym (IServerConstants.EVENING_AVERAGE);
               maximum = variablePersistence.getByAcronym (IServerConstants.EVENING_MAXIMUM);
               break;
            case MONTH:
               range [0] = DerivedUtils.getMonthIni (searched);
               range [1] = DerivedUtils.getMonthEnd (searched);
               minimum = variablePersistence.getByAcronym (IServerConstants.MONTH_MINIMUM);
               average = variablePersistence.getByAcronym (IServerConstants.MONTH_AVERAGE);
               maximum = variablePersistence.getByAcronym (IServerConstants.MONTH_MAXIMUM);
               break;
            case MORNING:
               range [0] = DerivedUtils.getMorningIni (searched);
               range [1] = DerivedUtils.getMorningEnd (searched);
               minimum = variablePersistence.getByAcronym (IServerConstants.MORNING_MINIMUM);
               average = variablePersistence.getByAcronym (IServerConstants.MORNING_AVERAGE);
               maximum = variablePersistence.getByAcronym (IServerConstants.MORNING_MAXIMUM);
               break;
            case NIGHT:
               range [0] = DerivedUtils.getNightIni (searched);
               range [1] = DerivedUtils.getNightEnd (searched);
               minimum = variablePersistence.getByAcronym (IServerConstants.NIGHT_MINIMUM);
               average = variablePersistence.getByAcronym (IServerConstants.NIGHT_AVERAGE);
               maximum = variablePersistence.getByAcronym (IServerConstants.NIGHT_MAXIMUM);
               break;
         }
         observations = observationPersistence.getDerivedInRange (range [0], range [1]);
         
         return observationServiceHelper.fillAndGroupAsRange (station, observations, range, minimum, average, maximum);
      } catch (Exception e) {
         logger.error ("Error listing derived observations in range", e);
         throw new RuntimeException ("Could not list derived observations in range. See server logs.");
      }
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
