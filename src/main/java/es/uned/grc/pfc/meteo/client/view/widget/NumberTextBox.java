package es.uned.grc.pfc.meteo.client.view.widget;

import com.google.gwt.uibinder.client.UiChild;

import es.uned.grc.pfc.meteo.client.view.util.DisplayUtils;

public class NumberTextBox extends MaskedTextBox {
   
   private static final int DEFAULT_DECIMAL_POSITIONS = 2;
   
   public NumberTextBox () {
      this (DEFAULT_DECIMAL_POSITIONS);
   }
   
   public NumberTextBox (int decimalPositions) {
      super (new RegularExpressionValidationStrategy ("\\d+[\\.\\,]?\\d" + getDecimalPositionsRE (decimalPositions) + ""), null);
   }

   private static String getDecimalPositionsRE (int decimalPositions) {
      return (decimalPositions <= 0) ? "*" : "{0," + decimalPositions + "}";
   }

   public void setDoubleValue (Double value) {
      String text = "";
      
      if (value != null) {
         text = DisplayUtils.toString (value);
      }

      setText (text);
   }

   public Double getDoubleValue () {
      String text = getText ();
      Double value = null;

      try {
         value = Double.valueOf (text.replaceAll ("\\s+", " ").replaceAll ("\\,", "."));
      } catch (Exception e) {} //silent
      
      return value;
   }

   @UiChild (tagname = "styleName")
   public void setStyleName (String styleName) {
      super.setStyleName (styleName);
   }
}