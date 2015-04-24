package es.uned.grc.pfc.meteo.server.persistence;

import java.util.Map;
import java.util.Set;

import es.uned.grc.pfc.meteo.server.model.Parameter;

public interface IParameterPersistence extends IPersistence <Integer, Parameter> {
   Map <String, String> asMap (Set <Parameter> parameters);
}
