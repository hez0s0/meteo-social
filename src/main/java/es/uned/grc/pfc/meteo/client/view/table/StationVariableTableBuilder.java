package es.uned.grc.pfc.meteo.client.view.table;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.cellview.client.AbstractCellTable.Style;
import com.google.gwt.user.cellview.client.AbstractCellTableBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.model.IStationVariableProxy;
import es.uned.grc.pfc.meteo.client.util.IStyleConstants;
import es.uned.grc.pfc.meteo.client.view.util.ColumnAppender;

public class StationVariableTableBuilder extends AbstractCellTableBuilder <IStationVariableProxy> {
   
   /** text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Variable") @Meaning ("StationVariable column")
      String variableColumn ();
      @DefaultStringValue ("Mínimo") @Meaning ("StationVariable column")
      String minimumColumn ();
      @DefaultStringValue ("Máximo") @Meaning ("StationVariable column")
      String maximumColumn ();
      @DefaultStringValue ("Mínimo físico") @Meaning ("StationVariable column")
      String physicalMinimumColumn ();
      @DefaultStringValue ("Máximo físico") @Meaning ("StationVariable column")
      String physicalMaximumColumn ();
   }
   public static TextConstants TEXT_CONSTANTS = GWT.create (TextConstants.class);
      
   private String evenRowStyle = null;
   private String oddRowStyle = null;
   private String selectedRowStyle = null;
   private String cellStyle = null;
   private String selectedCellStyle = null;
   
   private Column <IStationVariableProxy, String> variableColumn = null;
   private Column <IStationVariableProxy, String> minimumColumn = null;
   private Column <IStationVariableProxy, String> maximumColumn = null;
   private Column <IStationVariableProxy, Number> physicalMinimumColumn = null;
   private Column <IStationVariableProxy, Number> physicalMaximumColumn = null;

   private CellTable <IStationVariableProxy> cellTable = null;

   @Inject
   public StationVariableTableBuilder (CellTable <IStationVariableProxy> cellTable) {
      super (cellTable);
      
      this.cellTable = cellTable;

      initTableColumns ();
      addFieldUpdaters ();

      // Cache styles for faster access.
      Style style = this.cellTable.getResources ().style ();
      evenRowStyle = IStyleConstants.EVEN_ROW;
      oddRowStyle = IStyleConstants.ODD_ROW;
      selectedRowStyle = " " + style.selectedRow ();
      cellStyle = style.cell () + " " + style.evenRowCell ();
      selectedCellStyle = " " + style.selectedRowCell ();
   }

   @Override
   protected void buildRowImpl (IStationVariableProxy stationVariable, int absRowIndex) {
      // Calculate the row styles.
      SelectionModel <? super IStationVariableProxy> selectionModel = cellTable.getSelectionModel ();
      boolean isSelected = (selectionModel == null || stationVariable == null) ? false : selectionModel.isSelected (stationVariable);
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
      buildRow (stationVariable, absRowIndex, trClasses, cellStyles);
   }

   /**
    * Builds a row do display info for a param
    */
   private void buildRow (IStationVariableProxy stationVariable, int absRowIndex, StringBuilder trClasses, String cellStyles) {
      TableRowBuilder row = null;
      int cellIndex = 0;

      row = startRow ();
      row.className (trClasses.toString ());

      appendRegularCell (stationVariable, row, variableColumn, cellIndex ++, cellStyles);
      appendRegularCell (stationVariable, row, minimumColumn, cellIndex ++, cellStyles);
      appendRegularCell (stationVariable, row, maximumColumn, cellIndex ++, cellStyles);
      appendRegularCell (stationVariable, row, physicalMinimumColumn, cellIndex ++, cellStyles);
      appendRegularCell (stationVariable, row, physicalMaximumColumn, cellIndex ++, cellStyles);

      row.endTR ();
   }

   /**
    * Appends a cell whose content will be the specified for the table in
    * general (when column was added).
    */
   private void appendRegularCell (IStationVariableProxy stationVariable, TableRowBuilder row, Column <IStationVariableProxy, ?> column, int index, String cellStyles) {
      TableCellBuilder cell = row.startTD ();
      cell.className (cellStyles);
      cell.style ().outlineStyle (OutlineStyle.NONE).endStyle ();
      renderCell (cell, createContext (index), column, stationVariable);
      cell.endTD ();
   }

   /**
    * Add the columns to the table.
    */
   private void initTableColumns () {
      // name of the variable
      variableColumn = new ColumnAppender <String, IStationVariableProxy> ().addColumn (cellTable, new TextCell (), TEXT_CONSTANTS.variableColumn (), null, new ColumnAppender.GetValue <String, IStationVariableProxy> () {
         @Override
         public String getValue (IStationVariableProxy o) {
            return o.getVariable ().getName ();
         }
      }, null, false, 40);
      // minimum value
      minimumColumn = new ColumnAppender <String, IStationVariableProxy> ().addColumn (cellTable, new EditTextCell (), TEXT_CONSTANTS.minimumColumn (), null, new ColumnAppender.GetValue <String, IStationVariableProxy> () {
         @Override
         public String getValue (IStationVariableProxy o) {
            return o.getMinimum () != null ? "" + o.getMinimum () : "";
         }
      }, null, false, 20);
      // maximum value
      maximumColumn = new ColumnAppender <String, IStationVariableProxy> ().addColumn (cellTable, new EditTextCell (), TEXT_CONSTANTS.maximumColumn (), null, new ColumnAppender.GetValue <String, IStationVariableProxy> () {
         @Override
         public String getValue (IStationVariableProxy o) {
            return o.getMaximum () != null ? "" + o.getMaximum () : "";
         }
      }, null, false, 20);
      // physical minimum value
      physicalMinimumColumn = new ColumnAppender <Number, IStationVariableProxy> ().addColumn (cellTable, new NumberCell (), TEXT_CONSTANTS.physicalMinimumColumn (), null, new ColumnAppender.GetValue <Number, IStationVariableProxy> () {
         @Override
         public Number getValue (IStationVariableProxy o) {
            return o.getPhysicalMinimum ();
         }
      }, null, false, 20);
      // physical maximum value
      physicalMaximumColumn = new ColumnAppender <Number, IStationVariableProxy> ().addColumn (cellTable, new NumberCell (), TEXT_CONSTANTS.physicalMaximumColumn (), null, new ColumnAppender.GetValue <Number, IStationVariableProxy> () {
         @Override
         public Number getValue (IStationVariableProxy o) {
            return o.getPhysicalMaximum ();
         }
      }, null, false, 20);
   }

   private void addFieldUpdaters () {
      minimumColumn.setFieldUpdater (new FieldUpdater <IStationVariableProxy, String> () {
         @Override
         public void update (int index, IStationVariableProxy stationVariable, String value) {
            stationVariable.setMinimum (Double.valueOf (value));
         }
      });
      maximumColumn.setFieldUpdater (new FieldUpdater <IStationVariableProxy, String> () {
         @Override
         public void update (int index, IStationVariableProxy stationVariable, String value) {
            stationVariable.setMaximum (Double.valueOf (value));
         }
      });
   }

   protected String getPctg (Integer base, Integer expected) {
      Integer pctg = (base * 100) / expected;
      return String.valueOf (pctg);
   }
}

