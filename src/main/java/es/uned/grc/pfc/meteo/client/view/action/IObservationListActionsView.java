package es.uned.grc.pfc.meteo.client.view.action;

import com.google.gwt.user.client.ui.UIObject;

import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;
import es.uned.grc.pfc.meteo.client.view.base.IView;

public interface IObservationListActionsView extends IView {
   /** get the element that the user clicks to go to the table representation */
   IHasActionHandlers getTableHandler ();
   /** get the element that the user clicks to go to the graphics representation */
   IHasActionHandlers getGraphicsHandler ();
   /** get the element that the user clicks to display derived variables */
   IHasActionHandlers getDerivedHandler ();
   /** get the element that the user clicks to display the station map */
   IHasActionHandlers getMapHandler ();

   UIObject getTablePanel ();
   UIObject getGraphicsPanel ();
   UIObject getDerivedPanel ();
   UIObject getMapPanel ();
}
