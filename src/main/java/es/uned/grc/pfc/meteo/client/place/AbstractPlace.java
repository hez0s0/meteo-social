package es.uned.grc.pfc.meteo.client.place;

import com.google.gwt.place.shared.Place;

public abstract class AbstractPlace extends Place {

   private Integer entityID = null;

   public void setEntityID (Integer entityID) {
      this.entityID = entityID;
   }

   public Integer getEntityID () {
      return entityID;
   }
   
} //end of AbstractPlace
