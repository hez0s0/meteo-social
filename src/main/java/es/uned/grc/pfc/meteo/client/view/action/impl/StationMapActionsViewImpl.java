package es.uned.grc.pfc.meteo.client.view.action.impl;

import java.util.ArrayList;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

import es.uned.grc.pfc.meteo.client.model.IObservationProxy;
import es.uned.grc.pfc.meteo.client.model.IRequestParamFilterProxy;
import es.uned.grc.pfc.meteo.client.model.IRequestParamProxy;
import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.model.paged.IStationPagedListProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.request.IStationRequestContext;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.client.view.IStationMapView;
import es.uned.grc.pfc.meteo.client.view.action.IStationMapActionsView;
import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;
import es.uned.grc.pfc.meteo.client.view.widget.ExtendedTextBox;
import es.uned.grc.pfc.meteo.client.view.widget.ImageLabel;
import es.uned.grc.pfc.meteo.client.view.widget.LimitedSimplePager;
import es.uned.grc.pfc.meteo.client.view.widget.NumberTextBox;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.ActionResultDialogBox;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.impl.CitySuggestInputListBox;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.impl.CountrySuggestInputListBox;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

/**
 * Implementation of the StationMapActionsViewImpl with UiBinder.
 */
public class StationMapActionsViewImpl extends Composite implements IStationMapActionsView {
   interface StationMapActionsViewImplUiBinder extends UiBinder <HTMLPanel, StationMapActionsViewImpl> {
   }
   private static StationMapActionsViewImplUiBinder stationMapActionsViewImplUiBinder = GWT.create (StationMapActionsViewImplUiBinder.class);

   public class StationKeyProvider implements ProvidesKey <IStationProxy> {
      @Override
      public Object getKey (IStationProxy station) {
         return (station != null) ? station.getId () : null;
      }
   }
   protected StationKeyProvider stationKeyProvider = new StationKeyProvider ();

   public interface CellTemplates extends SafeHtmlTemplates {
      @SafeHtmlTemplates.Template ("<table style='border: 1px'>" + 
                                   "<tr><td style='font-size:95%;font-weight: bold;'><div>{0}</div></td></tr>" + 
                                   "<tr><td style='font-size:90%;font-weight: bold;'><div>{1}º, {2}º, {3}m.</div></td></tr>" + 
                                   "<tr><td style='font-size:90%'><div>{4} ({5})</div></td></tr>" +
                                   "<tr><td style='font-size:90%'><div>Última observación: {6}</div></td></tr>")
      SafeHtml stationCellIni (String name, String lat, String lon, String height, String city, String country, String lastObserved);
      
      @SafeHtmlTemplates.Template ("<tr><td style='font-size:80%'><div>{0}={1}{2}</div></td></tr>")
      SafeHtml stationCellObservationRow (String value, String acronym, String unit);
      
      @SafeHtmlTemplates.Template ("<tr><td style='font-size:80%'><div>{0}</div></td></tr>")
      SafeHtml stationCellNoObservation (String noRowsMessage);
      
      @SafeHtmlTemplates.Template ("</table>")
      SafeHtml stationCellEnd ();
   }
   private static CellTemplates cellTemplate = GWT.create (CellTemplates.class);

   private static final int PAGE_SIZE = 10;
            
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
   @UiField (provided = true)
   
   protected CellList <IStationProxy> stationCellList = null;
   @UiField (provided = true)
   protected LimitedSimplePager pagerTop = null;
   @UiField (provided = true)
   protected LimitedSimplePager pagerBottom = null;

   @UiField
   protected ImageLabel stationSearch = null;
   @UiField
   protected ImageLabel viewTable = null;
   
   protected SingleSelectionModel <IStationProxy> stationSelectionModel = new SingleSelectionModel <IStationProxy> (stationKeyProvider);
   protected AsyncDataProvider <IStationProxy> stationListProvider = null;
   
   protected IRequestFactory requestFactory = null;
   
