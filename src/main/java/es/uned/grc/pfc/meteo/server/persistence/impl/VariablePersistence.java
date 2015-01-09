package es.uned.grc.pfc.meteo.server.persistence.impl;

import org.springframework.stereotype.Repository;

import es.uned.grc.pfc.meteo.server.model.Variable;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IVariablePersistence;

@Repository
public class VariablePersistence extends AbstractPersistence <Integer, Variable> implements IVariablePersistence {
   
}
