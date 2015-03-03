package es.uned.grc.pfc.meteo.client.event;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.i18n.client.Messages;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class MessageChangeEvent extends GwtEvent <IMessageChangeEventHandler> {
   
   /** level or importance of the message provided: use None if no message must be displayed */
   public enum Level {NONE, INFORMATION, WARNING, ERROR};
   
   /**
    * The constants used in this Widget.
    */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextMessages extends Messages {
      @DefaultMessage ("An unknown event ocurred in the system") @Meaning ("An error message")
      String unknownEvent ();
      @DefaultMessage ("No elements were deleted") @Meaning ("An error message")
      String nothingDeleted ();
      @DefaultMessage ("An error ocurred in the system: ''{0}''") @Meaning ("An error message. Params: code")
      String serverError (String code);
      @DefaultMessage ("Selected elements could not be deleted") @Meaning ("An error message")
      String deleteError ();
      @DefaultMessage ("Element of type ''{0}'' with ID ''{1}'' could not be found")
      @Meaning ("An error message. Params: elementType, elementID")
      String getError (String elementType, Integer id);
      @DefaultMessage ("List of elements of type ''{0}'' could not be loaded")
      @Meaning ("An error message. Params: elementType")
      String listError (String elementType);
      @DefaultMessage ("Element of type ''{0}'' could not be saved")
      @Meaning ("An error message. Params: elementType, errorText")
      String saveElementError (String elementType);              
   }
   private static TextMessages textMessages = null;
   
   public static Type <IMessageChangeEventHandler> TYPE = new Type <IMessageChangeEventHandler> ();

   private Level level = null;
   private String text = null;
   private List <String> textList = null;
   private Throwable throwable = null;
   private ServerFailure serverFailure = null;
   
   /**
    * Build a Message event with the given info to display.
    * BE AWARE that for i18n to work, text must come from the 
    * strongly typed invokation of a method of getTextMessages ()
    */
   public MessageChangeEvent (Level level, String text, Throwable throwable) {
      this.level = level;
      this.text = text;
      this.throwable = throwable;
   } //end of MessageChangeEvent
   
   /**
    * Build a Message event with the given info to display.
    * BE AWARE that for i18n to work, text must come from the 
    * strongly typed invokation of a method of getTextMessages ()
    */
   public MessageChangeEvent (Level level, String text, ServerFailure serverFailure) {
      this.level = level;
      this.text = text;
      this.serverFailure = serverFailure;
   } //end of MessageChangeEvent
   
   /**
    * Build a Message event with the given info to display.
    */
   public MessageChangeEvent (Level level, List <String> textList, Throwable throwable) {
      this.level = level;
      this.textList = textList;
      this.throwable = throwable;
   } //end of MessageChangeEvent
   
   /**
    * Build a Message event with the given info to display.
    */
   public MessageChangeEvent (Level level, List <String> textList, ServerFailure serverFailure) {
      this.level = level;
      this.textList = textList;
      this.serverFailure = serverFailure;
   } //end of MessageChangeEvent

   @Override
   protected void dispatch (IMessageChangeEventHandler handler) {
      handler.onMessageChange (this);
   }

   @Override
   public Type <IMessageChangeEventHandler> getAssociatedType () {
      return TYPE;
   }
   
   /**
    * Gets a singleton instance of MessageChangeEvent.TextConstant to gain
    * access to the strongly typed way of getting internationalized messages.  
    */
   public static TextMessages getTextMessages () {
      if (textMessages == null) {
         textMessages = GWT.create (TextMessages.class);
      }
      return textMessages;
   } //end of getTextMessages
   
   public Level getLevel () {
      return level;
   } //end of getLevel
   
   /**
    * Gets a text ready to be reported
    */
   public String getText () {
      return text;
   } //end of getDisplayText
   
   /**
    * Gets the Throwable that caused the message, if any. 
    */
   public Throwable getThrowable () {
      return throwable;
   } //end of getThrowable
   
   /**
    * Gets the ServerFailure that caused the message, if any. 
    */
   public ServerFailure getServerFailure () {
      return serverFailure;
   } //end of getServerFailure

   public List <String> getTextList () {
      return textList;
   }

   public void setTextList (List <String> textList) {
      this.textList = textList;
   }
} //end of MessageChangeEvent
