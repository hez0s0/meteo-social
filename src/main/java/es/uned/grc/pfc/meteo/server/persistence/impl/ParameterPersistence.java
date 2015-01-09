package es.uned.grc.pfc.meteo.server.persistence.impl;

import org.springframework.stereotype.Repository;

import es.uned.grc.pfc.meteo.server.model.Parameter;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IParameterPersistence;

@Repository
public class ParameterPersistence extends AbstractPersistence <Integer, Parameter> implements IParameterPersistence {
   
}
