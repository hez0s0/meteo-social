package es.uned.grc.pfc.meteo.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface IMessageChangeEventHandler extends EventHandler {
   void onMessageChange (MessageChangeEvent messageChangeEvent);
}
