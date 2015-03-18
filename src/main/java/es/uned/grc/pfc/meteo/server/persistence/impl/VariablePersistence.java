package es.uned.grc.pfc.meteo.server.persistence.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import es.uned.grc.pfc.meteo.server.model.Variable;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IVariablePersistence;

@Repository
public class VariablePersistence extends AbstractPersistence <Integer, Variable> implements IVariablePersistence {
   
   @SuppressWarnings ("unchecked")
   @Override
   public List <Variable> getStationVariables (String filter, int stationId, boolean measuredOnly, boolean derivedOnly) {
      Criteria criteria = getBaseCriteria ();
      
      criteria.createCriteria ("station").add (Restrictions.idEq (stationId));
      if (measuredOnly) {
         criteria.add (Restrictions.eq ("internal", false));
      } else if (derivedOnly) {
         criteria.add (Restrictions.eq ("internal", true));
      }
      if (!StringUtils.isEmpty (filter)) {
         criteria.add (Restrictions.ilike ("name", asLike (filter)));
      }
      
      criteria.addOrder (Order.asc ("acronym"));
      
      return criteria.list ();
   }

   @Override
   public Variable getByAcronym (String acronym) {
      return (Variable) getBaseCriteria ()
                           .add (Restrictions.eq ("acronym", acronym))
                           .uniqueResult ();
   }
}
