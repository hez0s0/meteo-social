package es.uned.grc.pfc.meteo.server.model;

import java.util.Date;
import java.util.List;

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
   private List <Parameter> parameters = null;
   private List <Variable> variables = null;
   private Long latitude = null;
   private Long longitude = null;
   private Integer height = null;
   private String zip = null;
   private String city = null;
   private String country = null;
   private Date lastCollectedPeriod = null;

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
   public List <Parameter> getParameters () {
      return parameters;
   }
   public void setParameters (List <Parameter> parameters) {
      this.parameters = parameters;
   }

   @OneToMany (mappedBy = "station", cascade = {CascadeType.ALL}, orphanRemoval = true)
   public List <Variable> getVariables () {
      return variables;
   }
   public void setVariables (List <Variable> variables) {
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
   public Long getLatitude () {
      return latitude;
   }
   public void setLatitude (Long latitude) {
      this.latitude = latitude;
   }
   
   @Column
   public Long getLongitude () {
      return longitude;
   }
   public void setLongitude (Long longitude) {
      this.longitude = longitude;
   }
   
   @Column
   public String getZip () {
      return zip;
   }
   public void setZip (String zip) {
      this.zip = zip;
   }

   @Column
   public String getCity () {
      return city;
   }
   public void setCity (String city) {
      this.city = city;
   }

   @Column
   public String getCountry () {
      return country;
   }
   public void setCountry (String country) {
      this.country = country;
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
}
