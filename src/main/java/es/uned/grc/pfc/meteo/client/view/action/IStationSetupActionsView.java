package es.uned.grc.pfc.meteo.client.view.action;

import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.view.base.IView;

public interface IStationSetupActionsView extends IView {
   void setInput (IRequestFactory requestFactory);

//   /** get the element that the user clicks to go to the table representation */
//   IHasActionHandlers getTableHandler ();
//   /** get the element that the user clicks to display the station search form */
//   IHasActionHandlers getStationSearchHandler ();
//
//   UIObject getTablePanel ();
//   UIObject getStationSearchPanel ();
//   UIObject getStationFormPanel ();
}