   public StationMapActionsViewImpl () {
      initCellList ();
      
      initWidget (stationMapActionsViewImplUiBinder.createAndBindUi (this));

      citySuggestBox.getElement ().setAttribute ("autocapitalize", "off");
      citySuggestBox.getElement ().setAttribute ("autocorrect", "off");
      citySuggestBox.getElement ().setAttribute ("autocomplete", "off");

      countrySuggestBox.getElement ().setAttribute ("autocapitalize", "off");
      countrySuggestBox.getElement ().setAttribute ("autocorrect", "off");
      countrySuggestBox.getElement ().setAttribute ("autocomplete", "off");
   }
   
   private void initCellList () {
      // create a pager to control the cellList
      SimplePager.Resources pagerResources = GWT.create (LimitedSimplePager.Resources.class);
      pagerTop = new LimitedSimplePager (TextLocation.CENTER, pagerResources, false, 0, true);
      pagerBottom = new LimitedSimplePager (TextLocation.CENTER, pagerResources, false, 0, true);

      stationCellList = new CellList <IStationProxy> (new StationCell (), stationKeyProvider);
      stationCellList.setPageSize (PAGE_SIZE);
      stationCellList.setKeyboardPagingPolicy (KeyboardPagingPolicy.INCREASE_RANGE);
      stationCellList.setKeyboardSelectionPolicy (KeyboardSelectionPolicy.DISABLED);
      stationCellList.setSelectionModel (stationSelectionModel);
      stationCellList.setRowCount (0);

      pagerTop.setDisplay (stationCellList);
      pagerBottom.setDisplay (stationCellList);

      stationListProvider = new AsyncDataProvider <IStationProxy> () {
         @Override
         protected void onRangeChanged (HasData <IStationProxy> display) {
            doStationSearch (display.getVisibleRange ().getStart (), display.getVisibleRange ().getLength ());
         }
      };
      stationListProvider.addDataDisplay (stationCellList);
      
      stationCellList.addDomHandler (new DoubleClickHandler () {
         //handle add on double-click as well
         @Override
         public void onDoubleClick (DoubleClickEvent event) {
            handleStationDoubleClick (null);
         }
      }, DoubleClickEvent.getType ());
   }
   
   protected void handleStationDoubleClick (ClickEvent e) {
      IStationProxy selected = stationSelectionModel.getSelectedObject ();
      
      if (selected != null) {
         listView.setCenterStation (selected);
      }
   }

