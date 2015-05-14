package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import es.uned.grc.pfc.meteo.client.event.MessageChangeEvent;
import es.uned.grc.pfc.meteo.client.model.IUserProxy;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;
import es.uned.grc.pfc.meteo.client.place.UserSetupPlace;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.request.IStationRequestContext;
import es.uned.grc.pfc.meteo.client.view.IUserSetupView;
import es.uned.grc.pfc.meteo.client.view.widget.GlassPanel;

public class UserSetupActivity extends AbstractBaseActivity {

   private IUserSetupView editView = null;

   public UserSetupActivity (UserSetupPlace editPlace, IUserSetupView editView, PlaceController placeController) {
      super (placeController, editPlace);

      this.editView = editView;
   }

   @Override
   public void start (final AcceptsOneWidget panel, final EventBus eventBus) {
      final IRequestFactory requestFactory = getRequestFactory (eventBus);
      final IStationRequestContext stationContext = requestFactory.getStationContext ();

      // detail of the user
      GlassPanel.fireDistraction (panel,
                                  editView.asWidget (), 
                                  stationContext.getLoggedUser (),
                                  new Receiver <IUserProxy> () {
                                     @Override
                                     public void onSuccess (IUserProxy response) {
                                        if (response != null) {
                                           editView.setInput (response, requestFactory, requestFactory.getStationContext (), placeController);
                                        } else {
                                           placeController.goTo (new ObservationListPlace ());
                                        }
                                     }
                                     
                                     @Override
                                     public void onFailure (ServerFailure serverFailure) {
                                        eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, MessageChangeEvent.getTextMessages ().getError ("User", place.getEntityID ()), serverFailure));
                                     }
                                  });
   }

}
