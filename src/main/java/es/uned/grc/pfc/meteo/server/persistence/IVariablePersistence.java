package es.uned.grc.pfc.meteo.server.persistence;

import java.util.List;

import es.uned.grc.pfc.meteo.server.model.Variable;

public interface IVariablePersistence extends IPersistence <Integer, Variable> {

   List <Variable> getStationVariables (String filter, int stationId, boolean measuredOnly, boolean derivedOnly);

   Variable getByAcronym (String acronym);
}
