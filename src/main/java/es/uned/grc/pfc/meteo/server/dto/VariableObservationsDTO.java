package es.uned.grc.pfc.meteo.server.dto;

import java.util.List;

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.StationVariable;

public class VariableObservationsDTO {
   private StationVariable stationVariable = null;
   private List <Observation> observations = null;
   
   public List <Observation> getObservations () {
      return observations;
   }
   public void setObservations (List <Observation> observations) {
      this.observations = observations;
   }
   public StationVariable getStationVariable () {
      return stationVariable;
   }
   public void setStationVariable (StationVariable stationVariable) {
      this.stationVariable = stationVariable;
   }
}
