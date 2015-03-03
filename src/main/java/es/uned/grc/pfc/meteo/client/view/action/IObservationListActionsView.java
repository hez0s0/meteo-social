package es.uned.grc.pfc.meteo.client.view.action;

import com.google.gwt.user.client.ui.UIObject;

import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;
import es.uned.grc.pfc.meteo.client.view.base.IView;

public interface IObservationListActionsView extends IView {
   /** get the element that the user clicks to go to the graphics representation */
   IHasActionHandlers getGraphicsHandler ();
   /** get the element that the user clicks to display derived variables */
   IHasActionHandlers getDerivedHandler ();

   UIObject getGraphicsPanel ();
   UIObject getDerivedPanel ();
}
