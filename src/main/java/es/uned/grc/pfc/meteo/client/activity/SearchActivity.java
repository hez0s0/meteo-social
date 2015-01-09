package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import es.uned.grc.pfc.meteo.client.activity.mapper.MainActivityMapper;
import es.uned.grc.pfc.meteo.client.place.AbstractPlace;
import es.uned.grc.pfc.meteo.client.view.ISearchView;

/**
 * Activity (presenter) that acts as a bridge between the view and the
 * server-side services. Manages the logic for search display
 */
public class SearchActivity extends AbstractBaseActivity {

   @SuppressWarnings ("unused")
   private MainActivityMapper mainActivityMapper = null;
   @SuppressWarnings ("unused")
   private ISearchView searchView = null;
   
   public SearchActivity (MainActivityMapper mainActivityMapper, AbstractPlace place, ISearchView searchView, PlaceController placeController) {
      super (placeController, place);

      this.mainActivityMapper = mainActivityMapper;
      this.searchView = searchView;
   }

   @Override
   public void start (AcceptsOneWidget panel, final EventBus eventBus) {
      //do nothing
   }
}
