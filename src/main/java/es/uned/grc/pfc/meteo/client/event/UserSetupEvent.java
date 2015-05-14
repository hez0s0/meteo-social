package es.uned.grc.pfc.meteo.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class UserSetupEvent extends GwtEvent <IUserSetupEventHandler> {
   
   public static Type <IUserSetupEventHandler> TYPE = new Type <IUserSetupEventHandler> ();

   @Override
   protected void dispatch (IUserSetupEventHandler handler) {
      handler.onUserSetupEdit (this);
   }

   @Override
   public Type <IUserSetupEventHandler> getAssociatedType () {
      return TYPE;
   }

}