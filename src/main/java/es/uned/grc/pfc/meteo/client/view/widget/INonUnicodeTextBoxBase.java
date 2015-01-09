package es.uned.grc.pfc.meteo.client.view.widget;

import com.google.gwt.event.dom.client.HasAllDragAndDropHandlers;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasAllGestureHandlers;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasAllTouchHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesMouseEvents;

@SuppressWarnings ("deprecation")
public interface INonUnicodeTextBoxBase extends SourcesClickEvents, HasClickHandlers, HasDoubleClickHandlers, 
                                                HasFocus, HasEnabled, HasAllDragAndDropHandlers, HasAllFocusHandlers, 
                                                HasAllGestureHandlers, HasAllKeyHandlers, HasAllMouseHandlers, HasAllTouchHandlers, 
                                                SourcesMouseEvents {
   void setPreventUnicode (String preventUnicodeString);
   boolean getPreventUnicode ();

   String getText ();
   void setText (String text);
}
