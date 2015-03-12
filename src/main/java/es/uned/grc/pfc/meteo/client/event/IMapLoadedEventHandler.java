package es.uned.grc.pfc.meteo.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface IMapLoadedEventHandler extends EventHandler {
   void onMapLoaded (MapLoadedEvent event);
}
