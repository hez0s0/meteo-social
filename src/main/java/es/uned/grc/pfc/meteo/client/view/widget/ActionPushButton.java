package es.uned.grc.pfc.meteo.client.view.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.view.base.IActionHandler;
import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;
import es.uned.grc.pfc.meteo.client.view.base.MultipleHandlerRegistration;

public class ActionPushButton extends Composite implements IHasActionHandlers, HasAllMouseHandlers {
   
   interface ActionPushButtonUiBinder extends UiBinder <FocusPanel, ActionPushButton> {
   }
   private static ActionPushButtonUiBinder uiBinder = GWT.create (ActionPushButtonUiBinder.class);

   @UiField
   protected FocusPanel focusPanel = null;
   @UiField (provided = true)
   protected PushButton pushButton = null;
   
   protected boolean processing = false;
   
   public ActionPushButton (Image image) {
      pushButton = new PushButton (image);
      
      initWidget (uiBinder.createAndBindUi (this));
   }

   @Override
   public HandlerRegistration addActionHandler (final IActionHandler handler) {
      List <HandlerRegistration> handlerRegistrations = new ArrayList <HandlerRegistration> (2);
      
      processing = false;
      
      handlerRegistrations.add (focusPanel.addClickHandler (new ClickHandler() {
         @Override
         public void onClick (ClickEvent event) {
            if ( (!processing) && (pushButton.isEnabled ()) ) {
               processing = true;
               handler.onAction (event, ActionPushButton.this);
            }
         }
      }));
      
      handlerRegistrations.add (focusPanel.addKeyUpHandler (new KeyUpHandler() {
         @Override
         public void onKeyUp (KeyUpEvent event) {
            if ( (!processing) && (pushButton.isEnabled ()) ) {
               int keyCode = event.getNativeEvent ().getKeyCode ();
   
               for (int actionKeyCode : IClientConstants.ACTION_KEY_CODES) {
                  if (keyCode == actionKeyCode) {
                     processing = true;
                     handler.onAction (event, ActionPushButton.this);
                     break;
                  }
               }
            }
         } //end of onKeyPress
      }));
      
      return new MultipleHandlerRegistration (handlerRegistrations);
   }

   @Override
   public void setCompleted () {
      processing = false;
   }

   @Override
   public HandlerRegistration addMouseDownHandler (MouseDownHandler handler) {
      return focusPanel.addMouseDownHandler (handler);
   }

   @Override
   public HandlerRegistration addMouseUpHandler (MouseUpHandler handler) {
      return focusPanel.addMouseUpHandler (handler);
   }

   @Override
   public HandlerRegistration addMouseOutHandler (MouseOutHandler handler) {
      return focusPanel.addMouseOutHandler (handler);
   }

   @Override
   public HandlerRegistration addMouseOverHandler (MouseOverHandler handler) {
      return focusPanel.addMouseOverHandler (handler);
   }

   @Override
   public HandlerRegistration addMouseMoveHandler (MouseMoveHandler handler) {
      return focusPanel.addMouseMoveHandler (handler);
   }

   @Override
   public HandlerRegistration addMouseWheelHandler (MouseWheelHandler handler) {
      return focusPanel.addMouseWheelHandler (handler);
   }

   public void setEnabled (boolean enabled) {
      pushButton.setEnabled (enabled);
   }
}
