package es.uned.grc.pfc.meteo.client.view.widget.cell;

import es.uned.grc.pfc.meteo.client.view.widget.RegularExpressionValidationStrategy;



public class NumberInputCell extends MaskedTextInputCell {
   public NumberInputCell () {
      super (new RegularExpressionValidationStrategy ("\\s*\\d+\\.?\\d*\\s*"), null);
   }
}