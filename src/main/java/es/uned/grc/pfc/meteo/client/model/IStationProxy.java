package es.uned.grc.pfc.meteo.client.model;

import java.util.Date;
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
   
   Date getLastCollectedPeriod ();
   void setLastCollectedPeriod (Date lastCollectedPeriod);
   
   Boolean getOwn ();
   void setOwn (Boolean own);
}
