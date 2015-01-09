package es.uned.grc.pfc.meteo.server.model;

public class RequestParamFilter {
   
   private String param = null;
   private String value = null;
   private String [] values = null;
   private Boolean mustNull = null;
   private String representation = null;

   public String getParam () {
      return param;
   }
   public void setParam (String param) {
      this.param = param;
   }
   public String getValue () {
      return value;
   }
   public void setValue (String value) {
      this.value = value;
   }
   public Boolean getMustNull () {
      return mustNull;
   }
   public void setMustNull (Boolean mustNull) {
      this.mustNull = mustNull;
   }
   public String getRepresentation () {
      return representation;
   }
   public void setRepresentation (String representation) {
      this.representation = representation;
   }
   public String [] getValues () {
      return values;
   }
   public void setValues (String [] values) {
      this.values = values;
   }
      
} //end of RequestParamFilter
