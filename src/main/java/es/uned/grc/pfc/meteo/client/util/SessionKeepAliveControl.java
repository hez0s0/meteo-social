package es.uned.grc.pfc.meteo.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;

import es.uned.grc.pfc.meteo.client.request.IRequestFactory;

public class SessionKeepAliveControl {

   private static final int TIMER_INTERVAL = 10 * 60 * 1000; //10 minutes per keep-alive
   
   private static SessionKeepAliveControl instance = null;

   Timer inactivityTimer = null;

   public static synchronized SessionKeepAliveControl getInstance () {
      if (instance == null) {
         instance = new SessionKeepAliveControl ();
      }
      return instance;
   }
   
   private SessionKeepAliveControl () {
   }
   
   public void startKeepAlive (final EventBus eventBus) {
      final IRequestFactory requestFactory = GWT.create (IRequestFactory.class);
      requestFactory.initialize (eventBus);
      inactivityTimer = new Timer () {
         @Override
         public void run () {
//            requestFactory.getAdminContext ().keepAlive ().fire (); //do not act on failures (some little network down might be ok)
//            
//            inactivityTimer.schedule (TIMER_INTERVAL);
         }
      };
      inactivityTimer.schedule (TIMER_INTERVAL);
   }
}