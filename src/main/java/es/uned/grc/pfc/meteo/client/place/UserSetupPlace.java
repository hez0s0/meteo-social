package es.uned.grc.pfc.meteo.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Place to be used for URL representation of a user setup Place
 */
public class UserSetupPlace extends AbstractPlace {

   /**
    * URL to Place and Place to URL tokenizer
    */
   public static class Tokenizer implements PlaceTokenizer <UserSetupPlace> {

      /**
       * Given a URL, build a Place
       */
      @Override
      public UserSetupPlace getPlace (String token) {
         return new UserSetupPlace ();
      }

      /**
       * Given the place, build the URL
       */
      @Override
      public String getToken (UserSetupPlace place) {
         return "";
      }
   }
}