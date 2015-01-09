package es.uned.grc.pfc.meteo.server.collector.station;

import java.util.Date;

public class RawObservation {

   private String variableName = null;
   private String value = null;
   private Date observed = null;
   
   public String getVariableName () {
      return variableName;
   }
   public void setVariableName (String variableName) {
      this.variableName = variableName;
   }
   public String getValue () {
      return value;
   }
   public void setValue (String value) {
      this.value = value;
   }
   public Date getObserved () {
      return observed;
   }
   public void setObserved (Date observed) {
      this.observed = observed;
   }
   
   
}
