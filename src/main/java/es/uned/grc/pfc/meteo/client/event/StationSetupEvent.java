package es.uned.grc.pfc.meteo.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class StationSetupEvent extends GwtEvent <IStationSetupEventHandler> {
   
   public static Type <IStationSetupEventHandler> TYPE = new Type <IStationSetupEventHandler> ();

   @Override
   protected void dispatch (IStationSetupEventHandler handler) {
      handler.onStationSetupEdit (this);
   }

   @Override
   public Type <IStationSetupEventHandler> getAssociatedType () {
      return TYPE;
   }

}