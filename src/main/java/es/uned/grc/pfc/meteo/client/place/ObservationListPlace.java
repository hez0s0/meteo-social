package es.uned.grc.pfc.meteo.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Place to be used for URL representation of a list Place of observations
 */
public class ObservationListPlace extends AbstractPlace {

   /**
    * URL to Place and Place to URL tokenizer
    */
   public static class Tokenizer implements PlaceTokenizer <ObservationListPlace> {

      /**
       * Given a URL, build a Place
       */
      @Override
      public ObservationListPlace getPlace (String token) {
         return new ObservationListPlace ();
      }

      /**
       * Given the place, build the URL
       */
      @Override
      public String getToken (ObservationListPlace place) {
         return "";
      }
   }
}