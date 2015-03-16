package es.uned.grc.pfc.meteo.client.place;

import java.util.Map;

import com.google.gwt.place.shared.PlaceTokenizer;

import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

/**
 * Place to be used for URL representation of a list Place of observations
 */
public class ObservationListPlace extends AbstractPlace {

   public enum ObservationType {NORMAL, DERIVED};
   public enum Representation {TEXT, GRAPHIC};

   private static final String TYPE_PARAM = "type";
   private static final String REPRESENTATION_PARAM = "representation";
   
   private ObservationType observationType = null;
   private Representation representation = null;

   public ObservationListPlace () {
      this (ObservationType.NORMAL, Representation.TEXT);
   }
   
   public ObservationListPlace (Representation representation) {
      this (ObservationType.NORMAL, representation);
   }
   
   public ObservationListPlace (ObservationType observationType, Representation representation) {
      this.observationType = observationType;
      this.representation = representation;
   }
   
   /**
    * URL to Place and Place to URL tokenizer
    */
   public static class Tokenizer implements PlaceTokenizer <ObservationListPlace> {

      /**
       * Given a URL, build a Place
       */
      @Override
      public ObservationListPlace getPlace (String token) {
         Map <String, String> params = simpleParse (token);
         String obsTypeString = params.get (TYPE_PARAM);
         ObservationType obsTypeParam = ObservationType.NORMAL;
         if (!PortableStringUtils.isEmpty (obsTypeString)) {
            try {
               obsTypeParam = ObservationType.valueOf (obsTypeString);
            } catch (Exception e) {
               //silent
            }
         }
         String repString = params.get (REPRESENTATION_PARAM);
         Representation repParam = Representation.TEXT;
         if (!PortableStringUtils.isEmpty (repString)) {
            try {
               repParam = Representation.valueOf (repString);
            } catch (Exception e) {
               //silent
            }
         }
         return new ObservationListPlace (obsTypeParam, repParam);
      }

      /**
       * Given the place, build the URL
       */
      @Override
      public String getToken (ObservationListPlace place) {
         return TYPE_PARAM + ISharedConstants.PARAM_VALUE_SEP + place.getObservationType ().toString ()
                + ISharedConstants.PARAM_SEP + REPRESENTATION_PARAM + ISharedConstants.PARAM_VALUE_SEP + place.getRepresentation ().toString ();
      }
   }

   public Representation getRepresentation () {
      return representation;
   }

   public void setRepresentation (Representation representation) {
      this.representation = representation;
   }

   public ObservationType getObservationType () {
      return observationType;
   }

   public void setObservationType (ObservationType observationType) {
      this.observationType = observationType;
   }
}