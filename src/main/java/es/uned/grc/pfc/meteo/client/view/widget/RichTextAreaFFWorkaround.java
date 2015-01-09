package es.uned.grc.pfc.meteo.client.view.widget;

import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RichTextArea;

import es.uned.grc.pfc.meteo.client.view.util.TextBoxBaseUtils;

public class RichTextAreaFFWorkaround extends RichTextArea implements INonUnicodeTextBoxBase {

   private boolean enabled = false;

   protected String placeholder = null;
   protected boolean preventUnicode = true;
   
   public RichTextAreaFFWorkaround () {
      TextBoxBaseUtils.registerUnicodeHandlers (this);
   }
   
   @Override
   public void setEnabled (boolean enabled) {
      this.enabled = enabled;
      super.setEnabled (enabled);
   }

   @Override
   public void onBrowserEvent (Event event) {
      super.onBrowserEvent (event);
      if (event.getType ().equals ("focus")) {
         setEnabled (enabled);
      }
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
