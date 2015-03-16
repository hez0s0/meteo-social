package es.uned.grc.pfc.meteo.server.dto;

import java.util.Date;
import java.util.List;

import es.uned.grc.pfc.meteo.server.model.Station;

public class DerivedRangeDTO {
   private Station station = null;
   private Date ini = null;
   private Date end = null;
   private List <DerivedVariableDTO> derivedVariables = null;
   
   public Station getStation () {
      return station;
   }
   public void setStation (Station station) {
      this.station = station;
   }
   public Date getIni () {
      return ini;
   }
   public void setIni (Date ini) {
      this.ini = ini;
   }
   public Date getEnd () {
      return end;
   }
   public void setEnd (Date end) {
      this.end = end;
   }
   public List <DerivedVariableDTO> getDerivedVariables () {
      return derivedVariables;
   }
   public void setDerivedVariables (List <DerivedVariableDTO> derivedVariables) {
      this.derivedVariables = derivedVariables;
   }
}
