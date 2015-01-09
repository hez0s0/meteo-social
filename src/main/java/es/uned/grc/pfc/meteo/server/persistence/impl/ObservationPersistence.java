package es.uned.grc.pfc.meteo.server.persistence.impl;

import org.springframework.stereotype.Repository;

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;

@Repository
public class ObservationPersistence extends AbstractPersistence <Integer, Observation> implements IObservationPersistence {
   
}
