package es.uned.grc.pfc.meteo.client.view.action.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.view.IObservationListView;
import es.uned.grc.pfc.meteo.client.view.action.IObservationListActionsView;
import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;
import es.uned.grc.pfc.meteo.client.view.widget.ImageLabel;

/**
 * Implementation of the WorkSpaceListActionsView with UiBinder.
 */
public class ObservationListActionsViewImpl extends Composite implements IObservationListActionsView {

   interface WorkSpaceListActionsViewImplUiBinder extends UiBinder <HTMLPanel, ObservationListActionsViewImpl> {
   }

   private static WorkSpaceListActionsViewImplUiBinder workSpaceListActionsViewImplUiBinder = GWT.create (WorkSpaceListActionsViewImplUiBinder.class);

   @Inject
   EventBus eventBus = null;
   @Inject
   PlaceController placeController = null;
   @Inject
   IObservationListView workSpaceListView = null;

   @UiField
   protected HTMLPanel createWorkSpacePanel = null;
   @UiField
   protected HTMLPanel deleteWorkSpacePanel = null;
   @UiField
   protected HTMLPanel exportWorkSpacePanel = null;
   @UiField
   protected HTMLPanel importWorkSpacePanel = null;
   @UiField
   protected ImageLabel createWorkSpace = null;
   @UiField
   protected ImageLabel deleteWorkSpaceList = null;
   @UiField
   protected ImageLabel exportWorkSpaceList = null;
   @UiField
   protected ImageLabel importWorkSpaceList = null;

   public ObservationListActionsViewImpl () {
      initWidget (workSpaceListActionsViewImplUiBinder.createAndBindUi (this));
   }

   @Override
   public Widget asWidget () {
      return this;
   }
   
   @Override
   public UIObject getCreatePanel () {
      return createWorkSpacePanel;
   }
   
   @Override
   public UIObject getDeletePanel () {
      return deleteWorkSpacePanel;
   }
   
   @Override
   public UIObject getExportPanel () {
      return exportWorkSpacePanel;
   }
   
   @Override
   public UIObject getImportPanel () {
      return importWorkSpacePanel;
   }

   @Override
   public IHasActionHandlers getCreateHandler () {
      return createWorkSpace;
   }

   @Override
   public IHasActionHandlers getDeleteHandler () {
      return deleteWorkSpaceList;
   }

   @Override
   public IHasActionHandlers getExportHandler () {
      return exportWorkSpaceList;
   }

   @Override
   public IHasActionHandlers getImportHandler () {
      return importWorkSpaceList;
   }
}
