package es.uned.grc.pfc.meteo.client.view.base;

import com.google.gwt.event.dom.client.DomEvent;

public interface IActionHandler {
   /** action was triggered on an element */
   void onAction (DomEvent <?> event, IHasActionHandlers source);
}
