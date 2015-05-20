package es.uned.grc.pfc.meteo.server.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.uned.grc.pfc.meteo.server.model.base.AbstractVersionable;

@Entity
@Table (name = "metParameter",
        indexes = {@Index (name = "ix1Parameter", columnList = "name")})
public class Parameter extends AbstractVersionable <Integer> {

   public enum ParameterType {COMMUNICATION, SOCIAL};
   
   private Integer id = null;
   private Station station = null;
   private String name = null;
   private String description = null;
   private String value = null;
   private String defaultValue = null;
   private int internal = 0;

   @Id
   @GeneratedValue (strategy = GenerationType.AUTO, generator = "metParameterID_gen")
   @SequenceGenerator (name = "metParameterID_gen", sequenceName = "metParameterID_seq")
   @Column (unique = true, nullable = false)
   public Integer getId () {
      return id;
   }
   public void setId (Integer id) {
      this.id = id;
   }
   
   @Column (nullable = false, length = 64)
   public String getName () {
      return name;
   }
   public void setName (String name) {
      this.name = name;
   }
   
   @Column (length = 4000)
   public String getDescription () {
      return description;
   }
   public void setDescription (String description) {
      this.description = description;
   }

   @Column (length = 256)
   public String getValue () {
      return value;
   }
   public void setValue (String value) {
      this.value = value;
   }
   @Column (length = 256)
   public String getDefaultValue () {
      return defaultValue;
   }
   public void setDefaultValue (String defaultValue) {
      this.defaultValue = defaultValue;
   }
   @Column (nullable = false, columnDefinition = "int default 0")
   public int getInternal () {
      return internal;
   }
   public void setInternal (int internal) {
      this.internal = internal;
   }

   @ManyToOne (cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER ) 
   @JoinColumn (name = "stationId",
                foreignKey = @ForeignKey (name = "fk1Parameter"))
   public Station getStation () {
      return station;
   }
   public void setStation (Station station) {
      this.station = station;
   }
}
