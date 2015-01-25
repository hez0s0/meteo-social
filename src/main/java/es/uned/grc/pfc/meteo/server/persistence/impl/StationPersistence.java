package es.uned.grc.pfc.meteo.server.persistence.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import es.uned.grc.pfc.meteo.server.model.RequestParamFilter;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;

@Repository
public class StationPersistence extends AbstractPersistence <Integer, Station> implements IStationPersistence {

   @Override
   public Station getOwnStation () {
      return (Station) getBaseCriteria ()
                          .add (Restrictions.eq ("own", true))
                       .uniqueResult ();
   }
   
   @Override
   protected void applyFilter (Criteria criteria, List <RequestParamFilter> filters, Map <String, Object> contextParams) {
      
   }
}
