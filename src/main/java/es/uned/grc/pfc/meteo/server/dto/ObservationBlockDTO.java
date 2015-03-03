package es.uned.grc.pfc.meteo.server.dto;

import java.util.Date;
import java.util.List;

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.Station;

public class ObservationBlockDTO {
   private Station station = null;
   private Date observed = null;
   private List <Observation> observations = null;
   
   public Station getStation () {
      return station;
   }
   public void setStation (Station station) {
      this.station = station;
   }
   public Date getObserved () {
      return observed;
   }
   public void setObserved (Date observed) {
      this.observed = observed;
   }
   public List <Observation> getObservations () {
      return observations;
   }
   public void setObservations (List <Observation> observations) {
      this.observations = observations;
   }
}
