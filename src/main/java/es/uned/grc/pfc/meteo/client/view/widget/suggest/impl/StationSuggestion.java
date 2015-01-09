package es.uned.grc.pfc.meteo.client.view.widget.suggest.impl;

import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestion;

public class StationSuggestion extends EntitySuggestion <IStationProxy> {

   private static final long serialVersionUID = -6053279130257601995L;

   public StationSuggestion (IStationProxy entityProxy) {
      super (entityProxy);
   }

   /**
    * Gets the display string associated with this suggestion. The
    * interpretation of the display string depends upon the value of its
    * oracle's
    */
   public String getDisplayString () {
      return (entityProxy != null) ? entityProxy.getName () : "";
   }

   /**
    * Gets the replacement string associated with this suggestion. When this
    * suggestion is selected, the replacement string will be entered into the
    * SuggestBox's text box.
    */
   public String getReplacementString () {
      return (entityProxy != null) ? entityProxy.getName () : "";
   }
}
