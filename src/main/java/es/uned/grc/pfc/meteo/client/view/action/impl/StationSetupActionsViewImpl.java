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

import es.uned.grc.pfc.meteo.client.view.IStationSetupView;
import es.uned.grc.pfc.meteo.client.view.action.IStationSetupActionsView;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;
import es.uned.grc.pfc.meteo.client.view.widget.ImageLabel;

/**
 * Implementation of the IStationSetupActionsView with UiBinder.
 */
public class StationSetupActionsViewImpl extends Composite implements IStationSetupActionsView  {
   interface ActionsViewImplUiBinder extends UiBinder <HTMLPanel, StationSetupActionsViewImpl> {
   }
   private static ActionsViewImplUiBinder uiBinder = GWT.create (ActionsViewImplUiBinder.class);
   
   @Inject
   EventBus eventBus = null;
   @Inject
   PlaceController placeController = null;
   @Inject
   IStationSetupView stationSetupView = null;
   
   @UiField
   HTMLPanel viewActionsPanel = null;
   @UiField
   HTMLPanel editActionsPanel = null;
   @UiField
   protected ImageLabel editStation = null;
   @UiField
   protected ImageLabel saveStation = null;
   @UiField
   protected ImageLabel cancelStation = null;

   public StationSetupActionsViewImpl () {
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
      return editStation;
   }

   @Override
   public IHasActionHandlers getSaveHandler () {
      return saveStation;
   }

   @Override
   public IHasActionHandlers getCancelHandler () {
      return cancelStation;
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
