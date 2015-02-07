package es.uned.grc.pfc.meteo.client.model;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.server.model.Variable;
import es.uned.grc.pfc.meteo.shared.locators.PersistenceEntityLocator;

@ProxyFor (value = Variable.class, locator = PersistenceEntityLocator.class)
public interface IVariableProxy extends IEntityProxy {
   Integer getId ();
   void setId (Integer id);
   
   String getName ();
   void setName (String name);
   
   String getDescription ();
   void setDescription (String description);
   
   String getAcronym ();
   void setAcronym (String acronym);
   
   Double getDefaultMinimum ();
   void setDefaultMinimum (Double defaultMinimum);
   
   Double getDefaultMaximum ();
   void setDefaultMaximum (Double defaultMaximum);
   
   Double getPhysicalMinimum ();
   void setPhysicalMinimum (Double physicalMinimum);
   
   Double getPhysicalMaximum ();
   void setPhysicalMaximum (Double physicalMaximum);
   
   Double getMinimum ();
   void setMinimum (Double minimum);
   
   Double getMaximum ();
   void setMaximum (Double maximum);
   
   String getUnit ();
   void setUnit (String unit);
   
   boolean getInternal ();
   void setInternal (boolean internal);
   
   IStationProxy getStation ();
   void setStation (IStationProxy station);
   
   IVariableProxy getRelated ();
   void setRelated (IVariableProxy related);
}
