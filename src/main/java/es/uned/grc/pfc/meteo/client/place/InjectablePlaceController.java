package es.uned.grc.pfc.meteo.client.place;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class InjectablePlaceController extends PlaceController {

   @SuppressWarnings ("deprecation")
   @Inject
   public InjectablePlaceController (EventBus eventBus) {
      super (eventBus);
   }
}
