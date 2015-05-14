package es.uned.grc.pfc.meteo.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface IUserSetupEventHandler extends EventHandler {
   void onUserSetupEdit (UserSetupEvent userSetupEvent);
}
