package es.uned.grc.pfc.meteo.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface IStationSetupEventHandler extends EventHandler {
   void onStationSetupEdit (StationSetupEvent stationSetupEvent);
}
