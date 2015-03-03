package es.uned.grc.pfc.meteo.client.view.impl;

import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.model.IObservationBlockProxy;
import es.uned.grc.pfc.meteo.client.model.IObservationProxy;
import es.uned.grc.pfc.meteo.client.model.IVariableProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.view.IObservationListView;
import es.uned.grc.pfc.meteo.client.view.base.AbstractPage;
import es.uned.grc.pfc.meteo.client.view.table.IndexedObservationColumn;
import es.uned.grc.pfc.meteo.client.view.util.ColumnAppender;
import es.uned.grc.pfc.meteo.client.view.util.CustomCellTableResources;
import es.uned.grc.pfc.meteo.client.view.util.FormUtils;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.impl.VariableSuggestInputListBox;

public class ObservationListViewImpl extends AbstractPage implements IObservationListView {
   interface ObservationListViewUiBinder extends UiBinder <HTMLPanel, ObservationListViewImpl> {
   }
   private static ObservationListViewUiBinder uiBinder = GWT.create (ObservationListViewUiBinder.class);

   @Inject
   EventBus eventBus = null;
   @Inject
   PlaceController placeController = null;

   protected CustomCellTableResources cellTableResources = GWT.create (CustomCellTableResources.class);
   @UiField (provided = true)
   protected CellTable <IObservationBlockProxy> observationTable = new CellTable <IObservationBlockProxy> (Integer.MAX_VALUE, cellTableResources);
   @UiField
   protected DateBox startDateBox = null;
   @UiField
   protected DateBox endDateBox = null;
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

   public static final ProvidesKey <IObservationBlockProxy> keyProvider = new ProvidesKey <IObservationBlockProxy>() {
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
      return observationTable;
   }

   /**
    * Creates, binds and configures the UI
    */
   private void initUI () {
      SelectionModel <IObservationBlockProxy> selectionModel = null;
      
      // Bind the UI with myself
      initWidget (uiBinder.createAndBindUi (this));

      // Add a selection model so we can select cells
      selectionModel = new MultiSelectionModel <IObservationBlockProxy> (keyProvider);
      observationTable.setSelectionModel (selectionModel, DefaultSelectionEventManager.<IObservationBlockProxy> createCheckboxManager ());
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
      AsyncHandler columnSortHandler = null;
      Column <IObservationBlockProxy, String> nameColumn = null;
      DateTimeFormat dateFormat = DateTimeFormat.getFormat (PredefinedFormat.DATE_TIME_SHORT);
      int observedWidth = 15;
      int leftWidth = 100 - observedWidth;
      List <IObservationProxy> firstRow = null;
      IndexedObservationColumn indexedObservationColumn = null;
      int col = 0;
      
      //clear all the columns
      while (observationTable.getColumnCount () > 0) {
         observationTable.removeColumn (0);
      }
      
      // date observed
      new ColumnAppender <Date, IObservationBlockProxy> ().addColumn (observationTable, 
            new DateCell (dateFormat), "observed", null, new ColumnAppender.GetValue <Date, IObservationBlockProxy> () {
         @Override
         public Date getValue (IObservationBlockProxy o) {
            return o.getObserved ();
         }
      }, null, false, observedWidth);
      
      if (!observationBlock.isEmpty ()) {
         firstRow = observationBlock.get (0).getObservations ();
         for (IObservationProxy observation : firstRow) {
            indexedObservationColumn = new IndexedObservationColumn (col ++);
            
            observationTable.addColumn (indexedObservationColumn, observation != null ? observation.getVariable ().getAcronym () : "???");
            observationTable.setColumnWidth (indexedObservationColumn, leftWidth / firstRow.size (), Unit.PCT);
         }
      }

      columnSortHandler = new AsyncHandler (observationTable);
      observationTable.addColumnSortHandler (columnSortHandler);
      observationTable.getColumnSortList ().push (nameColumn);

      // Set alternating row styles
      FormUtils.setAlternatigRowStyle (observationTable);
   }
   
   @Override
   public Date getStartDate () {
      //TODO consider hour
      return startDateBox.getValue ();
   }
   
   @Override
   public void setStartDate (Date date) {
      startDateBox.setValue (date);
   }

   @Override
   public Date getEndDate () {
      //TODO consider hour
      return endDateBox.getValue ();
   }
   
   @Override
   public void setEndDate (Date date) {
      endDateBox.setValue (date);
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
}
