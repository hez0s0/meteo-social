package es.uned.grc.pfc.meteo.client.view.widget.suggest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.ListDataProvider;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import es.uned.grc.pfc.meteo.client.model.base.IHasProxies;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.base.IView;

public abstract class AbstractEntitySuggestListBox <E extends Object> extends Composite implements IView, IHasProxies <E>, HasValueChangeHandlers <List <E>> {

   protected EditorMode editorMode = null;
   protected ListDataProvider <E> entityListProvider = new ListDataProvider <E> ();
   /** Maximum number of elements allowed in the list. By default, unlimited (0). */
   protected int maxElements = 0;
   
   protected abstract EntitySuggestBox <E, ?, ?> getEntitySuggestBox ();
   protected abstract void display (List <E> values);
   protected abstract boolean areEqual (E e1, E e2);
   
   /**
    * Set the collection of elements to be displayed in the list at startup.
    */
   public void setValues (List <E> values) {
      if ( (values != null) && (maxElements > 0) && (values.size () > maxElements) ) {
         GWT.log (PortableStringUtils.format ("A maximum of %s elements allowed, but %s were provided", maxElements, values.size ()));
         
         return;
      }
      
      entityListProvider.getList ().clear ();
      
      display (values);
      
      if (values != null) {
         entityListProvider.getList ().addAll (values);
      }
      
      adjustInputVisibility ();
      dispatchValueChangeEvent ();
   }
   
   /**
    * Get the collection of elements within the list at a given time.
    */
   public List <E> getValues () {
      return entityListProvider.getList ();
   }
   
   /**
    * Checks if there is any value in the textBox
    * that has not been "normalized"
    */
   public boolean hasPendingValue () {
      return getEntitySuggestBox ().getSuggestBox ().getText ().length () > 0;
   }

   public void setRequestFactory (IRequestFactory requestFactory) {
      getEntitySuggestBox ().setRequestFactory (requestFactory);
   }

   /**
    * If the suggest box requires to be able to edit entities, please
    * provide the context that must do so.
    */
   public void setEditRequestContext (RequestContext editRequestContext) {
      getEntitySuggestBox ().setEditRequestContext (editRequestContext);
   }

   /**
    * Change availability of the interface fields depending on edition mode.
    */
   public void notifyEditMode (EditorMode editorMode) {
      this.editorMode = editorMode;
      
      getEntitySuggestBox ().notifyEditMode (editorMode);
   }
   
   protected boolean isStored (E searched) {
      boolean stored = false;
      
      if (searched != null) {
         for (E entityProxy : entityListProvider.getList ()) {
            if (areEqual (searched, entityProxy)) {
               stored = true;
               break;
            }
         }
      }

      return stored;
   }
   
   protected void store (E entityProxy) {
      if (entityProxy != null) {
         entityListProvider.getList ().add (entityProxy);
         getEntitySuggestBox ().clearText ();

         adjustInputVisibility ();
         dispatchValueChangeEvent ();
      }
   }
   
   protected void remove (E element) {
      if (element != null) {
         Set <E> elements = new HashSet <E> ();
         elements.add (element);
         
         remove (elements);
      }
   }
   
   protected void remove (Set <E> elements) {
      if ( (elements != null) && (elements.size () > 0) ) {
         entityListProvider.getList ().removeAll (elements);
         
         adjustInputVisibility ();
         dispatchValueChangeEvent ();
      }
   }
   
   private void dispatchValueChangeEvent () {
      ValueChangeEvent.fire (this, entityListProvider.getList ());
   }
   
   protected int getMaxElements () {
      return maxElements;
   }
   
   public void setMaxElements (int maxElements) {
      this.maxElements = maxElements;
   }
   
   protected boolean isFull () {
      int max = getMaxElements ();
      if (max > 0) {
         return entityListProvider.getList ().size () >= max;
      } else {
         return false;
      }
   }
   
   private void adjustInputVisibility () {
      getEntitySuggestBox ().getSuggestBox ().setVisible (!isFull());
   }
   
   @Override
   public HandlerRegistration addValueChangeHandler (ValueChangeHandler <List <E>> handler) {
      return addHandler (handler, ValueChangeEvent.getType ());
   }
   
}
