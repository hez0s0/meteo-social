package es.uned.grc.pfc.meteo.client.model;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.server.model.StationVariable;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;
import es.uned.grc.pfc.meteo.shared.locators.PersistenceEntityLocator;

@ProxyFor (value = StationVariable.class, locator = PersistenceEntityLocator.class)
public interface IStationVariableProxy extends IEntityProxy {
   Integer getId ();
   void setId (Integer id);
   
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
   
   IStationProxy getStation ();
   void setStation (IStationProxy station);
   
   IVariableProxy getVariable ();
   void setVariable (IVariableProxy variable);

   int getPosition ();
   void setPosition (int position);

   int getDisplayGroup ();
   void setDisplayGroup (int displayGroup);
   
   ISharedConstants.GraphType getGraphType ();
   void setGraphType (ISharedConstants.GraphType graphType);
}
