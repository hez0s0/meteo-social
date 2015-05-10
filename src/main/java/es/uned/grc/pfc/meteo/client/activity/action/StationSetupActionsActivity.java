package es.uned.grc.pfc.meteo.client.activity.action;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import es.uned.grc.pfc.meteo.client.activity.AbstractBaseActivity;
import es.uned.grc.pfc.meteo.client.place.StationSetupPlace;
import es.uned.grc.pfc.meteo.client.view.IStationSetupView;
import es.uned.grc.pfc.meteo.client.view.action.IStationSetupActionsView;

public class StationSetupActionsActivity extends AbstractBaseActivity {
   protected IStationSetupView view = null;
   protected IStationSetupActionsView actionsView = null;
   protected StationSetupPlace place = null;

   public StationSetupActionsActivity (StationSetupPlace place, 
                                       IStationSetupView view, 
                                       IStationSetupActionsView actionsView, 
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
