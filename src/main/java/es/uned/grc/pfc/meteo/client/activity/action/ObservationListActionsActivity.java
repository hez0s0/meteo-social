package es.uned.grc.pfc.meteo.client.activity.action;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import es.uned.grc.pfc.meteo.client.activity.AbstractBaseActivity;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;
import es.uned.grc.pfc.meteo.client.place.StationMapPlace;
import es.uned.grc.pfc.meteo.client.view.IObservationListView;
import es.uned.grc.pfc.meteo.client.view.action.IObservationListActionsView;
import es.uned.grc.pfc.meteo.client.view.base.IActionHandler;
import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;

public class ObservationListActionsActivity extends AbstractBaseActivity {
   protected IObservationListView observationListView = null;
   protected IObservationListActionsView observationListActionsView = null;
   protected ObservationListPlace observationListPlace = null;

   public ObservationListActionsActivity (ObservationListPlace observationListPlace, 
                                          IObservationListView observationListView, 
                                          IObservationListActionsView observationListActionsView, 
                                          PlaceController placeController) {
      super (placeController, observationListPlace);

      this.observationListPlace = observationListPlace;
      this.observationListView = observationListView;
      this.observationListActionsView = observationListActionsView;
   }

   @Override
   public void start (AcceptsOneWidget panel, final EventBus eventBus) {
      panel.setWidget (observationListActionsView.asWidget ());
      applyRoleVisibility ();
      observationListActionsView.getTablePanel ().setVisible (!observationListPlace.getRepresentation ().equals (ObservationListPlace.Representation.TEXT));
      observationListActionsView.getGraphicsPanel ().setVisible (!observationListPlace.getRepresentation ().equals (ObservationListPlace.Representation.GRAPHIC));
      bind (eventBus);
   }

   /**
    * Binds the event handlers.
    */
   private synchronized void bind (final EventBus eventBus) {
      /** handle click on view table */ 
      registerHandler (observationListActionsView.getTableHandler (), new IActionHandler () {
         @Override
         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
            placeController.goTo (new ObservationListPlace (ObservationListPlace.ObservationType.NORMAL, ObservationListPlace.Representation.TEXT));
            source.setCompleted ();
         }
      });
      /** handle click on view graphics */ 
      registerHandler (observationListActionsView.getGraphicsHandler (), new IActionHandler () {
         @Override
         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
            placeController.goTo (new ObservationListPlace (ObservationListPlace.ObservationType.NORMAL, ObservationListPlace.Representation.GRAPHIC));
            source.setCompleted ();
         }
      });
      /** handle click on view derived */ 
      registerHandler (observationListActionsView.getDerivedHandler (), new IActionHandler () {
         @Override
         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
            placeController.goTo (new ObservationListPlace (ObservationListPlace.ObservationType.DERIVED, ObservationListPlace.Representation.TEXT));
            source.setCompleted ();
         }
      });
      /** handle click on view derived */ 
      registerHandler (observationListActionsView.getMapHandler (), new IActionHandler () {
         @Override
         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
            placeController.goTo (new StationMapPlace ());
            source.setCompleted ();
         }
      });
   }
}
