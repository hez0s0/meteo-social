package es.uned.grc.pfc.meteo.client.activity.mapper;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.activity.action.ObservationListActionsActivity;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.view.IObservationListView;
import es.uned.grc.pfc.meteo.client.view.action.IObservationListActionsView;

/**
 * Mapper for the actions display of the screen.
 * Responsible both for mapping the Places to the correct Activities,
 * and to listen for events that may trigger Place changes, such as
 * creation of new elements. Those events are considered out of scope
 * of one single activity, and thus are managed here.
 */
public class ActionsActivityMapper implements ActivityMapper {

   @Inject
   private PlaceController placeController = null;
   @Inject
   private EventBus eventBus = null;

   @Inject
   private IObservationListView observationListView = null;

   @Inject
   private IObservationListActionsView observationListActionsView = null;
   
   private IRequestFactory requestFactory = null;

   public IRequestFactory getRequestFactory (EventBus eventBus) {
      if (requestFactory == null) {
         requestFactory = GWT.create (IRequestFactory.class);
         requestFactory.initialize (eventBus);
      }
      return requestFactory;
   }
   
   @Override
   public Activity getActivity (Place place) {
      if (place instanceof ObservationListPlace) {
         return new ObservationListActionsActivity ((ObservationListPlace) place, observationListView, observationListActionsView, placeController);
      }
      
      return null;
   }
   
}
