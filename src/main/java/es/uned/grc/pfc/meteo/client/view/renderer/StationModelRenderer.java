package es.uned.grc.pfc.meteo.client.view.renderer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

import es.uned.grc.pfc.meteo.client.model.IStationModelProxy;

/**
 * Renderer for ValueListBox for workgroups
 */
public class StationModelRenderer extends AbstractEntityRenderer <IStationModelProxy> {
   /** the text constants used by this class */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("(Select a station model)") @Meaning ("Empty list element title")
      String empty ();
   }
   private static TextConstants textConstants = GWT.create (TextConstants.class);
   
   @Override
   public String render (IStationModelProxy stationModel) {
      return (stationModel != null) ? stationModel.getName () : textConstants.empty ();
   }

   @Override
   protected String [] getConstants () {
      return null;
   }
}
