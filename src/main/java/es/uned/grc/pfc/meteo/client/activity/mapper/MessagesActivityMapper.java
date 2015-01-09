package es.uned.grc.pfc.meteo.client.activity.mapper;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.activity.MessagesActivity;
import es.uned.grc.pfc.meteo.client.event.IMessageChangeEventHandler;
import es.uned.grc.pfc.meteo.client.event.MessageChangeEvent;
import es.uned.grc.pfc.meteo.client.place.AbstractPlace;
import es.uned.grc.pfc.meteo.client.util.IStyleConstants;
import es.uned.grc.pfc.meteo.client.view.IMessagesView;

/**
 * Mapper for the message display region of the screen.
 * Responsible both for mapping the Places to the correct Activities,
 * and to listen for events that may trigger Place changes, such as
 * creation of new elements. Those events are considered out of scope
 * of one single activity, and thus are managed here.
 */
public class MessagesActivityMapper implements ActivityMapper {

   private static Logger log = Logger.getLogger(""); 
   
   @Inject
   private PlaceController placeController = null;
   @Inject
   private EventBus eventBus = null;

   @Inject
   private IMessagesView messagesView = null;

   private Boolean init = false;
   
   @Inject
   public MessagesActivityMapper () {
   }
   
   @Override
   public Activity getActivity (Place place) {
      if (!init) {
         bind ();
         init = true;
      }
      
      return new MessagesActivity ((AbstractPlace) place, messagesView, placeController);
   }

   /**
    * Wire all click handlers for singleton views here
    */
   private void bind () {
      //display a given message
      eventBus.addHandler (MessageChangeEvent.TYPE, new IMessageChangeEventHandler() {

         @Override
         public void onMessageChange (MessageChangeEvent event) {
            String styleName = null;
            HTMLPanel htmlPanel = messagesView.getMessagePanel ();
            
            // log the event
            logEvent (event);

            // remove whatever was there
            htmlPanel.clear ();
            
            // display message or messages
            if (!event.getLevel ().equals (MessageChangeEvent.Level.NONE)) {
               if ( (event.getTextList () != null) && (event.getTextList ().size () > 0) ) { //list of texts
                  for (String text : event.getTextList ()) {
                     addTextLabel (htmlPanel, text);
                  }
               } else { //simple text
                  addTextLabel (htmlPanel, event.getText ());
               }
            }
            
            //apply styles
            switch (event.getLevel ()) {
               case NONE:
                  styleName = "";
                  break;
               case INFORMATION:
                  styleName = IStyleConstants.MESSAGE;
                  break;
               case WARNING: //cascade
               case ERROR:
                  styleName = IStyleConstants.WARNING;
                  break;
            }
            htmlPanel.setStyleName (styleName);
         } //end of onMessageChange
      });
   }

   private void logEvent (MessageChangeEvent event) {
      if (event.getThrowable () != null) {
         log.log (Level.WARNING, event.getText (), event.getThrowable ());
      } else if (event.getServerFailure () != null) {
         log.log (Level.WARNING, event.getText () + "; " + event.getServerFailure ().getMessage ());
      } else {
         log.log (Level.INFO, event.getText ());
      }
   }
   
   private void addTextLabel (HTMLPanel htmlPanel, String text) {
      Label textLabel = new Label ();
      textLabel.setText (text);

      htmlPanel.add (textLabel);
   }

}
