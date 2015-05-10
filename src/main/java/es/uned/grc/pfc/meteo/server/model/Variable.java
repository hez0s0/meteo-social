package es.uned.grc.pfc.meteo.server.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import es.uned.grc.pfc.meteo.server.model.base.AbstractVersionable;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

@Entity
@Table (name = "metVariable",
        uniqueConstraints = {@UniqueConstraint (name = "uk1Variable", columnNames = {"name"})},
        indexes = {@Index (name = "ix1Variable", columnList = "name"),
                   @Index (name = "ix2Variable", columnList = "acronym")})
public class Variable extends AbstractVersionable <Integer> {

   private Integer id = null;
   private String name = null;
   private String acronym = null;
   private String description = null;
   private Double defaultMinimum = null;
   private Double defaultMaximum = null;
   private Double physicalMinimum = null;
   private Double physicalMaximum = null;
   private Double minimum = null;
   private Double maximum = null;
   private String unit = null;
   private boolean internal = false;
   private int position = 0;
   private int displayGroup = 0;
   private ISharedConstants.GraphType graphType = null;
   private Set <Station> stations = null;

   @Id
   @GeneratedValue (strategy = GenerationType.AUTO, generator = "metVariableID_gen")
   @SequenceGenerator (name = "metVariableID_gen", sequenceName = "metVariableID_seq")
   @Column (unique = true, nullable = false)
   public Integer getId () {
      return id;
   }
   public void setId (Integer id) {
      this.id = id;
   }
   
   @Column (nullable = false, length = 1024)
   public String getName () {
      return name;
   }
   public void setName (String name) {
      this.name = name;
   }

   @Column (nullable = false, length = 64)
   public String getAcronym () {
      return acronym;
   }
   public void setAcronym (String acronym) {
      this.acronym = acronym;
   }
   
   @Column (length = 4000)
   public String getDescription () {
      return description;
   }
   public void setDescription (String description) {
      this.description = description;
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
   
   @Column (length = 128)
   public String getUnit () {
      return unit;
   }
   public void setUnit (String unit) {
      this.unit = unit;
   }
   
   @Column (nullable = false)
   public boolean getInternal () {
      return internal;
   }
   public void setInternal (boolean internal) {
      this.internal = internal;
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
   
   @ManyToMany (fetch = FetchType.LAZY, mappedBy = "variables")
   public Set <Station> getStations () {
      return stations;
   }
   public void setStations (Set <Station> stations) {
      this.stations = stations;
   }
}
