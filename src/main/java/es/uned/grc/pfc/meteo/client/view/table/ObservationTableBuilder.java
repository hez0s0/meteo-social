package es.uned.grc.pfc.meteo.client.view.table;

import com.google.common.eventbus.EventBus;
import com.google.gwt.user.cellview.client.AbstractCellTableBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.model.IObservationProxy;

public class ObservationTableBuilder extends AbstractCellTableBuilder <IObservationProxy> {
//
//   /**
//    * The constants used in this Widget.
//    */
//   @Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
//   @GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
//   public interface TextConstants extends Constants {
//      @DefaultStringValue ("Name") @Meaning ("DrugItemTableBuilder Name column")
//      String nameColumn ();
//
//      @DefaultStringValue ("Status") @Meaning ("DrugItemTableBuilder Status column")
//      String statusColumn ();
//
//      @DefaultStringValue ("ROA Group") @Meaning ("DrugItemTableBuilder ROA Group column")
//      String roaGroupColumn ();
//
//      @DefaultStringValue ("Approved Version") @Meaning ("DrugItemTableBuilder Approved Version column")
//      String currentVersionColumn ();
//
//      @DefaultStringValue ("Synchronized") @Meaning ("DrugItemTableBuilder Synchronized column")
//      String syncVersionColumn ();
//      
//      @DefaultStringValue ("Description") @Meaning ("DrugItemTableBuilder Description column")
//      String descriptionColumn ();
//
//      @DefaultStringValue ("Action") @Meaning ("DrugItemTableBuilder Action column")
//      String actionColumn ();
//
//      @DefaultStringValue ("Detail") @Meaning ("DrugItemTableBuilder Detail action")
//      String detailAction ();
//   }
//   private TextConstants textConstants = GWT.create (TextConstants.class);
//
//   private static RoaGroupRenderer roaGroupRenderer = new RoaGroupRenderer ();
//
//   private String evenRowStyle = null;
//   private String oddRowStyle = null;
//   private String selectedRowStyle = null;
//   private String cellStyle = null;
//   private String selectedCellStyle = null;
//
//   private Column <IDrugItemProxy, String> nameColumn = null;
//   private Column <IDrugItemProxy, SafeUri> statusColumn = null;
//   private Column <IDrugItemProxy, String> roaGroupColumn = null;
//   private Column <IDrugItemProxy, String> currentVersionColumn = null;
//   private Column <IDrugItemProxy, String> syncVersionColumn = null;
//   private Column <IDrugItemProxy, String> descriptionColumn = null;
//   private Column <IDrugItemProxy, IDrugItemProxy> actionColumn = null;
//
//   private EventBus eventBus = null;
//   private PopupPanel altPopup = null; 
//   private Label popupStatusLabel = null;
//   
//   private CellTable <IDrugItemProxy> drugItemTable = null;
//   
//   private int statusColumnIndex = -1;
//
   @Inject
   public ObservationTableBuilder (CellTable <IObservationProxy> observationTable, EventBus eventBus) {
      super (observationTable);
//
//      this.drugItemTable = drugItemTable;
//      this.eventBus = eventBus;
//
//      initTableColumns ();
//
//      addAltHandler ();
//
//      // Cache styles for faster access.
//      Style style = this.drugItemTable.getResources ().style ();
//      evenRowStyle = IStyleConstants.EVEN_ROW;
//      oddRowStyle = IStyleConstants.ODD_ROW;
//      selectedRowStyle = " " + style.selectedRow ();
//      cellStyle = style.cell () + " " + style.evenRowCell ();
//      selectedCellStyle = " " + style.selectedRowCell ();
   }
//
   @Override
   protected void buildRowImpl (IObservationProxy observationProxy, int absRowIndex) {
//      // Calculate the row styles.
//      SelectionModel <? super IDrugItemProxy> selectionModel = drugItemTable.getSelectionModel ();
//      boolean isSelected = (selectionModel == null || drugItemProxy == null) ? false : selectionModel.isSelected (drugItemProxy);
//      StringBuilder trClasses = new StringBuilder ((absRowIndex % 2 == 0) ? evenRowStyle : oddRowStyle);
//      if (isSelected) {
//         trClasses.append (selectedRowStyle);
//      }
//
//      // Calculate the cell styles.
//      String cellStyles = cellStyle;
//      if (isSelected) {
//         cellStyles += selectedCellStyle;
//      }
//
//      //add the main row
//      buildDrugItemRow (drugItemProxy, absRowIndex, trClasses, cellStyles);
   }
//
//   /**
//    * Builds a row do display info for a drug item.
//    */
//   private void buildDrugItemRow (IDrugItemProxy drugItemProxy, int absRowIndex, StringBuilder trClasses, String cellStyles) {
//      TableRowBuilder row = null;
//      int cellIndex = 0;
//
//      row = startRow ();
//      row.className (trClasses.toString ());
//
//      appendRegularCell (drugItemProxy, row, statusColumn, cellIndex++, cellStyles); //status
//      appendRegularCell (drugItemProxy, row, nameColumn, cellIndex++, cellStyles); //name
//      appendRegularCell (drugItemProxy, row, roaGroupColumn, cellIndex++, cellStyles); //roa group
//      appendRegularCell (drugItemProxy, row, currentVersionColumn, cellIndex++, cellStyles); //current version
//      appendRegularCell (drugItemProxy, row, syncVersionColumn, cellIndex++, cellStyles); //sync version
//      appendRegularCell (drugItemProxy, row, descriptionColumn, cellIndex++, cellStyles); //description
//      appendRegularCell (drugItemProxy, row, actionColumn, cellIndex++, cellStyles); //action
//
//      row.endTR ();
//   } //end of buildDrugItemRow
//
//   /**
//    * Appends a cell whose content will be the specified for the table in
//    * general (when column was added).
//    */
//   private void appendRegularCell (IDrugItemProxy drugItemProxy, TableRowBuilder row, Column <IDrugItemProxy, ?> column, int index, String cellStyles) {
//      if (column.equals (statusColumn)) {
//         statusColumnIndex = index;
//      }
//      TableCellBuilder cell = row.startTD ();
//      cell.className (cellStyles);
//      cell.style ().outlineStyle (OutlineStyle.NONE).endStyle ();
//      renderCell (cell, createContext (index), column, drugItemProxy);
//      cell.endTD ();
//   } //end of appendRegularCell
//
//   /**
//    * Add the columns to the table.
//    */
//   private void initTableColumns () {
//      AsyncHandler columnSortHandler = null;
//
//      // status
//      statusColumn = new ColumnAppender <SafeUri, IDrugItemProxy> ().addColumn (drugItemTable, 
//            new SafeImageCell (), textConstants.statusColumn (), ISharedConstants.SPECIAL_ORDER_MARK + ISharedConstants.DrugItemSpecialSort.LifeAndSyncStatus, 
//            new ColumnAppender.GetValue <SafeUri, IDrugItemProxy> () {
//         @Override
//         public SafeUri getValue (IDrugItemProxy drugItemProxy) {
//            return EntityUtils.getStatusImageResource (drugItemProxy).getSafeUri ();
//         }
//      }, null, true, 3);
//
//      // name
//      nameColumn = new ColumnAppender <String, IDrugItemProxy> ().addColumn (drugItemTable, 
//            new TextCell (), textConstants.nameColumn (), "sortVersion.name", new ColumnAppender.GetValue <String, IDrugItemProxy> () {
//         @Override
//         public String getValue (IDrugItemProxy o) {
//            return o.getTransientName ();
//         }
//      }, null, true, 25);
//
//      // roa group
//      roaGroupColumn = new ColumnAppender <String, IDrugItemProxy> ().addColumn (drugItemTable, 
//            new TextCell (), textConstants.roaGroupColumn (), null, new ColumnAppender.GetValue <String, IDrugItemProxy> () {
//         @Override
//         public String getValue (IDrugItemProxy o) {
//            return roaGroupRenderer.render (o.getTransientRoaGroup ());
//         }
//      }, null, false, 10);
//
//      // currentVersion
//      currentVersionColumn = new ColumnAppender <String, IDrugItemProxy> ().addColumn (drugItemTable, 
//            new TextCell (), textConstants.currentVersionColumn (), null, new ColumnAppender.GetValue <String, IDrugItemProxy> () {
//         @Override
//         public String getValue (IDrugItemProxy o) {
//            return (o.getTransientCurrentMajor () != null) ? DisplayUtils.getVersion (o.getTransientCurrentMajor (), o.getTransientCurrentMinor ()) : IClientConstants.textConstants.emptyValue ();
//         }
//      }, null, false, 10);
//
//      // syncVersion
//      syncVersionColumn = new ColumnAppender <String, IDrugItemProxy> ().addColumn (drugItemTable, 
//            new TextCell (), textConstants.syncVersionColumn (), null, new ColumnAppender.GetValue <String, IDrugItemProxy> () {
//         @Override
//         public String getValue (IDrugItemProxy o) {
//            return (o.getTransientSyncMajor () != null) ? DisplayUtils.getVersion (o.getTransientSyncMajor (), o.getTransientSyncMinor ()) : IClientConstants.textConstants.emptyValue ();
//         }
//      }, null, false, 5);
//      
//      // description
//      descriptionColumn = new ColumnAppender <String, IDrugItemProxy> ().addColumn (drugItemTable, 
//            new TextCell (), textConstants.descriptionColumn (), "description", new ColumnAppender.GetValue <String, IDrugItemProxy> () {
//         @Override
//         public String getValue (IDrugItemProxy o) {
//            return DisplayUtils.prepareForTable (o.getTransientDescription ());
//         }
//      }, null, false, 30);
//
//      // edit button
//      actionColumn = new ColumnAppender <IDrugItemProxy, IDrugItemProxy> ().addColumn (drugItemTable, 
//            new ActionCell <IDrugItemProxy> (textConstants.detailAction (), new ActionCell.Delegate <IDrugItemProxy> () {
//         @Override
//         public void execute (IDrugItemProxy o) { //to edit an item, fire its edition event
//            eventBus.fireEvent (new DrugItemEditEvent (o.getId ()));
//         }
//      }), textConstants.actionColumn (), null, new ColumnAppender.GetValue <IDrugItemProxy, IDrugItemProxy> () {
//         @Override
//         public IDrugItemProxy getValue (IDrugItemProxy o) {
//            return o;
//         }
//      }, null, false, 5);
//
//      columnSortHandler = new AsyncHandler (drugItemTable);
//      drugItemTable.addColumnSortHandler (columnSortHandler);
//      drugItemTable.getColumnSortList ().push (nameColumn); //default sorting column
//   } //end of initTableColumns
//
//   protected void addAltHandler () {
//      drugItemTable.addCellPreviewHandler (new CellPreviewEvent.Handler <IDrugItemProxy> () {
//         @Override
//         public void onCellPreview (CellPreviewEvent <IDrugItemProxy> event) {
//            try {
//               String tooltip = null;
//               int left = event.getNativeEvent ().getClientX ();
//               int top = event.getNativeEvent ().getClientY ();
//               
//               if ("mouseover".equals (event.getNativeEvent ().getType ())) {
//                  if (event.getColumn () == statusColumnIndex) {
//                     tooltip = EntityUtils.getTooltip (event.getValue ());
//                     showPopup (left, top, tooltip);
//                  } else {
//                     hidePopup ();
//                  }
//               } else {
//                  hidePopup ();
//               }
//            } catch (Exception e) {
//              // silent, unimportant
//            }
//         }
//      });
//   } //end of addAltHandler
//   
//   private void hidePopup () {
//      getAltPopup ().hide ();
//   }
//   
//   private void showPopup (int left, int top, String text) {
//      PopupPanel popup = getAltPopup ();
//      popup.setPopupPosition (left, top);
//      if (!popup.isShowing ()) {
//         getAltLabel ().setText (text);
//         popup.show ();
//      }
//   }
//   
//   private PopupPanel getAltPopup () {
//      if (altPopup == null) {
//         popupStatusLabel = new Label ();
//         altPopup = new PopupPanel (true);
//         popupStatusLabel.setStyleName (IStyleConstants.TOOLTIP);
//         altPopup.add (popupStatusLabel);
//      }
//      return altPopup;
//   }
//
//   private Label getAltLabel () {
//      if (popupStatusLabel == null) {
//         getAltPopup ();
//      }
//      return popupStatusLabel;
//   }
}

