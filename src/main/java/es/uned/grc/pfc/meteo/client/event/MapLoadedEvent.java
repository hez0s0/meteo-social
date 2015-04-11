package es.uned.grc.pfc.meteo.client.event;

import com.google.gwt.event.shared.GwtEvent;

import es.uned.grc.pfc.meteo.client.model.IStationProxy;

public class MapLoadedEvent extends GwtEvent <IMapLoadedEventHandler> {
   
   public static Type <IMapLoadedEventHandler> TYPE = new Type <IMapLoadedEventHandler> ();

   private IStationProxy centerStation = null;
   
   public MapLoadedEvent (IStationProxy centerStation) {
      this.centerStation = centerStation;
   }
   
   @Override
   protected void dispatch (IMapLoadedEventHandler handler) {
      handler.onMapLoaded (this);
   }

   @Override
   public Type <IMapLoadedEventHandler> getAssociatedType () {
      return TYPE;
   }

   public IStationProxy getCenterStation () {
      return centerStation;
   }

   public void setCenterStation (IStationProxy centerStation) {
      this.centerStation = centerStation;
   }  
}
