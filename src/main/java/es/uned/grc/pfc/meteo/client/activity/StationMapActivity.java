package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;

import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.place.StationMapPlace;
import es.uned.grc.pfc.meteo.client.view.IStationMapView;
import es.uned.grc.pfc.meteo.client.view.widget.GlassPanel;

public class StationMapActivity extends AbstractBaseActivity {

   protected IStationMapView mapView = null;
   protected EventBus eventBus = null;
   
   public StationMapActivity (StationMapPlace listPlace, IStationMapView mapView, PlaceController placeController) {
      super (placeController, listPlace);
      this.mapView = mapView;
   }
   
   @Override
   public void start (final AcceptsOneWidget panel, final EventBus eventBus) {
      this.eventBus = eventBus;
      panel.setWidget (mapView.asWidget ());
      
      // load own station
      GlassPanel.fireDistraction (panel,
                                  mapView.asWidget (), 
                                  getRequestFactory (eventBus).getStationContext ().getOwnStation (true)
                                                              .with ("stationModel", "transientLastObservations", "transientLastObservations.variable"),
                                  new Receiver <IStationProxy> () {
                                     @Override
                                     public void onSuccess (IStationProxy response) {
                                        mapView.setCenterStation (response);
                                     }
                                  });
   } 
}
