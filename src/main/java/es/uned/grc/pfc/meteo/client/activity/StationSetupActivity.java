package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;

import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.place.StationMapPlace;
import es.uned.grc.pfc.meteo.client.view.IStationSetupView;

public class StationSetupActivity extends AbstractBaseActivity {

   protected IStationSetupView view = null;
   protected EventBus eventBus = null;
   
   public StationSetupActivity (StationMapPlace listPlace, IStationSetupView view, PlaceController placeController) {
      super (placeController, listPlace);
      this.view = view;
   }
   
   @Override
   public void start (final AcceptsOneWidget panel, final EventBus eventBus) {
      this.eventBus = eventBus;
      panel.setWidget (view.asWidget ());
            
      getRequestFactory (eventBus).getStationContext ().getOwnStation (true)
                                                       .with ("stationModel", "transientLastObservations", "transientLastObservations.variable")
                                                       .fire (new Receiver <IStationProxy> () {

         @Override
         public void onSuccess (IStationProxy response) {
            view.setStation (response);
         }
      });
   } 
}
