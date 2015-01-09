package es.uned.grc.pfc.meteo.server.collector.station;

public class VariableDescriptor {

   private String name = null;
   private String acronym = null;
   private String description = null;
   private String unit = null;
   private Double defaultMinimum = null;
   private Double defaultMaximum = null;
   private Double physicaltMinimum = null;
   private Double physicalMaximum = null;
   
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
   public Double getDefaultMinimum () {
      return defaultMinimum;
   }
   public void setDefaultMinimum (Double defaultMinimum) {
      this.defaultMinimum = defaultMinimum;
   }
   public Double getDefaultMaximum () {
      return defaultMaximum;
   }
   public void setDefaultMaximum (Double defaultMaximum) {
      this.defaultMaximum = defaultMaximum;
   }
   public String getUnit () {
      return unit;
   }
   public void setUnit (String unit) {
      this.unit = unit;
   }
   public String getAcronym () {
      return acronym;
   }
   public void setAcronym (String acronym) {
      this.acronym = acronym;
   }
   public Double getPhysicalMinimum () {
      return physicaltMinimum;
   }
   public void setPhysicalMinimum (Double physicalMinimum) {
      this.physicaltMinimum = physicalMinimum;
   }
   public Double getPhysicalMaximum () {
      return physicalMaximum;
   }
   public void setPhysicalMaximum (Double physicalMaximum) {
      this.physicalMaximum = physicalMaximum;
   }
   
}
