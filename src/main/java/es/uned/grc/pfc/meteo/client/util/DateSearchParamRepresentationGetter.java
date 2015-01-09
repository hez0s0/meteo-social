package es.uned.grc.pfc.meteo.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;

import es.uned.grc.pfc.meteo.shared.ISharedConstants;

public class DateSearchParamRepresentationGetter extends SearchParamRepresentationGetter <Date> {

   DateTimeFormat dateFormat = DateTimeFormat.getFormat (ISharedConstants.SHARED_SHORT_DATE_FORMAT);
   DateTimeFormat representationDateFormat = DateTimeFormat.getFormat (PredefinedFormat.DATE_SHORT);
   
   
   @Override
   public String getRepresentationGetter (Date value) {
      return representationDateFormat.format (value);
   }
   
   @Override
   public String getValue (Date value) {
      return (value != null) ? dateFormat.format (value) : null;
   }
}
