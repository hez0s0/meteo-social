package es.uned.grc.pfc.meteo.client.place;

import java.util.Map;

import com.google.gwt.place.shared.PlaceTokenizer;

import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

/**
 * Place to be used for URL representation of a station map Place
 */
public class StationMapPlace extends AbstractPlace {

   private static final Object STATION_ID_PARAM = "stationId";
   
   private Integer stationId = null;

   public StationMapPlace () {
      this.stationId = null;
   }
   
   public StationMapPlace (Integer stationId) {
      this.stationId = stationId;
   }
   
   /**
    * URL to Place and Place to URL tokenizer
    */
   public static class Tokenizer implements PlaceTokenizer <StationMapPlace> {

      /**
       * Given a URL, build a Place
       */
      @Override
      public StationMapPlace getPlace (String token) {
         Map <String, String> params = simpleParse (token);
         String idString = params.get (STATION_ID_PARAM);
         if (!PortableStringUtils.isEmpty (idString)) {
            return new StationMapPlace (Integer.valueOf (idString));
         } else {
            return new StationMapPlace ();
         }
      }

      /**
       * Given the place, build the URL
       */
      @Override
      public String getToken (StationMapPlace place) {
         if (place.getStationId () != null) {
            return STATION_ID_PARAM + ISharedConstants.PARAM_VALUE_SEP + place.getStationId ();
         } else {
            return "";
         }
      }
   }

   public Integer getStationId () {
      return stationId;
   }

   public void setStationId (Integer stationId) {
      this.stationId = stationId;
   }
}