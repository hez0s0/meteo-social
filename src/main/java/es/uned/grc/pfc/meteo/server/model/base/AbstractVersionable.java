package es.uned.grc.pfc.meteo.server.model.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

@SuppressWarnings ("serial")
@MappedSuperclass
public abstract class AbstractVersionable <K extends Number> implements IEntity <K> {
   
   private Integer version = null;
   private Boolean validate = false;

   @Version
   @Column (nullable = false, columnDefinition = "int default 1")
   public Integer getVersion () {
      return version;
   }
   public void setVersion (Integer version) {
      this.version = version;
   }
   
   /**
    * Determines if the bean should be validated by serviceLayerDecorator (server-side)
    * or not. Useful to prevent entities within the root to be validated on their own,
    * which makes no sense and creates problems when removing invalid items from inner collections.
    */
   @Transient
   public Boolean getValidate () {
      return validate;
   }
   
   public void setValidate (Boolean validate) {
      this.validate = validate;
   }
}
