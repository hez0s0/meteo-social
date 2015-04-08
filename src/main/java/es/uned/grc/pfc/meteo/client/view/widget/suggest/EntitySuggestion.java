package es.uned.grc.pfc.meteo.client.view.widget.suggest;

import java.io.Serializable;

import com.google.gwt.user.client.ui.SuggestOracle;

@SuppressWarnings ("serial")
public abstract class EntitySuggestion <E extends Object>  implements SuggestOracle.Suggestion, Serializable {
   
   protected E entityProxy = null;

   public EntitySuggestion (E entityProxy) {
      this.entityProxy = entityProxy;
   }
   
   public E getEntityProxy () {
      return entityProxy;
   }
}
