package es.uned.grc.pfc.meteo.client.activity.action;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import es.uned.grc.pfc.meteo.client.activity.AbstractBaseActivity;
import es.uned.grc.pfc.meteo.client.place.StationMapPlace;
import es.uned.grc.pfc.meteo.client.view.IStationMapView;
import es.uned.grc.pfc.meteo.client.view.action.IStationMapActionsView;

public class StationMapActionsActivity extends AbstractBaseActivity {
   protected IStationMapView stationMapView = null;
   protected IStationMapActionsView stationMapActionsView = null;
   protected StationMapPlace stationMapPlace = null;

   public StationMapActionsActivity (StationMapPlace stationMapPlace, 
                                     IStationMapView stationMapView, 
                                     IStationMapActionsView stationMapActionsView, 
                                     PlaceController placeController) {
      super (placeController, stationMapPlace);

      this.stationMapPlace = stationMapPlace;
      this.stationMapView = stationMapView;
      this.stationMapActionsView = stationMapActionsView;
   }

   @Override
   public void start (AcceptsOneWidget panel, final EventBus eventBus) {
      panel.setWidget (stationMapActionsView.asWidget ());
      applyRoleVisibility ();
      bind (eventBus);
   }

   /**
    * Binds the event handlers.
    */
   private synchronized void bind (final EventBus eventBus) {
      
   }
}
