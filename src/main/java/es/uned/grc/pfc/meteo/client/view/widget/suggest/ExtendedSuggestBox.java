package es.uned.grc.pfc.meteo.client.view.widget.suggest;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;

import es.uned.grc.pfc.meteo.client.view.IPlaceholderContainer;
import es.uned.grc.pfc.meteo.client.view.renderer.PlaceholderRenderer;

public class ExtendedSuggestBox extends SuggestBox implements IPlaceholderContainer {
   
   protected static PlaceholderRenderer placeholderRenderer = new PlaceholderRenderer ();
   
   protected String placeholder = null;

   @SuppressWarnings ("rawtypes")
   public ExtendedSuggestBox (EntitySuggestOracle entitySuggestOracle) {
      super (entitySuggestOracle);
   }
   
   @SuppressWarnings ("rawtypes")
   public ExtendedSuggestBox (EntitySuggestOracle entitySuggestOracle, TextBox textBox) {
      super (entitySuggestOracle, textBox);
   }

   @UiChild (tagname="placeholder")
   public void setPlaceholder (String placeholderKey) {
      this.placeholder = getPlaceHolderText (placeholderKey);
      
      InputElement inputElement = getElement ().cast ();
      inputElement.setPropertyString("placeholder", placeholder);
   } //end of setPlaceholder

   public String getPlaceholder () {
      return placeholder;
   }

   @Override
   public String getPlaceHolderText (String placeholderKey) {
      return placeholderRenderer.render (placeholderKey);
   }
}
