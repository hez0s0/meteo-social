package es.uned.grc.pfc.meteo.server.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.RequestParam;
import es.uned.grc.pfc.meteo.server.model.paged.PagedList;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
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
   private ObservationServiceHelper observationServiceHelper = null;
   
   /**
    * Obtains a list of observations for the given filter. If a start and end date
    * is provided, it fills all the possible gaps by pushing Observations with null values
    * for every period within the range and every variable.
    * IMPORTANT: this method assumes that 1 station is provided as filter, either by id
    * or by marking the own station flag !! 
    */
   public List <Observation> getObservations (RequestParam requestParam) {
      PagedList <Integer, Observation> observations =  null;
      try {

         if ( (StringUtils.isEmpty (observationServiceHelper.findFilterValue (requestParam, ISharedConstants.ObservationFilter.OWN)))
               && (StringUtils.isEmpty (observationServiceHelper.findFilterValue (requestParam, ISharedConstants.ObservationFilter.STATION_ID))) ) {
            throw new RuntimeException ("Either the 'own' flag must be active, or the station id must contain a value in order to run this method ");
         }
         observations = observationPersistence.getList (requestParam);
         
         return observationServiceHelper.fillGaps (observations.getList (), requestParam);
      } catch (Exception e) {
         logger.error ("Error listing observations", e);
         throw new RuntimeException ("Could not list observations. See server logs.");
      }
   }
}
