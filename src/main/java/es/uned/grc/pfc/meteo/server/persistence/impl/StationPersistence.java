package es.uned.grc.pfc.meteo.server.persistence.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.uned.grc.pfc.meteo.server.model.RequestParamFilter;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;

@Repository
public class StationPersistence extends AbstractPersistence <Integer, Station> implements IStationPersistence {

   @Transactional (propagation = Propagation.REQUIRED)
   @Override
   public Station getOwnStation () {
      return (Station) getBaseCriteria ()
                          .add (Restrictions.eq ("own", true))
                       .setFetchMode ("parameters", FetchMode.JOIN)
                       .setFetchMode ("variables", FetchMode.JOIN)
                       .uniqueResult ();
   }
   
   @Override
   protected void applyFilter (Criteria criteria, List <RequestParamFilter> filters, Map <String, Object> contextParams) {
      
   }
}
