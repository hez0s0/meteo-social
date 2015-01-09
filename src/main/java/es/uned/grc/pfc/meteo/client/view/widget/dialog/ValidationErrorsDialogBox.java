package es.uned.grc.pfc.meteo.client.view.widget.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

import es.uned.grc.pfc.meteo.client.view.util.FadeAnimation;

public class ValidationErrorsDialogBox extends DecoratedPopupPanel {

   interface ValidationErrorsDialogBoxUiBinder extends UiBinder <HTMLPanel, ValidationErrorsDialogBox> {
   }
   private static ValidationErrorsDialogBoxUiBinder uiBinder = GWT.create (ValidationErrorsDialogBoxUiBinder.class);
   
   protected static final int FADE_OUT_MILLIS = 6000;

   protected FadeAnimation fadeAnimation = null;

   public ValidationErrorsDialogBox () {
      setWidget (uiBinder.createAndBindUi (this));
      
      fadeAnimation = new FadeAnimation (getElement ());
   }

   public void show () {
      super.show ();

      fadeAnimation.fade (FADE_OUT_MILLIS, 1.0, 0.0, this);
   }

   public static ValidationErrorsDialogBox showDialog () {
      ValidationErrorsDialogBox validationErrorsDialogBox = new ValidationErrorsDialogBox ();
      validationErrorsDialogBox.setGlassEnabled (false);
      validationErrorsDialogBox.setAnimationEnabled (false);
      validationErrorsDialogBox.center ();
      validationErrorsDialogBox.show ();
      return validationErrorsDialogBox;
   }
}
