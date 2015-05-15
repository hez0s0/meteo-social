package es.uned.grc.pfc.meteo.server.persistence;

import java.util.Collection;

import es.uned.grc.pfc.meteo.server.model.StationVariable;

public interface IStationVariablePersistence extends IPersistence <Integer, StationVariable> {
   StationVariable findStationVariable (int stationId, int variableId);
   StationVariable findStationVariable (int stationId, int variableId, Collection <StationVariable> stationVariables);
}
