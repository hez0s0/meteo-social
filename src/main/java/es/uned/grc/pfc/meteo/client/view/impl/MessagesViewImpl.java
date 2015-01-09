package es.uned.grc.pfc.meteo.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.view.IMessagesView;
import es.uned.grc.pfc.meteo.client.view.util.CustomCellTableResources;

public class MessagesViewImpl extends Composite implements IMessagesView  {
   
   interface MessagesViewImplUiBinder extends UiBinder <HTMLPanel, MessagesViewImpl> {
   }
   private static MessagesViewImplUiBinder uiBinder = GWT.create (MessagesViewImplUiBinder.class);
   
   @Inject
   protected EventBus eventBus = null;
   @Inject
   protected PlaceController placeController = null;

   protected CustomCellTableResources cellTableResources = GWT.create (CustomCellTableResources.class);
   
   @UiField
   protected HTMLPanel messagePanel = null;
   
   public MessagesViewImpl () {
      initWidget (uiBinder.createAndBindUi (this));
   }

   @Override
   public Widget asWidget () {
      return this;
   }

   @Override
   public HTMLPanel getMessagePanel () {
      return messagePanel;
   }
}
