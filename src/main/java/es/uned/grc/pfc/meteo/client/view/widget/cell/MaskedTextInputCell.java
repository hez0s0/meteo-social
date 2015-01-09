package es.uned.grc.pfc.meteo.client.view.widget.cell;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;

import es.uned.grc.pfc.meteo.client.view.widget.IValidationStrategy;

public class MaskedTextInputCell extends KeypressableTextInputCell {
   private IValidationStrategy overallFormValidationStrategy = null;
   private IValidationStrategy validKeystrokeValidationStrategy = null;

   public MaskedTextInputCell (IValidationStrategy overallFormValidationStrategy, IValidationStrategy validKeystrokeValidationStrategy) {
      this.overallFormValidationStrategy = overallFormValidationStrategy;
      this.validKeystrokeValidationStrategy = validKeystrokeValidationStrategy;
   }

   @Override
   public void onBrowserEvent (Context context, Element parent, String value, NativeEvent event, ValueUpdater <String> valueUpdater) {
      String valueInInputElement = null;
      char keystroke = 0;

      super.onBrowserEvent (context, parent, value, event, valueUpdater);

      if ("keypress".equals (event.getType ())) {
         keystroke = (char) event.getCharCode ();
         if (validKeystrokeValidationStrategy != null) {
            handleInvalidKeystroke (String.valueOf (keystroke), event);
         } else {
            valueInInputElement = getInputElement (parent).getValue ();
            if (isPrintableChar (keystroke)) {
               valueInInputElement += String.valueOf (keystroke);
            }
            handleInvalidOverallForm (valueInInputElement, event);
         }
      } else if ("blur".equals (event.getType ())) {
         valueInInputElement = getInputElement (parent).getValue ();
         handleInvalidOverallForm (valueInInputElement, event);
      }
   }

   protected void handleInvalidOverallForm (String valueOfEntireField, NativeEvent event) {
      if ( (!valueOfEntireField.isEmpty ()) && (!overallFormValidationStrategy.matches (valueOfEntireField)) ) {
         event.preventDefault (); // prevent it, but maybe dispatch an event as well?
      }
   }

   protected void handleInvalidKeystroke (String keystroke, NativeEvent event) {
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
