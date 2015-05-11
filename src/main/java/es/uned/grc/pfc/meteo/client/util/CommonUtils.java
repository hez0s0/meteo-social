package es.uned.grc.pfc.meteo.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;

/**
 * A collection of utility methods
 */
public class CommonUtils {

   private CommonUtils () {
      //do not construct
   }

   public static String getBaseUrl () {
      String url = GWT.getHostPageBaseURL ();

      if ((url != null) && (url.endsWith ("/"))) {
         url = url.substring (0, url.length () - 1);
      }

      return url;
   }

   public static String getServerUrl () {
      String urlString = getBaseUrl ();

      //very ugly!! but there seems to be no replacement for java.net.URL
      AnchorElement anchor = Document.get ().createAnchorElement ();
      anchor.setHref (urlString);

      return PortableStringUtils.format ("%s//%s", anchor.getPropertyString ("protocol"), anchor.getPropertyString ("host"));
   }

   /**
    * Emulates the isAssignableFrom method of a class (not usable in GWT)
    */
   public static boolean isAssignableFrom (Object obj, Class <?> cls) {
      if (cls == null) {
         return false;
      }

      if (cls.equals (obj)) {
         return true;
      }

      Class <?> currentSuperClass = cls.getSuperclass ();
      while (currentSuperClass != null) {
         if (currentSuperClass.equals (cls)) {
            return true;
         }
         currentSuperClass = currentSuperClass.getSuperclass ();
      }

      return false;
   }
}