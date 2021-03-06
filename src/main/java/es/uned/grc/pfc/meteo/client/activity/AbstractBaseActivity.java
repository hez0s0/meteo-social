package es.uned.grc.pfc.meteo.client.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

import es.uned.grc.pfc.meteo.client.place.AbstractPlace;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.view.base.IActionHandler;
import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;

/**
 * Basic Activity to be extended by any other Activity in the system.
 */
public abstract class AbstractBaseActivity extends AbstractActivity {

   protected PlaceController placeController = null;
   protected AbstractPlace place = null;

   private IRequestFactory requestFactory = null;

   private List <HandlerRegistration> registrations = new ArrayList <HandlerRegistration> ();

   /**
    * Invoked when an activity is started. Eventually, this is the point to
    * invoke server-side and set received data on the display.
    */
   @Override
   public abstract void start (AcceptsOneWidget panel, EventBus eventBus);

   public AbstractBaseActivity (PlaceController placeController, AbstractPlace place) {
      this.placeController = placeController;
      this.place = place;
   }

   public IRequestFactory getRequestFactory (EventBus eventBus) {
      if (requestFactory == null) {
         requestFactory = GWT.create (IRequestFactory.class);
         requestFactory.initialize (eventBus);
      }
      return requestFactory;
   }

   public void setRequestFactory (IRequestFactory requestFactory) {
      this.requestFactory = requestFactory;
   }

   /**
    * Navigate to a new Place in the browser
    */
   public void goTo (Place place) {
      placeController.goTo (place);
   }

   /**
    * Ask user before stopping this activity
    */
   @Override
   public String mayStop () {
      return null;
   }

   /**
    * Gets the URL of the module (for history mapping).
    */
   protected String getModuleUrl () {
      String moduleUrl = Location.getHref ();

      if (moduleUrl.indexOf ("#") != -1) {
         moduleUrl = moduleUrl.substring (0, Location.getHref ().indexOf ("#"));
      }

      return moduleUrl;
   }

   @Override
   public void onStop () {
      // clear click handlers
      for (HandlerRegistration registration : registrations) {
         registration.removeHandler ();
      }
      registrations.clear ();
   }
   
   protected void registerOpenHandler (DisclosurePanel disclosurePanel, OpenHandler <DisclosurePanel> handler) {
      registrations.add (disclosurePanel.addOpenHandler (handler));
   }
   
   protected void registerHandler (IHasActionHandlers actionable, IActionHandler handler) {
      registrations.add (actionable.addActionHandler (handler));
   }
   
   protected void registerHandler (HasKeyPressHandlers pressable, KeyPressHandler handler) {
      registrations.add (pressable.addKeyPressHandler (handler));
   }
   
   protected void registerHandler (HasClickHandlers clickable, ClickHandler handler) {
      registrations.add (clickable.addClickHandler (handler));
   }
   
   protected void registerHandler (MultiSelectionModel <?> selectionModel, Handler handler) {
      registrations.add (selectionModel.addSelectionChangeHandler (handler));
   }
   
   protected <T> void registerHandler (HasValueChangeHandlers <T> changeable, ValueChangeHandler <T> handler) {
      registrations.add (changeable.addValueChangeHandler (handler));
   }

   protected Integer getEntityID () {
      return place.getEntityID ();
   }

   /**
    * Hide elements that should not be visible because of the logged user roles.
    * NOTE this method shall never show, only hide.
    * To be invoked by implementing classes on demand, this class does not enforce its usage.
    */
   protected void applyRoleVisibility () {
   }
}
