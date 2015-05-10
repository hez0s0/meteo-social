package es.uned.grc.pfc.meteo.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.uned.grc.pfc.meteo.server.model.base.AbstractVersionable;

@Entity
@Table (name = "metUser",
        indexes = {@Index (name = "ix1User", columnList = "username")})
public class User extends AbstractVersionable <Integer> {

   private Integer id = null;
   private String username = null;
   private String password = null;
   private String firstName = null;
   private String familyName = null;
   private String email = null;
   private boolean enabled = true;
   
   @Id
   @GeneratedValue (strategy = GenerationType.AUTO, generator = "metUserID_gen")
   @SequenceGenerator (name = "metUserID_gen", sequenceName = "metUserID_seq")
   @Column (unique = true, nullable = false)
   public Integer getId () {
      return id;
   }
   public void setId (Integer id) {
      this.id = id;
   }

   @Column (nullable = false, length = 64)
   public String getUsername () {
      return username;
   }
   public void setUsername (String username) {
      this.username = username;
   }

   @Column (nullable = false, length = 128)
   public String getPassword () {
      return password;
   }
   public void setPassword (String password) {
      this.password = password;
   }

   @Column (nullable = false, length = 1024)
   public String getFirstName () {
      return firstName;
   }
   public void setFirstName (String firstName) {
      this.firstName = firstName;
   }

   @Column (nullable = false, length = 1024)
   public String getFamilyName () {
      return familyName;
   }
   public void setFamilyName (String familyName) {
      this.familyName = familyName;
   }

   @Column (nullable = false, length = 1024)
   public String getEmail () {
      return email;
   }
   public void setEmail (String email) {
      this.email = email;
   }
   
   @Column (nullable = false)
   public boolean isEnabled () {
      return enabled;
   }
   public void setEnabled (boolean enabled) {
      this.enabled = enabled;
   }
}
