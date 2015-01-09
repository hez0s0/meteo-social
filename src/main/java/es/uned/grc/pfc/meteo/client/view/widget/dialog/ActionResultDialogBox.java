package es.uned.grc.pfc.meteo.client.view.widget.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.LocalizableResource.GenerateKeys;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.client.view.util.FadeAnimation;

public class ActionResultDialogBox extends DecoratedPopupPanel {
   /** global text constants */
   @Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Action executed successfully") @Meaning ("Success confirm")
      String defaultMessage ();
   }
   public static TextConstants textConstants = GWT.create (TextConstants.class);
   
   interface ActionResultDialogBoxUiBinder extends UiBinder <HTMLPanel, ActionResultDialogBox> {
   }
   private static ActionResultDialogBoxUiBinder uiBinder = GWT.create (ActionResultDialogBoxUiBinder.class);
   
   protected static final int FADE_OUT_MILLIS = 6000;

   protected FadeAnimation fadeAnimation = null;
   protected int fadeOutMillis = -1;
   
   @UiField
   protected Label messageLabel = null;

   public ActionResultDialogBox (String message, int fadeOutMillis) {
      setWidget (uiBinder.createAndBindUi (this));
      
      fadeAnimation = new FadeAnimation (getElement ());
      
      messageLabel.setText ((!PortableStringUtils.isEmpty (message)) ? message : textConstants.defaultMessage ());
      this.fadeOutMillis = (fadeOutMillis > 0) ? fadeOutMillis : FADE_OUT_MILLIS;
   }

   public void show () {
      super.show ();

      fadeAnimation.fade (fadeOutMillis, 1.0, 0.0, this);
   }

   public static ActionResultDialogBox showDialog () {
      return showDialog (textConstants.defaultMessage (), FADE_OUT_MILLIS);
   }
   
   public static ActionResultDialogBox showDialog (String message, int fadeOutMillis) {
      ActionResultDialogBox actionResultDialogBox = new ActionResultDialogBox (message, fadeOutMillis);
      actionResultDialogBox.setGlassEnabled (false);
      actionResultDialogBox.setAnimationEnabled (false);
      actionResultDialogBox.center ();
      actionResultDialogBox.show ();
      return actionResultDialogBox;
   }
}
