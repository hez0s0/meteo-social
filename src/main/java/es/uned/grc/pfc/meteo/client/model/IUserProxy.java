package es.uned.grc.pfc.meteo.client.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.server.model.User;
import es.uned.grc.pfc.meteo.shared.locators.PersistenceEntityLocator;

@ProxyFor (value = User.class, locator = PersistenceEntityLocator.class)
public interface IUserProxy extends IEntityProxy {
   Integer getId ();
   void setId (Integer id);
   
   @NotNull (groups = Default.class)
   @Size (min = 1, max = 64)
   String getUsername ();
   void setUsername (String usernname);
   
   String getPassword ();
   void setPassword (String password);

   @NotNull (groups = Default.class)
   @Size (min = 1, max = 1024)
   String getFirstName ();
   void setFirstName (String firstName);

   @NotNull (groups = Default.class)
   @Size (min = 1, max = 1024)
   String getFamilyName ();
   void setFamilyName (String familyName);

   @NotNull (groups = Default.class)
   @Size (min = 1, max = 1024)
   String getEmail ();
   void setEmail (String email);
   
   boolean isEnabled ();
   void setEnabled (boolean enabled);
  
   boolean isEnableTwitter ();
   void setEnableTwitter (boolean twitter);
   
   void setConsumerKey (String consumerKey);
   String getConsumerKey ();

   void setConsumerSecret (String consumerSecret);
   String getConsumerSecret ();
   
   String getTransientRepeatPassword ();
   void setTransientRepeatPassword (String transientRepeatPassword);
   
   String getTransientOldPassword ();
   void setTransientOldPassword (String transientOldPassword);
}
