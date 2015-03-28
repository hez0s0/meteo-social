package es.uned.grc.pfc.meteo.client.view.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.requestfactory.shared.Receiver;

import es.uned.grc.pfc.meteo.client.model.IDerivedVariableProxy;
import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;

public class StationTooltipPanel extends Composite {
   interface uiBinder extends UiBinder <HTMLPanel, StationTooltipPanel> {
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
   protected HTMLPanel mainPanel = null;
   @UiField
   protected Label titleLabel = null;
   @UiField
   protected Label latitudeLabel = null;
   @UiField
   protected Label longitudeLabel = null;
   @UiField
   protected VerticalPanel observationsPanel = null;
   
   @UiConstructor
   public StationTooltipPanel () {
      initWidget (uiBinder.createAndBindUi (this));
   }

   public void setInput (int stationId, IRequestFactory requestFactory, EventBus eventBus) {
      requestFactory.getStationContext ().getStationById (stationId)
                                         .fire (new Receiver <IStationProxy> () {
         @Override
         public void onSuccess (IStationProxy response) {
            setInput (response);
         }
      });
   }

   public void setInput (IStationProxy station) {
      titleLabel.setText (station.getName ());
      
      latitudeLabel.setText (String.valueOf (station.getLatitude ()));
      longitudeLabel.setText (String.valueOf (station.getLongitude ()));
   }
   
   public String getHtml () {
      return mainPanel.getElement ().getInnerHTML ();
   }
}
