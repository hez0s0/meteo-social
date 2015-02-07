package es.uned.grc.pfc.meteo.client.view.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import es.uned.grc.pfc.meteo.client.activity.mapper.MainActivityMapper;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.util.IStyleConstants;
import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.base.IEditNotifiableWidget;
import es.uned.grc.pfc.meteo.client.view.base.IServerValidatableForm;
import es.uned.grc.pfc.meteo.client.view.widget.RichTextToolbar;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.ActionDialogBox;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.ActionDialogBoxClickEvent;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.ConfirmationDialogBox;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.IActionDialogBoxEventHandler;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

public class FormUtils {

   /** a separator for key value pairs: it MUST differ from ',' */
   private static final String VALUES_SEPARATOR = "=";
   /** a text that was not translated on the .properties file */
   private static final String UNTRANSLATED_PREFIX = "???";
   
   /**
    * Given a value path and a list of localized text, in the shape "value.path.can.be.long=localized text",
    * this method returns the correct localized text, or the incoming value itself if not found.
    */
   public static String getLocalizedValue (String [] keyPath, String [] texts) {
      StringBuffer result = new StringBuffer ();
      String pathText = null;
      int i = 0;
      
      if ( (keyPath != null) && (keyPath.length > 0) && (texts != null) ) {
         for (String key : keyPath) {
            pathText = getLocalizedValue (key, texts);
            if (!PortableStringUtils.isEmpty (pathText)) {
               if (i > 0) {
                  result.append (" ");
               }
               result.append (pathText);
            }
            i++;
         }
      }
      
      return result.toString ();
   }
   
   /**
    * Given a value and a list of localized text, in the shape "value=localized text",
    * this method returns the correct localized text, or the incoming value itself if not found.
    */
   public static String getLocalizedValue (String key, String [] texts) {
      String result = "";
      
      if ( (key != null) && (texts != null) ) {
         key = key.trim ().toUpperCase () + VALUES_SEPARATOR;
         for (String text : texts) {
            text = text.trim ();
            if (text.toUpperCase ().startsWith (key)) {
               result = text.substring (key.length ());
               break;
            } else if (text.toUpperCase ().startsWith (UNTRANSLATED_PREFIX + key)) {
               result = UNTRANSLATED_PREFIX + text.substring (UNTRANSLATED_PREFIX.length () + key.length ());
               if (!result.endsWith (UNTRANSLATED_PREFIX)) {
                  result += UNTRANSLATED_PREFIX;
               }
               break;
            }
         }
      }
      
      return result;
   }

   /**
    * Sets a field readonly/enabled or not, depending on editorMode
    */
   public static void notifyEditMode (EditorMode editorMode, TextBoxBase textBox) {
      if ( (editorMode != null) && (textBox != null) ) {
         textBox.setEnabled (!editorMode.equals (EditorMode.VIEW));
         textBox.setReadOnly (editorMode.equals (EditorMode.VIEW));
      }
   }

   /**
    * Sets a field readonly/enabled or not, depending on editorMode
    */
   @SuppressWarnings ("deprecation")
   public static void notifyEditMode (EditorMode editorMode, ValueListBox <?> valueListBox) {
      if ( (editorMode != null) && (valueListBox != null) && (valueListBox.getElement () != null) ) {
         DOM.setElementPropertyBoolean (valueListBox.getElement (), "disabled", (editorMode.equals (EditorMode.VIEW)));
      }
   }
   
   /**
    * Sets a button visible or not, depending on editorMode
    */
   public static void notifyEditMode (EditorMode editorMode, Button button) {
      if ( (editorMode != null) && (button != null) ) {
         button.setVisible (!editorMode.equals (EditorMode.VIEW));
      }
   }
   
   public static void notifyEditMode (EditorMode editorMode, SuggestBox suggestBox) {
      if ( (editorMode != null) && (suggestBox != null) ) {
         suggestBox.setEnabled (!editorMode.equals (EditorMode.VIEW));
      }
   }

   public static void notifyEditMode (EditorMode editorMode, VerticalPanel panel) {
      Widget widget = null;
      int widgetLength = panel.getWidgetCount ();
      for (int i = 0; i < widgetLength; i++) {
         widget = panel.getWidget (i);
         if (widget instanceof IEditNotifiableWidget) {
            ((IEditNotifiableWidget) widget).notifyEditMode (editorMode);
         }
      } //end of sub-panel iteration
   }

   public static void notifyEditMode (EditorMode editorMode, FocusWidget focusWidget) {
      if ( (editorMode != null) && (focusWidget != null) ) {
         focusWidget.setEnabled (!editorMode.equals (EditorMode.VIEW));
      }
   }

