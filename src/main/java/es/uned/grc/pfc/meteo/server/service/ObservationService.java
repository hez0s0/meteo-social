package es.uned.grc.pfc.meteo.server.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.grc.pfc.meteo.server.dto.ObservationBlockDTO;
import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.RequestParam;
import es.uned.grc.pfc.meteo.server.model.paged.PagedList;
import es.uned.grc.pfc.meteo.server.model.paged.VariablePagedList;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IVariablePersistence;
import es.uned.grc.pfc.meteo.server.service.helper.ObservationServiceHelper;
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
   public List <ObservationBlockDTO> getObservations (RequestParam requestParam) {
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
