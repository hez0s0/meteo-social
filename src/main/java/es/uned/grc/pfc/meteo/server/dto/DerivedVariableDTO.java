package es.uned.grc.pfc.meteo.server.dto;

import java.util.Date;

import es.uned.grc.pfc.meteo.server.model.StationVariable;

public class DerivedVariableDTO {
   private StationVariable stationVariable = null;
   private String minimum = null;
   private String maximum = null;
   private String average = null;
   private Integer minimumDeriveBase = null;
   private Integer minimumDeriveIgnored = null;
   private Integer minimumDeriveExpected = null;
   private Integer maximumDeriveBase = null;
   private Integer maximumDeriveIgnored = null;
   private Integer maximumDeriveExpected = null;
   private Integer averageDeriveBase = null;
   private Integer averageDeriveIgnored = null;
   private Integer averageDeriveExpected = null;
   private Date displayDate = null;
   
   public StationVariable getStationVariable () {
      return stationVariable;
   }
   public void setStationVariable (StationVariable stationVariable) {
      this.stationVariable = stationVariable;
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
   public Integer getMinimumDeriveBase () {
      return minimumDeriveBase;
   }
   public void setMinimumDeriveBase (Integer minimumDeriveBase) {
      this.minimumDeriveBase = minimumDeriveBase;
   }
   public Integer getMinimumDeriveIgnored () {
      return minimumDeriveIgnored;
   }
   public void setMinimumDeriveIgnored (Integer minimumDeriveIgnored) {
      this.minimumDeriveIgnored = minimumDeriveIgnored;
   }
   public Integer getMinimumDeriveExpected () {
      return minimumDeriveExpected;
   }
   public void setMinimumDeriveExpected (Integer minimumDeriveExpected) {
      this.minimumDeriveExpected = minimumDeriveExpected;
   }
   public Integer getMaximumDeriveBase () {
      return maximumDeriveBase;
   }
   public void setMaximumDeriveBase (Integer maximumDeriveBase) {
      this.maximumDeriveBase = maximumDeriveBase;
   }
   public Integer getMaximumDeriveIgnored () {
      return maximumDeriveIgnored;
   }
   public void setMaximumDeriveIgnored (Integer maximumDeriveIgnored) {
      this.maximumDeriveIgnored = maximumDeriveIgnored;
   }
   public Integer getMaximumDeriveExpected () {
      return maximumDeriveExpected;
   }
   public void setMaximumDeriveExpected (Integer maximumDeriveExpected) {
      this.maximumDeriveExpected = maximumDeriveExpected;
   }
   public Integer getAverageDeriveBase () {
      return averageDeriveBase;
   }
   public void setAverageDeriveBase (Integer averageDeriveBase) {
      this.averageDeriveBase = averageDeriveBase;
   }
   public Integer getAverageDeriveIgnored () {
      return averageDeriveIgnored;
   }
   public void setAverageDeriveIgnored (Integer averageDeriveIgnored) {
      this.averageDeriveIgnored = averageDeriveIgnored;
   }
   public Integer getAverageDeriveExpected () {
      return averageDeriveExpected;
   }
   public void setAverageDeriveExpected (Integer averageDeriveExpected) {
      this.averageDeriveExpected = averageDeriveExpected;
   }
   public Date getDisplayDate () {
      return displayDate;
   }
   public void setDisplayDate (Date displayDate) {
      this.displayDate = displayDate;
   }
}
