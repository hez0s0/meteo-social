package es.uned.grc.pfc.meteo.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.util.CommonUtils;
import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.client.view.IHeaderView;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

/**
 * Implementation of the HeaderView with UiBinder.
 */
public class HeaderViewImpl extends Composite implements IHeaderView  {
   interface HeaderViewImplUiBinder extends UiBinder <HTMLPanel, HeaderViewImpl> {
   }
   private static HeaderViewImplUiBinder uiBinder = GWT.create (HeaderViewImplUiBinder.class);
   
   @UiField
   protected Image esImage = null;
   @UiField
   protected Image enImage = null;
   @UiField
   protected Label userLabel = null;
   
   @Inject
   protected PlaceController placeController = null;
   @Inject
   protected IRequestFactory requestFactory = null;

   public HeaderViewImpl () {
      initWidget (uiBinder.createAndBindUi (this));
      
      esImage.setUrl (PortableStringUtils.format (ISharedConstants.FLAG_IMAGE_URL, CommonUtils.getBaseUrl (), "es"));
      enImage.setUrl (PortableStringUtils.format (ISharedConstants.FLAG_IMAGE_URL, CommonUtils.getBaseUrl (), "gb"));
      
      bind ();
   }
   
   @Override
   public void setInput (IRequestFactory requestFactory) {
      this.requestFactory = requestFactory;
   }

   private void bind () {
   }

   @Override
   public Widget asWidget () {
      return this;
   }

   @Override
   public HasClickHandlers getEsButton () {
      return esImage;
   }

   @Override
   public HasClickHandlers getEnButton () {
      return enImage;
   }

   @Override
   public HasText getUserLabel () {
      return userLabel;
   }
}
