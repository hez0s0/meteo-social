package es.uned.grc.pfc.meteo.client.view.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.LocalizableResource.GenerateKeys;
import com.google.gwt.i18n.client.Messages;

import es.uned.grc.pfc.meteo.client.view.widget.INonUnicodeTextBoxBase;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.AlertDialogBox;

public class TextBoxBaseUtils {
   /** global text constants */
   @Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextMessages extends Messages {
      @DefaultMessage ("There were invalid characters that have been removed: {0}")
      @Meaning ("Display of an error message about invalid characters")
      String invalidCharacters (String invalidCharacters);
   }
   public static TextMessages textMessages = GWT.create (TextMessages.class);
   
   public static void registerUnicodeHandlers (final INonUnicodeTextBoxBase textBox) {
      textBox.addKeyPressHandler (new KeyPressHandler() {
         
         @Override
         public void onKeyPress (KeyPressEvent event) {
            if (textBox.getPreventUnicode ()) {
               handleInvalidKeystroke ((char) event.getCharCode (), event);
            }
         }
      });
      
      textBox.addBlurHandler (new BlurHandler() {
         
         @Override
         public void onBlur (BlurEvent event) {
            if (textBox.getPreventUnicode ()) {
               handleInvalidOverallForm (textBox, event);
            }
         }
      });
   }
   
   public static void handleInvalidOverallForm (INonUnicodeTextBoxBase textBox, DomEvent <?> event) {
      List <Character> unicode = null;
      StringBuffer errorChars = new StringBuffer ();
      StringBuffer correctValue = new StringBuffer (); //it must be constructed, since it is an output param!!
      
      unicode = getWithoutUnicode (textBox.getText (), correctValue);
      if (unicode.size () > 0) {
         for (Character character : unicode) {
            if (errorChars.length () > 0) {
               errorChars.append (", ");
            }
            errorChars.append (String.valueOf (character));
         }

         event.preventDefault ();
         event.stopPropagation ();
         
         textBox.setText (correctValue.toString ());
         
         AlertDialogBox.showWarning (textMessages.invalidCharacters (errorChars.toString ()));
      }
   }

   public static void handleInvalidKeystroke (char keystroke, DomEvent <?> event) {
      if (isUnicode (keystroke)) {
         event.preventDefault ();
      }
   }

   public static List <Character> getWithoutUnicode (String input, StringBuffer output) {
      List <Character> unicode = new ArrayList <Character> ();
      
      if (input != null) {
         for (int i = 0; i < input.length (); i ++) {
            if (isUnicode (input.charAt (i))) {
               unicode.add (input.charAt (i));
            } else {
               output.append (input.charAt (i));
            }
         }
      }
      
      return unicode;
   }
   
   public static boolean isUnicode (char c) {
      return c >= 256;
   }
}
