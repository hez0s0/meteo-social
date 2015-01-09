package es.uned.grc.pfc.meteo.client.activity.action;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import es.uned.grc.pfc.meteo.client.activity.AbstractBaseActivity;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;
import es.uned.grc.pfc.meteo.client.view.IObservationListView;
import es.uned.grc.pfc.meteo.client.view.action.IObservationListActionsView;

public class ObservationListActionsActivity extends AbstractBaseActivity {
   protected IObservationListView observationListView = null;
   protected IObservationListActionsView observationListActionsView = null;

   public ObservationListActionsActivity (ObservationListPlace observationListPlace, 
                                          IObservationListView observationListView, 
                                          IObservationListActionsView observationListActionsView, 
                                          PlaceController placeController) {
      super (placeController, observationListPlace);

      this.observationListView = observationListView;
      this.observationListActionsView = observationListActionsView;
   }

   @Override
   public void start (AcceptsOneWidget panel, final EventBus eventBus) {
      panel.setWidget (observationListActionsView.asWidget ());
      applyRoleVisibility ();
      bind (eventBus);
   }

   /**
    * Binds the event handlers.
    */
   private synchronized void bind (final EventBus eventBus) {
//      /** handle click on import */ 
//      registerHandler (observationListActionsView.getImportHandler (), new IActionHandler () {
//         @Override
//         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
//            ImportDialogBox.showDialog (ISharedConstants.ExpImpEntity.WORKSPACE_LIST).addClickHandler (ConfirmationDialogBox.ButtonType.ACCEPT, new IActionDialogBoxEventHandler () {
//               @Override
//               public void onActionClicked (ActionDialogBoxClickEvent event) { //simply refresh the view
//                  AlertDialogBox.showWarning (IClientConstants.textConstants.importSuccess ());
//                  placeController.goTo (new ObservationListPlace ());
//               }
//            });
//            source.setCompleted ();
//         }
//      });
   }
}
