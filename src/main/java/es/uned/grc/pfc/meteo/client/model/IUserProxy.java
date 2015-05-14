package es.uned.grc.pfc.meteo.client.model;

import javax.validation.constraints.NotNull;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.server.model.User;
import es.uned.grc.pfc.meteo.shared.locators.PersistenceEntityLocator;

@ProxyFor (value = User.class, locator = PersistenceEntityLocator.class)
public interface IUserProxy extends IEntityProxy {
   Integer getId ();
   void setId (Integer id);
   
   @NotNull
   String getUsername ();
   void setUsername (String usernname);
   
   String getPassword ();
   void setPassword (String password);

   @NotNull
   String getFirstName ();
   void setFirstName (String firstName);

   @NotNull
   String getFamilyName ();
   void setFamilyName (String familyName);

   @NotNull
   String getEmail ();
   void setEmail (String email);
   
   boolean isEnabled ();
   void setEnabled (boolean enabled);
}
