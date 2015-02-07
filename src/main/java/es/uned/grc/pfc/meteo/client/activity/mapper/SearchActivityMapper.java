package es.uned.grc.pfc.meteo.client.activity.mapper;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import es.uned.grc.pfc.meteo.client.activity.CustomActivityManager;
import es.uned.grc.pfc.meteo.client.activity.SearchActivity;
import es.uned.grc.pfc.meteo.client.place.AbstractPlace;
import es.uned.grc.pfc.meteo.client.view.ISearchView;

/**
 * Mapper for the menu display region of the screen.
 * Responsible both for mapping the Places to the correct Activities,
 * and to listen for events that may trigger Place changes, such as
 * creation of new elements. Those events are considered out of scope
 * of one single activity, and thus are managed here.
 */
public class SearchActivityMapper implements ActivityMapper {
   @Inject
   private PlaceController placeController = null;
   @Inject
   private EventBus eventBus = null;

   @Named ("main")
   private MainActivityMapper mainActivityMapper = null;
   @Inject
   private ISearchView searchView = null;

   private Boolean init = false;
   
   @Inject
   public SearchActivityMapper (@Named ("main") ActivityManager mainActivityManager) {
      this.mainActivityMapper = (MainActivityMapper) ((CustomActivityManager) mainActivityManager).getMapper ();
   }
   
   @Override
   public Activity getActivity (Place place) {
      if (!init) {
         bind ();
         init = true;
      }
      return new SearchActivity (mainActivityMapper, (AbstractPlace) place, searchView, placeController);
   }

   /**
    * Wire all click handlers for singleton views here
    */
   private void bind () {
      // no known events yet ...
   }

}
