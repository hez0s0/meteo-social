package es.uned.grc.pfc.meteo.server.dto;

import java.util.List;

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.model.Variable;

public class VariableObservationsDTO {
   private Station station = null;
   private Variable Variable = null;
   private List <Observation> observations = null;
   
   public Station getStation () {
      return station;
   }
   public void setStation (Station station) {
      this.station = station;
   }
   public Variable getVariable () {
      return Variable;
   }
   public void setVariable (Variable variable) {
      Variable = variable;
   }
   public List <Observation> getObservations () {
      return observations;
   }
   public void setObservations (List <Observation> observations) {
      this.observations = observations;
   }
   
   
}
