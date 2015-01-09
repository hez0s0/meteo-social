package es.uned.grc.pfc.meteo.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

import es.uned.grc.pfc.meteo.client.resource.IApplicationResources;
import es.uned.grc.pfc.meteo.client.util.ClientGlobals;
import es.uned.grc.pfc.meteo.client.util.IStyleConstants;

public class NumberSpinner extends Composite {

   private static IApplicationResources applicationResources = ClientGlobals.getInstance ().getApplicationResources ();
   
   interface ImageLabelUiBinder extends UiBinder <HorizontalPanel, NumberSpinner> {
   }
   private static ImageLabelUiBinder uiBinder = GWT.create (ImageLabelUiBinder.class);
   
   private int rate = 1;
   
   @UiField
   protected HorizontalPanel containerPanel = null;
   @UiField (provided = true)
   protected ValueTextBox valueTextBox = null;
   @UiField (provided = true)
   protected PushButton upPushButton = null;
   @UiField (provided = true)
   protected PushButton downPushButton = null;

   @UiConstructor
   public NumberSpinner (int defaultValue, int min, int max) {
      valueTextBox = new ValueTextBox (defaultValue, min, max);
      upPushButton = new PushButton (new Image (applicationResources.upBullet ().getSafeUri ()));
      downPushButton = new PushButton (new Image (applicationResources.downBullet ().getSafeUri ()));
      
      initWidget (uiBinder.createAndBindUi (this));
      
      containerPanel.setStylePrimaryName (IStyleConstants.SPINNER_CONTAINER);
      valueTextBox.setStylePrimaryName (IStyleConstants.SPINNER_BOX);
      upPushButton.setStylePrimaryName (IStyleConstants.SPINNER_ARROW);
      downPushButton.setStylePrimaryName (IStyleConstants.SPINNER_ARROW);
      
      upPushButton.addClickHandler (new ClickHandler () {
         public void onClick (ClickEvent event) {
            setValue (getValue () + rate);
         }
      });
      downPushButton.addClickHandler (new ClickHandler () {
         public void onClick (ClickEvent event) {
            if (getValue () == 0)
               return;
            setValue (getValue () - rate);
         }
      });
   } //end of NumberSpinner

   /**
    * Returns the value being held.
    */
   public Integer getValue () {
      return valueTextBox.getIntegerValue ();
   }

   /**
    * Sets the value to the control
    */
   public void setValue (Integer value) {
      valueTextBox.setValue (value);
   }

   /**
    * Sets the rate at which increment or decrement is done.
    */
   public void setRate (int rate) {
      this.rate = rate;
   }
} //end of NumberSpinner
