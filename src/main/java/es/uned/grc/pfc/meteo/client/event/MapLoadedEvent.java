package es.uned.grc.pfc.meteo.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class MapLoadedEvent extends GwtEvent <IMapLoadedEventHandler> {
   
   public static Type <IMapLoadedEventHandler> TYPE = new Type <IMapLoadedEventHandler> ();

   @Override
   protected void dispatch (IMapLoadedEventHandler handler) {
      handler.onMapLoaded (this);
   }

   @Override
   public Type <IMapLoadedEventHandler> getAssociatedType () {
      return TYPE;
   }  
}
