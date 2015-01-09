package es.uned.grc.pfc.meteo.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.CellTable;
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
import es.uned.grc.pfc.meteo.client.view.util.CustomCellTableResources;
import es.uned.grc.pfc.meteo.client.view.util.FormUtils;
import es.uned.grc.pfc.meteo.client.view.widget.LimitedSimplePager;

public class ObservationListViewImpl extends AbstractPage implements IObservationListView {
   interface WorkSpaceListViewUiBinder extends UiBinder <HTMLPanel, ObservationListViewImpl> {
   }

   private static WorkSpaceListViewUiBinder uiBinder = GWT.create (WorkSpaceListViewUiBinder.class);

   @Inject
   EventBus eventBus = null;
   @Inject
   PlaceController placeController = null;

   protected CustomCellTableResources cellTableResources = GWT.create (CustomCellTableResources.class);
   @UiField (provided = true)
   protected CellTable <IObservationProxy> workSpaceTable = new CellTable <IObservationProxy> (IClientConstants.DEFAULT_PAGE_SIZE, cellTableResources);
   @UiField (provided = true) //it has to be provided so we can use the options of the constructor !!
   protected LimitedSimplePager pagerTop = null;
   @UiField (provided = true) //it has to be provided so we can use the options of the constructor !!
   protected LimitedSimplePager pagerBottom = null;
   
   public static final ProvidesKey <IObservationProxy> keyProvider = new ProvidesKey <IObservationProxy>() {
      @Override
      public Object getKey(IObservationProxy workSpaceProxy) {
        return (workSpaceProxy != null) ? workSpaceProxy.getId () : null;
      }
   };
    
   public ObservationListViewImpl () {
      initUI ();
   }

   @Override
   public AbstractCellTable <IObservationProxy> getDataTable () {
      return workSpaceTable;
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
      pagerBottom.setDisplay (workSpaceTable);
      pagerTop.setDisplay (workSpaceTable);

      // Add a selection model so we can select cells
      selectionModel = new MultiSelectionModel <IObservationProxy> (keyProvider);
      workSpaceTable.setSelectionModel (selectionModel, DefaultSelectionEventManager.<IObservationProxy> createCheckboxManager ());

      // Initialize the columns
      initTableColumns (selectionModel);
      
      // Set alternating row styles
      FormUtils.setAlternatigRowStyle (workSpaceTable);
   }
   
   /**
    * Add the columns to the table.
    */
   private void initTableColumns (final SelectionModel <IObservationProxy> selectionModel) {
//      AsyncHandler columnSortHandler = null;
//      Column <IObservationProxy, String> nameColumn = null;
//      DateTimeFormat dateFormat = DateTimeFormat.getFormat (PredefinedFormat.DATE_SHORT);
//      
//      // Checkbox column. This table will use a checkbox column for selection.
//      ColumnAppender <Boolean, IObservationProxy> selectColumnAppender = new ColumnAppender <Boolean, IObservationProxy> ();
//      selectColumnAppender.addColumn (workSpaceTable, 
//            new CheckboxCell (true, false), selectColumnAppender.getSelectAllHeader (workSpaceTable, selectionModel), null, new ColumnAppender.GetValue <Boolean, IObservationProxy> () {
//         @Override
//         public Boolean getValue (IObservationProxy object) {
//            // Get the value from the selection model.
//            return selectionModel.isSelected (object);
//         }
//      }, null, false, 3);
//
//      // name
//      nameColumn = new ColumnAppender <String, IObservationProxy> ().addColumn (workSpaceTable, 
//            new TextCell (), textConstants.nameColumn (), "name", new ColumnAppender.GetValue <String, IObservationProxy> () {
//         @Override
//         public String getValue (IObservationProxy o) {
//            return o.getName ();
//         }
//      }, null, true, 30);
//      
//      // description
//      new ColumnAppender <String, IObservationProxy> ().addColumn (workSpaceTable, 
//            new TextCell (), textConstants.descriptionColumn (), "description", new ColumnAppender.GetValue <String, IObservationProxy> () {
//         @Override
//         public String getValue (IObservationProxy o) {
//            return DisplayUtils.prepareForTable (o.getDescription ());
//         }
//      }, null, true, 40);
//      
//      // date created
//      new ColumnAppender <Date, IObservationProxy> ().addColumn (workSpaceTable, 
//            new DateCell (dateFormat), textConstants.createdColumn (), "created", new ColumnAppender.GetValue <Date, IObservationProxy> () {
//         @Override
//         public Date getValue (IObservationProxy o) {
//            return o.getCreated ();
//         }
//      }, null, true, 15);
//      
//      // date modified
//      new ColumnAppender <Date, IObservationProxy> ().addColumn (workSpaceTable, 
//            new DateCell (dateFormat), textConstants.modifiedColumn (), "modified",  new ColumnAppender.GetValue <Date, IObservationProxy> () {
//         @Override
//         public Date getValue (IObservationProxy o) {
//            return o.getModified ();
//         }
//      }, null, true, 15);
//
//      // edit button
//      new ColumnAppender <IObservationProxy, IObservationProxy> ().addColumn (workSpaceTable, 
//            new ActionCell <IObservationProxy> (textConstants.detailAction (), new ActionCell.Delegate <IObservationProxy> () {
//         @Override
//         public void execute (IObservationProxy workSpaceProxy) { //to edit an item, fire its edition event
//            eventBus.fireEvent (new WorkSpaceEditEvent (workSpaceProxy.getId ()));
//         }
//      }), textConstants.actionColumn (), null, new ColumnAppender.GetValue <IObservationProxy, IObservationProxy> () {
//         @Override
//         public IObservationProxy getValue (IObservationProxy workSpaceProxy) {
//            return workSpaceProxy;
//         }
//      }, null, false, 5);
//      
//      columnSortHandler = new AsyncHandler (workSpaceTable);
//      workSpaceTable.addColumnSortHandler (columnSortHandler);
//      workSpaceTable.getColumnSortList ().push (nameColumn); //default sorting column
   }
}
