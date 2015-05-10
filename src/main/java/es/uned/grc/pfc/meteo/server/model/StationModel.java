package es.uned.grc.pfc.meteo.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import es.uned.grc.pfc.meteo.server.model.base.AbstractVersionable;

@Entity
@Table (name = "metStationModel",
        uniqueConstraints = {@UniqueConstraint (name = "uk1StationModel", columnNames = {"name"})},
        indexes = {@Index (name = "ix1StationModel", columnList = "name")})
public class StationModel extends AbstractVersionable <Integer> {
   
   private Integer id = null;
   private String name = null;
   private String description = null;
   private String className = null;
   
   @Id
   @GeneratedValue (strategy = GenerationType.AUTO, generator = "metStationModelID_gen")
   @SequenceGenerator (name = "metStationModelID_gen", sequenceName = "metStationModelID_seq")
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
   
   @Column (nullable = false, length = 4000)
   public String getClassName () {
      return className;
   }
   public void setClassName (String className) {
      this.className = className;
   }
}
