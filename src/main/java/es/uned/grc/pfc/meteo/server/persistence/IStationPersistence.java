package es.uned.grc.pfc.meteo.server.persistence;

import es.uned.grc.pfc.meteo.server.model.Station;

public interface IStationPersistence extends IPersistence <Integer, Station> {

   Station getOwnStation ();
}
