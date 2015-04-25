package es.uned.grc.pfc.meteo.client.view.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.cellview.client.AbstractCellTable.Style;
import com.google.gwt.user.cellview.client.AbstractCellTableBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.model.IObservationBlockProxy;
import es.uned.grc.pfc.meteo.client.model.IObservationProxy;
import es.uned.grc.pfc.meteo.client.util.IStyleConstants;
import es.uned.grc.pfc.meteo.client.view.util.ColumnAppender;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

public class ObservationTableBuilder extends AbstractCellTableBuilder <IObservationBlockProxy> {

   /** text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Observada") @Meaning ("Observation column")
      String observedColumn ();
      @DefaultStringValue ("Valor") @Meaning ("Observation column")
      String valueColumn ();
      @DefaultStringValue ("Calidad: buena") @Meaning ("tooltip at observation table")
      String qualityOk ();
      @DefaultStringValue ("Calidad: desconocida") @Meaning ("tooltip at observation table")
      String qualityUnkwown ();
   }
   public static TextConstants TEXT_CONSTANTS = GWT.create (TextConstants.class);
   
   /** global text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextMessages extends Messages {
      @DefaultMessage ("Calidad: sospechosa ({0})") @Meaning ("tooltip at observation table")
      String qualityKo (String suspectReason);
   }
   public static TextMessages TEXT_MESSAGES = GWT.create (TextMessages.class);
   
   private String evenRowStyle = null;
   private String oddRowStyle = null;
   private String selectedRowStyle = null;
   private String cellStyle = null;
   private String selectedCellStyle = null;
   
   private Column <IObservationBlockProxy, Date> observedColumn = null;
   private List <IndexedObservationColumn> columns = null;

   private PopupPanel altPopup = null; 
   private Label popupStatusLabel = null;
   
   private CellTable <IObservationBlockProxy> observationCellTable = null;
   private List <IObservationBlockProxy> observationBlock = null;

   @Inject
   public ObservationTableBuilder (CellTable <IObservationBlockProxy> observationCellTable, List <IObservationBlockProxy> observationBlock) {
      super (observationCellTable);
      
      this.observationCellTable = observationCellTable;
      this.observationBlock = observationBlock;

      initTableColumns ();

      addAltHandler ();

      // Cache styles for faster access.
      Style style = this.observationCellTable.getResources ().style ();
      evenRowStyle = IStyleConstants.EVEN_ROW;
      oddRowStyle = IStyleConstants.ODD_ROW;
      selectedRowStyle = " " + style.selectedRow ();
      cellStyle = style.cell () + " " + style.evenRowCell ();
      selectedCellStyle = " " + style.selectedRowCell ();
   }

   @Override
   protected void buildRowImpl (IObservationBlockProxy observationBlock, int absRowIndex) {
      // Calculate the row styles.
      SelectionModel <? super IObservationBlockProxy> selectionModel = observationCellTable.getSelectionModel ();
      boolean isSelected = (selectionModel == null || observationBlock == null) ? false : selectionModel.isSelected (observationBlock);
      StringBuilder trClasses = new StringBuilder ((absRowIndex % 2 == 0) ? evenRowStyle : oddRowStyle);
      if (isSelected) {
         trClasses.append (selectedRowStyle);
      }

      // Calculate the cell styles.
      String cellStyles = cellStyle;
      if (isSelected) {
         cellStyles += selectedCellStyle;
      }

      //add the main row
      buildObservationBlockRow (observationBlock, absRowIndex, trClasses, cellStyles);
   }

   /**
    * Builds a row do display info for a drug item.
    */
   private void buildObservationBlockRow (IObservationBlockProxy observationBlock, int absRowIndex, StringBuilder trClasses, String cellStyles) {
      TableRowBuilder row = null;
      int cellIndex = 0;

      row = startRow ();
      row.className (isBadQuality (observationBlock.getObservations ()) ? IStyleConstants.BAD_QUALITY_ROW : trClasses.toString ());

      appendRegularCell (observationBlock, row, observedColumn, cellIndex ++, cellStyles);
      while (cellIndex <= observationBlock.getObservations ().size ()) {
         appendRegularCell (observationBlock, row, columns.get (cellIndex - 1), cellIndex ++, cellStyles);
      }

      row.endTR ();
   }

   /**
    * Appends a cell whose content will be the specified for the table in
    * general (when column was added).
    */
   private void appendRegularCell (IObservationBlockProxy observationBlock, TableRowBuilder row, Column <IObservationBlockProxy, ?> column, int index, String cellStyles) {
      TableCellBuilder cell = row.startTD ();
      cell.className (cellStyles);
      cell.style ().outlineStyle (OutlineStyle.NONE).endStyle ();
      renderCell (cell, createContext (index), column, observationBlock);
      cell.endTD ();
   }

