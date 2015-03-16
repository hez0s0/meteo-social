package es.uned.grc.pfc.meteo.server.dto;

import es.uned.grc.pfc.meteo.server.model.Variable;

public class DerivedVariableDTO {
   private Variable variable = null;
   private String minimum = null;
   private String maximum = null;
   private String average = null;
   
   public Variable getVariable () {
      return variable;
   }
   public void setVariable (Variable variable) {
      this.variable = variable;
   }
   public String getMinimum () {
      return minimum;
   }
   public void setMinimum (String minimum) {
      this.minimum = minimum;
   }
   public String getMaximum () {
      return maximum;
   }
   public void setMaximum (String maximum) {
      this.maximum = maximum;
   }
   public String getAverage () {
      return average;
   }
   public void setAverage (String average) {
      this.average = average;
   }
}
