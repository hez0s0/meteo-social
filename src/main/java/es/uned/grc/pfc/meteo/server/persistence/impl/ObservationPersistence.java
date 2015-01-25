package es.uned.grc.pfc.meteo.server.persistence.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.RequestParamFilter;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;

@Repository
public class ObservationPersistence extends AbstractPersistence <Integer, Observation> implements IObservationPersistence {
   
   @Override
   protected void applyFilter (Criteria criteria, List <RequestParamFilter> filters, Map <String, Object> contextParams) {
      
   }

   @Override
   public List <Observation> getUncontrolled () {
      // TODO Auto-generated method stub
      return null;
   }
}
