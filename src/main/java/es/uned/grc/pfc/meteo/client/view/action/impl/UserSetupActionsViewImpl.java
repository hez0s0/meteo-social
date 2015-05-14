package es.uned.grc.pfc.meteo.client.view.action.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.view.IUserSetupView;
import es.uned.grc.pfc.meteo.client.view.action.IUserSetupActionsView;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;
import es.uned.grc.pfc.meteo.client.view.widget.ImageLabel;

/**
 * Implementation of the IUserSetupActionsView with UiBinder.
 */
public class UserSetupActionsViewImpl extends Composite implements IUserSetupActionsView  {
   interface ActionsViewImplUiBinder extends UiBinder <HTMLPanel, UserSetupActionsViewImpl> {
   }
   private static ActionsViewImplUiBinder uiBinder = GWT.create (ActionsViewImplUiBinder.class);
   
   @Inject
   EventBus eventBus = null;
   @Inject
   PlaceController placeController = null;
   @Inject
   IUserSetupView userSetupView = null;
   
   @UiField
   HTMLPanel viewActionsPanel = null;
   @UiField
   HTMLPanel editActionsPanel = null;
   @UiField
   protected ImageLabel editUser = null;
   @UiField
   protected ImageLabel saveUser = null;
   @UiField
   protected ImageLabel cancelUser = null;

   public UserSetupActionsViewImpl () {
      initWidget (uiBinder.createAndBindUi (this));
   }

   @Override
   public Widget asWidget () {
      return this;
   }
   
   @Override
   public void setEditorMode (EditorMode editorMode) {
      viewActionsPanel.setVisible (editorMode.equals (EditorMode.VIEW));
      editActionsPanel.setVisible (!editorMode.equals (EditorMode.VIEW));
   }

   @Override
   public IHasActionHandlers getEditHandler () {
      return editUser;
   }

   @Override
   public IHasActionHandlers getSaveHandler () {
      return saveUser;
   }

   @Override
   public IHasActionHandlers getCancelHandler () {
      return cancelUser;
   }

   @Override
   public Panel getViewActionsPanel () {
      return viewActionsPanel;
   }

   @Override
   public Panel getEditActionsPanel () {
      return editActionsPanel;
   }
}
