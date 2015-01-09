package es.uned.grc.pfc.meteo.client.util;

/**
 * Portable (for client-sice compilation) version of Apache Commons StringUtils.
 */
public class PortableStringUtils {

   public static boolean isEmpty (String input) {
      return ((input == null) || (input.isEmpty ()));
   }

   public static boolean isNumeric (CharSequence input) {
      if (input == null || input.length () == 0) {
         return false;
      }
      int sz = input.length ();
      for (int i = 0; i < sz; i++) {
         if (!Character.isDigit (input.charAt (i))) {
            return false;
         }
      }
      return true;
   }

   public static String format (final String format, final Object... args) {
      String [] split = format.split ("%s");
      final StringBuffer msg = new StringBuffer ();
      
      for (int pos = 0; pos < split.length; pos += 1) {
         msg.append (split [pos]);
         if (args.length > pos) {
            msg.append (args [pos]);
         }
      }
      
      return msg.toString ();
   }

   public static boolean isBoolean (String input) {
      try {
         Boolean.parseBoolean (input);
         return true;
      } catch (Exception e) {
         return false;
      }
   }
   
   public static String repeat (String input, int repetitions) {
      if ( (input != null) && (repetitions > 0) ){
         StringBuilder sb = new StringBuilder ();
         for (int i = 0; i < repetitions; i++) {
            sb.append (input);
         }
         return sb.toString ();
      } else {
         return input;
      }
   }

   public static String join (String [] list) {
      return join (list, null);
   }
   
   public static String join (String [] list, String sep) {
      String separator = (sep != null) ? sep : ", ";
      StringBuffer result = new StringBuffer ();
      
      if (list != null) {
         for (String element : list) {
            if (result.length () > 0) {
               result.append (separator);
            }
            result.append (element);
         }
      }
      
      return result.toString ();
   }

   public static Double parseDouble (String value) {
      Double result = null;
      try {
         result = Double.valueOf (value.replaceAll ("\\s+", ""));
      } catch (Exception e) {
         //silent
      }
      return result;
   }
}
