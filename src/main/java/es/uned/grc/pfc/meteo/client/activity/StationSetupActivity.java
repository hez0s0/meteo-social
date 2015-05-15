package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import es.uned.grc.pfc.meteo.client.event.MessageChangeEvent;
import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;
import es.uned.grc.pfc.meteo.client.place.StationSetupPlace;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.request.IStationRequestContext;
import es.uned.grc.pfc.meteo.client.view.IStationSetupView;
import es.uned.grc.pfc.meteo.client.view.widget.GlassPanel;

public class StationSetupActivity extends AbstractBaseActivity {

   private IStationSetupView editView = null;

   public StationSetupActivity (StationSetupPlace editPlace, IStationSetupView editView, PlaceController placeController) {
      super (placeController, editPlace);

      this.editView = editView;
   }

   @Override
   public void start (final AcceptsOneWidget panel, final EventBus eventBus) {
      final IRequestFactory requestFactory = getRequestFactory (eventBus);
      final IStationRequestContext stationContext = requestFactory.getStationContext ();

      // detail of the station
      GlassPanel.fireDistraction (panel,
                                  editView.asWidget (), 
                                  stationContext.getOwnStation ()
                                                .with ("stationModel", "variables", "parameters"),
                                  new Receiver <IStationProxy> () {
                                     @Override
                                     public void onSuccess (IStationProxy response) {
                                        if (response != null) {
                                           editView.setInput (response, requestFactory, requestFactory.getStationContext (), placeController);
                                        } else {
                                           placeController.goTo (new ObservationListPlace ());
                                        }
                                     }
                                     
                                     @Override
                                     public void onFailure (ServerFailure serverFailure) {
                                        eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, MessageChangeEvent.getTextMessages ().getError ("Station", place.getEntityID ()), serverFailure));
                                     }
                                  });
   }

}
