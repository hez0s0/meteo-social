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
 * Implementation of the ObservationListActionsViewImpl with UiBinder.
 */
public class ObservationListActionsViewImpl extends Composite implements IObservationListActionsView {

   interface ObservationListActionsViewImplUiBinder extends UiBinder <HTMLPanel, ObservationListActionsViewImpl> {
   }
   private static ObservationListActionsViewImplUiBinder observationListActionsViewImplUiBinder = GWT.create (ObservationListActionsViewImplUiBinder.class);

   @Inject
   EventBus eventBus = null;
   @Inject
   PlaceController placeController = null;
   @Inject
   IObservationListView listView = null;

   @UiField
   protected HTMLPanel viewGraphicsPanel = null;
   @UiField
   protected HTMLPanel viewDerivedPanel = null;
   
   @UiField
   protected ImageLabel viewGraphics = null;
   @UiField
   protected ImageLabel viewDerived = null;

   public ObservationListActionsViewImpl () {
      initWidget (observationListActionsViewImplUiBinder.createAndBindUi (this));
   }

   @Override
   public Widget asWidget () {
      return this;
   }
   
   @Override
   public UIObject getGraphicsPanel () {
      return viewGraphicsPanel;
   }
   
   @Override
   public UIObject getDerivedPanel () {
      return viewDerivedPanel;
   }
   
   @Override
   public IHasActionHandlers getGraphicsHandler () {
      return viewGraphics;
   }

   @Override
   public IHasActionHandlers getDerivedHandler () {
      return viewDerived;
   }
}
