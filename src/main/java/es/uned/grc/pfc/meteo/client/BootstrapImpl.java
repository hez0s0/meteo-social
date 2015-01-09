package es.uned.grc.pfc.meteo.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.activity.ActivityManagerInitializer;
import es.uned.grc.pfc.meteo.client.event.MessageChangeEvent;
import es.uned.grc.pfc.meteo.client.ioc.IGinjector;
import es.uned.grc.pfc.meteo.client.util.SessionKeepAliveControl;
import es.uned.grc.pfc.meteo.client.util.UserIdleTimeOutControl;
import es.uned.grc.pfc.meteo.client.view.IMainLayoutView;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

public class BootstrapImpl implements IBootstrap {
   
   private IMainLayoutView mainLayout = null;
   private PlaceHistoryHandler historyHandler = null;

   @Inject
   public BootstrapImpl (IHistoryMapper historyMapper, IMainLayoutView mainLayout, ActivityManagerInitializer initializer) {
      this.historyHandler = new PlaceHistoryHandler (historyMapper);
      this.mainLayout = mainLayout;
   }

   @Override
   public void start (final IGinjector injector) {

      //once the user and its roles where loaded, the app can be displayed
      display (injector);
      
      //let us check for auto-logout on inactivity
      UserIdleTimeOutControl.getInstance ().startCheck ();
      //let us keep the http session alive to prevent problems with long client-side work
      SessionKeepAliveControl.getInstance ().startKeepAlive (injector.getEventBus ());
   }

   @SuppressWarnings ("deprecation")
   private void display (final IGinjector injector) {
      RootLayoutPanel.get ().add (mainLayout.asWidget ());
      
      historyHandler.register (injector.getPlaceController (), injector.getEventBus (), injector.getDefaultPlace ());
      historyHandler.handleCurrentHistory ();

      GWT.setUncaughtExceptionHandler (new GWT.UncaughtExceptionHandler () {
         public void onUncaughtException (Throwable t) {
            if (t.getClass ().equals (IllegalArgumentException.class)) {
               //no logged user ... so let us redirect to login page
               Window.Location.assign (ISharedConstants.LOGIN_URL);
            } else {
               MessageChangeEvent messageChangeEvent = null;
               Throwable unwrapped = unwrap (t);
   
               messageChangeEvent = new MessageChangeEvent (MessageChangeEvent.Level.ERROR, 
                     MessageChangeEvent.getTextMessages ().serverError (unwrapped.getMessage ()), unwrapped);
               
               injector.getEventBus ().fireEvent (messageChangeEvent);
            }
         }
      });
   }
   
   private Throwable unwrap (Throwable t) {
      if (t instanceof UmbrellaException) {
         UmbrellaException ue = (UmbrellaException) t;
         if (ue.getCauses ().size () == 1) {
            return unwrap (ue.getCauses ().iterator ().next ());
         }
      }
      return t;
   }
}