   /**
    * Add the columns to the table.
    */
   private void initTableColumns () {
      AsyncHandler columnSortHandler = null;
      Column <IObservationBlockProxy, String> nameColumn = null;
      DateTimeFormat dateFormat = DateTimeFormat.getFormat (PredefinedFormat.DATE_TIME_SHORT);
      int observedWidth = 15;
      int leftWidth = 100 - observedWidth;
      List <IObservationProxy> firstRow = null;
      IndexedObservationColumn indexedObservationColumn = null;
      int col = 0;

      //clear all the columns
      while (observationCellTable.getColumnCount () > 0) {
         observationCellTable.removeColumn (0);
      }

      // date observed
      observedColumn = new ColumnAppender <Date, IObservationBlockProxy> ().addColumn (observationCellTable, new DateCell (dateFormat), TEXT_CONSTANTS.observedColumn (), null, new ColumnAppender.GetValue <Date, IObservationBlockProxy> () {
         @Override
         public Date getValue (IObservationBlockProxy o) {
            return o.getObserved ();
         }
      }, null, false, observedWidth);

      if (!observationBlock.isEmpty ()) {
         firstRow = observationBlock.get (0).getObservations ();
         columns = new ArrayList <IndexedObservationColumn> (observationBlock.get (0).getObservations ().size ());
         for (IObservationProxy observation : firstRow) {
            indexedObservationColumn = new IndexedObservationColumn (col ++);

            observationCellTable.addColumn (indexedObservationColumn, observation != null ? 
                                                                      observation.getVariable ().getAcronym () + " (" + observation.getVariable ().getUnit () + ")" 
                                                                      : "???");
            observationCellTable.setColumnWidth (indexedObservationColumn, leftWidth / firstRow.size (), Unit.PCT);
            
            columns.add (indexedObservationColumn);
         }
      }

      columnSortHandler = new AsyncHandler (observationCellTable);
      observationCellTable.addColumnSortHandler (columnSortHandler);
      observationCellTable.getColumnSortList ().push (nameColumn);
   }

   protected void addAltHandler () {
      observationCellTable.addCellPreviewHandler (new CellPreviewEvent.Handler <IObservationBlockProxy> () {
         @Override
         public void onCellPreview (CellPreviewEvent <IObservationBlockProxy> event) {
            try {
               StringBuilder tooltip = new StringBuilder ();
               int left = event.getNativeEvent ().getClientX ();
               int top = event.getNativeEvent ().getClientY ();
               
               if ("mouseover".equals (event.getNativeEvent ().getType ())) {
                  if (event.getColumn () > 0) {
                     IObservationProxy observation = event.getValue ().getObservations ().get (event.getColumn () - 1);
                     
                     tooltip.append (observation.getVariable ().getName ());
                     tooltip.append (ISharedConstants.WORD_SEPARATOR);
                     if (observation.getQuality () == null) {
                        tooltip.append (TEXT_CONSTANTS.qualityUnkwown ());
                     } else if (observation.getQuality ()) {
                        tooltip.append (TEXT_CONSTANTS.qualityOk ());
                     } else {
                        tooltip.append (TEXT_MESSAGES.qualityKo (observation.getWarning ()));
                     }
                     showPopup (left, top, tooltip.toString ());
                  } else {
                     hidePopup ();
                  }
               } else {
                  hidePopup ();
               }
            } catch (Exception e) {
              // silent, unimportant
            }
         }
      });
   }
   
   private void hidePopup () {
      getAltPopup ().hide ();
   }
   
   private void showPopup (int left, int top, String text) {
      PopupPanel popup = getAltPopup ();
      popup.setPopupPosition (left, top);
      if (!popup.isShowing ()) {
         getAltLabel ().setText (text);
         popup.show ();
      }
   }
   
   private PopupPanel getAltPopup () {
      if (altPopup == null) {
         popupStatusLabel = new Label ();
         altPopup = new PopupPanel (true);
         popupStatusLabel.setStyleName (IStyleConstants.TOOLTIP);
         altPopup.add (popupStatusLabel);
      }
      return altPopup;
   }

   private Label getAltLabel () {
      if (popupStatusLabel == null) {
         getAltPopup ();
      }
      return popupStatusLabel;
   }

   private boolean isBadQuality (List <IObservationProxy> observations) {
      for (IObservationProxy observation : observations) {
         if (observation.getQuality () != null && !observation.getQuality ()) {
            return true;
         }
      }
      return false;
   }
}
