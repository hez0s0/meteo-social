package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import es.uned.grc.pfc.meteo.client.place.StationMapPlace;
import es.uned.grc.pfc.meteo.client.view.IStationMapView;

public class StationMapActivity extends AbstractBaseActivity {

   protected IStationMapView mapView = null;
   
   public StationMapActivity (StationMapPlace listPlace, IStationMapView mapView, PlaceController placeController) {
      super (placeController, listPlace);
      this.mapView = mapView;
   }
   
   @Override
   public void start (final AcceptsOneWidget panel, final EventBus eventBus) {
   }   
}
