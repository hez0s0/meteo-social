package es.uned.grc.pfc.meteo.client.view.widget.dialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;

import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.view.util.DisplayUtils;
import es.uned.grc.pfc.meteo.shared.validator.ClientValidationGroup;

public class ActionDialogBox extends DialogBox {

   public enum ButtonType {ACCEPT, CANCEL};

   @UiField
   protected Button acceptButton = null;
   @UiField
   protected Button cancelButton = null;
   
   protected boolean actionTaken = false;

   protected Map <String, SpanElement> pathToSpanMap = new HashMap <String, SpanElement> ();
   protected Map <ButtonType, IActionDialogBoxEventHandler> clickHandlers = new HashMap <ButtonType, IActionDialogBoxEventHandler> ();

   public ActionDialogBox () {
   }
   
   public ActionDialogBox (boolean autohide, boolean modal) {
      super (autohide, modal);
   }

   public HasClickHandlers getAcceptClickHandler () {
      return acceptButton;
   }

   public HasClickHandlers getCancelClickHandler () {
      return cancelButton;
   }

   @UiHandler ("acceptButton")
   protected synchronized void handleAcceptClick (ClickEvent e) {
      if (!actionTaken) {
         actionTaken = true;
         
         Set <ConstraintViolation <Object>> violations = null;
         clearErrors ();
         violations = validate ();
         if ((violations == null) || (violations.isEmpty ())) {
            fireEvents (ButtonType.ACCEPT);
            if (closeOnAccept ()) {
               hide ();
            }
         } else {
            displayValidationErrors (violations);
         }
         
         actionTaken = false;
      }
   }

   @UiHandler ("cancelButton")
   protected synchronized void handleCancelClick (ClickEvent e) {
      if (!actionTaken) {
         actionTaken = true;
         
         fireEvents (ButtonType.CANCEL);
         hide ();
         
         actionTaken = false;
      }
   }

   protected void clearClickHandlers () {
      clickHandlers.clear ();
   }

   protected void fireEvents (ButtonType selectedButtonType) {
      ActionDialogBoxClickEvent event = new ActionDialogBoxClickEvent (this);
      for (Map.Entry <ButtonType, IActionDialogBoxEventHandler> entry : clickHandlers.entrySet ()) {
         if (entry.getKey ().equals (selectedButtonType)) {
            entry.getValue ().onActionClicked (event);
         }
      }
   }

   public ActionDialogBox addClickHandler (ButtonType type, IActionDialogBoxEventHandler handler) {
      clickHandlers.put (type, handler);
      return this; //so that chained calls are possible
   }

   protected static ActionDialogBox showDialog (ActionDialogBox actionDialogBox) {
      return showDialog (actionDialogBox, true);
   }

   protected static ActionDialogBox showDialog (ActionDialogBox actionDialogBox, boolean modal) {
      return showDialog (actionDialogBox, modal, true);
   }
   
   protected static ActionDialogBox showDialog (ActionDialogBox actionDialogBox, boolean modal, boolean center) {
      actionDialogBox.setModal (modal);
      if (modal) {
         actionDialogBox.setGlassEnabled (true);
      }
      actionDialogBox.setAnimationEnabled (false);
      if (!center) {
         actionDialogBox.setPopupPosition (actionDialogBox.getPopupLeft (), 0);
         actionDialogBox.show ();
      } else {
         actionDialogBox.center ();
      }

      return actionDialogBox; //so that chained calls are possible
   }

   protected Set <ConstraintViolation <Object>> validate () {
      Object model = getValidatableModel ();
      if (model != null) {
         Validator validator = Validation.buildDefaultValidatorFactory ().getValidator ();
         return validator.validate (model, Default.class, ClientValidationGroup.class);
      } else {
         return null;
      }
   }

   public void displayValidationErrors (Set <ConstraintViolation <Object>> violations) {
      if (pathToSpanMap != null) {
         for (ConstraintViolation <Object> violation : violations) {
            DisplayUtils.showSpan (pathToSpanMap.get (violation.getPropertyPath ().toString ()), IClientConstants.textMessages.fieldMessage (violation.getMessage ()));
         }
      }
   }

   public void clearErrors () {
      DisplayUtils.clearErrors (pathToSpanMap);
   }

   protected void createSpanMap () {
      //to be implemented by children, if needed
   }

   protected Object getValidatableModel () {
      //to be implemented by children, if needed
      return null;
   }

   /**
    * If the dialog shall be closed after clicking on accept (if no validation
    * errors happened).
    */
   protected boolean closeOnAccept () {
      return true;
   }
}
