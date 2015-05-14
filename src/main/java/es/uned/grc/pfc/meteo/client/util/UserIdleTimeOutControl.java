package es.uned.grc.pfc.meteo.client.util;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

import es.uned.grc.pfc.meteo.shared.ISharedConstants;

public class UserIdleTimeOutControl {

   // 20 minutes max user inactivity
   private static final int TIMEOUT = 20 * 60 * 1000;
   
   private static UserIdleTimeOutControl instance = null;

   Timer inactivityTimer = null;
   HandlerRegistration handlerRegistration = null;

   public static synchronized UserIdleTimeOutControl getInstance () {
      if (instance == null) {
         instance = new UserIdleTimeOutControl ();
      }
      return instance;
   }
   
   private UserIdleTimeOutControl () {
      
   }
   
   public void startCheck () {
      NativePreviewHandler handler = null;

      inactivityTimer = new Timer () {
         @Override
         public void run () {
            // Logout
            handlerRegistration.removeHandler ();
            inactivityTimer.cancel ();

            //perform logout by using spring logout servlet
            Window.Location.assign (PortableStringUtils.format (ISharedConstants.LOGOUT_URL, CommonUtils.getBaseUrl ()));
         }
      };
      inactivityTimer.schedule (TIMEOUT);

      handler = new NativePreviewHandler () { 
         //register for user events
         @Override
         public void onPreviewNativeEvent (NativePreviewEvent event) {
            inactivityTimer.schedule (TIMEOUT);
         }
      };
      handlerRegistration = Event.addNativePreviewHandler (handler);
   }
}
