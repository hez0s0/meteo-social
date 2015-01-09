package es.uned.grc.pfc.meteo.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.LocalizableResource.GenerateKeys;
import com.google.gwt.i18n.client.Messages;

public interface IClientConstants {
   /** key codes to be considered action triggers */
   int [] ACTION_KEY_CODES = {32, KeyCodes.KEY_ENTER }; //32=spacebar
      
   /** global text constants */
   @Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Open and synchronized") @Meaning ("Drug item status")
      String statusSynchedOpen ();
      @DefaultStringValue ("Open but NOT synchronized") @Meaning ("Drug item status")
      String statusUnsynchedOpen ();
      @DefaultStringValue ("Locked by me") @Meaning ("Drug item status")
      String statusLockedByMe ();
      @DefaultStringValue ("Locked by another user") @Meaning ("Drug item status")
      String statusLocked ();
      @DefaultStringValue ("Deleted") @Meaning ("Drug item status")
      String statusDeleted ();
      @DefaultStringValue ("In review") @Meaning ("Drug item status")
      String statusInReview ();
      @DefaultStringValue ("Refused by reviewer") @Meaning ("Drug item status")
      String statusRefused ();
      
      @DefaultStringValue ("-") @Meaning ("Empty value")
      String emptyValue ();
      
      @DefaultStringValue ("Synchronization errors") @Meaning ("Title for sync errors display")
      String syncErrorsTitle ();
      
      @DefaultStringValue ("By changind the ROA group all proposals will get ROA and units reset. Would you like to proceed?")
      @Meaning ("Question to confirm change of ROA group")
      String roaGroupChangeQuestion ();
      
      @DefaultStringValue ("Import process was successful") @Meaning ("Success confirm message")
      String importSuccess ();
      
      @DefaultStringValue ("This field cannot be left empty") @Meaning ("Field validation message")
      String notEmpty ();

      @DefaultStringValue ("Another drug item with the same name is already present in given catalogs")
      @Meaning ("Drug item name collision in catalog")
      String drugNameCatalogUnique ();

      @DefaultStringValue ("Another drug item with the same name is already present in given drug lists")
      @Meaning ("Drug item name collision in drug list")
      String drugNameDrugListUnique ();

      @DefaultStringValue ("The current version of the drug item is synchronized with VMobil")
      @Meaning ("Tooltip within sync view")
      String synced ();
      @DefaultStringValue ("No version or an older version (not current) of the drug item is synchronized with VMobil")
      @Meaning ("Tooltip within sync view")
      String outdated ();
      @DefaultStringValue ("The drug item has no drug components, and thus cannot be synchronized with VMobil")
      @Meaning ("Tooltip within sync view")
      String unsynchronizable ();
      
      @DefaultStringValue ("yes") @Meaning ("true value representation")
      String trueValue ();
      @DefaultStringValue ("no") @Meaning ("false value representation")
      String falseValue ();
      
      @DefaultStringValue ("Some unsaved changes were made on the form that will be lost. Do you still want to proceed?")
      @Meaning ("Unsaved changes being lost confirm question")
      String pendingChangesQuestion ();
   }
   public static TextConstants textConstants = GWT.create (TextConstants.class);
   
   /** global text constants */
   @Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextMessages extends Messages {
      @DefaultMessage ("{0} (by {1})")
      @Meaning ("Identification of a version within drug item general form (detail). Params: version and userName")
      String versionBy (String version, String username);
      
      @DefaultMessage ("This field {0}") @Meaning ("Some validation message about a field. Params: the specific message")
      String fieldMessage (String message);
   }
   public static TextMessages textMessages = GWT.create (TextMessages.class);
   
   /** global text constants */
   public interface UntranslatableTextMessages extends Messages {
      @DefaultMessage ("{0}.{1}")
      String version (Integer major, Integer minor);
   }
   public static UntranslatableTextMessages untranslatableTextMessages = GWT.create (UntranslatableTextMessages.class);

   /** default page size for tables */
   int DEFAULT_PAGE_SIZE = 20;
   
   /** the name of the cookie that stores the selected locale */
   String LOCALE_COOKIE_NAME = "localeCookie";
   
   /** spanish locale string on client-side */
   String LOCALE_SPANISH = "es_ES";
   /** english locale string on client-side */
   String LOCALE_ENGLISH = "en_EN";
}
