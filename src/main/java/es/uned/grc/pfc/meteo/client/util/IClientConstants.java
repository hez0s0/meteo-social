package es.uned.grc.pfc.meteo.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Messages;

public interface IClientConstants {
   /** key codes to be considered action triggers */
   int [] ACTION_KEY_CODES = {32, KeyCodes.KEY_ENTER }; //32=spacebar
      
   /** global text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("-") @Meaning ("Empty value")
      String emptyValue ();
      
      @DefaultStringValue ("This field cannot be left empty") @Meaning ("Field validation message")
      String notEmpty ();

      @DefaultStringValue ("yes") @Meaning ("true value representation")
      String trueValue ();
      @DefaultStringValue ("no") @Meaning ("false value representation")
      String falseValue ();
      
      @DefaultStringValue ("Some unsaved changes were made on the form that will be lost. Do you still want to proceed?")
      @Meaning ("Unsaved changes being lost confirm question")
      String pendingChangesQuestion ();
   }
   public static TextConstants TEXT_CONSTANTS = GWT.create (TextConstants.class);
   
   /** global text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextMessages extends Messages {
      @DefaultMessage ("This field {0}") @Meaning ("Some validation message about a field. Params: the specific message")
      String fieldMessage (String message);
   }
   public static TextMessages TEXT_MESSAGES = GWT.create (TextMessages.class);

   /** default page size for tables */
   int DEFAULT_PAGE_SIZE = 20;
   
   /** default page size for tables */
   double DEFAULT_ZOOM_LEVEL = 12.0;
   
   /** the name of the cookie that stores the selected locale */
   String LOCALE_COOKIE_NAME = "localeCookie";
   
   /** spanish locale string on client-side */
   String LOCALE_SPANISH = "es_ES";
   /** english locale string on client-side */
   String LOCALE_ENGLISH = "en_EN";
   
   String DISPLAY_SHORT_DATE = "dd/MM/yyyy";
}
