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
        indexes = {@Index (name = "ix1Observation", columnList = "observed"),
                   @Index (name = "ix2Observation", columnList = "received"),
                   @Index (name = "ix3Observation", columnList = "sent"),
                   @Index (name = "ix4Observation", columnList = "controlled"),
                   @Index (name = "ix5Observation", columnList = "derived"),
                   @Index (name = "ix6Observation", columnList = "rangeIni"),
                   @Index (name = "ix7Observation", columnList = "rangeEnd")})
public class Observation extends AbstractVersionable <Integer> {

   private Integer id = null;
   private Variable variable = null;
   private Station station = null;
   private String value = null;
   private Date observed = null;
   private Date received = null;
   private Date sent = null;
   private Date controlled = null;
   private Date derived = null;
   private Date rangeIni = null;
   private Date rangeEnd = null;
   private String warning = null;
   private Boolean quality = null;
   private Variable derivedVariable = null;
   private Integer deriveBase = null;
   private Integer deriveIgnored = null;
   private Integer deriveExpected = null;

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
                foreignKey = @ForeignKey (name = "fk1Observation"),
                nullable = false)
   public Variable getVariable () {
      return variable;
   }
   public void setVariable (Variable variable) {
      this.variable = variable;
   }

   @ManyToOne (cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER ) 
   @JoinColumn (name = "stationId",
                foreignKey = @ForeignKey (name = "fk2Observation"),
                nullable = false)
   public Station getStation () {
      return station;
   }
   public void setStation (Station station) {
      this.station = station;
   }
   
   @Column (length = 64)
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
   
   @Column
   public Date getControlled () {
      return controlled;
   }
   public void setControlled (Date controlled) {
      this.controlled = controlled;
   }
   
   @Column
   public String getWarning () {
      return warning;
   }
   public void setWarning (String warning) {
      this.warning = warning;
   }
   
   @Column
   public Boolean getQuality () {
      return quality;
   }
   public void setQuality (Boolean quality) {
      this.quality = quality;
   }
   
   @Column
   public Date getDerived () {
      return derived;
   }
   public void setDerived (Date derived) {
      this.derived = derived;
   }

   @ManyToOne (cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER ) 
   @JoinColumn (name = "derivedVariableId",
                foreignKey = @ForeignKey (name = "fk3Observation"))
   public Variable getDerivedVariable () {
      return derivedVariable;
   }
   public void setDerivedVariable (Variable derivedVariable) {
      this.derivedVariable = derivedVariable;
   }
   
   @Column
   public Date getRangeIni () {
      return rangeIni;
   }
   public void setRangeIni (Date rangeIni) {
      this.rangeIni = rangeIni;
   }
   
   @Column
   public Date getRangeEnd () {
      return rangeEnd;
   }
   public void setRangeEnd (Date rangeEnd) {
      this.rangeEnd = rangeEnd;
   }
   
   @Column
   public Integer getDeriveBase () {
      return deriveBase;
   }
   public void setDeriveBase (Integer deriveBase) {
      this.deriveBase = deriveBase;
   }
   
   @Column
   public Integer getDeriveIgnored () {
      return deriveIgnored;
   }
   public void setDeriveIgnored (Integer deriveIgnored) {
      this.deriveIgnored = deriveIgnored;
   }
   
   @Column
   public Integer getDeriveExpected () {
      return deriveExpected;
   }
   public void setDeriveExpected (Integer deriveExpected) {
      this.deriveExpected = deriveExpected;
   }
}
