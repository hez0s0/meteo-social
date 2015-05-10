package es.uned.grc.pfc.meteo.server.persistence;

import java.util.List;

import es.uned.grc.pfc.meteo.server.job.station.IStationPlugin;
import es.uned.grc.pfc.meteo.server.model.Station;

public interface IStationPersistence extends IPersistence <Integer, Station> {
   /** obtain the local station of an user */
   Station getOwnStation (Integer userId);
   /** obtain the local station of an user with or without the last observations */
   Station getOwnStation (Integer userId, boolean includeLastObservations);
   /** obtain stations within a rectangle with or without the last observations */
   List <Station> getStationsInArea (double minLatitude, double minLongitude, double maxLatitude, double maxLongitude, boolean includeLastObservations);
   IStationPlugin getStationPlugin (int stationId);
   IStationPlugin getStationPlugin (Station station);
}
