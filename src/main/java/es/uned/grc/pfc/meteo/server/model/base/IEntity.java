package es.uned.grc.pfc.meteo.server.model.base;

import java.io.Serializable;

public interface IEntity <K extends Number> extends Serializable, IValidatable {
   
   K getId ();
   void setId (K ID);
   
   Integer getVersion ();
   void setVersion (Integer version);
}
