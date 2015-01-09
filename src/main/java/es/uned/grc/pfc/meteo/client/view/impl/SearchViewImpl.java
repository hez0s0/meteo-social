package es.uned.grc.pfc.meteo.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.view.ISearchView;

public class SearchViewImpl extends Composite implements ISearchView  {
   
   interface SearchUiBinder extends UiBinder <HTMLPanel, SearchViewImpl> {
   }
   private static SearchUiBinder uiBinder = GWT.create (SearchUiBinder.class);

   @Inject
   protected IRequestFactory requestFactory = null;
   @Inject
   protected EventBus eventBus = null;
   @Inject
   protected PlaceController placeController = null;
   
   public SearchViewImpl () {
      initWidget (uiBinder.createAndBindUi (this));
   }
   
   @Override
   public Widget asWidget () {
      return this;
   }
}
