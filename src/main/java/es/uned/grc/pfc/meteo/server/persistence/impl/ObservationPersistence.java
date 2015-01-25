package es.uned.grc.pfc.meteo.server.persistence.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.RequestParamFilter;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;

@Repository
public class ObservationPersistence extends AbstractPersistence <Integer, Observation> implements IObservationPersistence {
   
   @Override
   protected void applyFilter (Criteria criteria, List <RequestParamFilter> filters, Map <String, Object> contextParams) {
      
   }

   @SuppressWarnings ("unchecked")
   @Override
   @Transactional (propagation = Propagation.REQUIRED)
   public List <Observation> getUncontrolled () {
      return getBaseCriteria ().add (Restrictions.isNull ("quality"))
                               .addOrder (Order.asc ("observed"))
                               .list ();
   }
}
