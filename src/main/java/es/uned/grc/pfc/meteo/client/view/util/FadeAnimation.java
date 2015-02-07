package es.uned.grc.pfc.meteo.client.view.util;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;

@SuppressWarnings ("deprecation")
public class FadeAnimation {

   private static final int REFRESH_RATE = 100;
   
   private Timer timer = null;
   private Element element = null;
   private int duration = -1;
   private double opacityIncrement = -1;
   private double targetOpacity = -1;
   private double sourceOpacity = -1;
   private DecoratedPopupPanel dialogToClose = null;
   double currentOpacity = -1;

   public FadeAnimation (Element element) {
      this.element = element;
   }
   
   public void fade (int duration, double sourceOpacity, double targetOpacity, DecoratedPopupPanel dialogToClose) {
      this.duration = (duration >= REFRESH_RATE) ? duration : REFRESH_RATE;
      this.sourceOpacity = normalizeOpacity (sourceOpacity);
      this.targetOpacity = normalizeOpacity (targetOpacity);
      this.dialogToClose = dialogToClose;
      this.currentOpacity = 1.0;
      
      this.opacityIncrement = (this.targetOpacity - this.sourceOpacity) / ((double) this.duration / (double) REFRESH_RATE);
      
      timer = new Timer () {
         @Override
         public void run() {
            updateOpacity ();
         }
      };

      timer.scheduleRepeating (REFRESH_RATE);
   }

   private void updateOpacity () {
      double nextOpacity = getCurrentOpacity () + opacityIncrement;
      
      if (isDone (nextOpacity)) {
         timer.cancel ();
         if (dialogToClose != null) {
            dialogToClose.hide ();
         }
      } else {
         element.getStyle ().setProperty("filter", "alpha(opacity \\=" + nextOpacity + ")"); //for IE8
         element.getStyle ().setOpacity (nextOpacity);
         currentOpacity = nextOpacity; //kept as internal value rather than getting it form the element because of IE8 (see below)
      }
   }

   private boolean isDone (double nextOpacity) {
      if (opacityIncrement > 0) { //fading in
         return nextOpacity >= targetOpacity;
      } else { //fading out
         return nextOpacity <= targetOpacity;
      }
   }
   
   private double getCurrentOpacity () {
      //IE8 throws an Exception on getOpacity ... so it is maintained externally by this object
//      String opacityStr = null;
//      
//      try {
//         opacityStr = element.getStyle ().getOpacity ();
//         currentOpacity = new BigDecimal (opacityStr).doubleValue ();
//      } catch (Exception e) {
//         currentOpacity = 1.0; //just some default
//      }
      
      return currentOpacity;
   }

   private double normalizeOpacity (double opacity) {
      if (opacity > 1.0) {
         opacity = 1.0;
      }
      if (opacity < 0.0) {
         opacity = 0.0;
      }
      return opacity;
   }
   
}