   public static void notifyEditMode (EditorMode editorMode, RichTextToolbar descriptionToolbar) {
      if ( (editorMode != null) && (descriptionToolbar != null) ) {
         descriptionToolbar.setVisible (!editorMode.equals (EditorMode.VIEW));
      }
   }

   /**
    * Within a VerticalPanel, an internal widget is moved one position up.
    */
   public static synchronized void pushUpForm (Widget form, VerticalPanel panel) {
      Widget priorWidget = null;
      int currentIndex = panel.getWidgetIndex (form);
      if (currentIndex > 0) {
         priorWidget = panel.getWidget (currentIndex - 1);
         
         if (priorWidget.getClass ().getName ().equals (form.getClass ().getName ())) {
            //only move over same kind of form!
            panel.remove (form);
            panel.insert (form, currentIndex - 1);
         }
      }
   }

   /**
    * Within a VerticalPanel, an internal widget is moved one position down.
    */
   public static synchronized void pushDownForm (Widget form, VerticalPanel panel) {
      int currentIndex = panel.getWidgetIndex (form);
      if (panel.getWidgetCount () > currentIndex + 1) {
         panel.remove (form);
         panel.insert (form, currentIndex + 1);
      }
   }
   
   /**
    * Gets the children elements within a panel of a given type
    */
   public static List <Widget> getAllChildren (VerticalPanel panelContainer, Class <?> subPanelClass) {
      Widget widget = null;
      int children = panelContainer.getWidgetCount ();
      List <Widget> result = new ArrayList <Widget> (children);
      
      for (int i = 0; i < children; i++) {
         widget = panelContainer.getWidget (i);
         if (widget.getClass ().getName ().equals (subPanelClass.getName ())) {
            //clazz.isInstance is not supported by GWT, so ... 
            result.add (widget);
         }
      }
      return result;
   }
   
   /**
    * Finds the next position to stack a panel of given class into a container VerticalPanel
    */
   public static Short getNextPosition (VerticalPanel panelContainer, Class <?> subPanelClass) {
      short next = 0;
      Widget widget = null;
      int children = panelContainer.getWidgetCount ();
      for (int i = 0; i < children; i++) {
         widget = panelContainer.getWidget (i);
         if (widget.getClass ().getName ().equals (subPanelClass.getName ())) {
            //clazz.isInstance is not supported by GWT, so ... 
            next ++;
         }
      }
      return next;
   }

   /**
    * Locates a form of a given class within a container by index (ignoring other contents of different class)
    */
   public static Widget findForm (int index, VerticalPanel panelContainer, Class <?> subPanelClass) {
      Widget widget = null;
      int indicationForms = 0;
      int length = panelContainer.getWidgetCount ();
      
      for (int i = 0 ; ( (i < length) && (indicationForms <= index) ); i++) {
         widget = panelContainer.getWidget (i);
         
         if (widget.getClass ().getName ().equals (subPanelClass.getName ())) {
            //clazz.isInstance is not supported by GWT, so ...
            if (indicationForms ++ == index) {
               return widget;
            }
         }
      }
      
      return null;
   }

   /**
    * Locates a {@link IServerValidatableForm} form of a given class within a container by index (ignoring other contents of different class)
    */
   public static IServerValidatableForm findServerValidatableForm (int index, VerticalPanel panelContainer, Class <?> subPanelClass) {
      Widget widget = findForm (index, panelContainer, subPanelClass);
      
      return (widget instanceof IServerValidatableForm) ? (IServerValidatableForm) widget : null;
   }

   /**
    * Clears all the forms of a given Class within a container VerticalPanel, keeping at least minimum of them
    * to be eventually re-used
    */
   public static synchronized void clearForms (VerticalPanel panelContainer, Class <?> subPanelClass, int minimum) {
      Widget widget = null;
      List <Widget> toRemove = null;
      if (panelContainer != null) {
         int kept = 0;
         int length = panelContainer.getWidgetCount ();
         toRemove = new ArrayList <Widget> (length);
         for (int i = 0; i < length; i++) {
            widget = panelContainer.getWidget (i);
            //clazz.isInstance is not supported by GWT, so ...
            if (widget.getClass ().getName ().equals (subPanelClass.getName ())) {
               if (kept >= minimum) {
                  //select it for removal only if it should not be kept
                  toRemove.add (widget);
               } else {
                  kept ++;
               }
            }
         }
         
         for (Widget widgetToRemove : toRemove) {
            panelContainer.remove (widgetToRemove);
         }
      }
   }

