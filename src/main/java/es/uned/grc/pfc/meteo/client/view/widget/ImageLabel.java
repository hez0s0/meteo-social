package es.uned.grc.pfc.meteo.client.view.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.base.IActionHandler;
import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;
import es.uned.grc.pfc.meteo.client.view.base.MultipleHandlerRegistration;

/**
 * A clickable label with an image, for visual action display.
 */
// if click events (instead of action) should be allowed, uncomment HasClickHandlers and addClickHandler */
public class ImageLabel extends Composite implements HasText, IHasActionHandlers/*, HasClickHandlers*/  {
   interface ImageLabelUiBinder extends UiBinder <Widget, ImageLabel> {
   }
   private static ImageLabelUiBinder uiBinder = GWT.create (ImageLabelUiBinder.class);

   @UiField
   protected FocusPanel containerPanel = null;
   @UiField
   protected Image iconImage = null;
   @UiField
   protected Label textLabel = null;
   
   protected boolean processing = false;
   
   @UiConstructor
   public ImageLabel (String imageUrl) {
      initWidget (uiBinder.createAndBindUi (this));
      iconImage.setUrl (imageUrl);
   } //end of ImageLabel

   @Override
   public HandlerRegistration addActionHandler (final IActionHandler handler) {
      List <HandlerRegistration> handlerRegistrations = new ArrayList <HandlerRegistration> (2);
      
      processing = false;
      
      handlerRegistrations.add (containerPanel.addClickHandler (new ClickHandler() {
         @Override
         public void onClick (ClickEvent event) {
            if (!processing) {
               processing = true;
               handler.onAction (event, ImageLabel.this);
            }
         }
      }));
      
      handlerRegistrations.add (containerPanel.addKeyUpHandler (new KeyUpHandler() {
         @Override
         public void onKeyUp (KeyUpEvent event) {
            if (!processing) {
               int keyCode = event.getNativeEvent ().getKeyCode ();
   
               for (int actionKeyCode : IClientConstants.ACTION_KEY_CODES) {
                  if (keyCode == actionKeyCode) {
                     processing = true;
                     handler.onAction (event, ImageLabel.this);
                     break;
                  }
               }
            }
         } //end of onKeyPress
      }));
      
      return new MultipleHandlerRegistration (handlerRegistrations);
   } //end of addActionHandler

   @Override
   public void setCompleted () {
      processing = false;
   }

   @Override
   public String getText () {
      return textLabel.getText ();
   }

   @Override
   public void setText (String text) {
      textLabel.setText (text);
   }

   public void notifyEditMode (EditorMode editorMode) {
      if ( (containerPanel != null) && (editorMode != null) ){
         containerPanel.setVisible (!editorMode.equals (EditorMode.VIEW));
      }
   }
   
   @UiChild (tagname = "styleName")
   public void setStyleName (String style) {
      containerPanel.setStyleName (style);
   }
} //end of ImageLabel
