package es.uned.grc.pfc.meteo.server.model;

import java.util.Date;

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
@Table (name = "metObservation",
        indexes = {@Index (name = "ix1Observation", columnList = "observed")})
public class Observation extends AbstractVersionable <Integer> {

   private Integer id = null;
   private Variable variable = null;
   private Station station = null;
   private String value = null;
   private Date observed = null;
   private Date received = null;
   private Date sent = null;

   @Id
   @GeneratedValue (strategy = GenerationType.AUTO, generator = "metObservationID_gen")
   @SequenceGenerator (name = "metObservationID_gen", sequenceName = "metObservationID_seq")
   @Column (unique = true, nullable = false)
   public Integer getId () {
      return id;
   }
   public void setId (Integer id) {
      this.id = id;
   }

   @ManyToOne (cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER ) 
   @JoinColumn (name = "variableId",
                foreignKey = @ForeignKey (name = "fk1Observation"))
   public Variable getVariable () {
      return variable;
   }
   public void setVariable (Variable variable) {
      this.variable = variable;
   }

   @ManyToOne (cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER ) 
   @JoinColumn (name = "stationId",
                foreignKey = @ForeignKey (name = "fk2Observation"))
   public Station getStation () {
      return station;
   }
   public void setStation (Station station) {
      this.station = station;
   }
   
   @Column (nullable = false, length = 64)
   public String getValue () {
      return value;
   }
   public void setValue (String value) {
      this.value = value;
   }
   
   @Column (nullable = false)
   public Date getObserved () {
      return observed;
   }
   public void setObserved (Date observed) {
      this.observed = observed;
   }
   
   @Column (nullable = false)
   public Date getReceived () {
      return received;
   }
   public void setReceived (Date received) {
      this.received = received;
   }
   
   @Column
   public Date getSent () {
      return sent;
   }
   public void setSent (Date sent) {
      this.sent = sent;
   }
}
