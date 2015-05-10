package es.uned.grc.pfc.meteo.client.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.shared.locators.PersistenceEntityLocator;

@ProxyFor (value = Station.class, locator = PersistenceEntityLocator.class)
public interface IStationProxy extends IEntityProxy {
   Integer getId ();
   void setId (Integer id);
   
   String getName ();
   void setName (String name);
   
   Set <IParameterProxy> getParameters ();
   void setParameters (Set <IParameterProxy> parameters);
   
   Set <IVariableProxy> getVariables ();
   void setVariables (Set <IVariableProxy> variables);
   
   IStationModelProxy getStationModel ();
   void setStationModel (IStationModelProxy stationModel);
   
   Double getLatitude ();
   void setLatitude (Double latitude);
   
   Double getLongitude ();
   void setLongitude (Double longitude);
   
   Integer getHeight ();
   void setHeight (Integer height);
   
   Date getLastCollectedPeriod ();
   void setLastCollectedPeriod (Date lastCollectedPeriod);

   String getStreet ();
   void setStreet (String street);
   
   String getZip ();
   void setZip (String zip);
   
   String getCity ();
   void setCity (String city);
   
   String getCountry ();
   void setCountry (String country);
   
   List <IObservationProxy> getTransientLastObservations ();
   void setTransientLastObservations (List <IObservationProxy> transientLastObservations);
}
