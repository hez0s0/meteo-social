package es.uned.grc.pfc.meteo.client.model;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.server.model.User;
import es.uned.grc.pfc.meteo.shared.locators.PersistenceEntityLocator;

@ProxyFor (value = User.class, locator = PersistenceEntityLocator.class)
public interface IUserProxy extends IEntityProxy {
   Integer getId ();
   void setId (Integer id);
   
   String getUsername ();
   void setUsername (String usernname);
   
   String getPassword ();
   void setPassword (String password);
   
   String getFirstName ();
   void setFirstName (String firstName);
   
   String getFamilyName ();
   void setFamilyName (String familyName);
   
   String getEmail ();
   void setEmail (String email);
   
   boolean isEnabled ();
   void setEnabled (boolean enabled);
}
