package es.uned.grc.pfc.meteo.client.activity.mapper;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.LocalizableResource.GenerateKeys;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

import es.uned.grc.pfc.meteo.client.activity.ObservationListActivity;
import es.uned.grc.pfc.meteo.client.place.AbstractPlace;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.view.IMainLayoutView;
import es.uned.grc.pfc.meteo.client.view.IObservationListView;
import es.uned.grc.pfc.meteo.client.view.base.IFormView;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.ActionDialogBoxClickEvent;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.ConfirmationDialogBox;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.IActionDialogBoxEventHandler;

/**
 * Mapper for the main region of the screen.
 * Responsible both for mapping the Places to the correct Activities,
 * and to listen for events that may trigger Place changes, such as
 * creation of new elements. Those events are considered out of scope
 * of one single activity, and thus are managed here.
 */
public class MainActivityMapper implements ActivityMapper {
   /** global text constants */
   @Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Observation list") @Meaning ("Detail region title")
      String observationListPlace ();
   }
   public static TextConstants textConstants = GWT.create (TextConstants.class);

   @Inject
   private PlaceController placeController = null;
   @Inject
   private EventBus eventBus = null;

   @Inject
   private IMainLayoutView mainLayoutView = null;
   @Inject
   private IObservationListView observationListView = null;
   
   private boolean init = false;

   private IRequestFactory requestFactory = null;
   
   private AbstractPlace configuredListPlace = null;
   private IFormView <?> configuredFormView = null;

   @Inject
   public MainActivityMapper () {
      
   }

   public IRequestFactory getRequestFactory (EventBus eventBus) {
      if (requestFactory == null) {
         requestFactory = GWT.create (IRequestFactory.class);
         requestFactory.initialize (eventBus);
      }
      return requestFactory;
   }

   @Override
   public Activity getActivity (Place place) {
      if (!init) {
         bind ();
         init = true;
      }

      // by default, no back to list link
      mainLayoutView.getBackToList ().setVisible (false);
      configuredListPlace = null;
      configuredFormView = null;
      
      // configure place header, back to list link and return the correct activity
      if (place instanceof ObservationListPlace) {
         mainLayoutView.getDetailsTitle ().setText (textConstants.observationListPlace ());
         return new ObservationListActivity ((ObservationListPlace) place, observationListView, placeController);
      }
      
      return null;
   }
   
   /**
    * Wire all click handlers for singleton views here
    */
   private void bind () {
      bindGeneralViewEvents ();
   }
   
   private void bindGeneralViewEvents () {
      mainLayoutView.getBackToList ().addClickHandler (new ClickHandler() {
         @Override
         public void onClick (ClickEvent event) {
            goToList ();
         }
      });
   }
   
   private void setListPlace (AbstractPlace listPlace, IFormView <?> formView) {
      mainLayoutView.getBackToList ().setVisible (true);
      configuredListPlace = listPlace;
      configuredFormView = formView;
   }

   private void goToList () {
      if (configuredListPlace != null) {
         if ( (configuredFormView != null) && (configuredFormView.isDirty ()) ) {
            ConfirmationDialogBox.askConfirmation (IClientConstants.textConstants.pendingChangesQuestion ()).addClickHandler (ConfirmationDialogBox.ButtonType.ACCEPT, new IActionDialogBoxEventHandler () {
               @Override
               public void onActionClicked (ActionDialogBoxClickEvent event) {
                  placeController.goTo (configuredListPlace);
               }
            });
         } else {
            placeController.goTo (configuredListPlace);
         }
      }
   }
   
   public boolean isMainViewDirty () {
      return (configuredFormView != null) ? configuredFormView.isDirty () : false;
   }
}
