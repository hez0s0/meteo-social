package es.uned.grc.pfc.meteo.client.view;

import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.UIObject;

import es.uned.grc.pfc.meteo.client.view.base.IView;

public interface IMenuView extends IView {

   /** the root menubar element */
   UIObject getRootMenuBar ();

   /** handler for the refresh menu item */
   MenuItem getRefreshMenuItem ();
}
