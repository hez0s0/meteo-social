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
   private static final String STATION_ID_PARAM = "stationId";
   
   private Integer stationId = null;
   private ObservationType observationType = null;
   private Representation representation = null;
   private boolean emptyParams = false;

   public ObservationListPlace () {
      this (ObservationType.NORMAL, Representation.TEXT, null, true);
   }
   
   public ObservationListPlace (Representation representation) {
      this (ObservationType.NORMAL, representation);
   }
   
   public ObservationListPlace (ObservationType observationType, Representation representation) {
      this (observationType, representation, null, false);
   }
   
   public ObservationListPlace (ObservationType observationType, Representation representation, Integer stationId) {
      this (observationType, representation, stationId, false);
   }
   
   public ObservationListPlace (ObservationType observationType, Representation representation, Integer stationId, boolean emptyParams) {
      this.observationType = observationType;
      this.representation = representation;
      this.stationId = stationId;
      this.emptyParams = emptyParams;
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
         String stationIdString = params.get (STATION_ID_PARAM);
         Integer stationIdParam = null;
         if (!PortableStringUtils.isEmpty (stationIdString)) {
            try {
               stationIdParam = Integer.valueOf (stationIdString);
            } catch (Exception e) {
               //silent
            }
         }
         return new ObservationListPlace (obsTypeParam, repParam, stationIdParam);
      }

      /**
       * Given the place, build the URL
       */
      @Override
      public String getToken (ObservationListPlace place) {
         StringBuilder builder = new StringBuilder ();
         
         builder.append (TYPE_PARAM);
         builder.append (ISharedConstants.PARAM_VALUE_SEP);
         builder.append (place.getObservationType ().toString ());

         builder.append (ISharedConstants.PARAM_SEP);
         builder.append (REPRESENTATION_PARAM);
         builder.append (ISharedConstants.PARAM_VALUE_SEP);
         builder.append (place.getRepresentation ().toString ());

         if (place.getStationId () != null) {
            builder.append (ISharedConstants.PARAM_SEP);
            builder.append (STATION_ID_PARAM);
            builder.append (ISharedConstants.PARAM_VALUE_SEP);
            builder.append (place.getStationId ().toString ());
         }
         
         return builder.toString ();
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

   public Integer getStationId () {
      return stationId;
   }

   public void setStationId (Integer stationId) {
      this.stationId = stationId;
   }

   public boolean isEmptyParams () {
      return emptyParams;
   }

   public void setEmptyParams (boolean emptyParams) {
      this.emptyParams = emptyParams;
   }
}