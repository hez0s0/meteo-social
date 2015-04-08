package es.uned.grc.pfc.meteo.client.view.widget.suggest.impl;

import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestion;

public class CountrySuggestion extends EntitySuggestion <String> {

   public CountrySuggestion (String entityProxy) {
      super (entityProxy);
   }

   /**
    * Gets the display string associated with this suggestion. The
    * interpretation of the display string depends upon the value of its
    * oracle's
    */
   public String getDisplayString () {
      return (entityProxy != null) ? entityProxy : "";
   }

   /**
    * Gets the replacement string associated with this suggestion. When this
    * suggestion is selected, the replacement string will be entered into the
    * SuggestBox's text box.
    */
   public String getReplacementString () {
      return (entityProxy != null) ? entityProxy : "";
   }
}
