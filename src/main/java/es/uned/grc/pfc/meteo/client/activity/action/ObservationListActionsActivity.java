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
      observationListActionsView.getTablePanel ().setVisible (false);
      observationListActionsView.getGraphicsPanel ().setVisible (false);
      observationListActionsView.getDerivedPanel ().setVisible (false);
      observationListActionsView.getDerivedGraphicsPanel ().setVisible (false);
      if (observationListPlace.getObservationType ().equals (ObservationListPlace.ObservationType.NORMAL)) {
         observationListActionsView.getTablePanel ().setVisible (!observationListPlace.getRepresentation ().equals (ObservationListPlace.Representation.TEXT));
         observationListActionsView.getGraphicsPanel ().setVisible (!observationListPlace.getRepresentation ().equals (ObservationListPlace.Representation.GRAPHIC));
         observationListActionsView.getDerivedPanel ().setVisible (true);
      } else {
         observationListActionsView.getDerivedPanel ().setVisible (!observationListPlace.getRepresentation ().equals (ObservationListPlace.Representation.TEXT));
         observationListActionsView.getDerivedGraphicsPanel ().setVisible (!observationListPlace.getRepresentation ().equals (ObservationListPlace.Representation.GRAPHIC));
         observationListActionsView.getTablePanel ().setVisible (true);
      }

      observationListActionsView.getStationFormPanel ().setVisible (false);
      observationListActionsView.setInput (getRequestFactory (eventBus), observationListPlace);
      bind (eventBus);
   }

   /**
    * Binds the event handlers.
    */
   private synchronized void bind (final EventBus eventBus) {
      /** handle click on station search */ 
      registerHandler (observationListActionsView.getStationSearchHandler (), new IActionHandler () {
         @Override
         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
            observationListActionsView.getStationFormPanel ().setVisible (!observationListActionsView.getStationFormPanel ().isVisible ());
            observationListActionsView.setCellVisible (false);
            observationListActionsView.clearSearchFields ();
            source.setCompleted ();
         }
      });
      /** handle click on view table */ 
      registerHandler (observationListActionsView.getTableHandler (), new IActionHandler () {
         @Override
         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
            ObservationListPlace place = new ObservationListPlace (ObservationListPlace.ObservationType.NORMAL, ObservationListPlace.Representation.TEXT);
            if (observationListPlace.getStationId () != null) {
               place.setStationId (observationListPlace.getStationId ());
            }
            placeController.goTo (place);
            source.setCompleted ();
         }
      });
      /** handle click on view graphics */ 
      registerHandler (observationListActionsView.getGraphicsHandler (), new IActionHandler () {
         @Override
         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
            ObservationListPlace place = new ObservationListPlace (ObservationListPlace.ObservationType.NORMAL, ObservationListPlace.Representation.GRAPHIC);
            if (observationListPlace.getStationId () != null) {
               place.setStationId (observationListPlace.getStationId ());
            }
            placeController.goTo (place);
            source.setCompleted ();
         }
      });
      /** handle click on view derived */ 
      registerHandler (observationListActionsView.getDerivedHandler (), new IActionHandler () {
         @Override
         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
            ObservationListPlace place = new ObservationListPlace (ObservationListPlace.ObservationType.DERIVED, ObservationListPlace.Representation.TEXT);
            if (observationListPlace.getStationId () != null) {
               place.setStationId (observationListPlace.getStationId ());
            }
            placeController.goTo (place);
            source.setCompleted ();
         }
      });
      /** handle click on view derived */ 
      registerHandler (observationListActionsView.getDerivedGraphicsHandler (), new IActionHandler () {
         @Override
         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
            ObservationListPlace place = new ObservationListPlace (ObservationListPlace.ObservationType.DERIVED, ObservationListPlace.Representation.GRAPHIC);
            if (observationListPlace.getStationId () != null) {
               place.setStationId (observationListPlace.getStationId ());
            }
            placeController.goTo (place);
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
