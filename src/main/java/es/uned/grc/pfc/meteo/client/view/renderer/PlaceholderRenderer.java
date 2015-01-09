package es.uned.grc.pfc.meteo.client.view.renderer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.text.shared.AbstractRenderer;

import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.client.view.IPlaceholderContainer;
import es.uned.grc.pfc.meteo.client.view.util.EnumTranslator;

/**
 * Renderer for placeholders
 */
public class PlaceholderRenderer extends AbstractRenderer <String> {

   private static IPlaceholderContainer.EnumTextConstants enumTextConstants = GWT.create (IPlaceholderContainer.EnumTextConstants.class);
   private static EnumTranslator enumTranslator = new EnumTranslator (enumTextConstants);

   @Override
   public String render (String placeholderKey) {
      IPlaceholderContainer.Placeholders enumValue = null;
      String result = null;
      
      if (!PortableStringUtils.isEmpty (placeholderKey)) {
         try {
            enumValue = IPlaceholderContainer.Placeholders.valueOf (placeholderKey);
            result = enumTranslator.getText (enumValue);
         } catch (Exception e) {
            //silent: if the value is not an enum, it is directly some text
            GWT.log ("Placeholder " + placeholderKey + " not identified within the enum");
         }
   
         if (PortableStringUtils.isEmpty (result)) {
            result = placeholderKey;
         }
      } else {
         result = "";
      }
      
      return result;
   }
}
