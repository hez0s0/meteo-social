package es.uned.grc.pfc.meteo.client.view.table;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.cellview.client.AbstractCellTable.Style;
import com.google.gwt.user.cellview.client.AbstractCellTableBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.model.IDerivedVariableProxy;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.util.IStyleConstants;
import es.uned.grc.pfc.meteo.client.view.util.ColumnAppender;

public class DerivedTableBuilder extends AbstractCellTableBuilder <IDerivedVariableProxy> {

   private static final int MINIMUM_COLUMN = 1;
   private static final int AVERAGE_COLUMN = 2;
   private static final int MAXIMUM_COLUMN = 3;
   
   /** text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Variable") @Meaning ("Derived column")
      String variableColumn ();
      @DefaultStringValue ("Mínima") @Meaning ("Derived column")
      String minimumColumn ();
      @DefaultStringValue ("Media") @Meaning ("Derived column")
      String averageColumn ();
      @DefaultStringValue ("Máxima") @Meaning ("Derived column")
      String maximumColumn ();
   }
   public static TextConstants TEXT_CONSTANTS = GWT.create (TextConstants.class);
   
   /** global text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextMessages extends Messages {
      @DefaultMessage ("{0} ({1}% calculado)") @Meaning ("value displayed at derived table")
      String value (String value, String pctg);
      @DefaultMessage ("{0} de {1} usadas para el cálculo: {2}%") @Meaning ("tooltip at derived table")
      String used (String used, String expected, String pctg);
      @DefaultMessage ("{0} ignoradas por motivos de calidad: {1}%") @Meaning ("tooltip at derived table")
      String ignored (String ignored, String pctg);
   }
   public static TextMessages TEXT_MESSAGES = GWT.create (TextMessages.class);
   
   private String evenRowStyle = null;
   private String oddRowStyle = null;
   private String selectedRowStyle = null;
   private String cellStyle = null;
   private String selectedCellStyle = null;
   
   private Column <IDerivedVariableProxy, String> variableColumn = null;
   private Column <IDerivedVariableProxy, String> minimumColumn = null;
   private Column <IDerivedVariableProxy, String> averageColumn = null;
   private Column <IDerivedVariableProxy, String> maximumColumn = null;

   private PopupPanel altPopup = null; 
   private Label popupStatusLabel = null;
   
   private CellTable <IDerivedVariableProxy> derivedCellTable = null;

   @Inject
   public DerivedTableBuilder (CellTable <IDerivedVariableProxy> derivedCellTable) {
      super (derivedCellTable);
      
      this.derivedCellTable = derivedCellTable;

      initTableColumns ();

      addAltHandler ();

      // Cache styles for faster access.
      Style style = this.derivedCellTable.getResources ().style ();
      evenRowStyle = IStyleConstants.EVEN_ROW;
      oddRowStyle = IStyleConstants.ODD_ROW;
      selectedRowStyle = " " + style.selectedRow ();
      cellStyle = style.cell () + " " + style.evenRowCell ();
      selectedCellStyle = " " + style.selectedRowCell ();
   }

   @Override
   protected void buildRowImpl (IDerivedVariableProxy observationBlock, int absRowIndex) {
      // Calculate the row styles.
      SelectionModel <? super IDerivedVariableProxy> selectionModel = derivedCellTable.getSelectionModel ();
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
   private void buildObservationBlockRow (IDerivedVariableProxy derived, int absRowIndex, StringBuilder trClasses, String cellStyles) {
      TableRowBuilder row = null;
      int cellIndex = 0;

      row = startRow ();
      row.className (isBadQuality (derived) ? IStyleConstants.BAD_QUALITY_ROW : trClasses.toString ());

      appendRegularCell (derived, row, variableColumn, cellIndex ++, cellStyles);
      appendRegularCell (derived, row, minimumColumn, cellIndex ++, cellStyles);
      appendRegularCell (derived, row, averageColumn, cellIndex ++, cellStyles);
      appendRegularCell (derived, row, maximumColumn, cellIndex ++, cellStyles);

      row.endTR ();
   }

   /**
    * Appends a cell whose content will be the specified for the table in
    * general (when column was added).
    */
   private void appendRegularCell (IDerivedVariableProxy observationBlock, TableRowBuilder row, Column <IDerivedVariableProxy, ?> column, int index, String cellStyles) {
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
      // name of the variable
      variableColumn = new ColumnAppender <String, IDerivedVariableProxy> ().addColumn (derivedCellTable, new TextCell (), TEXT_CONSTANTS.variableColumn (), null, new ColumnAppender.GetValue <String, IDerivedVariableProxy> () {
         @Override
         public String getValue (IDerivedVariableProxy o) {
            return o.getStationVariable ().getVariable ().getName ();
         }
      }, null, false, 40);
      // minimum value
      minimumColumn = new ColumnAppender <String, IDerivedVariableProxy> ().addColumn (derivedCellTable, new TextCell (), TEXT_CONSTANTS.minimumColumn (), null, new ColumnAppender.GetValue <String, IDerivedVariableProxy> () {
         @Override
         public String getValue (IDerivedVariableProxy o) {
            if (o.getMinimum () != null) {
               return TEXT_MESSAGES.value (o.getMinimum (), getPctg (o.getMinimumDeriveBase (), o.getMinimumDeriveExpected ()));
            } else {
               return IClientConstants.TEXT_CONSTANTS.emptyValue ();
            }
         }
      }, null, false, 20);
      // average value
      averageColumn = new ColumnAppender <String, IDerivedVariableProxy> ().addColumn (derivedCellTable, new TextCell (), TEXT_CONSTANTS.averageColumn (), null, new ColumnAppender.GetValue <String, IDerivedVariableProxy> () {
         @Override
         public String getValue (IDerivedVariableProxy o) {
            if (o.getAverage () != null) {
               return TEXT_MESSAGES.value (o.getAverage (), getPctg (o.getAverageDeriveBase (), o.getAverageDeriveExpected ()));
            } else {
               return IClientConstants.TEXT_CONSTANTS.emptyValue ();
            }
         }
      }, null, false, 20);
      // maximum value
      maximumColumn = new ColumnAppender <String, IDerivedVariableProxy> ().addColumn (derivedCellTable, new TextCell (), TEXT_CONSTANTS.maximumColumn (), null, new ColumnAppender.GetValue <String, IDerivedVariableProxy> () {
         @Override
         public String getValue (IDerivedVariableProxy o) {
            if (o.getMaximum () != null) {
               return TEXT_MESSAGES.value (o.getMaximum (), getPctg (o.getMaximumDeriveBase (), o.getMaximumDeriveExpected ()));
            } else {
               return IClientConstants.TEXT_CONSTANTS.emptyValue ();
            }
         }
      }, null, false, 20);
   }

