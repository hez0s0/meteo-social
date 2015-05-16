package es.uned.grc.pfc.meteo.client.view.table;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
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

import es.uned.grc.pfc.meteo.client.model.IParameterProxy;
import es.uned.grc.pfc.meteo.client.util.IStyleConstants;
import es.uned.grc.pfc.meteo.client.view.util.ColumnAppender;

public class ParameterTableBuilder extends AbstractCellTableBuilder <IParameterProxy> {

   
   /** text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Nombre") @Meaning ("Param column")
      String nameColumn ();
      @DefaultStringValue ("Descripción") @Meaning ("Param column")
      String descriptionColumn ();
      @DefaultStringValue ("Valor") @Meaning ("Param column")
      String valueColumn ();
      @DefaultStringValue ("Valor por defecto") @Meaning ("Param column")
      String defaultColumn ();
   }
   public static TextConstants TEXT_CONSTANTS = GWT.create (TextConstants.class);
      
   private String evenRowStyle = null;
   private String oddRowStyle = null;
   private String selectedRowStyle = null;
   private String cellStyle = null;
   private String selectedCellStyle = null;
   
   private Column <IParameterProxy, String> nameColumn = null;
   private Column <IParameterProxy, String> descriptionColumn = null;
   private Column <IParameterProxy, String> valueColumn = null;
   private Column <IParameterProxy, String> defaultColumn = null;

   private CellTable <IParameterProxy> cellTable = null;

   @Inject
   public ParameterTableBuilder (CellTable <IParameterProxy> cellTable) {
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
   protected void buildRowImpl (IParameterProxy parameter, int absRowIndex) {
      // Calculate the row styles.
      SelectionModel <? super IParameterProxy> selectionModel = cellTable.getSelectionModel ();
      boolean isSelected = (selectionModel == null || parameter == null) ? false : selectionModel.isSelected (parameter);
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
      buildRow (parameter, absRowIndex, trClasses, cellStyles);
   }

   /**
    * Builds a row do display info for a param
    */
   private void buildRow (IParameterProxy parameter, int absRowIndex, StringBuilder trClasses, String cellStyles) {
      TableRowBuilder row = null;
      int cellIndex = 0;

      row = startRow ();
      row.className (trClasses.toString ());

      appendRegularCell (parameter, row, nameColumn, cellIndex ++, cellStyles);
      appendRegularCell (parameter, row, descriptionColumn, cellIndex ++, cellStyles);
      appendRegularCell (parameter, row, valueColumn, cellIndex ++, cellStyles);
      appendRegularCell (parameter, row, defaultColumn, cellIndex ++, cellStyles);

      row.endTR ();
   }

   /**
    * Appends a cell whose content will be the specified for the table in
    * general (when column was added).
    */
   private void appendRegularCell (IParameterProxy parameter, TableRowBuilder row, Column <IParameterProxy, ?> column, int index, String cellStyles) {
      TableCellBuilder cell = row.startTD ();
      cell.className (cellStyles);
      cell.style ().outlineStyle (OutlineStyle.NONE).endStyle ();
      renderCell (cell, createContext (index), column, parameter);
      cell.endTD ();
   }

   /**
    * Add the columns to the table.
    */
   private void initTableColumns () {
      // name of the param
      nameColumn = new ColumnAppender <String, IParameterProxy> ().addColumn (cellTable, new TextCell (), TEXT_CONSTANTS.nameColumn (), null, new ColumnAppender.GetValue <String, IParameterProxy> () {
         @Override
         public String getValue (IParameterProxy o) {
            return o.getName ();
         }
      }, null, false, 20);
      // description of the param
      descriptionColumn = new ColumnAppender <String, IParameterProxy> ().addColumn (cellTable, new TextCell (), TEXT_CONSTANTS.nameColumn (), null, new ColumnAppender.GetValue <String, IParameterProxy> () {
         @Override
         public String getValue (IParameterProxy o) {
            return o.getDescription ();
         }
      }, null, false, 40);
      // value
      valueColumn = new ColumnAppender <String, IParameterProxy> ().addColumn (cellTable, new EditTextCell (), TEXT_CONSTANTS.valueColumn (), null, new ColumnAppender.GetValue <String, IParameterProxy> () {
         @Override
         public String getValue (IParameterProxy o) {
            return o.getValue () != null ? o.getValue () : o.getDefaultValue ();
         }
      }, null, false, 20);
      // default value
      defaultColumn = new ColumnAppender <String, IParameterProxy> ().addColumn (cellTable, new TextCell (), TEXT_CONSTANTS.defaultColumn (), null, new ColumnAppender.GetValue <String, IParameterProxy> () {
         @Override
         public String getValue (IParameterProxy o) {
            return o.getDefaultValue ();
         }
      }, null, false, 20);
   }

   private void addFieldUpdaters () {
      valueColumn.setFieldUpdater (new FieldUpdater <IParameterProxy, String> () {
         @Override
         public void update (int index, IParameterProxy parameter, String value) {
            parameter.setValue (value);
         }
      });
   }

   protected String getPctg (Integer base, Integer expected) {
      Integer pctg = (base * 100) / expected;
      return String.valueOf (pctg);
   }
}

