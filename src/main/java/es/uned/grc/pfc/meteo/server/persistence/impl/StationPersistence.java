package es.uned.grc.pfc.meteo.server.persistence.impl;

import org.springframework.stereotype.Repository;

import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;

@Repository
public class StationPersistence extends AbstractPersistence <Integer, Station> implements IStationPersistence {

   @Override
   public Station getOwnStation () {
      // TODO Auto-generated method stub
      return null;
   }
}
