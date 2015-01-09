package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import es.uned.grc.pfc.meteo.client.place.AbstractPlace;
import es.uned.grc.pfc.meteo.client.view.IMessagesView;

/**
 * Activity (presenter) that acts as a bridge between the view and the server-side
 * services.
 * Manages the logic for message display
 */
public class MessagesActivity extends AbstractBaseActivity {
   
   private IMessagesView messagesView = null;
   
   public MessagesActivity (AbstractPlace place, IMessagesView messagesView, PlaceController placeController) {
      super (placeController, place);

      this.messagesView = messagesView;
   }
   
   @Override
   public void start (AcceptsOneWidget panel, final EventBus eventBus) {
      panel.setWidget (messagesView.asWidget ());
      
      messagesView.getMessagePanel ().clear ();
      messagesView.getMessagePanel ().setStyleName ("");
      
   }
  
}
