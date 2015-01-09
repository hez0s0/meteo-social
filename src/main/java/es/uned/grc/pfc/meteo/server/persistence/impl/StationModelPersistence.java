package es.uned.grc.pfc.meteo.server.persistence.impl;

import org.springframework.stereotype.Repository;

import es.uned.grc.pfc.meteo.server.model.StationModel;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationModelPersistence;

@Repository
public class StationModelPersistence extends AbstractPersistence <Integer, StationModel> implements IStationModelPersistence {
   
}
