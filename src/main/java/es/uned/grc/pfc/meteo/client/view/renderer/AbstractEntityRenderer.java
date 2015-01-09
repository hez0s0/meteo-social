package es.uned.grc.pfc.meteo.client.view.renderer;

import com.google.gwt.text.shared.AbstractRenderer;

import es.uned.grc.pfc.meteo.client.view.util.FormUtils;

public abstract class AbstractEntityRenderer <E extends Object> extends AbstractRenderer <E> implements ILocalizedRenderer {

   protected abstract String [] getConstants ();
   protected String [] constants = null;
   
   public String getLocalizedValue (E element) {
      return (element != null) ? element.toString () : ""; 
   }
   
   @Override
   public String getLocalizedValue (String value) {
      initConstants ();
      
      return (constants != null) ? FormUtils.getLocalizedValue (value, constants) : value;
   }

   protected void initConstants () {
      if (constants == null) {
         constants = getConstants ();
      }
   }
   
}
