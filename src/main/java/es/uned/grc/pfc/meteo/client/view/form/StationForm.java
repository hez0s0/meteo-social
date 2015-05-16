package es.uned.grc.pfc.meteo.client.view.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import es.uned.grc.pfc.meteo.client.model.IParameterProxy;
import es.uned.grc.pfc.meteo.client.model.IStationModelProxy;
import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.model.IStationVariableProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.base.AbstractFormView;
import es.uned.grc.pfc.meteo.client.view.base.IEditNotifiableWidget;
import es.uned.grc.pfc.meteo.client.view.base.IValidatableForm;
import es.uned.grc.pfc.meteo.client.view.base.IView;
import es.uned.grc.pfc.meteo.client.view.table.ParameterTableBuilder;
import es.uned.grc.pfc.meteo.client.view.table.StationVariableTableBuilder;
import es.uned.grc.pfc.meteo.client.view.util.DisplayUtils;
import es.uned.grc.pfc.meteo.client.view.util.FormUtils;
import es.uned.grc.pfc.meteo.client.view.widget.ExtendedValueListBox;
import es.uned.grc.pfc.meteo.client.view.widget.NumberTextBox;

public class StationForm  extends Composite implements IView, IEditNotifiableWidget, IValidatableForm {
   interface FormUiBinder extends UiBinder <HTMLPanel, StationForm> {
   }
   private static FormUiBinder uiBinder = GWT.create (FormUiBinder.class);
   
   public class StationModelRenderer implements Renderer <IStationModelProxy> {
      @Override
      public String render (IStationModelProxy stationModel) {
         StringBuilder builder = new StringBuilder ();
         if (stationModel == null) {
            builder.append (IClientConstants.TEXT_CONSTANTS.emptyValue ());
         } else {
            builder.append (stationModel.getName ());
            if (!PortableStringUtils.isEmpty (stationModel.getDescription ())) {
               builder.append (" (");
               builder.append (stationModel.getDescription ());
               builder.append (")");
            }
         }
         return builder.toString ();
      }
      @Override
      public void render (IStationModelProxy stationModel, Appendable appendable) throws IOException {
         appendable.append (render (stationModel));
      }
   };
   public static final ProvidesKey <IStationModelProxy> stationModelKeyProvider = new ProvidesKey <IStationModelProxy> () {
      @Override
      public Object getKey (IStationModelProxy stationModel) {
         return (stationModel != null) ? stationModel.getId () : null;
      }
   };
   public static final ProvidesKey <IParameterProxy> paramKeyProvider = new ProvidesKey <IParameterProxy> () {
      @Override
      public Object getKey (IParameterProxy parameter) {
         return (parameter != null) ? parameter.getId () : null;
      }
   };
   public static final ProvidesKey <IStationVariableProxy> variableKeyProvider = new ProvidesKey <IStationVariableProxy> () {
      @Override
      public Object getKey (IStationVariableProxy stationVariable) {
         return (stationVariable != null) ? stationVariable.getId () : null;
      }
   };

   protected IRequestFactory requestFactory = null;
   protected RequestContext requestContext = null;
   protected EventBus eventBus = null;
   protected IStationProxy station = null;
   protected Map <String, SpanElement> propertySpanMap = new HashMap <String, SpanElement> ();

   @UiField (provided = true)
   protected ExtendedValueListBox <IStationModelProxy> stationModelListBox = new ExtendedValueListBox <IStationModelProxy> (new StationModelRenderer (), stationModelKeyProvider);
   @UiField
   protected TextBox nameTextBox = null;
   @UiField
   protected NumberTextBox latitudeNumberTextBox = null;
   @UiField
   protected NumberTextBox longitudeNumberTextBox = null;
   @UiField
   protected NumberTextBox heightNumberTextBox = null;
   @UiField
   protected TextBox streetTextBox = null;
   @UiField
   protected TextBox zipTextBox = null;
   @UiField
   protected TextBox cityTextBox = null;
   @UiField
   protected TextBox countryTextBox = null;
   @UiField (provided = true)
   protected CellTable <IParameterProxy> parameterCellTable = new CellTable <IParameterProxy> (Integer.MAX_VALUE);
   @UiField (provided = true)
   protected CellTable <IStationVariableProxy> variableCellTable = new CellTable <IStationVariableProxy> (Integer.MAX_VALUE);

   // NOW COME THE VALIDATION DISPLAY SPANS
   @UiField
   protected SpanElement nameSpan = null;
   @UiField
   protected SpanElement latitudeSpan = null;
   @UiField
   protected SpanElement longitudeSpan = null;
   @UiField
   protected SpanElement heightSpan = null;
   
   public StationForm () {
      SelectionModel <IParameterProxy> paramSelectionModel = null;
      SelectionModel <IStationVariableProxy> variableSelectionModel = null;
      initWidget (uiBinder.createAndBindUi (this));
      
      propertySpanMap.put ("name", nameSpan);
      propertySpanMap.put ("latitude", latitudeSpan);
      propertySpanMap.put ("longitude", longitudeSpan);
      propertySpanMap.put ("height", heightSpan);

      // Add a selection model so we can select cells
      paramSelectionModel = new MultiSelectionModel <IParameterProxy> (paramKeyProvider);
      parameterCellTable.setSelectionModel (paramSelectionModel, DefaultSelectionEventManager.<IParameterProxy> createCheckboxManager ());
      parameterCellTable.setTableBuilder (new ParameterTableBuilder (parameterCellTable));
      
      variableSelectionModel = new MultiSelectionModel <IStationVariableProxy> (variableKeyProvider);
      variableCellTable.setSelectionModel (variableSelectionModel, DefaultSelectionEventManager.<IStationVariableProxy> createCheckboxManager ());
      variableCellTable.setTableBuilder (new StationVariableTableBuilder (variableCellTable));
   }

