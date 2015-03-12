package es.uned.grc.pfc.meteo.server.model;

import java.util.Date;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.uned.grc.pfc.meteo.server.model.base.AbstractVersionable;

@Entity
@Table (name = "metStation",
        indexes = {@Index (name = "ix1Station", columnList = "name")})
public class Station extends AbstractVersionable <Integer> {
   
   private Integer id = null;
   private String name = null;
   private StationModel stationModel = null;
   private Set <Parameter> parameters = null;
   private Set <Variable> variables = null;
   private Double latitude = null;
   private Double longitude = null;
   private Integer height = null;
   private Date lastCollectedPeriod = null;
   private Boolean own = false;

   @Id
   @GeneratedValue (strategy = GenerationType.AUTO, generator = "metStationID_gen")
   @SequenceGenerator (name = "metStationID_gen", sequenceName = "metStationID_seq")
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

   @OneToMany (mappedBy = "station", cascade = {CascadeType.ALL}, orphanRemoval = true)
   public Set <Parameter> getParameters () {
      return parameters;
   }
   public void setParameters (Set <Parameter> parameters) {
      this.parameters = parameters;
   }

   @OneToMany (mappedBy = "station", cascade = {CascadeType.ALL}, orphanRemoval = true)
   public Set <Variable> getVariables () {
      return variables;
   }
   public void setVariables (Set <Variable> variables) {
      this.variables = variables;
   }

   @ManyToOne (cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER ) 
   @JoinColumn (name = "stationId",
                foreignKey = @ForeignKey (name = "fk1Station"))
   public StationModel getStationModel () {
      return stationModel;
   }
   public void setStationModel (StationModel stationModel) {
      this.stationModel = stationModel;
   }
   
   @Column
   public Double getLatitude () {
      return latitude;
   }
   public void setLatitude (Double latitude) {
      this.latitude = latitude;
   }
   
   @Column
   public Double getLongitude () {
      return longitude;
   }
   public void setLongitude (Double longitude) {
      this.longitude = longitude;
   }
   
   @Column
   public Date getLastCollectedPeriod () {
      return lastCollectedPeriod;
   }
   public void setLastCollectedPeriod (Date lastCollectedPeriod) {
      this.lastCollectedPeriod = lastCollectedPeriod;
   }
   
   @Column
   public Integer getHeight () {
      return height;
   }
   public void setHeight (Integer height) {
      this.height = height;
   }

   @Column (nullable = false)
   public Boolean getOwn () {
      return own;
   }
   public void setOwn (Boolean own) {
      this.own = own;
   }
}
