package es.uned.grc.pfc.meteo.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import es.uned.grc.pfc.meteo.server.model.base.AbstractVersionable;

@Entity
@Table (name = "metStation",
        indexes = {@Index (name = "ix1Station", columnList = "name"),
                   @Index (name = "ix2Station", columnList = "latitude"),
                   @Index (name = "ix3Station", columnList = "longitude"),
                   @Index (name = "ix4Station", columnList = "street"),
                   @Index (name = "ix5Station", columnList = "zip"),
                   @Index (name = "ix6Station", columnList = "city"),
                   @Index (name = "ix7Station", columnList = "country")})
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
   private String street = null;
   private String zip = null;
   private String city = null;
   private String country = null;
   private User user = null;
   
   //transient property!
   private List <Observation> transientLastObservations = new ArrayList <Observation> ();

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

   @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinTable (name = "metStationVariable", 
               joinColumns = {@JoinColumn (name = "stationId", nullable = false, updatable = false)}, 
               inverseJoinColumns = {@JoinColumn(name = "variableId", nullable = false, updatable = false)})
   public Set <Variable> getVariables () {
      return variables;
   }
   public void setVariables (Set <Variable> variables) {
      this.variables = variables;
   }

   @ManyToOne (cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER ) 
   @JoinColumn (name = "stationModelId",
                foreignKey = @ForeignKey (name = "fk1Station"))
   public StationModel getStationModel () {
      return stationModel;
   }
   public void setStationModel (StationModel stationModel) {
      this.stationModel = stationModel;
   }
   
   @Column (nullable = false)
   public Double getLatitude () {
      return latitude;
   }
   public void setLatitude (Double latitude) {
      this.latitude = latitude;
   }
   
   @Column (nullable = false)
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
   
   @Column (nullable = false)
   public Integer getHeight () {
      return height;
   }
   public void setHeight (Integer height) {
      this.height = height;
   }

   @Column (length = 1024)
   public String getStreet () {
      return street;
   }
   public void setStreet (String street) {
      this.street = street;
   }

   @Column (length = 8)
   public String getZip () {
      return zip;
   }
   public void setZip (String zip) {
      this.zip = zip;
   }

   @Column (length = 1024)
   public String getCity () {
      return city;
   }
   public void setCity (String city) {
      this.city = city;
   }

   @Column (length = 1024)
   public String getCountry () {
      return country;
   }
   public void setCountry (String country) {
      this.country = country;
   }
   
   @Transient
   public List <Observation> getTransientLastObservations () {
      return transientLastObservations;
   }
   public void setTransientLastObservations (List <Observation> transientLastObservations) {
      this.transientLastObservations = transientLastObservations;
   }
   
   @OneToOne (cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER ) 
   @JoinColumn (name = "userId",
                foreignKey = @ForeignKey (name = "fk2Station"))
   public User getUser () {
      return user;
   }
   public void setUser (User user) {
      this.user = user;
   }
}
