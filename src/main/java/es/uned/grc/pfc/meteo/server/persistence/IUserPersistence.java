package es.uned.grc.pfc.meteo.server.persistence;

import es.uned.grc.pfc.meteo.server.model.User;

public interface IUserPersistence extends IPersistence <Integer, User> {
   public User getByUsername (String username);
}
