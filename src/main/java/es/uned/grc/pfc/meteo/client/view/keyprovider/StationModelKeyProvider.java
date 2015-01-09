package es.uned.grc.pfc.meteo.client.view.keyprovider;

import com.google.gwt.view.client.ProvidesKey;

import es.uned.grc.pfc.meteo.client.model.IStationModelProxy;

public class StationModelKeyProvider implements ProvidesKey <IStationModelProxy> {
   @Override
   public Object getKey (IStationModelProxy stationModelProxy) {
      return (stationModelProxy != null) ? stationModelProxy.getId () : null;
   }
}