   @Override
   public void setInput (IRequestFactory requestFactory) {
      this.requestFactory = requestFactory;
      
      setCellVisible (false);
      
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
   
   @UiHandler ("searchButton")
   protected void handleSearchClick (ClickEvent e) {
      pagerTop.firstPage ();
      pagerBottom.firstPage ();
      doStationSearch (0, PAGE_SIZE);
   }

   private void doStationSearch (final int start, final int pageSize) {
      // search for stations and display results
      if (requestFactory != null) {
         IStationRequestContext stationRequestContext = requestFactory.getStationContext ();
         stationRequestContext.getStations (getRequestParamProxy (start, pageSize, stationRequestContext), true)
                              .with ("list", "list.stationModel", "list.transientLastObservations", "list.transientLastObservations.variable")
                              .fire (new Receiver <IStationPagedListProxy> () {
            @Override
            public void onSuccess (IStationPagedListProxy response) {
               // Push the data back into the list
               if (response.getRealSize () > Integer.MAX_VALUE) {
                  throw new ClassCastException (PortableStringUtils.format ("Cannot cast long to int (%s)", response.getRealSize ()));
               }
               if (response.getRealSize () == 0) {
                  ActionResultDialogBox.showDialog (IClientConstants.TEXT_CONSTANTS.emptySearchResult (), ActionResultDialogBox.FADE_OUT_MILLIS);
               }

               setCellVisible (true);
               
               stationCellList.setRowCount (0);
               //we need to narrow it down (nastily): hibernate returns Long
               stationCellList.setRowCount (Long.valueOf (response.getRealSize ()).intValue ()); 
               stationCellList.setRowData (start, response.getList ());
            }
         });
      }
   }
   
   @Override
   public void clearSearchFields () {
      zipSuggestBox.setValue (null);
      latTextBox.setValue (null);
      lonTextBox.setValue (null);
      radiusTextBox.setValue (null);
      citySuggestBox.setValues (null);
      countrySuggestBox.setValues (null);
   }
   
   @Override
   public void setCellVisible (boolean visible) {
      stationCellList.setVisible (visible);
      pagerTop.setVisible (visible);
      pagerBottom.setVisible (visible);
   }
   
   private IRequestParamProxy getRequestParamProxy (int start, int pageSize, IStationRequestContext stationRequestContext) {
      IRequestParamFilterProxy paramFilter = null;
      IRequestParamProxy requestParamProxy = stationRequestContext.create (IRequestParamProxy.class);

      requestParamProxy.setStart (0);
      requestParamProxy.setLength (Integer.MAX_VALUE);

      requestParamProxy.setFilters (new ArrayList <IRequestParamFilterProxy> ());
      
      if (!PortableStringUtils.isEmpty (zipSuggestBox.getValue ())) {
         paramFilter = stationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.StationFilter.ZIP.toString ());
         paramFilter.setValue (zipSuggestBox.getValue ());
         requestParamProxy.getFilters ().add (paramFilter);
      }
      if (!PortableStringUtils.isEmpty (latTextBox.getValue ())) {
         paramFilter = stationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.StationFilter.LAT.toString ());
         paramFilter.setValue (latTextBox.getValue ());
         requestParamProxy.getFilters ().add (paramFilter);
      }
      if (!PortableStringUtils.isEmpty (lonTextBox.getValue ())) {
         paramFilter = stationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.StationFilter.LON.toString ());
         paramFilter.setValue (lonTextBox.getValue ());
         requestParamProxy.getFilters ().add (paramFilter);
      }
      if (!PortableStringUtils.isEmpty (radiusTextBox.getValue ())) {
         paramFilter = stationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.StationFilter.RADIUS.toString ());
         paramFilter.setValue (radiusTextBox.getValue ());
         requestParamProxy.getFilters ().add (paramFilter);
      }
      if (citySuggestBox.getValues () != null && !citySuggestBox.getValues ().isEmpty ()) {
         paramFilter = stationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.StationFilter.CITY.toString ());
         paramFilter.setValue (citySuggestBox.getValues ().get (0));
         requestParamProxy.getFilters ().add (paramFilter);
      }
      if (countrySuggestBox.getValues () != null && !countrySuggestBox.getValues ().isEmpty ()) {
         paramFilter = stationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.StationFilter.COUNTRY.toString ());
         paramFilter.setValue (countrySuggestBox.getValues ().get (0));
         requestParamProxy.getFilters ().add (paramFilter);
      }
      
      return requestParamProxy;
   }

   /**
    * The Cell used to render a {@link IStationProxy}.
    */
   static class StationCell extends AbstractCell <IStationProxy> {
      @Override
      public void render (Context context, IStationProxy station, SafeHtmlBuilder safeHtmlBuilder) {
         // station can be null, so do a null check..
         if (station == null) {
            return;
         }
         DateTimeFormat dateFormat = DateTimeFormat.getFormat (PredefinedFormat.DATE_TIME_SHORT);
         safeHtmlBuilder.append (cellTemplate.stationCellIni (station.getName (),
                                                              String.valueOf (station.getLatitude ()),
                                                              String.valueOf (station.getLongitude ()),
                                                              String.valueOf (station.getHeight ()),
                                                              station.getCity () != null ? station.getCity () : IClientConstants.TEXT_CONSTANTS.emptyValue (),
                                                              station.getCountry () != null ? station.getCountry () : IClientConstants.TEXT_CONSTANTS.emptyValue (),
                                                              station.getLastCollectedPeriod () != null ? dateFormat.format (station.getLastCollectedPeriod ()) : IClientConstants.TEXT_CONSTANTS.emptyValue ()));
         
         if (station.getTransientLastObservations () != null && !station.getTransientLastObservations ().isEmpty ()) {
            for (IObservationProxy observation : station.getTransientLastObservations ()) {
               safeHtmlBuilder.append (cellTemplate.stationCellObservationRow (observation.getVariable ().getAcronym (), observation.getValue (), observation.getVariable ().getUnit ()));
            }
         } else {
            safeHtmlBuilder.append (cellTemplate.stationCellNoObservation (IClientConstants.TEXT_CONSTANTS.noRows ()));
         }
         safeHtmlBuilder.append (cellTemplate.stationCellEnd ());
      }
   }
}
