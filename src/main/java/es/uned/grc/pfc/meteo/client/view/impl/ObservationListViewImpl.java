package es.uned.grc.pfc.meteo.client.view.impl;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AreaChart;
import com.google.gwt.visualization.client.visualizations.BarChart;
import com.google.gwt.visualization.client.visualizations.ColumnChart;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.model.IDerivedRangeProxy;
import es.uned.grc.pfc.meteo.client.model.IObservationBlockProxy;
import es.uned.grc.pfc.meteo.client.model.IObservationProxy;
import es.uned.grc.pfc.meteo.client.model.IVariableObservationsProxy;
import es.uned.grc.pfc.meteo.client.model.IVariableProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.view.IObservationListView;
import es.uned.grc.pfc.meteo.client.view.base.AbstractPage;
import es.uned.grc.pfc.meteo.client.view.form.DerivedRangePanel;
import es.uned.grc.pfc.meteo.client.view.table.ObservationTableBuilder;
import es.uned.grc.pfc.meteo.client.view.util.ChartUtils;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.impl.VariableSuggestInputListBox;
import es.uned.grc.pfc.meteo.shared.ISharedConstants.DerivedRangeType;

public class ObservationListViewImpl extends AbstractPage implements IObservationListView {
   interface ObservationListViewUiBinder extends UiBinder <HTMLPanel, ObservationListViewImpl> {
   }
   private static ObservationListViewUiBinder uiBinder = GWT.create (ObservationListViewUiBinder.class);

   /** text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Observed") @Meaning ("Observation column")
      String observedColumn ();
      @DefaultStringValue ("Value") @Meaning ("Observation column")
      String valueColumn ();
   }
   public static TextConstants TEXT_CONSTANTS = GWT.create (TextConstants.class);
   
   @Inject
   EventBus eventBus = null;
   @Inject
   PlaceController placeController = null;

   @UiField (provided = true)
   protected CellTable <IObservationBlockProxy> observationCellTable = new CellTable <IObservationBlockProxy> (Integer.MAX_VALUE);
   @UiField
   protected DateBox startDateBox = null;
   @UiField
   protected DateBox exactDateBox = null;
   @UiField
   protected VariableSuggestInputListBox variableSuggestInputListBox = null;
   @UiField
   protected CheckBox onlyMeasuredCheckBox = null;
   @UiField
   protected CheckBox onlyDerivedCheckBox = null;
   @UiField
   protected Button searchButton = null;
   @UiField
   protected Panel textPanel = null;
   @UiField
   protected Panel graphPanel = null;
   @UiField
   protected VerticalPanel derivedPanel = null;
   @UiField
   protected Panel observedFilterPanel = null;
   @UiField
   protected Panel derivedFilterPanel = null;

   public static final ProvidesKey <IObservationBlockProxy> keyProvider = new ProvidesKey <IObservationBlockProxy> () {
      @Override
      public Object getKey (IObservationBlockProxy observationBlockProxy) {
         return (observationBlockProxy != null) ? observationBlockProxy.getStation ().getId () + "_" + observationBlockProxy.getObserved ().getTime () : null;
      }
   };

   public ObservationListViewImpl () {
      initUI ();
   }

   @Override
   public AbstractCellTable <IObservationBlockProxy> getDataTable () {
      return observationCellTable;
   }

   /**
    * Creates, binds and configures the UI
    */
   private void initUI () {
      SelectionModel <IObservationBlockProxy> selectionModel = null;

      // Bind the UI with myself
      initWidget (uiBinder.createAndBindUi (this));
      
      // set date formats
      startDateBox.setFormat (new DateBox.DefaultFormat (DateTimeFormat.getFormat (IClientConstants.DISPLAY_SHORT_DATE)));
      exactDateBox.setFormat (new DateBox.DefaultFormat (DateTimeFormat.getFormat (IClientConstants.DISPLAY_SHORT_DATE)));
      
      // Add a selection model so we can select cells
      selectionModel = new MultiSelectionModel <IObservationBlockProxy> (keyProvider);
      observationCellTable.setSelectionModel (selectionModel, DefaultSelectionEventManager.<IObservationBlockProxy> createCheckboxManager ());
   }

   @Override
   public void setInput (IRequestFactory requestFactory) {
      variableSuggestInputListBox.setRequestFactory (requestFactory);

      onlyMeasuredCheckBox.setValue (true);
      onlyDerivedCheckBox.setValue (false);
   }

   /**
    * Add the columns to the table.
    */
   @Override
   public void initTableColumns (List <IObservationBlockProxy> observationBlock) {
      observationCellTable.setTableBuilder (new ObservationTableBuilder (observationCellTable, observationBlock));
   }

   @Override
   public Date getExactDate () {
      return exactDateBox.getValue ();
   }

   @Override
   public Date getStartDate () {
      return startDateBox.getValue ();
   }

   @Override
   public void setStartDate (Date date) {
      startDateBox.setValue (date);
   }

   @Override
   public void setExactDate (Date date) {
      exactDateBox.setValue (date);
   }

   @Override
   public List <IVariableProxy> getVariables () {
      return variableSuggestInputListBox.getValues ();
   }

