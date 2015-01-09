package es.uned.grc.pfc.meteo.client.view.widget.dialog;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;

import es.uned.grc.pfc.meteo.client.request.IRequestFactory;

public class MessageDialogBox extends ActionDialogBox {
   interface MessageDialogBoxUiBinder extends UiBinder <HTMLPanel, MessageDialogBox> {
   }
   private static MessageDialogBoxUiBinder uiBinder = GWT.create (MessageDialogBoxUiBinder.class);
   
   protected IRequestFactory requestFactory = null;

   @UiField
   protected UListElement messagesUnorderedList = null;
   
   public MessageDialogBox (String title, List <String> messages) {
      
      setWidget (uiBinder.createAndBindUi (this));

      super.getCaption ().setText (title);
      
      for (String message : messages) {
         addMessage (message);
      }
   }
   
   private void addMessage (String message) {
      Element messageListElement = DOM.createElement ("LI");
      
      messageListElement.setInnerText (message);
      messagesUnorderedList.appendChild (messageListElement);
   }

   public static MessageDialogBox showDialog (String title, List <String> messages) {
      MessageDialogBox dialogBox = new MessageDialogBox (title, messages);

      return (MessageDialogBox) ActionDialogBox.showDialog (dialogBox);
   }
}
