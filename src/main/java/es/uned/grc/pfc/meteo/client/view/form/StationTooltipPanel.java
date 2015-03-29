package es.uned.grc.pfc.meteo.client.view.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import es.uned.grc.pfc.meteo.client.model.IObservationProxy;
import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;
import es.uned.grc.pfc.meteo.client.util.IStyleConstants;

public class StationTooltipPanel extends Composite {
   interface uiBinder extends UiBinder <HTMLPanel, StationTooltipPanel> {
   }
   private static uiBinder uiBinder = GWT.create (uiBinder.class);

   /** text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Observed") @Meaning ("Observation column at station tooltip")
      String observedColumn ();
      @DefaultStringValue ("Received") @Meaning ("Observation column at station tooltip")
      String receivedColumn ();
      @DefaultStringValue ("Variable") @Meaning ("Observation column at station tooltip")
      String variableColumn ();
      @DefaultStringValue ("Value") @Meaning ("Observation column at station tooltip")
      String valueColumn ();
      @DefaultStringValue ("Quality") @Meaning ("Observation column at station tooltip")
      String qualityColumn ();
      @DefaultStringValue ("Good") @Meaning ("Quality value at station tooltip")
      String qualityOk ();
      @DefaultStringValue ("Suspicious") @Meaning ("Quality value at station tooltip")
      String qualityKo ();
      @DefaultStringValue ("Unknown") @Meaning ("Quality value at station tooltip")
      String qualityUnkwown ();
      @DefaultStringValue ("º") @Meaning ("Latitude/longitude unit")
      String latlongUnit ();
      @DefaultStringValue ("m.") @Meaning ("Height unit")
      String heightUnit ();
   }
   public static TextConstants TEXT_CONSTANTS = GWT.create (TextConstants.class);

   @UiField
   protected HTMLPanel mainPanel = null;
   @UiField
   protected Label titleLabel = null;
   @UiField
   protected Label modelLabel = null;
   @UiField
   protected Label latitudeLabel = null;
   @UiField
   protected Label longitudeLabel = null;
   @UiField
   protected Label heightLabel = null;
   @UiField
   protected Label noResultsLabel = null;
   @UiField
   protected Label viewAllLabel = null;
   @UiField
   protected VerticalPanel observationsPanel = null;
   @UiField (provided = true)
   protected FlexTable observationFlexTable = new FlexTable ();
   
   @UiConstructor
   public StationTooltipPanel () {
      initWidget (uiBinder.createAndBindUi (this));
   }

   public void setInput (final IStationProxy station, final PlaceController placeController) {
      int row = 0;
      int col = 0;
      DateTimeFormat dateFormat = DateTimeFormat.getFormat (PredefinedFormat.DATE_TIME_SHORT);
      
      //display station properties
      titleLabel.setText (station.getName ());
      modelLabel.setText (station.getStationModel ().getName ());
      
      latitudeLabel.setText (String.valueOf (station.getLatitude ()) + TEXT_CONSTANTS.latlongUnit ());
      longitudeLabel.setText (String.valueOf (station.getLongitude ()) + TEXT_CONSTANTS.latlongUnit ());
      heightLabel.setText (String.valueOf (station.getHeight ()) + TEXT_CONSTANTS.heightUnit ());
      
      //display last observations
      if (station.getTransientLastObservations ().isEmpty ()) {
         noResultsLabel.setVisible (true);
         viewAllLabel.setVisible (false);
         observationFlexTable.setVisible (false);
      } else {
         noResultsLabel.setVisible (false);
         observationFlexTable.setVisible (true);
         viewAllLabel.setVisible (false); //TODO the click handler does not work, thus the link remains hidden
         viewAllLabel.addClickHandler (new ClickHandler () {
            @Override
            public void onClick (ClickEvent event) {
               placeController.goTo (new ObservationListPlace (ObservationListPlace.ObservationType.NORMAL, ObservationListPlace.Representation.GRAPHIC, station.getId ()));
            }
         });
         
         observationFlexTable.getRowFormatter ().addStyleName (0, IStyleConstants.TABLE_HEADER);
         observationFlexTable.setText (row, col ++, TEXT_CONSTANTS.variableColumn ());
         observationFlexTable.setText (row, col ++, TEXT_CONSTANTS.valueColumn ());
         observationFlexTable.setText (row, col ++, TEXT_CONSTANTS.observedColumn ());
         observationFlexTable.setText (row, col ++, TEXT_CONSTANTS.receivedColumn ());
         observationFlexTable.setText (row ++, col ++, TEXT_CONSTANTS.qualityColumn ());
         for (IObservationProxy observation : station.getTransientLastObservations ()) {
            col = 0;

            observationFlexTable.insertRow (row);  
            observationFlexTable.setText (row, col ++, observation.getVariable ().getAcronym ());
            observationFlexTable.setText (row, col ++, observation.getValue ());
            observationFlexTable.setText (row, col ++, dateFormat.format (observation.getObserved ()));
            observationFlexTable.setText (row, col ++, dateFormat.format (observation.getReceived ()));
            if (observation.getQuality () != null) {
               observationFlexTable.setText (row ++, col ++, observation.getQuality () ? TEXT_CONSTANTS.qualityOk () : TEXT_CONSTANTS.qualityKo ());
            } else {
               observationFlexTable.setText (row ++, col ++, TEXT_CONSTANTS.qualityUnkwown ());
            }
         }
      }
   }
   
   public String getHtml () {
      return mainPanel.getElement ().getInnerHTML ();
   }
   
   public Label getViewAll () {
      return viewAllLabel;
   }
}
