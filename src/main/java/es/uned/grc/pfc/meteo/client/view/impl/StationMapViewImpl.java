package es.uned.grc.pfc.meteo.client.view.impl;

import com.google.gwt.ajaxloader.client.AjaxLoader;
import com.google.gwt.ajaxloader.client.AjaxLoader.AjaxLoaderOptions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.view.IStationMapView;
import es.uned.grc.pfc.meteo.client.view.base.AbstractPage;

public class StationMapViewImpl extends AbstractPage implements IStationMapView {
   interface StationMapViewUiBinder extends UiBinder <HTMLPanel, StationMapViewImpl> {
   }

   private static StationMapViewUiBinder uiBinder = GWT.create (StationMapViewUiBinder.class);

   @Inject
   EventBus eventBus = null;
   @Inject
   PlaceController placeController = null;

   public StationMapViewImpl () {
      initUI ();
   }

   /**
    * Creates, binds and configures the UI
    */
   private void initUI () {
      // Bind the UI with myself
      initWidget (uiBinder.createAndBindUi (this));

      AjaxLoaderOptions options = AjaxLoaderOptions.newInstance ();
      options.setOtherParms ("sensor=false&language=ja");
      Runnable callback = new Runnable () {
         public void run () {
            renderMap ();
         }
      };
      AjaxLoader.loadApi ("maps", "3", callback, options);
   }

   protected void renderMap () {
      LatLng myLatLng = LatLng.create (40, -4);
      MapOptions myOptions = MapOptions.create ();
      myOptions.setZoom (8.0);
      myOptions.setCenter (myLatLng);
      myOptions.setMapTypeId (MapTypeId.ROADMAP);
      GoogleMap.create (Document.get ().getElementById ("map_canvas"), myOptions);
   }

   @Override
   public void setCenterStation (IStationProxy station) {
      Window.alert ("UNIMPLENTED setCenterStation");
   }
}
