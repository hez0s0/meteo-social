package es.uned.grc.pfc.meteo.client.view.form;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;

import es.uned.grc.pfc.meteo.client.model.IDerivedRangeProxy;
import es.uned.grc.pfc.meteo.client.model.IDerivedVariableProxy;
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
   
   @UiField
   protected Label title = null;
   @UiField (provided = true)
   protected DataGrid <IDerivedVariableProxy> observationDataGrid = new DataGrid <IDerivedVariableProxy> (Integer.MAX_VALUE);
   
   @UiConstructor
   public DerivedRangePanel () {
      SelectionModel <IDerivedVariableProxy> selectionModel = null;
      
      initWidget (uiBinder.createAndBindUi (this));
      
      initTableColumns ();

      // Add a selection model so we can select cells
      selectionModel = new MultiSelectionModel <IDerivedVariableProxy> (keyProvider);
      observationDataGrid.setSelectionModel (selectionModel, DefaultSelectionEventManager.<IDerivedVariableProxy> createCheckboxManager ());
   }

   public void setInput (DerivedRangeType derivedRangeType, IDerivedRangeProxy derivedRange) {
      DateTimeFormat df = DateTimeFormat.getFormat (PredefinedFormat.DATE_TIME_SHORT);
      title.setText (derivedRangeType.toString () + " (" + df.format (derivedRange.getIni ()) + "-" + df.format (derivedRange.getEnd ()));

      observationDataGrid.setRowCount (0);
      observationDataGrid.setRowCount (derivedRange.getDerivedVariables ().size ());
      observationDataGrid.setRowData (0, derivedRange.getDerivedVariables ());
   }
   
   /**
    * Add the columns to the table.
    */
   public void initTableColumns () {
      // name of the variable
      new ColumnAppender <String, IDerivedVariableProxy> ().addColumn (observationDataGrid, new TextCell (), "Variable", null, new ColumnAppender.GetValue <String, IDerivedVariableProxy> () {
         @Override
         public String getValue (IDerivedVariableProxy o) {
            return o.getVariable ().getName ();
         }
      }, null, false, 40);
      // minimum value
      new ColumnAppender <String, IDerivedVariableProxy> ().addColumn (observationDataGrid, new TextCell (), "Minimum", null, new ColumnAppender.GetValue <String, IDerivedVariableProxy> () {
         @Override
         public String getValue (IDerivedVariableProxy o) {
            return o.getMinimum ();
         }
      }, null, false, 20);
      // average value
      new ColumnAppender <String, IDerivedVariableProxy> ().addColumn (observationDataGrid, new TextCell (), "Average", null, new ColumnAppender.GetValue <String, IDerivedVariableProxy> () {
         @Override
         public String getValue (IDerivedVariableProxy o) {
            return o.getAverage ();
         }
      }, null, false, 20);
      // maximum value
      new ColumnAppender <String, IDerivedVariableProxy> ().addColumn (observationDataGrid, new TextCell (), "Maximum", null, new ColumnAppender.GetValue <String, IDerivedVariableProxy> () {
         @Override
         public String getValue (IDerivedVariableProxy o) {
            return o.getMaximum ();
         }
      }, null, false, 20);
   }
}
