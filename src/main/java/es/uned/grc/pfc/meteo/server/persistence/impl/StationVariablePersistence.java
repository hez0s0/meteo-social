package es.uned.grc.pfc.meteo.server.persistence.impl;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import es.uned.grc.pfc.meteo.server.model.StationVariable;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationVariablePersistence;

@Repository
public class StationVariablePersistence extends AbstractPersistence <Integer, StationVariable> implements IStationVariablePersistence {

   @Override
   public StationVariable findStationVariable (int stationId, int variableId) {
      Criteria criteria = getBaseCriteria ();
      criteria.createCriteria ("station").add (Restrictions.idEq (stationId));
      criteria.createCriteria ("variable").add (Restrictions.idEq (variableId));
      return (StationVariable) criteria.setMaxResults (1).uniqueResult ();
   }

   @Override
   public StationVariable findStationVariable (int stationId, int variableId, Collection <StationVariable> stationVariables) {
      for (StationVariable stationVariable : stationVariables) {
         if (stationVariable.getStation ().getId () == stationId && stationVariable.getVariable ().getId () == variableId) {
            return stationVariable;
         }
      }
      return null;
   }
}
