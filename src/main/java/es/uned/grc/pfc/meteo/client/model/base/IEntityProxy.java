package es.uned.grc.pfc.meteo.client.model.base;

import com.google.web.bindery.requestfactory.shared.EntityProxy;

public interface IEntityProxy extends EntityProxy {
   Integer getId ();
   void setId (Integer id);
   Integer getVersion ();
   void setVersion (Integer version);
   
   Boolean getValidate ();
   void setValidate (Boolean validate);
}
