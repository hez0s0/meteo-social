package es.uned.grc.pfc.meteo.client.view.action;

import com.google.gwt.user.client.ui.UIObject;

import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;
import es.uned.grc.pfc.meteo.client.view.base.IView;

public interface IStationMapActionsView extends IView {
   void setInput (IRequestFactory requestFactory);

   /** get the element that the user clicks to go to the table representation */
   IHasActionHandlers getTableHandler ();
   /** get the element that the user clicks to display the station search form */
   IHasActionHandlers getStationSearchHandler ();

   UIObject getTablePanel ();
   UIObject getStationSearchPanel ();
   UIObject getStationFormPanel ();

   void clearSearchFields ();

   void setCellVisible (boolean visible);
}