   protected void init (IStationProxy station,
                        IRequestFactory requestFactory, 
                        RequestContext requestContext, 
                        PlaceController placeController,
                        EventBus eventBus) {
      this.requestFactory = requestFactory;
      this.requestContext = requestContext;
      this.station = station;
      this.eventBus = eventBus;
   }
   
   public void setInput (IStationProxy station, IRequestFactory requestFactory, RequestContext requestContext, PlaceController placeController, EventBus eventBus) {
      init (station, requestFactory, requestContext, placeController, eventBus);
      
      loadListBoxes (station);
      
      display (station);
   }
   
   private void loadListBoxes (final IStationProxy station) {
      requestFactory.getStationContext ().getStationModels ().fire (new Receiver <List <IStationModelProxy>> () {
         @Override
         public void onSuccess (List <IStationModelProxy> response) {
            response.add (0, null);
            stationModelListBox.setAcceptableValues (response);
            stationModelListBox.setValue (station.getStationModel ());
         }
      });  
   }

   @Override
   public void notifyEditMode (EditorMode editorMode) {
      FormUtils.notifyEditMode (editorMode, stationModelListBox);
      FormUtils.notifyEditMode (editorMode, nameTextBox);
      FormUtils.notifyEditMode (editorMode, latitudeNumberTextBox);
      FormUtils.notifyEditMode (editorMode, longitudeNumberTextBox);
      FormUtils.notifyEditMode (editorMode, heightNumberTextBox);
      FormUtils.notifyEditMode (editorMode, streetTextBox);
      FormUtils.notifyEditMode (editorMode, zipTextBox);
      FormUtils.notifyEditMode (editorMode, cityTextBox);
      FormUtils.notifyEditMode (editorMode, countryTextBox);
   }

   public void display (IStationProxy station) {
      List <IStationVariableProxy> stationVariables = getMeasured (station.getStationVariables ());
      nameTextBox.setText (station.getName ());
      latitudeNumberTextBox.setDoubleValue (station.getLatitude ());
      longitudeNumberTextBox.setDoubleValue (station.getLongitude ());
      heightNumberTextBox.setDoubleValue ((double) station.getHeight ());
      streetTextBox.setText (station.getStreet ());
      zipTextBox.setText (station.getZip ());
      cityTextBox.setText (station.getCity ());
      countryTextBox.setText (station.getCountry ());
      
      parameterCellTable.setRowCount (0);
      parameterCellTable.setRowCount (station.getParameters ().size ());
      parameterCellTable.setRowData (0, new ArrayList <IParameterProxy> (station.getParameters ()));
      
      variableCellTable.setRowCount (0);
      variableCellTable.setRowCount (stationVariables.size ());
      variableCellTable.setRowData (0, stationVariables);
   }

   private List <IStationVariableProxy> getMeasured (Set <IStationVariableProxy> stationVariables) {
      List <IStationVariableProxy> result = new ArrayList <IStationVariableProxy> ();
      
      for (IStationVariableProxy stationVariable : stationVariables) {
         if (!stationVariable.getVariable ().getInternal ()) {
            result.add (stationVariable);
         }
      }
      
      return result;
   }

   @Override
   public void addPathToSpanMap (String prefix, Map <String, SpanElement> pathToSpanMap, Map <String, DisclosurePanel> disclosurePanels) {
      pathToSpanMap.put (AbstractFormView.getPropertyPath (prefix, "name"), nameSpan);
      pathToSpanMap.put (AbstractFormView.getPropertyPath (prefix, "latitude"), latitudeSpan);
      pathToSpanMap.put (AbstractFormView.getPropertyPath (prefix, "longitude"), longitudeSpan);
      pathToSpanMap.put (AbstractFormView.getPropertyPath (prefix, "height"), heightSpan);
   }
   
   /**
    * Composes the entity from the fields.
    */
   public IStationProxy getEntity (IStationProxy station) {
      station.setStationModel (stationModelListBox.getValue ());
      station.setName (nameTextBox.getText ());
      station.setLatitude (latitudeNumberTextBox.getDoubleValue ());
      station.setLongitude (longitudeNumberTextBox.getDoubleValue ());
      station.setHeight (heightNumberTextBox.getDoubleValue () != null ? heightNumberTextBox.getDoubleValue ().intValue () : 0);
      station.setStreet (streetTextBox.getText ());
      station.setZip (zipTextBox.getText ());
      station.setCity (cityTextBox.getText ());
      station.setCountry (countryTextBox.getText ());
      
      return station;
   }
   
   public void displayServerValidationError (ConstraintViolation <?> violation) {
      DisplayUtils.showSpan (propertySpanMap.get (violation.getPropertyPath ().toString ()), violation.getMessage ());
   }

   public void clearServerErrors () {
      DisplayUtils.clearErrors (propertySpanMap);
   }
   
   public void addDataFields (List <HasValueChangeHandlers <?>> dataFields) {
      dataFields.add (nameTextBox);
   }
}
