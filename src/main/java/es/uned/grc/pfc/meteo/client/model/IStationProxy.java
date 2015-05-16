package es.uned.grc.pfc.meteo.client.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.shared.locators.PersistenceEntityLocator;

@ProxyFor (value = Station.class, locator = PersistenceEntityLocator.class)
public interface IStationProxy extends IEntityProxy {
   Integer getId ();
   void setId (Integer id);

   @NotNull (groups = Default.class)
   @Size (min = 1, max = 64)
   String getName ();
   void setName (String name);
   
   Set <IParameterProxy> getParameters ();
   void setParameters (Set <IParameterProxy> parameters);
   
   Set <IStationVariableProxy> getStationVariables ();
   void setStationVariables (Set <IStationVariableProxy> stationVariables);
   
   IStationModelProxy getStationModel ();
   void setStationModel (IStationModelProxy stationModel);

   @NotNull (groups = Default.class)
   Double getLatitude ();
   void setLatitude (Double latitude);

   @NotNull (groups = Default.class)
   Double getLongitude ();
   void setLongitude (Double longitude);

   @NotNull (groups = Default.class)
   Integer getHeight ();
   void setHeight (Integer height);
   
   Date getLastCollectedPeriod ();
   void setLastCollectedPeriod (Date lastCollectedPeriod);

   @Size (min = 0, max = 1024)
   String getStreet ();
   void setStreet (String street);

   @Size (min = 0, max = 10)
   String getZip ();
   void setZip (String zip);

   @Size (min = 0, max = 1024)
   String getCity ();
   void setCity (String city);

   @Size (min = 1, max = 1024)
   String getCountry ();
   void setCountry (String country);
   
   List <IObservationProxy> getTransientLastObservations ();
   void setTransientLastObservations (List <IObservationProxy> transientLastObservations);
}
