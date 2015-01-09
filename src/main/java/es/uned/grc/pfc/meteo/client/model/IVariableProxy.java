package es.uned.grc.pfc.meteo.client.model;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.server.model.Variable;
import es.uned.grc.pfc.meteo.shared.locators.PersistenceEntityLocator;

@ProxyFor (value = Variable.class, locator = PersistenceEntityLocator.class)
public interface IVariableProxy extends IEntityProxy {
   Integer getId ();
   void setId (Integer id);
   
   String getName ();
   void setName (String name);
   
   String getDescription ();
   void setDescription (String description);
}
