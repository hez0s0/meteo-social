package es.uned.grc.pfc.meteo.server.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.grc.pfc.meteo.server.model.RequestParam;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.model.paged.StationPagedList;
import es.uned.grc.pfc.meteo.server.model.paged.StringPagedList;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.service.helper.StationServiceHelper;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

/**
 * Service to manage stations
 * To be wrapped and invoked through RequestFactory via a twin context.
 */
@Service
public class StationService {

   private static Logger logger = LoggerFactory.getLogger (StationService.class);
   
   @Autowired
   private IStationPersistence stationPersistence = null;
   @Autowired
   private IObservationPersistence observationPersistence = null;

   @Autowired
   private StationServiceHelper stationServiceHelper = null;
   
   /**
    * Get the own station
    */
   public Station getOwnStation () {
      try {
         return stationPersistence.getOwnStation ();
      } catch (Exception e) {
         logger.error ("Error getting own station", e);
         throw new RuntimeException ("Could not get own station. See server logs.");
      }
   }
   
   /**
    * Get the own station with or without the last observations
    */
   public Station getOwnStation (boolean includeLastObservations) {
      try {
         return stationPersistence.getOwnStation (includeLastObservations);
      } catch (Exception e) {
         logger.error ("Error getting own station", e);
         throw new RuntimeException ("Could not get own station. See server logs.");
      }
   }
   
   /**
    * Get the station by id
    */
   public Station getStationById (int stationId) {
      try {
         return stationPersistence.findById (stationId);
      } catch (Exception e) {
         logger.error ("Error getting station by id", e);
         throw new RuntimeException ("Could not get station by id. See server logs.");
      }
   }
   
   /**
    * Obtained the list of stations contained within a rectagle
    */
   public List <Station> getStationsInArea (double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
      try {
         Station own = stationPersistence.getOwnStation (true);
         List <Station> stations = stationPersistence.getStationsInArea (minLatitude, minLongitude, maxLatitude, maxLongitude, true);
         stations.remove (own);
         return stations;
      } catch (Exception e) {
         logger.error ("Error listin stations in an area", e);
         throw new RuntimeException ("Could not list stations in an area. See server logs.");
      }
   }
   
   /**
    * Obtains a list of stations for the given filter
    */
   public StationPagedList getStations (RequestParam requestParam, boolean withLastObservations) {
      try {
         StationPagedList result = new StationPagedList (stationPersistence.getList (requestParam));
         
         if (withLastObservations) {
            for (Station station : result.getList ()) {
               station.setTransientLastObservations (observationPersistence.getLastReceived (station.getId ()));
            }
         }
         
         return result;
      } catch (Exception e) {
         logger.error ("Error listing stations", e);
         throw new RuntimeException ("Could not list stations. See server logs.");
      }
   }
   
   /**
    * Obtains a list of cities for the given filter
    */
   public StringPagedList getCities (String filter) {
      return stationServiceHelper.getStationProperties (filter, ISharedConstants.StationFilter.CITY);
   }
   
   /**
    * Obtains a list of countries for the given filter
    */
   public StringPagedList getCountries (String filter) {
      return stationServiceHelper.getStationProperties (filter, ISharedConstants.StationFilter.COUNTRY);
   }
}
