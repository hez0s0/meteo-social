package es.uned.grc.pfc.meteo.client.view.widget;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;

import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;

public class ValueTextBox extends ExtendedTextBox {

   /*************************************** Variables ***************************************/
   private int min = 0;
   private int max = 100;
   private boolean minConstrained = true;
   private boolean maxConstrained = true;
   private int minDigits = 1;
   private int step = 1;

   private KeyUpHandler keyUpHandler = new KeyUpHandler () {

      @Override
      public void onKeyUp (KeyUpEvent event) {
         if (isReadOnly () || !isEnabled ()) {
            return;
         }

         int keyCode = event.getNativeEvent ().getKeyCode ();
         boolean processed = false;

         switch (keyCode) {
            case KeyCodes.KEY_LEFT:
            case KeyCodes.KEY_RIGHT:
            case KeyCodes.KEY_BACKSPACE:
            case KeyCodes.KEY_DELETE:
            case KeyCodes.KEY_TAB:
               if (getText ().isEmpty ()) {
                  setValue (formatValue (min));
               }
               return;
            case KeyCodes.KEY_UP:
               if (step != 0) {
                  increaseValue ();
                  processed = true;
               }
               break;
            case KeyCodes.KEY_DOWN:
               if (step != 0) {
                  decreaseValue ();
                  processed = true;
               }
               break;
            default:
               break;
         }

         if (processed) {
            cancelKey ();
         }
      }
   };

   private KeyPressHandler keyPressHandler = new KeyPressHandler () {

      @Override
      public void onKeyPress (KeyPressEvent event) {
         if (isReadOnly () || !isEnabled ()) {
            return;
         }

         int keyCode = event.getNativeEvent ().getKeyCode ();

         switch (keyCode) {
            case KeyCodes.KEY_LEFT:
            case KeyCodes.KEY_RIGHT:
            case KeyCodes.KEY_BACKSPACE:
            case KeyCodes.KEY_DELETE:
            case KeyCodes.KEY_TAB:
            case KeyCodes.KEY_UP:
            case KeyCodes.KEY_DOWN:
               return;
         }

         int index = getCursorPos ();
         String previousText = getText ();
         String newText;
         if (getSelectionLength () > 0) {
            newText = previousText.substring (0, getCursorPos ()) + event.getCharCode () + previousText.substring (getCursorPos () + getSelectionLength (), previousText.length ());
         } else {
            newText = previousText.substring (0, index) + event.getCharCode () + previousText.substring (index, previousText.length ());
         }
         cancelKey ();

         setValue (newText, true);
      }
   };

   public ValueTextBox (int value) {
      this (value, 0, 100);
   }

   public ValueTextBox (int value, int min, int max) {
      this (value, min, max, true);
   }

   public ValueTextBox (int value, int min, int max, boolean constrained) {
      this (value, min, max, constrained, constrained);
   }

   public ValueTextBox (int value, int min, int max, boolean minConstrained, boolean maxConstrained) {
      super ();

      addKeyPressHandler (keyPressHandler);
      addKeyUpHandler (keyUpHandler);

      this.min = min;
      this.max = max;
      this.minConstrained = minConstrained;
      this.maxConstrained = maxConstrained;

      setValue (formatValue (value), false);
      setAlignment (TextAlignment.CENTER);
   }

   /**
    * Sets the minimal digits
    */
   public void setMinDigits (int minDigits) {
      if (minDigits > 0) {
         this.minDigits = minDigits;

         String value = getText ();
         long newValue = parseValue (value);

         setText (formatValue (newValue));
      }
   }

   /**
    * Sets the steps
    */
   public void setSteps (int step) {
      this.step = step;
   }

   /**
    * Increase with the given value
    */
   protected void increaseValue () {
      if (step != 0) {
         String value = getText ();
         long newValue = parseValue (value);
         newValue += step;
         if (maxConstrained && (newValue > max)) {
            return;
         }
         setValue (formatValue (newValue));
      }
   }

   /**
    * Decrease with the given value
    */
   protected void decreaseValue () {
      if (step != 0) {
         String value = getText ();
         long newValue = parseValue (value);
         newValue -= step;
         if (minConstrained && (newValue < min)) {
            return;
         }
         setValue (formatValue (newValue));
      }
   }

   protected String formatValue (long value) {
      String newValue = String.valueOf (value);

      if (minDigits > newValue.length ()) {
         String leading = PortableStringUtils.repeat ("0", (minDigits - newValue.length ()));
         newValue = leading + newValue;
      }

      return newValue;
   }
   
   protected long parseValue (String value) {
      return Long.valueOf (value);
   }

   /*************************************** Override Methods ***************************************/

   @Override
   public void setValue (String value) {
      setValue (value, false);
   }

   public void setValue (Integer value) {
      setValue (String.valueOf (value));
   }

   /**
    * Gets the value of the field as an Double, or null if none or not
    * parseable
    */
   public Double getDoubleValue () {
      Double doubleValue = null;
      String value = null;

      try {
         value = getValueOrThrow ();
         doubleValue = Double.parseDouble (value);
      } catch (Exception e) {
      }

      return doubleValue;
   } //end of getIntegerValue

   /**
    * Gets the value of the field as an Integer, or null if none or not
    * parseable
    */
   public Integer getIntegerValue () {
      Integer intValue = null;
      String value = null;

      try {
         value = getValueOrThrow ();
         intValue = Integer.parseInt (value);
      } catch (Exception e) {
      }

      return intValue;
   } //end of getIntegerValue

   @Override
   public void setValue (String value, boolean fireEvents) {
      try {
         long newValue = parseValue (value);
         if ((maxConstrained && (newValue > max)) || (minConstrained && (newValue < min))) {
            return;
         }
         String prevText = getValue ();
         super.setText (formatValue (newValue));
         if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual (this, getValue (), prevText);
         }
      } catch (Exception ex) {
         // Do Nothing
      }
   }
} //end of ValueTextBox
