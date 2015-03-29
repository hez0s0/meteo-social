package es.uned.grc.pfc.meteo.client.view.form;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
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

import es.uned.grc.pfc.meteo.client.model.IDerivedRangeProxy;
import es.uned.grc.pfc.meteo.client.model.IDerivedVariableProxy;
import es.uned.grc.pfc.meteo.client.model.IVariableProxy;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.client.view.util.ChartUtils;
import es.uned.grc.pfc.meteo.client.view.util.ColumnAppender;
import es.uned.grc.pfc.meteo.shared.ISharedConstants.DerivedRangeType;

public class DerivedRangePanel extends Composite {
   interface uiBinder extends UiBinder <VerticalPanel, DerivedRangePanel> {
   }
   private static uiBinder uiBinder = GWT.create (uiBinder.class);

   public static final ProvidesKey <IDerivedVariableProxy> keyProvider = new ProvidesKey <IDerivedVariableProxy> () {
      @Override
      public Object getKey (IDerivedVariableProxy derivedVariableProxy) {
         return (derivedVariableProxy != null) ? derivedVariableProxy.getVariable ().getId () : null;
      }
   };
   
   /** text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Observed") @Meaning ("Derived observation column")
      String observedColumn ();
      @DefaultStringValue ("Minimum") @Meaning ("Derived observation column")
      String minimumColumn ();
      @DefaultStringValue ("Average") @Meaning ("Derived observation column")
      String averageColumn ();
      @DefaultStringValue ("Maximum") @Meaning ("Derived observation column")
      String maximumColumn ();
   }
   public static TextConstants TEXT_CONSTANTS = GWT.create (TextConstants.class);
   
   @UiField
   protected Label titleLabel = null;
   @UiField (provided = true)
   protected CellTable <IDerivedVariableProxy> observationCellTable = new CellTable <IDerivedVariableProxy> (Integer.MAX_VALUE);
   @UiField
   protected HorizontalPanel tablePanel = null;
   @UiField
   protected VerticalPanel graphPanel = null;
   
   private DerivedRangeType derivedRangeType = null;
   private IDerivedRangeProxy derivedRange = null;
   
   @UiConstructor
   public DerivedRangePanel () {
      SelectionModel <IDerivedVariableProxy> selectionModel = null;
      
      initWidget (uiBinder.createAndBindUi (this));
      
      tablePanel.setVisible (false);
      graphPanel.setVisible (false);
      initTableColumns ();

      // Add a selection model so we can select cells
      selectionModel = new MultiSelectionModel <IDerivedVariableProxy> (keyProvider);
      observationCellTable.setSelectionModel (selectionModel, DefaultSelectionEventManager.<IDerivedVariableProxy> createCheckboxManager ());
   }

   public void setInput (DerivedRangeType derivedRangeType, IDerivedRangeProxy derivedRange) {
      this.derivedRangeType = derivedRangeType;
      this.derivedRange = derivedRange;
      
      tablePanel.setVisible (true);
      
      DateTimeFormat df = DateTimeFormat.getFormat (PredefinedFormat.DATE_TIME_SHORT);
      titleLabel.setText (derivedRangeType.toString () + " (" + df.format (derivedRange.getIni ()) + "-" + df.format (derivedRange.getEnd ()) + ")");

      observationCellTable.setRowCount (0);
      observationCellTable.setRowCount (derivedRange.getDerivedVariables ().size ());
      observationCellTable.setRowData (0, derivedRange.getDerivedVariables ());
   }

   public void setInput (DerivedRangeType derivedRangeType, final List <IDerivedRangeProxy> observations) {
      this.derivedRangeType = derivedRangeType;
      
      graphPanel.setVisible (true);
      
      DateTimeFormat df = DateTimeFormat.getFormat (PredefinedFormat.DATE_TIME_SHORT);
      titleLabel.setText (derivedRangeType.toString () + " (" + df.format (observations.get (0).getIni ()) + "-" + df.format (observations.get (0).getEnd ()) + ")");
      
      Runnable onLoadCallback = new Runnable () {
         public void run () {
            int i = 0;
            List <IVariableProxy> variables = null;
            HorizontalPanel horizontalPanel = GWT.create (HorizontalPanel.class);
            Widget chart = null;
            variables = extractVariables (observations);
            for (IVariableProxy variable : variables) {
               AbstractDataTable data = createDataTable (observations, variable);
               switch (variable.getGraphType ()) {
                  case AREA:
                     AreaChart.Options areaOptions = ChartUtils.createAreaOptions (variable);
                     chart = new AreaChart (data, areaOptions);
                     break;
                  case BAR:
                     BarChart.Options barOptions = ChartUtils.createBarOptions (variable);
                     chart = new BarChart (data, barOptions);
                     break;
                  case COLUMN:
                     ColumnChart.Options columnOptions = ChartUtils.createColumnOptions (variable);
                     chart = new ColumnChart (data, columnOptions);
                     break;
                  case LINE:
                     LineChart.Options lineOptions = ChartUtils.createLineOptions (variable);
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

   private List <IVariableProxy> extractVariables (List <IDerivedRangeProxy> observations) {
      List <IVariableProxy> result = new ArrayList <IVariableProxy> ();
      if (!observations.isEmpty ()) {
         for (IDerivedVariableProxy derivedVariable : observations.get (0).getDerivedVariables ()) {
            result.add (derivedVariable.getVariable ());
         }
      }
      return result;
   }
   
   private AbstractDataTable createDataTable (List <IDerivedRangeProxy> observations, IVariableProxy variable) {
      int row = 0;
      DateTimeFormat representationDateFormat = DateTimeFormat.getFormat (PredefinedFormat.DATE_TIME_SHORT);
      DataTable data = DataTable.create ();
      data.addColumn (ColumnType.STRING, TEXT_CONSTANTS.observedColumn ());
      data.addColumn (ColumnType.NUMBER, TEXT_CONSTANTS.minimumColumn ());
      data.addColumn (ColumnType.NUMBER, TEXT_CONSTANTS.averageColumn ());
      data.addColumn (ColumnType.NUMBER, TEXT_CONSTANTS.maximumColumn ());
      data.addRows (observations.size ());
      for (IDerivedRangeProxy derivedRange : observations) {
         for (IDerivedVariableProxy derivedVariable : derivedRange.getDerivedVariables ()) {
            if (derivedVariable.getVariable ().getId ().equals (variable.getId ())) {
               data.setValue (row, 0, representationDateFormat.format (derivedVariable.getDisplayDate ()));
               data.setValue (row, 1, derivedVariable.getMinimum ());
               data.setValue (row, 2, derivedVariable.getAverage ());
               data.setValue (row ++, 3, derivedVariable.getMaximum ());
            }
         }
      }
      return data;
   }

   /**
    * Add the columns to the table.
    */
   public void initTableColumns () {
      // name of the variable
      new ColumnAppender <String, IDerivedVariableProxy> ().addColumn (observationCellTable, new TextCell (), "Variable", null, new ColumnAppender.GetValue <String, IDerivedVariableProxy> () {
         @Override
         public String getValue (IDerivedVariableProxy o) {
            return o.getVariable ().getName ();
         }
      }, null, false, 40);
      // minimum value
      new ColumnAppender <String, IDerivedVariableProxy> ().addColumn (observationCellTable, new TextCell (), "Minimum", null, new ColumnAppender.GetValue <String, IDerivedVariableProxy> () {
         @Override
         public String getValue (IDerivedVariableProxy o) {
            if (o.getMinimum () != null) {
               return PortableStringUtils.format ("%s (%s used, %s ignored, %s expected)", o.getMinimum (), o.getMinimumDeriveBase (), o.getMinimumDeriveIgnored (), o.getMinimumDeriveExpected ());
            } else {
               return IClientConstants.TEXT_CONSTANTS.emptyValue ();
            }
         }
      }, null, false, 20);
      // average value
      new ColumnAppender <String, IDerivedVariableProxy> ().addColumn (observationCellTable, new TextCell (), "Average", null, new ColumnAppender.GetValue <String, IDerivedVariableProxy> () {
         @Override
         public String getValue (IDerivedVariableProxy o) {
            if (o.getAverage () != null) {
               return PortableStringUtils.format ("%s (%s used, %s ignored, %s expected)", o.getAverage (), o.getAverageDeriveBase (), o.getAverageDeriveIgnored (), o.getAverageDeriveExpected ());
            } else {
               return IClientConstants.TEXT_CONSTANTS.emptyValue ();
            }
         }
      }, null, false, 20);
      // maximum value
      new ColumnAppender <String, IDerivedVariableProxy> ().addColumn (observationCellTable, new TextCell (), "Maximum", null, new ColumnAppender.GetValue <String, IDerivedVariableProxy> () {
         @Override
         public String getValue (IDerivedVariableProxy o) {
            if (o.getMaximum () != null) {
               return PortableStringUtils.format ("%s (%s used, %s ignored, %s expected)", o.getMaximum (), o.getMaximumDeriveBase (), o.getMaximumDeriveIgnored (), o.getMaximumDeriveExpected ());
            } else {
               return IClientConstants.TEXT_CONSTANTS.emptyValue ();
            }
         }
      }, null, false, 20);
   }

   public DerivedRangeType getDerivedRangeType () {
      return derivedRangeType;
   }

   public IDerivedRangeProxy getDerivedRange () {
      return derivedRange;
   }
}