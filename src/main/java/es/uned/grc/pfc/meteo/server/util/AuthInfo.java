package es.uned.grc.pfc.meteo.server.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.uned.grc.pfc.meteo.server.model.User;
import es.uned.grc.pfc.meteo.server.persistence.IUserPersistence;

@Component
public class AuthInfo {
   
   @Autowired
   private IUserPersistence userPersistence = null;

   @Transactional (propagation = Propagation.REQUIRED)
   public int getLoggedUserId () {
      return getLoggedUser ().getId ();
   }
   
   @Transactional (propagation = Propagation.REQUIRED)
   public User getLoggedUser () {
      UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();
      
      return userPersistence.getByUsername (userDetails.getUsername ());
   }
}
