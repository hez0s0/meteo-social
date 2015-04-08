package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.activity.mapper.MainActivityMapper;
import es.uned.grc.pfc.meteo.client.place.AbstractPlace;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.view.IHeaderView;
import es.uned.grc.pfc.meteo.client.view.util.FormUtils;

/**
 * Activity (presenter) that acts as a bridge between aplication logic
 * and {@link IHeaderView} representation.
 */
public class HeaderActivity extends AbstractBaseActivity {
   
   private MainActivityMapper mainActivityMapper = null;
   private IHeaderView headerView = null;
   
   @Inject
   public HeaderActivity (MainActivityMapper mainActivityMapper, AbstractPlace place, IHeaderView headerView, PlaceController placeController) {
      super (placeController, place);

      this.mainActivityMapper = mainActivityMapper;
      this.headerView = headerView;
   }
   
   @Override
   public void start (AcceptsOneWidget panel, final EventBus eventBus) {
      panel.setWidget (headerView.asWidget ());
      
      bind (eventBus);
      
      headerView.setInput (getRequestFactory (eventBus));
   }
   
   private void bind (EventBus eventBus) {
      /** set ES locale */
      registerHandler (headerView.getEsButton (), new ClickHandler () {
         @Override
         public void onClick (ClickEvent event) {
            setLocale (IClientConstants.LOCALE_SPANISH);
         }
      });
      /** set GB locale */
      registerHandler (headerView.getEnButton (), new ClickHandler () {
         @Override
         public void onClick (ClickEvent event) {
            setLocale (IClientConstants.LOCALE_ENGLISH);
         }
      });
   }

   private void setLocale (String locale) {
      Cookies.setCookie (IClientConstants.LOCALE_COOKIE_NAME, locale);
      FormUtils.reloadConditionally (mainActivityMapper);
   }
}
