package es.uned.grc.pfc.meteo.server.collector.station;

public class ParameterDescriptor {
   
   private String name = null;
   private String description = null;
   private String defaultValue = null;
   
   public String getName () {
      return name;
   }
   public void setName (String name) {
      this.name = name;
   }
   public String getDescription () {
      return description;
   }
   public void setDescription (String description) {
      this.description = description;
   }
   public String getDefaultValue () {
      return defaultValue;
   }
   public void setDefaultValue (String defaultValue) {
      this.defaultValue = defaultValue;
   }
   
   
}
