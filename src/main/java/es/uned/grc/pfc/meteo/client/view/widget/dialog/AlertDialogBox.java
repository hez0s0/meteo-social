package es.uned.grc.pfc.meteo.client.view.widget.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import es.uned.grc.pfc.meteo.client.util.ClientGlobals;
import es.uned.grc.pfc.meteo.client.util.IStyleConstants;

public class AlertDialogBox extends ActionDialogBox {
   /** global text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Información") @Meaning ("AlertDialogBox Dialog title")
      String caption ();
   }
   public static TextConstants textConstants = GWT.create (TextConstants.class);

   interface AlertDialogBoxUiBinder extends UiBinder <HTMLPanel, AlertDialogBox> {
   }
   private static AlertDialogBoxUiBinder uiBinder = GWT.create (AlertDialogBoxUiBinder.class);

   @UiField
   protected Image warningImage = null;
   @UiField
   protected Label alertLabel = null;

   /**
    * Private constructor. Use static methods to display
    */
   private AlertDialogBox (String alert) {
      super.getCaption ().setText (textConstants.caption ());
      
      setWidget (uiBinder.createAndBindUi (this));
      
      addStyleName (IStyleConstants.ALERT_DIALOG_BOX);

      warningImage.setUrl (ClientGlobals.getInstance ().getApplicationResources ().information ().getSafeUri ());
      alertLabel.setText (alert);
   }

   public static AlertDialogBox showWarning (String alert) {
      AlertDialogBox alertDialogBox = new AlertDialogBox (alert);
      return (AlertDialogBox) ActionDialogBox.showDialog (alertDialogBox);
   }
}
