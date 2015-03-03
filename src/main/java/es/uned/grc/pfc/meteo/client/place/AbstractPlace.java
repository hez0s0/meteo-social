package es.uned.grc.pfc.meteo.client.place;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.place.shared.Place;

import es.uned.grc.pfc.meteo.shared.ISharedConstants;

public abstract class AbstractPlace extends Place {

   private Integer entityID = null;

   public void setEntityID (Integer entityID) {
      this.entityID = entityID;
   }

   public Integer getEntityID () {
      return entityID;
   }

   protected static final Map <String, String> simpleParse (String token) {
      Map <String, String> map = new HashMap <String, String> ();
      if (token != null) {
         String [] params = token.split (ISharedConstants.PARAM_SEP);
         for (String param : params) {
            String [] keyValues = param.split (ISharedConstants.PARAM_VALUE_SEP);
            if (keyValues.length > 1) {
               map.put (keyValues [0], keyValues [1]);
            }
         }
      }
      return map;
   }
}