   /**
    * Finds a subpanel in given position and returns it
    */
   @SuppressWarnings ("unchecked")
   public static synchronized <W extends Widget> W getSubPanel (VerticalPanel panelContainer, Class <W> subPanelClass, int index) {
      Widget widget = null;
      int found = 0;
      
      if (panelContainer != null) {
         int length = panelContainer.getWidgetCount ();
         for (int i = 0; i < length; i++) {
            widget = panelContainer.getWidget (i);
            //clazz.isInstance is not supported by GWT, so ...
            if (widget.getClass ().getName ().equals (subPanelClass.getName ())) {
               if (found ++ == index) {
                  return (W) widget;
               }
            }
         }
      }
      
      return null;
   }

   /**
    * Add a subpanel it it was not already present
    */
   public static synchronized void addIfNew (VerticalPanel panelContainer, Widget widget, int position) {
      int currentIndex = panelContainer.getWidgetIndex (widget);
      
      if (currentIndex < 0) {
         //if not present, just add it
         panelContainer.add (widget);
      } else if (position != currentIndex) {
         //it already present, but in a different position and thus should be moved
         if (position < panelContainer.getWidgetCount () - 1) {
            panelContainer.insert (widget, position);
         } else {
            panelContainer.add (widget);   
         }
      }
      //in any other case we leave it be
   }
   
   public static void passDownServerValidationError (String matchPropertyName,
                                                     VerticalPanel containerPanel, 
                                                     Class <?> subClass,
                                                     int baseElement, 
                                                     String [] propertyPath, 
                                                     String message) {
      passDownServerValidationError (matchPropertyName,
                                     containerPanel, 
                                     subClass,
                                     baseElement,
                                     -1,
                                     propertyPath, 
                                     message);
   }
   
   public static void passDownServerValidationError (String matchPropertyName,
                                                     VerticalPanel containerPanel, 
                                                     Class <?> subClass,
                                                     int baseElement,
                                                     int module,
                                                     String [] propertyPath, 
                                                     String message) {
      IServerValidatableForm childForm = null;
      Integer propertyIndex = null;
      String rootProperty = (propertyPath.length > baseElement) ? propertyPath [baseElement] : null;

      if ( (rootProperty != null) && (rootProperty.toLowerCase ().contains (matchPropertyName.toLowerCase ())) ) {
         propertyIndex = getPropertyIndex (rootProperty);
         if (propertyIndex != null) {
            if (module > 1) {
               propertyIndex = propertyIndex / module;
            }
            childForm = FormUtils.findServerValidatableForm (propertyIndex, containerPanel, subClass);
            if (childForm != null) {
               childForm.displayServerValidationError (baseElement + 1, propertyPath, message);
            }
         }
      }
   }
   
   /**
    * Find out the position of some property in a validation path, 
    * in the form 'whatever.comes.before.property[index].whatever.comes.after
    */
   public static Integer getPropertyIndex (String rootProperty) {
      String position = null;
      Integer index = null;
      int firstBracket = -1;
      int lastBracket = -1;
      
      firstBracket = rootProperty.indexOf (ISharedConstants.OPEN_BRACKET);
      lastBracket = rootProperty.indexOf (ISharedConstants.CLOSE_BRACKET);
      if ( (firstBracket > 0) && (lastBracket > 0) && (lastBracket > firstBracket) ) {
         if (lastBracket == firstBracket + 1) {
            //empty brackets means position is 0
            index = 0;
         } else {
            position = rootProperty.substring (firstBracket + 1, lastBracket);
            
            if (PortableStringUtils.isNumeric (position)) {
               try {
                  index = Integer.parseInt (position);
               } catch (Exception e) {
                  // silent, unimportant
                }
            }
         }
      }
      
      return index;
   }
   
   /**
    * Clears spans of server-side errors for all the children of given panel
    */
   public static void clearServerErrors (VerticalPanel containerPanel) {
      for (int i = 0; i < containerPanel.getWidgetCount (); i++) {
         if (containerPanel.getWidget (i) instanceof IServerValidatableForm) {
            ((IServerValidatableForm) containerPanel.getWidget (i)).clearServerErrors ();
         }
      }
   }
   
