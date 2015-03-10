package es.uned.grc.pfc.meteo.client.view.action.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.view.IStationMapView;
import es.uned.grc.pfc.meteo.client.view.action.IStationMapActionsView;

/**
 * Implementation of the StationMapActionsViewImpl with UiBinder.
 */
public class StationMapActionsViewImpl extends Composite implements IStationMapActionsView {
   interface StationMapActionsViewImplUiBinder extends UiBinder <HTMLPanel, StationMapActionsViewImpl> {
   }
   private static StationMapActionsViewImplUiBinder stationMapActionsViewImplUiBinder = GWT.create (StationMapActionsViewImplUiBinder.class);

   @Inject
   EventBus eventBus = null;
   @Inject
   PlaceController placeController = null;
   @Inject
   IStationMapView listView = null;

   public StationMapActionsViewImpl () {
      initWidget (stationMapActionsViewImplUiBinder.createAndBindUi (this));
   }

   @Override
   public Widget asWidget () {
      return this;
   }
}
