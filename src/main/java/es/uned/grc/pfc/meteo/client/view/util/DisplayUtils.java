package es.uned.grc.pfc.meteo.client.view.util;

import java.util.Date;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.LocalizableResource.GenerateKeys;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.ui.HTML;

import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

/**
 * A bunch of static methods to format certain elements for display
 * with the same format everywhere.
 */
public class DisplayUtils {

   /**
    * The constants used in this Widget.
    */
   @Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextMessages extends Messages {
      @DefaultMessage ("From: {0} at {1}") @Meaning ("From label for tasks. Params: user, date")
      String fromLabel (String userName, String date);
      @DefaultMessage ("To: {0}") @Meaning ("To label for tasks. Params: user")
      String toLabel (String userName);
   }
   public static TextMessages textMessages = GWT.create (TextMessages.class);

   private static final int MAX_LENGTH = 50;
   
   /**
    * Gets the given String if not null or EMPTY.
    */
   public static String getString (String input) {
      return (!PortableStringUtils.isEmpty (input)) ? input : IClientConstants.textConstants.emptyValue ();
   }
   
   /**
    * Append a given string into a buffer if not empty. Append a blank if required afterwards.
    */
   public static void appendIfNotEmpty (StringBuffer target, String input, boolean appendBlank) {
      if (!PortableStringUtils.isEmpty (input)) {
         target.append (input);
         if (appendBlank) {
            target.append (" ");
         }
      }
   }

   /**
    * Gets a date formatted with default DATE_TIME_SHORT format, or EMPTY.
    */
   public static String getFormattedDate (Date date) {
      return getFormattedDate (date, DateTimeFormat.getFormat (PredefinedFormat.DATE_TIME_SHORT));
   }

   /**
    * Gets a date formatted with dgiven format, or EMPTY.
    */
   public static String getFormattedDate (Date date, DateTimeFormat dateTimeFormat) {
      return (date != null) ? dateTimeFormat.format (date) : IClientConstants.textConstants.emptyValue ();
   }
      
   /**
    * Clears (hides) some error fields
    */
   public static void clearErrors (Map <String, SpanElement> pathToSpanMap) {
      if (pathToSpanMap != null) {
         for (SpanElement span : pathToSpanMap.values ()) {
            clearErrors (span);
         }
      }
   }
   
   public static void clearErrors (SpanElement span) {
      DivElement divElement = null;
      if (span != null) {
         span.setInnerText ("");
         FormUtils.setElementVisible (span, false);
         
         if ( (span.getParentElement () != null)
               && (span.getParentElement ().getClass ().getName ().equals (DivElement.class.getName ())) ) { //no instanceof client-side
            divElement = (DivElement) span.getParentElement ();
            divElement.removeClassName ("failedValidatableField");
         }
      }
   }

   public static void showSpan (SpanElement span, String message) {
      DivElement divElement = null;
//      List <DisclosurePanel> disclosurePanels = new ArrayList <DisclosurePanel> ();
      
      if (span != null) {
         span.setInnerText (message);
         FormUtils.setElementVisible (span, "block");
         
         if ( (span.getParentElement () != null)
           && (span.getParentElement ().getClass ().getName ().equals (DivElement.class.getName ())) ) { //no instanceof client-side
            divElement = (DivElement) span.getParentElement ();
            divElement.addClassName ("failedValidatableField");
         }
      }
   }

   public static String toString (Double value) {
      return (value != null) ? String.valueOf (value).replaceAll ("\\.0*$", "")
                                                     .replaceAll ("\\.", ISharedConstants.DECIMAL_SEPARATOR)
                             : "";
   }
   
   public static String formatDouble (String value) {
      String [] parts = null;
      double rightPart = -1;
      StringBuffer partsConcat = new StringBuffer ();
      String result = value;
      
      try {
         if (!PortableStringUtils.isEmpty (value)) {
            if (value.indexOf (ISharedConstants.DECIMAL_SEPARATOR) > 0) {
               parts = value.split (ISharedConstants.DECIMAL_SEPARATOR_RE);
               if (parts.length == 2) {
                  partsConcat.append (parts [0]);
                  rightPart = Double.parseDouble (parts [1]);
                  if (rightPart > 0) {
                     partsConcat.append (ISharedConstants.DECIMAL_SEPARATOR);
                     partsConcat.append (rightPart);
                  }
                  result = partsConcat.toString ();
               }
            }
         }
      } catch (Exception e) {
         //nothing to do, just get the raw representation
      }
      
      return result;
   }

   public static String prepareForTable (String input) {
      String result = null;
      
      if (input != null) {
         result = input;
         result = result.replaceAll ("\\<[^>]*>", ""); //remove HTML tags
         result = new HTML (result).getText (); //unescape HTML text (the ones like &lt; &gt; ...) 

         if (result.length () > MAX_LENGTH) {
            result = result.substring (0, MAX_LENGTH);
            result += " ...";
         }
      }
      
      return result;
   }
}
