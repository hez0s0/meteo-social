package es.uned.grc.pfc.meteo.client.view.action.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.view.IStationMapView;
import es.uned.grc.pfc.meteo.client.view.action.IStationMapActionsView;
import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;
import es.uned.grc.pfc.meteo.client.view.widget.ExtendedTextBox;
import es.uned.grc.pfc.meteo.client.view.widget.ImageLabel;
import es.uned.grc.pfc.meteo.client.view.widget.NumberTextBox;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.impl.CitySuggestInputListBox;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.impl.CountrySuggestInputListBox;

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
   
   @UiField
   protected HTMLPanel stationSearchPanel = null;
   @UiField
   protected HTMLPanel viewTablePanel = null;
   @UiField
   protected VerticalPanel stationFormPanel = null;
   
   @UiField
   protected ExtendedTextBox zipSuggestBox = null;
   @UiField
   protected CitySuggestInputListBox citySuggestBox = null;
   @UiField
   protected CountrySuggestInputListBox countrySuggestBox = null;
   @UiField
   protected NumberTextBox latTextBox = null;
   @UiField
   protected NumberTextBox lonTextBox = null;
   @UiField
   protected NumberTextBox radiusTextBox = null;
//   @UiField (provided = true)
//   protected CellList <IStationProxy> stationCellList = null;

   @UiField
   protected ImageLabel stationSearch = null;
   @UiField
   protected ImageLabel viewTable = null;
   
   public StationMapActionsViewImpl () {
//      stationCellList = new CellList ();
      initWidget (stationMapActionsViewImplUiBinder.createAndBindUi (this));

      citySuggestBox.getElement ().setAttribute ("autocapitalize", "off");
      citySuggestBox.getElement ().setAttribute ("autocorrect", "off");
      citySuggestBox.getElement ().setAttribute ("autocomplete", "off");

      countrySuggestBox.getElement ().setAttribute ("autocapitalize", "off");
      countrySuggestBox.getElement ().setAttribute ("autocorrect", "off");
      countrySuggestBox.getElement ().setAttribute ("autocomplete", "off");
   }
   
   @Override
   public void setInput (IRequestFactory requestFactory) {
      citySuggestBox.setRequestFactory (requestFactory);
      countrySuggestBox.setRequestFactory (requestFactory);
   }

   @Override
   public Widget asWidget () {
      return this;
   }

   @Override
   public IHasActionHandlers getTableHandler () {
      return viewTable;
   }

   @Override
   public IHasActionHandlers getStationSearchHandler () {
      return stationSearch;
   }

   @Override
   public UIObject getTablePanel () {
      return viewTablePanel;
   }

   @Override
   public UIObject getStationSearchPanel () {
      return stationSearchPanel;
   }

   @Override
   public UIObject getStationFormPanel () {
      return stationFormPanel;
   }
}
