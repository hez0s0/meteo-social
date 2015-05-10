package es.uned.grc.pfc.meteo.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.view.IMenuView;

public class MenuViewImpl extends Composite implements IMenuView {
   interface MenuUiBinder extends UiBinder <HTMLPanel, MenuViewImpl> {
   }
   private static MenuUiBinder uiBinder = GWT.create (MenuUiBinder.class);

   @Inject
   protected EventBus eventBus = null;
   @Inject
   protected PlaceController placeController = null;

   @UiField
   protected MenuBar menuBar = null;
   @UiField
   protected MenuItem refreshMenuItem = null;
   @UiField
   protected MenuItem stationConfigurationMenuItem = null;
   @UiField
   protected MenuItem profileConfigurationMenuItem = null;

   public MenuViewImpl () {
      initWidget (uiBinder.createAndBindUi (this));
   }

   @Override
   public Widget asWidget () {
      return this;
   }

   @Override
   public MenuItem getRefreshMenuItem () {
      return refreshMenuItem;
   }

   @Override
   public MenuItem getStationConfigurationMenuItem () {
      return stationConfigurationMenuItem;
   }

   @Override
   public MenuItem getUserConfigurationMenuItem () {
      return profileConfigurationMenuItem;
   }
   
   @Override
   public UIObject getRootMenuBar () {
      return menuBar;
   }
}
