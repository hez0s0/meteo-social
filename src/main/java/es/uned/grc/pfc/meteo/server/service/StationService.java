package es.uned.grc.pfc.meteo.server.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;

/**
 * Service to manage stations
 * To be wrapped and invoked through RequestFactory via a twin context.
 */
@Service
public class StationService {

   private static Logger logger = LoggerFactory.getLogger (StationService.class);
   
   @Autowired
   private IStationPersistence stationPersistence = null;
   
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
         //TODO: consider geo rectangle
         Station own = stationPersistence.getOwnStation ();
         List <Station> stations = stationPersistence.findAll ();
         stations.remove (own);
         return stations;
      } catch (Exception e) {
         logger.error ("Error listin stations in an area", e);
         throw new RuntimeException ("Could not list stations in an area. See server logs.");
      }
   }
}
