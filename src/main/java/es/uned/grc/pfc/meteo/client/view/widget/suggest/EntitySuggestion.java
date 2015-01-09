package es.uned.grc.pfc.meteo.client.view.widget.suggest;

import java.io.Serializable;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.web.bindery.requestfactory.shared.BaseProxy;

@SuppressWarnings ("serial")
public abstract class EntitySuggestion <E extends BaseProxy>  implements SuggestOracle.Suggestion, Serializable {
   
   protected E entityProxy = null;

   public EntitySuggestion (E entityProxy) {
      this.entityProxy = entityProxy;
   } //end of EntitySuggestion
   
   public E getEntityProxy () {
      return entityProxy;
   } //end of getEntityProxy
} //end of EntitySuggestion
