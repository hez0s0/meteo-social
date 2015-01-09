package es.uned.grc.pfc.meteo.client.view.base;

import com.google.gwt.event.shared.HandlerRegistration;

public interface IHasActionHandlers {
   
   /** adds a handler for click events, as well as action keys typing (space, enter, ...) */
   HandlerRegistration addActionHandler (final IActionHandler handler);
   
   /** marks the action as completed, so that further clicks are allowed */
   void setCompleted ();
}
