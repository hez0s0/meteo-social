package es.uned.grc.pfc.meteo.client.view.widget.suggest;

import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import es.uned.grc.pfc.meteo.client.model.base.IPagedListProxy;
import es.uned.grc.pfc.meteo.client.resource.IApplicationResources;
import es.uned.grc.pfc.meteo.client.util.ClientGlobals;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.util.FormUtils;

public abstract class EntitySuggestListBox <E extends BaseProxy, R extends RequestContext, P extends IPagedListProxy <E>> extends AbstractEntitySuggestListBox <E> {
   
   interface EntitySuggestListBoxUiBinder extends UiBinder <VerticalPanel, EntitySuggestListBox <?, ?, ?>> {
   }
   private static EntitySuggestListBoxUiBinder uiBinder = GWT.create (EntitySuggestListBoxUiBinder.class);
   
   protected static IApplicationResources applicationResources = ClientGlobals.getInstance ().getApplicationResources ();
   
   @UiField (provided = true)
   protected EntitySuggestBox <E, R, P> entitySuggestBox = createEntitySuggestBox ();
   @UiField (provided = true)
   protected CellList <E> entityCellList = null;

   @UiField
   protected VerticalPanel mainPanel = null;
   @UiField (provided = true)
   protected PushButton addEntity = null;
   @UiField (provided = true)
   protected PushButton deleteEntity = null;

   protected SingleSelectionModel <E> selectionModel = new SingleSelectionModel <E> (getEntityKeyProvider ());
   
   /** creates a suggest box for the given entity */
   protected abstract EntitySuggestBox <E, R, P> createEntitySuggestBox ();
   /** gets the cell renderer for the specific entity */
   protected abstract Cell <E> getEntityCell ();
   /** gets the cell entity key provider for the specific entity */
   protected abstract ProvidesKey <E> getEntityKeyProvider ();
   
   public EntitySuggestListBox () {
      // images with buttons
      addEntity = new PushButton (new Image (applicationResources.add ().getSafeUri ()));
      deleteEntity = new PushButton (new Image (applicationResources.delete ().getSafeUri ()));
      
      // create the celllist for entities
      entityCellList = new CellList <E> (getEntityCell (), getEntityKeyProvider ());
      entityCellList.setPageSize (5);
      entityCellList.setKeyboardPagingPolicy (KeyboardPagingPolicy.INCREASE_RANGE);
      entityCellList.setKeyboardSelectionPolicy (KeyboardSelectionPolicy.DISABLED);
      entityCellList.setSelectionModel (selectionModel);
      entityListProvider.addDataDisplay (entityCellList);

      initWidget (uiBinder.createAndBindUi (this));
   } //end of EntitySuggestListBox
   
   @Override
   protected EntitySuggestBox <E, R, P> getEntitySuggestBox () {
      return entitySuggestBox;
   }
   
   @Override
   protected void display (List <E> values) {
      //nothing to do, the table is bound to the list
   }

   @UiHandler ("addEntity")
   void handleAddEntityClick (ClickEvent e) {
      store (entitySuggestBox.getValue ());
   } //end of handleAddEntityClick

   @UiHandler("deleteEntity")
   void handleRemoveEntityClick (ClickEvent e) {
      remove (selectionModel.getSelectedSet ());
   } //end of handleRemoveEntityClick

   /**
    * Change availability of the interface fields depending on edition mode.
    */
   public void notifyEditMode (EditorMode editorMode) {
      super.notifyEditMode (editorMode);
      
      FormUtils.notifyEditMode (editorMode, addEntity);
      FormUtils.notifyEditMode (editorMode, deleteEntity);
   } //end of notifyEditMode
   
} //end of EntitySuggestListBox
