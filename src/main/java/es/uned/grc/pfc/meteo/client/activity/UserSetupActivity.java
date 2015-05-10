package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import es.uned.grc.pfc.meteo.client.place.StationMapPlace;
import es.uned.grc.pfc.meteo.client.view.IUserSetupView;

public class UserSetupActivity extends AbstractBaseActivity {

   protected IUserSetupView view = null;
   protected EventBus eventBus = null;
   
   public UserSetupActivity (StationMapPlace listPlace, IUserSetupView view, PlaceController placeController) {
      super (placeController, listPlace);
      this.view = view;
   }
   
   @Override
   public void start (final AcceptsOneWidget panel, final EventBus eventBus) {
      this.eventBus = eventBus;
      panel.setWidget (view.asWidget ());
            
      //TODO
//      getRequestFactory (eventBus).getStationContext ().getOwnStation (true)
//                                                       .with ("stationModel", "transientLastObservations", "transientLastObservations.variable")
//                                                       .fire (new Receiver <IStationProxy> () {
//
//         @Override
//         public void onSuccess (IStationProxy response) {
//            view.setStation (response);
//         }
//      });
   } 
}