   public static List <HandlerRegistration> addAltHandler (HasAllMouseHandlers widget, String text) {
      List <HandlerRegistration> handlers = new ArrayList <HandlerRegistration> (2);
      final PopupPanel popupPanel = createAltPopupPanel (text);
      
      handlers.add (widget.addMouseOverHandler (new MouseOverHandler () {
         @Override
         public void onMouseOver (MouseOverEvent event) {
            try {
               Widget source = (Widget) event.getSource ();
               int left = source.getAbsoluteLeft ();
               int top = source.getAbsoluteTop () + source.getOffsetHeight ();
               
               showPopup (popupPanel, left, top);
            } catch (Exception e) {
              // silent, unimportant
            }
         }
      }));
      handlers.add (widget.addMouseOutHandler (new MouseOutHandler () {
         @Override
         public void onMouseOut (MouseOutEvent event) {
            try {
               hidePopup (popupPanel);
            } catch (Exception e) {
              // silent, unimportant
            }
         }
      }));
      
      return handlers;
   }
   
   public static PopupPanel createAltPopupPanel (String text) {
      Label popupStatusLabel = new Label ();
      PopupPanel altPopup = new PopupPanel (true);
      
      popupStatusLabel.setStyleName (IStyleConstants.TOOLTIP);
      popupStatusLabel.setText (text);
      altPopup.add (popupStatusLabel);
      
      return altPopup;
   }

   public static void showPopup (final PopupPanel popupPanel, int left, int top) {
      if (popupPanel != null) {
         popupPanel.setPopupPosition (left, top);
         if (!popupPanel.isShowing ()) {
            popupPanel.show ();
         }
      }
   }

   public static void hidePopup (final PopupPanel popupPanel) {
      if (popupPanel != null) {
         popupPanel.hide ();
      }
   }
   
   public static void setElementVisible (Element element, boolean visible) {
      setElementVisible (element, visible ? "inline" : "none");
   }

   public static void setElementVisible (Element element, String displayValue) {
      element.getStyle ().setProperty ("display", displayValue);
   }

   public static void setRowVisible (TableRowElement rowElement, boolean visible) {
      if (rowElement != null) {
         rowElement.setAttribute ("style", visible ? "display: table-row;" : "display: none");
      }
   }

   public static void notifyEditMode (EditorMode editorMode, TableRowElement rowElement, boolean condition) {
      if ( (editorMode == null) || (editorMode.equals (EditorMode.VIEW)) ) {
         setRowVisible (rowElement, condition);
      } else {
         setRowVisible (rowElement, true);
      }
   }
   
   public static <E extends Object> void setAlternatigRowStyle (CellTable <E> cellTable) {
      cellTable.setRowStyles (new RowStyles <E> () {
         public String getStyleNames (E row, int rowIndex) {
            if (rowIndex % 2 == 0) {
               return IStyleConstants.EVEN_ROW;
            } else {
               return IStyleConstants.ODD_ROW;
            }
         }
      });
   }

   public static void reloadConditionally (MainActivityMapper mainActivityMapper) {
      if (mainActivityMapper.isMainViewDirty ()) {
         //if there are unsaved changes, confirmation is needed to proceed
         ConfirmationDialogBox.askConfirmation (IClientConstants.textConstants.pendingChangesQuestion ()).addClickHandler (ActionDialogBox.ButtonType.ACCEPT, new IActionDialogBoxEventHandler () {
            @Override
            public void onActionClicked (ActionDialogBoxClickEvent event) {
               Window.Location.reload ();
            }
         });
      } else {
         Window.Location.reload ();
      }
   }

   public static void goConditionallyToUrl (MainActivityMapper mainActivityMapper, final String url) {
      if (mainActivityMapper.isMainViewDirty ()) {
         //if there are unsaved changes, confirmation is needed to proceed
         ConfirmationDialogBox.askConfirmation (IClientConstants.textConstants.pendingChangesQuestion ()).addClickHandler (ActionDialogBox.ButtonType.ACCEPT, new IActionDialogBoxEventHandler () {
            @Override
            public void onActionClicked (ActionDialogBoxClickEvent event) {
               Window.Location.assign (url);
            }
         });
      } else {
         Window.Location.assign (url);
      }
   }
   
   public static void goConditionallyToPlace (MainActivityMapper mainActivityMapper, final Place place, final PlaceController placeController) {
      if (mainActivityMapper.isMainViewDirty ()) {
         //if there are unsaved changes, confirmation is needed to proceed
         ConfirmationDialogBox.askConfirmation (IClientConstants.textConstants.pendingChangesQuestion ()).addClickHandler (ActionDialogBox.ButtonType.ACCEPT, new IActionDialogBoxEventHandler () {
            @Override
            public void onActionClicked (ActionDialogBoxClickEvent event) {
               placeController.goTo (place);
            }
         });
      } else {
         placeController.goTo (place);
      }
   }
}
