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
   Integer getMinimumDeriveBase ();
   void setMinimumDeriveBase (Integer minimumDeriveBase);
   Integer getMinimumDeriveIgnored ();
   void setMinimumDeriveIgnored (Integer minimumDeriveIgnored);
   Integer getMinimumDeriveExpected ();
   void setMinimumDeriveExpected (Integer minimumDeriveExpected);
   Integer getMaximumDeriveBase ();
   void setMaximumDeriveBase (Integer maximumDeriveBase);
   Integer getMaximumDeriveIgnored ();
   void setMaximumDeriveIgnored (Integer maximumDeriveIgnored);
   Integer getMaximumDeriveExpected ();
   void setMaximumDeriveExpected (Integer maximumDeriveExpected);
   Integer getAverageDeriveBase ();
   void setAverageDeriveBase (Integer averageDeriveBase);
   Integer getAverageDeriveIgnored ();
   void setAverageDeriveIgnored (Integer averageDeriveIgnored);
   Integer getAverageDeriveExpected ();
   void setAverageDeriveExpected (Integer averageDeriveExpected);
}
