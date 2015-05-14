package es.uned.grc.pfc.meteo.client.ioc;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import es.uned.grc.pfc.meteo.client.BootstrapImpl;
import es.uned.grc.pfc.meteo.client.IBootstrap;
import es.uned.grc.pfc.meteo.client.activity.CustomActivityManager;
import es.uned.grc.pfc.meteo.client.activity.mapper.ActionsActivityMapper;
import es.uned.grc.pfc.meteo.client.activity.mapper.HeaderActivityMapper;
import es.uned.grc.pfc.meteo.client.activity.mapper.MainActivityMapper;
import es.uned.grc.pfc.meteo.client.activity.mapper.MenuActivityMapper;
import es.uned.grc.pfc.meteo.client.activity.mapper.MessagesActivityMapper;
import es.uned.grc.pfc.meteo.client.activity.mapper.SearchActivityMapper;
import es.uned.grc.pfc.meteo.client.place.InjectablePlaceController;
import es.uned.grc.pfc.meteo.client.resource.IApplicationResources;
import es.uned.grc.pfc.meteo.client.view.IHeaderView;
import es.uned.grc.pfc.meteo.client.view.IMainLayoutView;
import es.uned.grc.pfc.meteo.client.view.IMenuView;
import es.uned.grc.pfc.meteo.client.view.IMessagesView;
import es.uned.grc.pfc.meteo.client.view.IObservationListView;
import es.uned.grc.pfc.meteo.client.view.ISearchView;
import es.uned.grc.pfc.meteo.client.view.IStationMapView;
import es.uned.grc.pfc.meteo.client.view.IStationSetupView;
import es.uned.grc.pfc.meteo.client.view.IUserSetupView;
import es.uned.grc.pfc.meteo.client.view.action.IObservationListActionsView;
import es.uned.grc.pfc.meteo.client.view.action.IStationMapActionsView;
import es.uned.grc.pfc.meteo.client.view.action.IStationSetupActionsView;
import es.uned.grc.pfc.meteo.client.view.action.IUserSetupActionsView;
import es.uned.grc.pfc.meteo.client.view.action.impl.ObservationListActionsViewImpl;
import es.uned.grc.pfc.meteo.client.view.action.impl.StationMapActionsViewImpl;
import es.uned.grc.pfc.meteo.client.view.action.impl.StationSetupActionsViewImpl;
import es.uned.grc.pfc.meteo.client.view.action.impl.UserSetupActionsViewImpl;
import es.uned.grc.pfc.meteo.client.view.impl.HeaderViewImpl;
import es.uned.grc.pfc.meteo.client.view.impl.MainLayoutViewImpl;
import es.uned.grc.pfc.meteo.client.view.impl.MenuViewImpl;
import es.uned.grc.pfc.meteo.client.view.impl.MessagesViewImpl;
import es.uned.grc.pfc.meteo.client.view.impl.ObservationListViewImpl;
import es.uned.grc.pfc.meteo.client.view.impl.SearchViewImpl;
import es.uned.grc.pfc.meteo.client.view.impl.StationMapViewImpl;
import es.uned.grc.pfc.meteo.client.view.impl.StationSetupViewImpl;
import es.uned.grc.pfc.meteo.client.view.impl.UserSetupViewImpl;

public class MainGinModule extends AbstractGinModule {

   @Override
   protected void configure () {
      // global-wide elements for control and communication
      bind (EventBus.class).to (SimpleEventBus.class).in (Singleton.class);
      bind (PlaceController.class).to (InjectablePlaceController.class).in (Singleton.class);
      bind (IBootstrap.class).to (BootstrapImpl.class).in (Singleton.class);
      
      // singleton views for better performance 
      bind (IMainLayoutView.class).to (MainLayoutViewImpl.class).in (Singleton.class);
      bind (IHeaderView.class).to (HeaderViewImpl.class).in (Singleton.class);
      bind (IMessagesView.class).to (MessagesViewImpl.class).in (Singleton.class);
      bind (IMenuView.class).to (MenuViewImpl.class).in (Singleton.class);
      bind (ISearchView.class).to (SearchViewImpl.class).in (Singleton.class);
      
      //detail singleton views
      bind (IObservationListView.class).to (ObservationListViewImpl.class).in (Singleton.class);
      bind (IStationMapView.class).to (StationMapViewImpl.class).in (Singleton.class);
      bind (IUserSetupView.class).to (UserSetupViewImpl.class).in (Singleton.class);
      bind (IStationSetupView.class).to (StationSetupViewImpl.class).in (Singleton.class);

      //action singleton views
      bind (IObservationListActionsView.class).to (ObservationListActionsViewImpl.class).in (Singleton.class);
      bind (IStationMapActionsView.class).to (StationMapActionsViewImpl.class).in (Singleton.class);
      bind (IUserSetupActionsView.class).to (UserSetupActionsViewImpl.class).in (Singleton.class);
      bind (IStationSetupActionsView.class).to (StationSetupActionsViewImpl.class).in (Singleton.class);
   }

   /**
    * Activity mapper for the header display region
    */
   @Provides
   @Singleton
   @Named ("header")
   public ActivityManager getHeaderActivityManager (HeaderActivityMapper activityMapper, EventBus eventBus) {
      return new ActivityManager (activityMapper, eventBus);
   }

   /**
    * Activity mapper for the menu display region
    */
   @Provides
   @Singleton
   @Named ("menu")
   public ActivityManager getMenuActivityManager (MenuActivityMapper activityMapper, EventBus eventBus) {
      return new ActivityManager (activityMapper, eventBus);
   }

   /**
    * Activity mapper for the search display region
    */
   @Provides
   @Singleton
   @Named ("search")
   public ActivityManager getSearchActivityManager (SearchActivityMapper activityMapper, EventBus eventBus) {
      return new ActivityManager (activityMapper, eventBus);
   }

   /**
    * Activity mapper for the messages display region
    */
   @Provides
   @Singleton
   @Named ("messages")
   public ActivityManager getMessagesActivityManager (MessagesActivityMapper activityMapper, EventBus eventBus) {
      return new ActivityManager (activityMapper, eventBus);
   }

   /**
    * Activity mapper for the main display region
    */
   @Provides
   @Singleton
   @Named ("main")
   public ActivityManager getMainActivityManager (MainActivityMapper activityMapper, EventBus eventBus) {
      return new CustomActivityManager (activityMapper, eventBus);
   }

   /**
    * Activity mapper for the actions display region
    */
   @Provides
   @Singleton
   @Named ("actions")
   public ActivityManager getActionsActivityManager (ActionsActivityMapper activityMapper, EventBus eventBus) {
      return new ActivityManager (activityMapper, eventBus);
   }
   
   /**
    * Application resources singleton instance.
    */
   @Provides
   @Singleton
   public IApplicationResources getApplicationResources () {
      return GWT.create (IApplicationResources.class);
   }

}
