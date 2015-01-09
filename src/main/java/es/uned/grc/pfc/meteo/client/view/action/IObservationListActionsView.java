package es.uned.grc.pfc.meteo.client.view.action;

import com.google.gwt.user.client.ui.UIObject;

import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;
import es.uned.grc.pfc.meteo.client.view.base.IView;

public interface IObservationListActionsView extends IView {
   /** get the element that the user clicks to go to the new element form */
   IHasActionHandlers getCreateHandler ();
   /** get the element that the user clicks to delete a list of elements */
   IHasActionHandlers getDeleteHandler ();
   /** get the element that the user clicks to export */
   IHasActionHandlers getExportHandler ();
   /** get the element that the user clicks to import */
   IHasActionHandlers getImportHandler ();

   UIObject getCreatePanel ();
   UIObject getDeletePanel ();
   UIObject getExportPanel ();
   UIObject getImportPanel ();
}
