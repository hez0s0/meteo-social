package es.uned.grc.pfc.meteo.server.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.BatchSize;

import es.uned.grc.pfc.meteo.server.model.base.AbstractVersionable;

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
   private String unit = null;
   private boolean internal = false;
   private Set <StationVariable> stationVariables = null;

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
   
   @OneToMany (mappedBy = "variable", cascade = {CascadeType.ALL}, orphanRemoval = true)
   @BatchSize (size = 20)
   public Set <StationVariable> getStationVariables () {
      return stationVariables;
   }
   public void setStationVariables (Set <StationVariable> stationVariables) {
      this.stationVariables = stationVariables;
   }
}
