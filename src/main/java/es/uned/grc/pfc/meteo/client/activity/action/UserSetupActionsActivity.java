package es.uned.grc.pfc.meteo.client.activity.action;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import es.uned.grc.pfc.meteo.client.activity.AbstractBaseActivity;
import es.uned.grc.pfc.meteo.client.place.UserSetupPlace;
import es.uned.grc.pfc.meteo.client.view.IUserSetupView;
import es.uned.grc.pfc.meteo.client.view.action.IUserSetupActionsView;

public class UserSetupActionsActivity extends AbstractBaseActivity {
   protected IUserSetupView view = null;
   protected IUserSetupActionsView actionsView = null;
   protected UserSetupPlace place = null;

   public UserSetupActionsActivity (UserSetupPlace place, 
                                    IUserSetupView view, 
                                    IUserSetupActionsView actionsView, 
                                    PlaceController placeController) {
      super (placeController, place);

      this.place = place;
      this.view = view;
      this.actionsView = actionsView;
   }

   @Override
   public void start (AcceptsOneWidget panel, final EventBus eventBus) {
      panel.setWidget (actionsView.asWidget ());
      
      applyRoleVisibility ();
      bind (eventBus);

      actionsView.setInput (getRequestFactory (eventBus));
   }

   /**
    * Binds the event handlers.
    */
   private synchronized void bind (final EventBus eventBus) {
//      /** handle click on view table */ 
//      registerHandler (actionsView.getTableHandler (), new IActionHandler () {
//         @Override
//         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
//            placeController.goTo (new ObservationListPlace (ObservationListPlace.ObservationType.NORMAL, ObservationListPlace.Representation.TEXT));
//            source.setCompleted ();
//         }
//      });
//      /** handle click on station search */ 
//      registerHandler (actionsView.getStationSearchHandler (), new IActionHandler () {
//         @Override
//         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
//            actionsView.getStationFormPanel ().setVisible (!actionsView.getStationFormPanel ().isVisible ());
//            actionsView.setCellVisible (false);
//            actionsView.clearSearchFields ();
//            source.setCompleted ();
//         }
//      });
   }
}
