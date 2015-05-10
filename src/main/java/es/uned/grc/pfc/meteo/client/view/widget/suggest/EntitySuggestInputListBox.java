package es.uned.grc.pfc.meteo.client.view.widget.suggest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import es.uned.grc.pfc.meteo.client.model.base.IPagedListProxy;
import es.uned.grc.pfc.meteo.client.util.IStyleConstants;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.base.IEnterOnEmptyFieldHandler;
import es.uned.grc.pfc.meteo.client.view.util.DisplayUtils;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.AlertDialogBox;

public abstract class EntitySuggestInputListBox <E extends Object, R extends RequestContext, P extends IPagedListProxy <E>> extends AbstractEntitySuggestListBox <E> {
   
   private final static int CURSOR_LEFT = 37; 
   private final static int CURSOR_UP = 38; 
   private final static int CURSOR_RIGHT = 39; 
   private final static int CURSOR_DOWN = 40; 
   
   /** global text constants */
   @com.google.gwt.i18n.client.LocalizableResource.Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @com.google.gwt.i18n.client.LocalizableResource.GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Unregognized input. It shall be ignored")
      @Meaning ("Mensaje de validación")
      String unknownInput ();
   }
   public static TextConstants textConstants = GWT.create (TextConstants.class);
   
   private static final String CLOSE_TEXT = "x";
   
   interface EntitySuggestInputListBoxUiBinder extends UiBinder <FocusPanel, EntitySuggestInputListBox <?, ?, ?>> {
   }
   private static EntitySuggestInputListBoxUiBinder uiBinder = GWT.create (EntitySuggestInputListBoxUiBinder.class);

   private static Logger log = Logger.getLogger(""); 

   @UiField
   protected FocusPanel rootFocusPanel = null;
   @UiField
   protected FlowPanel rootFlowPanel = null;
   @UiField
   protected BulletList bulletList = null;
   @UiField (provided = true)
   protected ListItem <E> defaultListItem = new ListItem <E> (null);

   //next come unbound fields
   protected TextBox itemTextBox = new TextBox ();
   protected EntitySuggestBox <E, R, P> entitySuggestBox = createEntitySuggestBox (itemTextBox);
   
   protected List <IEnterOnEmptyFieldHandler> enterOnEmptyFieldHandlers = new ArrayList <IEnterOnEmptyFieldHandler> ();

   /** creates a suggest box for the given entity */
   protected abstract EntitySuggestBox <E, R, P> createEntitySuggestBox (TextBox textBox);
   /** decide if an entry that does not match with any given is allowed to be introduced or not */
   protected abstract boolean allowFreeEntry (String freeText);
   /** if free entries are allowed, provide a mechanism to create entities based on free text */
   protected abstract E createFreeEntry (String freeText) throws Exception;
   /** create a representation for the entity to be diplayed */
   protected abstract String getDisplay (E entity);
   /** decide if duplicated entries shall be allowed */
   protected abstract boolean allowDuplicated ();

   public EntitySuggestInputListBox () {
      initWidget (uiBinder.createAndBindUi (this));
      
      entitySuggestBox.setEntitySuggestInputListBox (this);

      // apply styles
      itemTextBox.setStyleName (IStyleConstants.TOKEN_SUGGEST);
      bulletList.setStyleName (IStyleConstants.TOKEN_LIST);
      defaultListItem.setStyleName (IStyleConstants.TOKEN_INPUT);

      // compose the initial structure
      defaultListItem.add (entitySuggestBox);
      clearAll ();
      
      bind ();

      setFocus ();
   }

   @Override
   protected EntitySuggestBox <E, R, P> getEntitySuggestBox () {
      return entitySuggestBox;
   }
   
   @Override
   protected void display (List <E> values) {
      clearAll ();
      
      if (values != null) {
         for (E entity : values) {
            addItem (entity, true);
         }
      }
   }

   private void clearAll () {
      bulletList.clear ();
      bulletList.add (defaultListItem);
   }
   
   private void bind () {
      // set focus when clicked
      rootFocusPanel.addClickHandler (new ClickHandler () {
         @Override
         public void onClick (ClickEvent event) {
            setFocus ();
         }
      });
      
      // this needs to be on the itemBox rather than entitySuggestBox, or backspace will get executed twice
      itemTextBox.addKeyDownHandler (new KeyDownHandler () {
         
         @SuppressWarnings ("unchecked")
         public void onKeyDown (KeyDownEvent event) {
            try {
               if (event.getNativeKeyCode () == KeyCodes.KEY_ENTER) {
                  handleNewItem ();
               }
               // handle backspace
               if (event.getNativeKeyCode () == KeyCodes.KEY_BACKSPACE) {
                  int index = bulletList.getWidgetCount () - 2;
                  if ( ("".equals (itemTextBox.getValue ().trim ())) && (index >= 0) ) {
                     ListItem <E> li = (ListItem <E>) bulletList.getWidget (index);
                     
                     removeListItem (li);
                     
                     itemTextBox.setFocus (true);
                  }
               }
               // handle arrow keys
               if (isArrowKey (event.getNativeKeyCode ())) {
                  if (!((DefaultSuggestionDisplay) entitySuggestBox.getSuggestBox ().getSuggestionDisplay ()).isSuggestionListShowing ()) {
                     entitySuggestBox.getSuggestBox ().showSuggestionList ();
                  } else {
                     entitySuggestBox.getSuggestBox ().fireEvent (event);
                  }
               }
            } catch (Exception e) {
               log.log (Level.WARNING, e.getMessage ());
            }
         }
      });
      
      // when going out, check if new items should be created as well
      itemTextBox.addBlurHandler (new BlurHandler() {
         @Override
         public void onBlur (BlurEvent event) {
            try {
               if (!((DefaultSuggestionDisplay) entitySuggestBox.getSuggestBox ().getSuggestionDisplay ()).isSuggestionListShowing ()) {
                  handleNewItem ();
               }
            } catch (Exception e) {
               log.log (Level.WARNING, e.getMessage ());
            }
         }
      });

      // add an item when clicking on the suggest list
      entitySuggestBox.getSuggestBox ().addSelectionHandler (new SelectionHandler <SuggestOracle.Suggestion> () {
         @SuppressWarnings ("unchecked")
         public void onSelection (SelectionEvent <SuggestOracle.Suggestion> selectionEvent) {
            if (selectionEvent.getSelectedItem () != null) {
               addItem (((EntitySuggestion <E>) selectionEvent.getSelectedItem ()).getEntityProxy (), false);
            }
         }
      });
   } //end of bind

   private boolean isArrowKey (int nativeKeyCode) {
      return nativeKeyCode == CURSOR_LEFT || nativeKeyCode == CURSOR_RIGHT 
          || nativeKeyCode == CURSOR_UP || nativeKeyCode == CURSOR_DOWN;
   }

   private void handleNewItem () throws Exception {
      clearSpanElement ();
      
      if ( (allowFreeEntry (itemTextBox.getValue ())) && (!"".equals (itemTextBox.getValue ().trim ())) ) { //are manual entries allowed
         E freeEntry = createFreeEntry (itemTextBox.getValue ());
         if (freeEntry != null) {
            addItem (freeEntry, false);
         } else if (getSpanElement () != null) {
            DisplayUtils.showSpan (getSpanElement (), textConstants.unknownInput ());
         }
      }
   }
   
   protected void clearSpanElement () {
      if (getSpanElement () != null) {
         DisplayUtils.clearErrors (getSpanElement ());
      }
   }

   public SpanElement getSpanElement () {
      return null; //by default, no span used for validation
   }
   
   private void setFocus () {
      entitySuggestBox.getSuggestBox ().setFocus (true);
   }
   
   private void addItem (E entity, boolean onlyDisplay) {
      Span span = null;
      Paragraph p = null;
      
      if ( (entity != null) &&
          ((allowDuplicated ()) || (!isStored (entity))) ) {
         final ListItem <E> displayItem = new ListItem <E> (entity);

         span = new Span (CLOSE_TEXT);
         p = new Paragraph (getDisplay (entity));
         
         displayItem.setStyleName (IStyleConstants.TOKEN_TOKEN);

         displayItem.addClickHandler (new ClickHandler () {
            public void onClick (ClickEvent clickEvent) {
               displayItem.setSelected (!displayItem.isSelected ());
               
               if (displayItem.isSelected ()) {
                  displayItem.addStyleName (IStyleConstants.TOKEN_TOKEN_SELECTED);
               } else {
                  displayItem.removeStyleName (IStyleConstants.TOKEN_TOKEN_SELECTED);
               }
            }
         });
         span.addClickHandler (new ClickHandler () {
            public void onClick (ClickEvent clickEvent) {
               if ( (editorMode == null) || (!editorMode.equals (EditorMode.VIEW)) ) {
                  removeListItem (displayItem);
               }
            }
         });

         displayItem.add (p);
         displayItem.add (span);
         
         if (!onlyDisplay) {
            // hold the original value of the item selected
            store (entity);
         }

         bulletList.insert (displayItem, bulletList.getWidgetCount () - 1);
      }
      
      itemTextBox.setValue ("");
      itemTextBox.setFocus (true);
   } //end of addItem

   private void removeListItem (ListItem <E> displayItem) {
      String deleteMessage = null;
      
      if ((deleteMessage = checkDelete (displayItem.getEntity ())) == null) {
         remove (displayItem.getEntity ());
   
         bulletList.remove (displayItem);
      } else {
         AlertDialogBox.showWarning (deleteMessage);
      }
   }

   /**
    * Checks if an item can be deleted.
    * @return null if it can be deleted, an error text otherwise
    */
   protected String checkDelete (E displayItem) {
      return null; //by default no problem, override to prevent certain deletions
   }

   public void addEnterOnEmptyFieldHandler (IEnterOnEmptyFieldHandler enterOnEmptyFieldHandler) {
      enterOnEmptyFieldHandlers.add (enterOnEmptyFieldHandler);
   }
}
