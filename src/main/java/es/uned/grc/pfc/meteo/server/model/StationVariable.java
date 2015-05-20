package es.uned.grc.pfc.meteo.server.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.uned.grc.pfc.meteo.server.model.base.AbstractVersionable;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

@Entity
@Table (name = "metStationVariable")
public class StationVariable extends AbstractVersionable <Integer> {
   
   private Integer id = null;
   private Double defaultMinimum = null;
   private Double defaultMaximum = null;
   private Double physicalMinimum = null;
   private Double physicalMaximum = null;
   private Double minimum = null;
   private Double maximum = null;
   private Station station = null;
   private Variable variable = null;
   private int position = 0;
   private int displayGroup = 0;
   private ISharedConstants.GraphType graphType = null;

   @Id
   @GeneratedValue (strategy = GenerationType.AUTO, generator = "metStationVariableID_gen")
   @SequenceGenerator (name = "metStationVariableID_gen", sequenceName = "metStationVariableID_seq")
   @Column (unique = true, nullable = false)
   public Integer getId () {
      return id;
   }
   public void setId (Integer id) {
      this.id = id;
   }
   
   @Column
   public Double getDefaultMinimum () {
      return defaultMinimum;
   }
   public void setDefaultMinimum (Double defaultMinimum) {
      this.defaultMinimum = defaultMinimum;
   }
   
   @Column
   public Double getDefaultMaximum () {
      return defaultMaximum;
   }
   public void setDefaultMaximum (Double defaultMaximum) {
      this.defaultMaximum = defaultMaximum;
   }
   
   @Column
   public Double getPhysicalMinimum () {
      return physicalMinimum;
   }
   public void setPhysicalMinimum (Double physicalMinimum) {
      this.physicalMinimum = physicalMinimum;
   }
   
   @Column
   public Double getPhysicalMaximum () {
      return physicalMaximum;
   }
   public void setPhysicalMaximum (Double physicalMaximum) {
      this.physicalMaximum = physicalMaximum;
   }
   
   @Column
   public Double getMinimum () {
      return minimum;
   }
   public void setMinimum (Double minimum) {
      this.minimum = minimum;
   }
   
   @Column
   public Double getMaximum () {
      return maximum;
   }
   public void setMaximum (Double maximum) {
      this.maximum = maximum;
   }
   
   @Column (nullable = false)
   public int getPosition () {
      return position;
   }
   public void setPosition (int position) {
      this.position = position;
   }

   @Column (nullable = false)
   public int getDisplayGroup () {
      return displayGroup;
   }
   public void setDisplayGroup (int displayGroup) {
      this.displayGroup = displayGroup;
   }

   @Enumerated (EnumType.STRING)
   @Column (nullable = false)
   public ISharedConstants.GraphType getGraphType () {
      return graphType;
   }
   public void setGraphType (ISharedConstants.GraphType graphType) {
      this.graphType = graphType;
   }

   @ManyToOne (cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
   @JoinColumn (name = "stationId",            
                foreignKey = @ForeignKey (name = "fk1StationVariable"))
   public Station getStation () {
      return station;
   }
   public void setStation (Station station) {
      this.station = station;
   }
   
   @ManyToOne (cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER ) 
   @JoinColumn (name = "variableId",
                foreignKey = @ForeignKey (name = "fk2StationVariable"))
   public Variable getVariable () {
      return variable;
   }
   public void setVariable (Variable variable) {
      this.variable = variable;
   }
}
