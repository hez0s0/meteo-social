package es.uned.grc.pfc.meteo.client.model;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.server.model.Parameter;
import es.uned.grc.pfc.meteo.shared.locators.PersistenceEntityLocator;

@ProxyFor (value = Parameter.class, locator = PersistenceEntityLocator.class)
public interface IParameterProxy extends IEntityProxy {
   Integer getId ();
   void setId (Integer id);
   
   String getName ();
   void setName (String name);
   
   String getDescription ();
   void setDescription (String description);
   
   String getValue ();
   void setValue (String value);
   
   String getDefaultValue ();
   void setDefaultValue (String defaultValue);
   
   int getInternal ();
   void setInternal (int internal);
   
   IStationProxy getStation ();
   void setStation (IStationProxy station);
}
