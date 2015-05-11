package es.uned.grc.pfc.meteo.server.persistence.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.uned.grc.pfc.meteo.server.job.station.IStationPlugin;
import es.uned.grc.pfc.meteo.server.model.RequestParamFilter;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.util.AuthInfo;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

@Repository
public class StationPersistence extends AbstractPersistence <Integer, Station> implements IStationPersistence {
   
   /** the radius of the Earth */
   double EARTH_RADIUS = 6371;
   
   @Autowired
   private IObservationPersistence observationPersistence = null;
   @Autowired
   private AuthInfo authInfo = null;
   
   @Override
   protected void applyDefaultSort (Criteria criteria, Map <String, Object> contextParams) {
      criteria.addOrder (Order.asc ("name"));
   }
   
   @Override
   protected void applyFilter (Criteria criteria, List <RequestParamFilter> filters, Map <String, Object> contextParams) {
      ISharedConstants.StationFilter property = null;
      double radius = 0.0;
      double lat = 0.0;
      double lon = 0.0;
      
      //first find the area, if any
      for (RequestParamFilter filter : filters) {
         if (filter.getParam () != null) {
            property = ISharedConstants.StationFilter.valueOf (filter.getParam ());
            
            if (!StringUtils.isEmpty (filter.getValue ())) {
               if (property.equals (ISharedConstants.StationFilter.RADIUS)) {
                  radius = Double.parseDouble (filter.getValue ());
               } else if (property.equals (ISharedConstants.StationFilter.LAT)) {
                  lat = Double.parseDouble (filter.getValue ());
               } else if (property.equals (ISharedConstants.StationFilter.LON)) {
                  lon = Double.parseDouble (filter.getValue ());
               }
            }
         }
      }
      
      //iterate and apply all the filters
      for (RequestParamFilter filter : filters) {
         if (filter.getParam () != null) {
            property = ISharedConstants.StationFilter.valueOf (filter.getParam ());
            
            if (!StringUtils.isEmpty (filter.getValue ())) {
               switch (property) {
                  case OWN:
                     criteria.createCriteria ("user").add (Restrictions.idEq (authInfo.getLoggedUserId ()));
                     break;
                  case RADIUS:
                     //a radius around given lat/lon: nothing to be done here, it affects the LAT/LON params
                     break;
                  case CITY:
                     criteria.add (Restrictions.ilike ("city", asLike (filter.getValue ())));
                     break;
                  case COUNTRY:
                     criteria.add (Restrictions.ilike ("country", asLike (filter.getValue ())));
                     break;
                  case LAT:
                     if (radius > 0) {
                        criteria.add (Restrictions.ge ("latitude", getMinLatitude (lat, radius)));
                        criteria.add (Restrictions.le ("latitude", getMaxLatitude (lat, radius)));
                     } else {
                        criteria.add (Restrictions.eq ("latitude", Double.parseDouble (filter.getValue ())));
                     }
                     break;
                  case LON:
                     if (radius > 0) {
                        criteria.add (Restrictions.ge ("longitude", getMinLongitude (lat, lon, radius)));
                        criteria.add (Restrictions.le ("longitude", getMaxLongitude (lat, lon, radius)));
                     } else {
                        criteria.add (Restrictions.eq ("longitude", Double.parseDouble (filter.getValue ())));
                     }
                     break;
                  case NAME:
                     criteria.add (Restrictions.ilike ("name", asLike (filter.getValue ())));
                     break;
                  case ZIP:
                     criteria.add (Restrictions.eq ("zip", filter.getValue ()));
                     break;
               }
            }
         }
      }
   }

   @Transactional (propagation = Propagation.REQUIRED)
   @Override
   public Station getOwnStation (Integer userId) {
      return getOwnStation (userId, false);
   }
   
   @Transactional (propagation = Propagation.REQUIRED)
   @Override
   public Station getOwnStation (Integer userId, boolean includeLastObservations) {
      Criteria criteria = getBaseCriteria ();
      
      criteria.createCriteria ("user").add (Restrictions.idEq (userId));
      if (!includeLastObservations) {
         criteria.setFetchMode ("parameters", FetchMode.JOIN);
         criteria.setFetchMode ("variables", FetchMode.JOIN);
      }
      Station station = (Station) criteria.uniqueResult ();
      if (includeLastObservations) {
         station.setTransientLastObservations (observationPersistence.getLastReceived (station.getId ()));
      }
      return station;
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List <Station> getStationsInArea (double minLatitude, double minLongitude, double maxLatitude, double maxLongitude, boolean includeLastObservations) {
      List <Station> stations = (List <Station>) getBaseCriteria ()
                                                    .add (Restrictions.ge ("latitude", minLatitude))
                                                    .add (Restrictions.le ("latitude", maxLatitude))
                                                    .add (Restrictions.ge ("longitude", minLongitude))
                                                    .add (Restrictions.le ("longitude", maxLongitude))
                                                 .list ();
      if (includeLastObservations) {
         //TODO load more efficiently
         for (Station station : stations) {
            station.setTransientLastObservations (observationPersistence.getLastReceived (station.getId ()));
         }
      }
      return stations;
   }
   
   @Override
   public IStationPlugin getStationPlugin (int stationId) {
      return getStationPlugin (findById (stationId));
   }
   
   @Override
   public IStationPlugin getStationPlugin (Station station) {
      if (station != null) {
         try {
            return station.getStationModel () != null ? (IStationPlugin) Class.forName (station.getStationModel ().getClassName ()).newInstance () : null;
         } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException (cnfe);
         } catch (InstantiationException ie) {
            throw new RuntimeException (ie);
         } catch (IllegalAccessException iae) {
            throw new RuntimeException (iae);
         }
      } else {
         return null;
      }
   }
   
   private double getMinLatitude (double lat, double radius) {
      return lat + Math.toDegrees (radius / EARTH_RADIUS);
   }

   private double getMaxLatitude (double lat, double radius) {
      return lat - Math.toDegrees (radius / EARTH_RADIUS);
   }

   private double getMinLongitude (double lat, double lon, double radius) {
      return lon - Math.toDegrees (radius / EARTH_RADIUS / Math.cos (Math.toRadians (lat)));
   }

   private double getMaxLongitude (double lat, double lon, double radius) {
      return lon + Math.toDegrees (radius / EARTH_RADIUS / Math.cos (Math.toRadians (lat)));
   }
}
