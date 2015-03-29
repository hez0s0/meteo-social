package es.uned.grc.pfc.meteo.server.persistence;

import java.util.List;

import es.uned.grc.pfc.meteo.server.model.Station;

public interface IStationPersistence extends IPersistence <Integer, Station> {
   /** obtain the local station */
   Station getOwnStation ();
   /** obtain the local station with or without the last observations */
   Station getOwnStation (boolean includeLastObservations);
   /** obtain stations within a rectangle with or without the last observations */
   List <Station> getStationsInArea (double minLatitude, double minLongitude, double maxLatitude, double maxLongitude, boolean includeLastObservations);
}
