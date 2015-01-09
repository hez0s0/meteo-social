package es.uned.grc.pfc.meteo.client.view;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.LocalizableResource.GenerateKeys;

public interface IPlaceholderContainer {

   public enum Placeholders {stationNameKey};

   @Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface EnumTextConstants extends ConstantsWithLookup {
      @DefaultStringValue ("type in some text within names or descriptions") @Meaning ("es_uned_grc_pfc_meteo_client_view_IPlaceholderContainer_Placeholders_stationNameKey")
      String es_uned_grc_pfc_meteo_client_view_IPlaceholderContainer_Placeholders_stationNameKey ();
   }

   String getPlaceholder ();
   void setPlaceholder (String placeholder);
   String getPlaceHolderText (String placeholderKey);
}
