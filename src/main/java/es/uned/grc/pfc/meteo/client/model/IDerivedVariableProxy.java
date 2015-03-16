package es.uned.grc.pfc.meteo.client.model;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import es.uned.grc.pfc.meteo.server.dto.DerivedVariableDTO;

@ProxyFor (DerivedVariableDTO.class)
public interface IDerivedVariableProxy extends ValueProxy {
   IVariableProxy getVariable ();
   void setVariable (IVariableProxy variable);
   String getMinimum ();
   void setMinimum (String minimum);
   String getMaximum ();
   void setMaximum (String maximum);
   String getAverage ();
   void setAverage (String average);
}
