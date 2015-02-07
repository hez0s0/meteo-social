package es.uned.grc.pfc.meteo.client.view.impl;

import java.util.Date;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.model.IObservationProxy;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.view.IObservationListView;
import es.uned.grc.pfc.meteo.client.view.base.AbstractPage;
import es.uned.grc.pfc.meteo.client.view.util.ColumnAppender;
import es.uned.grc.pfc.meteo.client.view.util.CustomCellTableResources;
import es.uned.grc.pfc.meteo.client.view.util.FormUtils;
import es.uned.grc.pfc.meteo.client.view.widget.LimitedSimplePager;

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
   protected CellTable <IObservationProxy> observationTable = new CellTable <IObservationProxy> (IClientConstants.DEFAULT_PAGE_SIZE, cellTableResources);
   @UiField (provided = true) //it has to be provided so we can use the options of the constructor !!
   protected LimitedSimplePager pagerTop = null;
   @UiField (provided = true) //it has to be provided so we can use the options of the constructor !!
   protected LimitedSimplePager pagerBottom = null;
   
   public static final ProvidesKey <IObservationProxy> keyProvider = new ProvidesKey <IObservationProxy>() {
      @Override
      public Object getKey(IObservationProxy ObservationProxy) {
        return (ObservationProxy != null) ? ObservationProxy.getId () : null;
      }
   };
    
   public ObservationListViewImpl () {
      initUI ();
   }

   @Override
   public AbstractCellTable <IObservationProxy> getDataTable () {
      return observationTable;
   }

   /**
    * Creates, binds and configures the UI
    */
   private void initUI () {
      SelectionModel <IObservationProxy> selectionModel = null;

      // Create a Pager to control the table
      SimplePager.Resources pagerResources = GWT.create (LimitedSimplePager.Resources.class);
      pagerBottom = new LimitedSimplePager (TextLocation.CENTER, pagerResources, false, 0, true);
      pagerTop = new LimitedSimplePager (TextLocation.CENTER, pagerResources, false, 0, true);
      
      // Bind the UI with myself
      initWidget (uiBinder.createAndBindUi (this));
      
      // Set the Pager on the table
      pagerBottom.setDisplay (observationTable);
      pagerTop.setDisplay (observationTable);

      // Add a selection model so we can select cells
      selectionModel = new MultiSelectionModel <IObservationProxy> (keyProvider);
      observationTable.setSelectionModel (selectionModel, DefaultSelectionEventManager.<IObservationProxy> createCheckboxManager ());

      // Initialize the columns
      initTableColumns (selectionModel);
      
      // Set alternating row styles
      FormUtils.setAlternatigRowStyle (observationTable);
   }
   
   /**
    * Add the columns to the table.
    */
   private void initTableColumns (final SelectionModel <IObservationProxy> selectionModel) {
      AsyncHandler columnSortHandler = null;
      Column <IObservationProxy, String> nameColumn = null;
      DateTimeFormat dateFormat = DateTimeFormat.getFormat (PredefinedFormat.TIME_SHORT);
      
      // variable
      nameColumn = new ColumnAppender <String, IObservationProxy> ().addColumn (observationTable, 
            new TextCell (), "variable", null, new ColumnAppender.GetValue <String, IObservationProxy> () {
         @Override
         public String getValue (IObservationProxy o) {
            return o.getVariable ().getAcronym ();
         }
      }, null, false, 30);
      
      // date created
      new ColumnAppender <Date, IObservationProxy> ().addColumn (observationTable, 
            new DateCell (dateFormat), "observed", null, new ColumnAppender.GetValue <Date, IObservationProxy> () {
         @Override
         public Date getValue (IObservationProxy o) {
            return o.getObserved ();
         }
      }, null, false, 15);
      
      // description
      new ColumnAppender <String, IObservationProxy> ().addColumn (observationTable, 
            new TextCell (), "value", null, new ColumnAppender.GetValue <String, IObservationProxy> () {
         @Override
         public String getValue (IObservationProxy o) {
            return o.getValue ();
         }
      }, null, false, 40);

      columnSortHandler = new AsyncHandler (observationTable);
      observationTable.addColumnSortHandler (columnSortHandler);
      observationTable.getColumnSortList ().push (nameColumn);
   }
}
