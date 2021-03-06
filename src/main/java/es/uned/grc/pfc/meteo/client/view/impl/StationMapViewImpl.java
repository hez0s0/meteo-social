package es.uned.grc.pfc.meteo.client.view.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.ajaxloader.client.AjaxLoader;
import com.google.gwt.ajaxloader.client.AjaxLoader.AjaxLoaderOptions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;

import es.uned.grc.pfc.meteo.client.event.MapLoadedEvent;
import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.view.IStationMapView;
import es.uned.grc.pfc.meteo.client.view.base.AbstractPage;
import es.uned.grc.pfc.meteo.client.view.form.StationTooltipPanel;
import es.uned.grc.pfc.meteo.client.view.util.IMapsContants;

public class StationMapViewImpl extends AbstractPage implements IStationMapView {
   interface StationMapViewUiBinder extends UiBinder <HTMLPanel, StationMapViewImpl> {
   }
   private static StationMapViewUiBinder uiBinder = GWT.create (StationMapViewUiBinder.class);

   @Inject
   EventBus eventBus = null;
   @Inject
   PlaceController placeController = null;

   protected GoogleMap map = null;
   protected Map <IStationProxy, InfoWindow> infoWindows = null;

   private IStationProxy centerStation = null;
   
   public StationMapViewImpl () {
      initUI ();
   }

   /**
    * Creates, binds and configures the UI
    */
   private void initUI () {
      // Bind the UI with myself
      initWidget (uiBinder.createAndBindUi (this));
   }

   protected void renderMap (IStationProxy station) {
      infoWindows = new HashMap <IStationProxy, InfoWindow> ();
      LatLng stationPosition = LatLng.create (station.getLatitude (), station.getLongitude ());
      MapOptions mapOptions = MapOptions.create ();
      mapOptions.setZoom (IClientConstants.DEFAULT_ZOOM_LEVEL);
      mapOptions.setCenter (stationPosition);
      mapOptions.setMapTypeId (MapTypeId.ROADMAP);
      map = GoogleMap.create (Document.get ().getElementById ("map_canvas"), mapOptions);

      addMarker (station, true);
      
      eventBus.fireEvent (new MapLoadedEvent (station));
   }
   
   private void addMarker (final IStationProxy station, boolean own) {
      LatLng stationPosition = LatLng.create (station.getLatitude (), station.getLongitude ());
      MarkerOptions stationMarkerOptions = MarkerOptions.create ();
      if (!own) {
         stationMarkerOptions.setIcon (IMapsContants.OWN_MARKER);
      }
      stationMarkerOptions.setPosition (stationPosition);
      stationMarkerOptions.setMap (map);
      stationMarkerOptions.setTitle (station.getName ());
      infoWindows.put (station, getStationInfoWindow (station));
      final Marker marker = Marker.create (stationMarkerOptions);
      marker.addClickListener (new ClickHandler () {
         public void handle (MouseEvent event) {
            infoWindows.get (station).open (map, marker);
         }
      });
   }

   @Override
   public void setCenterStation (final IStationProxy station) {
      this.centerStation  = station;
      AjaxLoaderOptions options = AjaxLoaderOptions.newInstance ();
      Runnable callback = new Runnable () {
         public void run () {
            renderMap (station);
         }
      };
      AjaxLoader.loadApi ("maps", "3", callback, options);
   }

   @Override
   public void renderStations (List <IStationProxy> stations) {
      for (IStationProxy station : stations) {
         if (centerStation == null || !centerStation.getId ().equals (station.getId ())) {
            addMarker (station, false);
         }
      }
   }
   
   private InfoWindow getStationInfoWindow (final IStationProxy station) {
      InfoWindowOptions infowindowOpts = null;
      StationTooltipPanel stationTooltipPanel = null;
      
      stationTooltipPanel = GWT.create (StationTooltipPanel.class);
      stationTooltipPanel.setInput (station, placeController);
      infowindowOpts = InfoWindowOptions.create ();
      infowindowOpts.setContent (stationTooltipPanel.getHtml ());
      return InfoWindow.create(infowindowOpts);
   }
}
