package es.uned.grc.pfc.meteo.server.persistence.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;

@Repository
public class StationPersistence extends AbstractPersistence <Integer, Station> implements IStationPersistence {

   @Autowired
   private IObservationPersistence observationPersistence = null;
   
   @Transactional (propagation = Propagation.REQUIRED)
   @Override
   public Station getOwnStation () {
      return getOwnStation (false);
   }
   
   @Transactional (propagation = Propagation.REQUIRED)
   @Override
   public Station getOwnStation (boolean includeLastObservations) {
      Criteria criteria = getBaseCriteria ()
                             .add (Restrictions.eq ("own", true));
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
}
