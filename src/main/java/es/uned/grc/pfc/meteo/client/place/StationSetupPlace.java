package es.uned.grc.pfc.meteo.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Place to be used for URL representation of a station setup Place
 */
public class StationSetupPlace extends AbstractPlace {

   /**
    * URL to Place and Place to URL tokenizer
    */
   public static class Tokenizer implements PlaceTokenizer <StationSetupPlace> {

      /**
       * Given a URL, build a Place
       */
      @Override
      public StationSetupPlace getPlace (String token) {
         return new StationSetupPlace ();
      }

      /**
       * Given the place, build the URL
       */
      @Override
      public String getToken (StationSetupPlace place) {
         return "";
      }
   }
}