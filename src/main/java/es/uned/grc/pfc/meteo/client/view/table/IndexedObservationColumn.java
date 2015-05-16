package es.uned.grc.pfc.meteo.client.view.table;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.cellview.client.Column;

import es.uned.grc.pfc.meteo.client.model.IObservationBlockProxy;
import es.uned.grc.pfc.meteo.client.model.IObservationProxy;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;

public class IndexedObservationColumn extends Column <IObservationBlockProxy, String> {
   /** text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue (" (*)") @Meaning ("a mark that should attract attention")
      String attentionMark ();
   }
   public static TextConstants TEXT_CONSTANTS = GWT.create (TextConstants.class);
   
   private final int index;

   public IndexedObservationColumn (int index) {
      super (new TextCell ());
      this.index = index;
   }

   @Override
   public String getValue (IObservationBlockProxy observationBlock) {
      StringBuilder builder = new StringBuilder ();
      IObservationProxy observation = observationBlock.getObservations ().get (index);
      
      if (observation == null || observation.getValue () == null) {
         builder.append (IClientConstants.TEXT_CONSTANTS.emptyValue ());
      } else {
         builder.append (observation.getValue ());
         if (observation.getQuality () != null && !observation.getQuality ()) {
            builder.append (TEXT_CONSTANTS.attentionMark ());
         }
      }
      return builder.toString ();
   }
}