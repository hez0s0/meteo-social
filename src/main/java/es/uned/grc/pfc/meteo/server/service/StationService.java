package es.uned.grc.pfc.meteo.server.service;

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
}
