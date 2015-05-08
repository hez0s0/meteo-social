package es.uned.grc.pfc.meteo.server.job.station;

import es.uned.grc.pfc.meteo.shared.ISharedConstants;

public class VariableDescriptor {

   private String name = null;
   private String acronym = null;
   private String description = null;
   private String unit = null;
   private Double defaultMinimum = null;
   private Double defaultMaximum = null;
   private Double physicalMinimum = null;
   private Double physicalMaximum = null;
   private Integer position = null;
   private Integer displayGroup = null;
   private ISharedConstants.GraphType graphType = null;
   
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
      return physicalMinimum;
   }
   public void setPhysicalMinimum (Double physicalMinimum) {
      this.physicalMinimum = physicalMinimum;
   }
   public Double getPhysicalMaximum () {
      return physicalMaximum;
   }
   public void setPhysicalMaximum (Double physicalMaximum) {
      this.physicalMaximum = physicalMaximum;
   }
   public Integer getPosition () {
      return position;
   }
   public void setPosition (Integer position) {
      this.position = position;
   }
   public Integer getDisplayGroup () {
      return displayGroup;
   }
   public void setDisplayGroup (Integer displayGroup) {
      this.displayGroup = displayGroup;
   }
   public ISharedConstants.GraphType getGraphType () {
      return graphType;
   }
   public void setGraphType (ISharedConstants.GraphType graphType) {
      this.graphType = graphType;
   }
   
}
