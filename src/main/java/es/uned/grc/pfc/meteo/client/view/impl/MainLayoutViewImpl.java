package es.uned.grc.pfc.meteo.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.activity.mapper.MainActivityMapper;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;
import es.uned.grc.pfc.meteo.client.view.IMainLayoutView;
import es.uned.grc.pfc.meteo.client.view.util.FormUtils;

public class MainLayoutViewImpl extends Composite implements IMainLayoutView {

   interface MainLayoutViewUiBinder extends UiBinder <ScrollPanel, MainLayoutViewImpl> {
   }
   private static MainLayoutViewUiBinder uiBinder = GWT.create (MainLayoutViewUiBinder.class);
   
   @UiField
   protected SimplePanel headerPanel = null;
   @UiField
   protected SimplePanel menuPanel = null;
   @UiField
   protected SimplePanel searchPanel = null;
   @UiField
   protected SimplePanel messagesPanel = null;
   @UiField
   protected SimplePanel mainPanel = null;
   @UiField
   protected SimplePanel actionsPanel = null;

   @UiField
   protected DivElement mainDiv = null;
   @UiField
   protected DivElement actionsDiv = null;
   
   @UiField
   protected Label detailsTitleLabel = null;
   @UiField
   protected Label backToListLabel = null;
   @UiField
   protected Anchor homeAnchor = null;

   private MainActivityMapper mainActivityMapper = null;
   @Inject
   PlaceController placeController = null;
   
   @Inject
   public MainLayoutViewImpl () {
      // Bind the UI with myself
      initWidget (uiBinder.createAndBindUi (this));
      
      homeAnchor.addClickHandler (new ClickHandler() {
         @Override
         public void onClick (ClickEvent event) {
            FormUtils.goConditionallyToPlace (mainActivityMapper, new ObservationListPlace (), placeController);
         }
      });
   }
   
   @Override
   public SimplePanel getHeaderPanel () {
      return headerPanel;
   }
   
   @Override
   public SimplePanel getMenuPanel () {
      return menuPanel;
   }
   
   @Override
   public SimplePanel getSearchPanel () {
      return searchPanel;
   }
   
   @Override
   public SimplePanel getMessagesPanel () {
      return messagesPanel;
   }
   
   @Override
   public SimplePanel getMainPanel () {
      return mainPanel;
   }
   
   @Override
   public SimplePanel getActionsPanel () {
      return actionsPanel;
   }
   
   @Override
   public DivElement getMainDiv () {
      return mainDiv;
   }

   @Override
   public DivElement getActionsDiv () {
      return actionsDiv;
   }

   @Override
   public Label getDetailsTitle () {
      return detailsTitleLabel;
   }

   @Override
   public Label getBackToList () {
      return backToListLabel;
   }

   @Override
   public void setMainActivityMapper (MainActivityMapper mainActivityMapper) {
      this.mainActivityMapper = mainActivityMapper;
   }
   
}
