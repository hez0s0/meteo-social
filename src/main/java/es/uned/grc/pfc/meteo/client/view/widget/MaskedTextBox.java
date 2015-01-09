package es.uned.grc.pfc.meteo.client.view.widget;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;

public class MaskedTextBox extends ExtendedTextBox {
   
   private IValidationStrategy overallFormValidationStrategy = null;
   private IValidationStrategy validKeystrokeValidationStrategy = null;

   public MaskedTextBox (IValidationStrategy overallFormValidationStrategy, IValidationStrategy validKeystrokeValidationStrategy) {
      this.overallFormValidationStrategy = overallFormValidationStrategy;
      this.validKeystrokeValidationStrategy = validKeystrokeValidationStrategy;
      
      addKeyPressHandler (new KeyPressHandler() {
         
         @Override
         public void onKeyPress (KeyPressEvent event) {
            String valueInInputElement = null;
            char keystroke = 0;
            
            keystroke = (char) event.getCharCode ();
            if (MaskedTextBox.this.validKeystrokeValidationStrategy != null) {
               handleInvalidKeystroke (String.valueOf (keystroke), event);
            } else {
               valueInInputElement = getText ();
               if (isPrintableChar (keystroke)) {
                  valueInInputElement += String.valueOf (keystroke);
               }
               handleInvalidOverallForm (valueInInputElement, event);
            }
         } //end of onKeyPress
      });
      
      addBlurHandler (new BlurHandler() {
         
         @Override
         public void onBlur (BlurEvent event) {
            handleInvalidOverallForm (getText (), event);
         }
      });
   } //end of MaskedTextBox

   protected void handleInvalidOverallForm (String valueOfEntireField, DomEvent <?> event) {
      if ( (!valueOfEntireField.isEmpty ()) && (!overallFormValidationStrategy.matches (valueOfEntireField)) ) {
         event.preventDefault (); // prevent it, but maybe dispatch an event as well?
      }
   }

   protected void handleInvalidKeystroke (String keystroke, DomEvent <?> event) {
      if (!validKeystrokeValidationStrategy.matches (keystroke)) {
         event.preventDefault ();
      }
   }

   protected boolean isPrintableChar (char c) {
//      Character.UnicodeBlock block = Character.UnicodeBlock.of (c);
//      return (!Character.isISOControl (c)) && block != null && block != Character.UnicodeBlock.SPECIALS;
      return c > 19; //the good solution is not client-side portable
   }
}