   protected String getPctg (Integer base, Integer expected) {
      Integer pctg = (base * 100) / expected;
      return String.valueOf (pctg);
   }

   protected void addAltHandler () {
      derivedCellTable.addCellPreviewHandler (new CellPreviewEvent.Handler <IDerivedVariableProxy> () {
         @Override
         public void onCellPreview (CellPreviewEvent <IDerivedVariableProxy> event) {
            try {
               int left = event.getNativeEvent ().getClientX ();
               int top = event.getNativeEvent ().getClientY ();
               
               if ("mouseover".equals (event.getNativeEvent ().getType ())) {
                  if (event.getColumn () == MINIMUM_COLUMN) {
                     showPopup (left, top, getTooltip (event.getValue ().getMinimumDeriveBase (), event.getValue ().getMinimumDeriveIgnored (), event.getValue ().getMinimumDeriveExpected ()));
                  } else if (event.getColumn () == AVERAGE_COLUMN) {
                     showPopup (left, top, getTooltip (event.getValue ().getAverageDeriveBase (), event.getValue ().getAverageDeriveIgnored (), event.getValue ().getAverageDeriveExpected ()));
                  } else if (event.getColumn () == MAXIMUM_COLUMN) {
                     showPopup (left, top, getTooltip (event.getValue ().getMaximumDeriveBase (), event.getValue ().getMaximumDeriveIgnored (), event.getValue ().getMaximumDeriveExpected ()));
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

   private String getTooltip (Integer base, Integer ignored, Integer expected) {
      StringBuilder tooltip = new StringBuilder ();
      tooltip.append (TEXT_MESSAGES.used (String.valueOf (base), String.valueOf (expected), getPctg (base, expected)));
      tooltip.append ("; "); //TODO multiline not picked
      tooltip.append (TEXT_MESSAGES.ignored (String.valueOf (ignored), getPctg (ignored, expected)));
      return tooltip.toString ();
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

   private boolean isBadQuality (IDerivedVariableProxy derived) {
      int totalIgnored = 0;
      totalIgnored += derived.getMinimumDeriveIgnored () != null ? derived.getMinimumDeriveIgnored ().intValue () : 0;
      totalIgnored += derived.getAverageDeriveIgnored () != null ? derived.getAverageDeriveIgnored ().intValue () : 0;
      totalIgnored += derived.getMaximumDeriveIgnored () != null ? derived.getMaximumDeriveIgnored ().intValue () : 0; 
      return totalIgnored > 0;
   }
}

