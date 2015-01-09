package es.uned.grc.pfc.meteo.client.view.widget.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.LocalizableResource.GenerateKeys;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import es.uned.grc.pfc.meteo.client.util.IStyleConstants;

public class ConfirmationDialogBox extends ActionDialogBox {
   /** global text constants */
   @Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Please confirm your option") @Meaning ("ConfirmationDialogBox Dialog title")
      String caption ();
   }
   public static TextConstants textConstants = GWT.create (TextConstants.class);

   interface ConfirmationDialogBoxUiBinder extends UiBinder <HTMLPanel, ConfirmationDialogBox> {
   }
   private static ConfirmationDialogBoxUiBinder uiBinder = GWT.create (ConfirmationDialogBoxUiBinder.class);
   
   @UiField
   protected Label questionLabel = null;

   /**
    * Private constructor. Use static methods to display
    */
   private ConfirmationDialogBox (String question) {
      super.getCaption ().setText (textConstants.caption ());
      
      setWidget (uiBinder.createAndBindUi (this));
      
      addStyleName (IStyleConstants.CONFIRM_DIALOG_BOX);
      
      questionLabel.setText (question);
   } //end of ConfirmationDialogBox

   public static ConfirmationDialogBox askConfirmation (String question) {
      ConfirmationDialogBox confirmationDialogBox = new ConfirmationDialogBox (question);
      return (ConfirmationDialogBox) ActionDialogBox.showDialog (confirmationDialogBox);
   } //end of askConfirmation
} //end of ConfirmationDialogBox
