package es.uned.grc.pfc.meteo.client.model;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.server.model.StationModel;
import es.uned.grc.pfc.meteo.shared.locators.PersistenceEntityLocator;

@ProxyFor (value = StationModel.class, locator = PersistenceEntityLocator.class)
public interface IStationModelProxy extends IEntityProxy {
   Integer getId ();
   void setId (Integer id);
   
   String getName ();
   void setName (String name);
   
   String getDescription ();
   void setDescription (String description);
}
