package es.uned.grc.pfc.meteo.client.model;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.shared.locators.PersistenceEntityLocator;

@ProxyFor (value = Observation.class, locator = PersistenceEntityLocator.class)
public interface IObservationProxy extends IEntityProxy {
   Integer getId ();
   void setId (Integer id);
   
   IVariableProxy getVariable ();
   void setVariable (IVariableProxy variable);
   
   IStationProxy getStation ();
   void setStation (IStationProxy station);
   
   String getValue ();
   void setValue (String value);
   
   Date getObserved ();
   void setObserved (Date observed);
   
   Date getReceived ();
   void setReceived (Date received);
   
   Date getSent ();
   void setSent (Date sent);
}
