package es.uned.grc.pfc.meteo.server.persistence.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import es.uned.grc.pfc.meteo.server.model.User;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IUserPersistence;

@Repository
public class UserPersistence extends AbstractPersistence <Integer, User> implements IUserPersistence {

   @Override
   public User getByUsername (String username) {
      return (User) getBaseCriteria ().add (Restrictions.eq ("username", username)).setMaxResults (1).uniqueResult ();
   }
   
}
