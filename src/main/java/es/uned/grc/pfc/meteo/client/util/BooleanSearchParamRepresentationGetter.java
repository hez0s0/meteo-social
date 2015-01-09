package es.uned.grc.pfc.meteo.client.util;

public class BooleanSearchParamRepresentationGetter extends SearchParamRepresentationGetter <Boolean> {
   
   @Override
   public String getRepresentationGetter (Boolean value) {
      return (value != null && value) ? IClientConstants.textConstants.trueValue () : IClientConstants.textConstants.falseValue ();
   }
}
