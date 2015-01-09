package es.uned.grc.pfc.meteo.client.view.renderer;

import com.google.gwt.text.shared.AbstractRenderer;

import es.uned.grc.pfc.meteo.client.util.IClientConstants;

/**
 * Renderer for ValueListBox for Boolean
 */
public class BooleanRenderer extends AbstractRenderer <Boolean> implements ILocalizedRenderer {
   
   @Override
   public String render (Boolean value) {
      return (value != null && value) ? IClientConstants.textConstants.trueValue () : IClientConstants.textConstants.falseValue ();
   }

   @Override
   public String getLocalizedValue (String value) {
      Boolean booleanValue = false;
      
      if (value != null) {
         if ("1".equals (value)) {
            booleanValue = true;
         } else {
            booleanValue = Boolean.parseBoolean (value);
         }
      }
      
      return render (booleanValue);
   }
}
