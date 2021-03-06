package es.uned.grc.pfc.meteo.server.service;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.grc.pfc.meteo.server.model.RequestParam;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.model.StationModel;
import es.uned.grc.pfc.meteo.server.model.User;
import es.uned.grc.pfc.meteo.server.model.paged.StationPagedList;
import es.uned.grc.pfc.meteo.server.model.paged.StringPagedList;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationModelPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IUserPersistence;
import es.uned.grc.pfc.meteo.server.service.helper.StationServiceHelper;
import es.uned.grc.pfc.meteo.server.util.AuthInfo;
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
   private IStationModelPersistence stationModelPersistence = null;
   @Autowired
   private IObservationPersistence observationPersistence = null;
   @Autowired
   private IUserPersistence userPersistence = null;

   @Autowired
   private StationServiceHelper stationServiceHelper = null;
   @Autowired
   private AuthInfo authInfo = null;
   
   /**
    * Get the own station
    */
   public User getLoggedUser () {
      try {
         return authInfo.getLoggedUser ();
      } catch (Exception e) {
         logger.error ("Error getting logged user", e);
         throw new RuntimeException ("Could not get logged user. See server logs.");
      }
   }
   
   /**
    * Persits an user
    */
   public User saveUser (User user) {
      try {
         if (user.getPassword ().equals (ISharedConstants.SAMPLE_PASSWORD)) {
            user.setPassword (user.getTransientOldPassword ());
         } else {
            user.setPassword (DigestUtils.md5Hex (user.getPassword ()));
         }
         return userPersistence.saveOrMerge (user);
      } catch (Exception e) {
         logger.error ("Error saving user", e);
         throw new RuntimeException ("Could not save user. See server logs.");
      }
   }
   
   /**
    * Persists a station
    */
   public Station saveStation (Station station) {
      try {
         return stationPersistence.saveOrMerge (station);
      } catch (Exception e) {
         logger.error ("Error saving station", e);
         throw new RuntimeException ("Could not save station. See server logs.");
      }
   }
   
   /**
    * Get the own station
    */
   public Station getOwnStation () {
      try {
         return stationPersistence.getOwnStation (authInfo.getLoggedUserId ());
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
         return stationPersistence.getOwnStation (authInfo.getLoggedUserId (), includeLastObservations);
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
         return stationPersistence.getStationsInArea (minLatitude, minLongitude, maxLatitude, maxLongitude, true);
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
    * Obtains a list of stationModels
    */
   public List <StationModel> getStationModels () {
      try {
         return stationModelPersistence.findAll ();
      } catch (Exception e) {
         logger.error ("Error listing station models", e);
         throw new RuntimeException ("Could not list station models. See server logs.");
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
