package es.uned.grc.pfc.meteo.server.persistence.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import es.uned.grc.pfc.meteo.server.model.User;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IUserPersistence;

@Repository
public class UserPersistence extends AbstractPersistence <Integer, User> implements IUserPersistence {

   private List <User> allUsers = null;
   
   @Override
   public User getByUsername (String username) {
      User result = null;
      
      if (allUsers == null) {
         allUsers = findAll ();
      }
      for (User user : allUsers) {
         if (user.getUsername ().equals (username)) {
            return user;
         }
      }
      //if not cached, query for it and add it to the cache
      result = (User) getBaseCriteria ().add (Restrictions.eq ("username", username)).setMaxResults (1).uniqueResult ();
      allUsers.add (result);
      
      return result;
   }
   
}
