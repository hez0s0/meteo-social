package es.uned.grc.pfc.meteo.client.view;

import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.UIObject;

import es.uned.grc.pfc.meteo.client.view.base.IView;

public interface IMenuView extends IView {

   /** the root menubar element */
   UIObject getRootMenuBar ();

   /** handler for the refresh menu item */
   MenuItem getRefreshMenuItem ();

   /** handler for the configuration menu item */
   MenuItem getConfigurationMenuItem ();

   /** handler for the drug code domains menu item */
   MenuItem getDrugCodeDomainsMenuItem ();
   /** handler for the workSpaces menu item */
   MenuItem getObservationsMenuItem ();
   /** handler for the catalogs menu item */
   MenuItem getCatalogsMenuItem ();
   /** handler for the workgroups menu item */
   MenuItem getWorkGroupsMenuItem ();
   /** handler for the tags menu item */
   MenuItem getTagsMenuItem ();
   /** handler for the reports menu item */
   MenuItem getReportsMenuItem ();
   /** handler for the drug item template menu item */
   MenuItem getDrugItemTemplatesMenuItem ();
   /** handler for the indication templates menu item */
   MenuItem getIndicationTemplatesMenuItem ();

   /** the bar that separates menu items */ 
   UIObject getSubBarSeparator ();
}
