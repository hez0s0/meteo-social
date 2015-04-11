package es.uned.grc.pfc.meteo.client.view.widget.suggest;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import es.uned.grc.pfc.meteo.client.model.base.IPagedListProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.util.FormUtils;

public abstract class EntitySuggestBox <E extends Object, R extends RequestContext, P extends IPagedListProxy <E>> 
   implements IsWidget, LeafValueEditor <E> {

   protected EntitySuggestOracle <E, R, P> entitySuggestOracle = null;
   protected ExtendedSuggestBox suggestBox = null;
   protected RequestContext editRequestContext = null;
   
   /** create a suggest oracle implementation for this specific entity */
   protected abstract EntitySuggestOracle <E, R, P> createEntitySuggestOracle ();

   public EntitySuggestBox () {
      this (null);
   }
   
   public EntitySuggestBox (TextBox textBox) {
      entitySuggestOracle = createEntitySuggestOracle ();
      
      if (textBox != null) {
         suggestBox = new ExtendedSuggestBox (entitySuggestOracle, textBox);
      } else {
         suggestBox = new ExtendedSuggestBox (entitySuggestOracle);
      }
//      suggestBox.setPlaceholder ("suggestBoxKey");
      
      entitySuggestOracle.setSuggestBox (suggestBox);
   }
   
   public void setEntitySuggestInputListBox (EntitySuggestInputListBox <E, R, P> inputListBox) {
      entitySuggestOracle.setEntitySuggestInputListBox (inputListBox);
   }
   
   @Override
   public Widget asWidget () {
      return suggestBox;
   }
   
   public SuggestBox getSuggestBox () {
      return suggestBox;
   }

   public IRequestFactory getRequestFactory () {
      return entitySuggestOracle.getRequestFactory ();
   }
   
   public void setRequestFactory (IRequestFactory requestFactory) {
      entitySuggestOracle.setRequestFactory (requestFactory);
   }

   public RequestContext getEditRequestContext () {
      return editRequestContext;
   }

   public void setEditRequestContext (RequestContext editRequestContext) {
      this.editRequestContext = editRequestContext;
   }

   public void notifyEditMode (EditorMode editorMode) {
      FormUtils.notifyEditMode (editorMode, suggestBox);
   }

   public void clearText () {
      suggestBox.setText ("");
   }
   
   @UiChild (tagname = "stylePrimaryName")
   public void setStylePrimaryName (String style) {
      suggestBox.setStylePrimaryName (style);
   }
}
