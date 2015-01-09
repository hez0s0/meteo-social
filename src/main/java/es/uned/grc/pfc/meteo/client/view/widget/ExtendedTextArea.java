package es.uned.grc.pfc.meteo.client.view.widget;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.TextArea;

import es.uned.grc.pfc.meteo.client.view.IPlaceholderContainer;
import es.uned.grc.pfc.meteo.client.view.renderer.PlaceholderRenderer;
import es.uned.grc.pfc.meteo.client.view.util.TextBoxBaseUtils;

/**
 * Extension to support HTML5 attributes (like placeholder)
 */
public class ExtendedTextArea extends TextArea implements IPlaceholderContainer, INonUnicodeTextBoxBase {
   
   protected static PlaceholderRenderer placeholderRenderer = new PlaceholderRenderer ();
   
   protected String placeholder = null;
   protected boolean preventUnicode = true;
   
   public ExtendedTextArea () {
      TextBoxBaseUtils.registerUnicodeHandlers (this);
   }
   
   @Override
   @UiChild (tagname = "placeholder")
   public void setPlaceholder (String placeholderKey) {
      this.placeholder = getPlaceHolderText (placeholderKey);
      
      InputElement inputElement = getElement ().cast ();
      inputElement.setPropertyString ("placeholder", placeholder);
   }

   @Override
   public String getPlaceholder () {
      return placeholder;
   }

   @Override
   public String getPlaceHolderText (String placeholderKey) {
      return placeholderRenderer.render (placeholderKey);
   }
   
   @Override
   @UiChild (tagname = "preventUnicode")
   public void setPreventUnicode (String preventUnicodeString) {
      this.preventUnicode = Boolean.valueOf (preventUnicodeString);
   }
   
   @Override
   public boolean getPreventUnicode () {
      return preventUnicode;
   }
}