   @Override
   public boolean getOnlyMeasured () {
      return onlyMeasuredCheckBox.getValue ();
   }

   @Override
   public boolean getOnlyDerived () {
      return onlyDerivedCheckBox.getValue ();
   }

   @Override
   public HasClickHandlers getSearchHandler () {
      return searchButton;
   }

   @Override
   public void setTextVisible (boolean visible) {
      textPanel.setVisible (visible);
   }

   @Override
   public void setGraphVisible (boolean visible) {
      graphPanel.setVisible (visible);
   }

   @Override
   public void setDerivedVisible (boolean visible) {
      derivedPanel.setVisible (visible);
   }

   @Override
   public void generateGraphics (final List <IVariableObservationsProxy> variableObservations) {
      graphPanel.clear ();

      Runnable onLoadCallback = new Runnable () {
         public void run () {
            int i = 0;
            HorizontalPanel horizontalPanel = GWT.create (HorizontalPanel.class);
            for (IVariableObservationsProxy variableObservation : variableObservations) {
               Widget chart = null;
               AbstractDataTable data = createDataTable (variableObservation.getObservations ());
               switch (variableObservation.getVariable ().getGraphType ()) {
                  case AREA:
                     AreaChart.Options areaOptions = ChartUtils.createAreaOptions (variableObservation.getVariable ());
                     chart = new AreaChart (data, areaOptions);
                     break;
                  case BAR:
                     BarChart.Options barOptions = ChartUtils.createBarOptions (variableObservation.getVariable ());
                     chart = new BarChart (data, barOptions);
                     break;
                  case COLUMN:
                     ColumnChart.Options columnOptions = ChartUtils.createColumnOptions (variableObservation.getVariable ());
                     chart = new ColumnChart (data, columnOptions);
                     break;
                  case LINE:
                     LineChart.Options lineOptions = ChartUtils.createLineOptions (variableObservation.getVariable ());
                     chart = new LineChart (data, lineOptions);
                     break;
                  case NONE:
                     chart = null;
                     break;
               }
               
               if (chart != null) {
                  horizontalPanel.add (chart);
                  if (++ i == 2) {
                     graphPanel.add (horizontalPanel);
                     
                     i = 0;
                     horizontalPanel = GWT.create (HorizontalPanel.class);
                  }
               }
            }

            if (i != 0) {
               graphPanel.add (horizontalPanel);
            }
         }
      };
      VisualizationUtils.loadVisualizationApi (onLoadCallback, LineChart.PACKAGE);
   }
   
   private AbstractDataTable createDataTable (List <IObservationProxy> observations) {
      int row = 0;
      DateTimeFormat representationDateFormat = DateTimeFormat.getFormat (PredefinedFormat.DATE_TIME_SHORT);
      DataTable data = DataTable.create ();
      data.addColumn (ColumnType.STRING, TEXT_CONSTANTS.observedColumn ());
      data.addColumn (ColumnType.NUMBER, TEXT_CONSTANTS.valueColumn ());
      data.addRows (observations.size ());
      for (IObservationProxy observation : observations) {
         data.setValue (row, 0, representationDateFormat.format (observation.getObserved ()));
         data.setValue (row ++, 1, observation.getValue ());
      }
      return data;
   }

   @Override
   public void appendDerived (DerivedRangeType derivedRangeType, IDerivedRangeProxy derivedRange) {
      int i = 0;
      int order = getOrder (derivedRangeType);
      int count = derivedPanel.getWidgetCount ();
      DerivedRangePanel panel = GWT.create (DerivedRangePanel.class);
      panel.setInput (derivedRangeType, derivedRange);
      
      for (i = 0; i < count; i ++) {
         if (order < getOrder (((DerivedRangePanel) derivedPanel.getWidget (i)).getDerivedRangeType ())) {
            break;
         }
      }
      derivedPanel.insert (panel, i);
   }

   @Override
   public void appendDerivedGraphics (DerivedRangeType derivedRangeType, List <IDerivedRangeProxy> observations) {
      int i = 0;
      int order = getOrder (derivedRangeType);
      int count = derivedPanel.getWidgetCount ();
      DerivedRangePanel panel = GWT.create (DerivedRangePanel.class);
      panel.setInput (derivedRangeType, observations);
      
      for (i = 0; i < count; i ++) {
         if (order < getOrder (((DerivedRangePanel) derivedPanel.getWidget (i)).getDerivedRangeType ())) {
            break;
         }
      }
      derivedPanel.insert (panel, i);
   }

   @Override
   public void clear () {
      derivedPanel.clear ();
      graphPanel.clear ();
   }
   
   private int getOrder (DerivedRangeType derivedRangeType) {
      switch (derivedRangeType) {
         case MONTH:
            return 0;
         case DAY:
            return 1;
         case NIGHT:
            return 2;
         case MORNING:
            return 3;
         case AFTERNOON:
            return 4;
         case EVENING:
            return 5;
      }
      return Integer.MAX_VALUE;
   }

   @Override
   public void setDerived (boolean derived) {
      observedFilterPanel.setVisible (!derived);
      derivedFilterPanel.setVisible (derived);
   }
}
