package es.uned.grc.pfc.meteo.server.persistence.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import es.uned.grc.pfc.meteo.server.model.Parameter;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IParameterPersistence;

@Repository
public class ParameterPersistence extends AbstractPersistence <Integer, Parameter> implements IParameterPersistence {

   @Override
   public Map <String, String> asMap (Set <Parameter> parameters) {
      Map <String, String> configuredParameters = new HashMap <String, String> (parameters.size ());
      
      for (Parameter parameter : parameters) {
         if (parameter.getValue () != null) {
            configuredParameters.put (parameter.getName (), parameter.getValue ());
         } else {
            configuredParameters.put (parameter.getName (), parameter.getDefaultValue ());
         }
      }
      
      return configuredParameters;
   }
}
