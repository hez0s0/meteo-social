package es.uned.grc.pfc.meteo.client.view.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import es.uned.grc.pfc.meteo.client.event.MessageChangeEvent;
import es.uned.grc.pfc.meteo.client.model.IUserProxy;
import es.uned.grc.pfc.meteo.client.place.UserSetupPlace;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.request.IStationRequestContext;
import es.uned.grc.pfc.meteo.client.validator.ConstraintViolationWrapper;
import es.uned.grc.pfc.meteo.client.view.IUserSetupView;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.base.AbstractFormView;
import es.uned.grc.pfc.meteo.client.view.form.UserForm;

public class UserSetupViewImpl extends AbstractFormView <IUserProxy> implements IUserSetupView {
   interface ViewUiBinder extends UiBinder <HTMLPanel, UserSetupViewImpl> {
   }
   private static ViewUiBinder uiBinder = GWT.create (ViewUiBinder.class);
   
   @Inject
   EventBus eventBus = null;
   @Inject
   PlaceController placeController = null;

   @UiField
   UserForm userForm = null;

   public UserSetupViewImpl () {
      initWidget (uiBinder.createAndBindUi (this));
      
      userForm.setVisible (true);
   }

   @Override
   public Widget asWidget () {
      return this;
   }

   @Override
   public void setInput (IUserProxy user, IRequestFactory requestFactory, RequestContext requestContext, PlaceController placeController) {
      user = requestContext.edit (user);
      super.setInput (user, requestFactory, requestContext, placeController);

      userForm.setInput (entityProxy, requestFactory, requestContext, placeController, eventBus);
      dirty = false;
   }

   @Override
   public void notifyEditMode (EditorMode editorMode) {
      userForm.notifyEditMode (editorMode);
   }

   @Override
   public Place getCancelCreationPlace () {
      return new UserSetupPlace ();
   }

   @Override
   public void display (IUserProxy user) {
      userForm.display (user);
      dirty = false;
   }

   @Override
   public void addPathToSpanMap (String prefix, Map <String, SpanElement> pathToSpanMap, Map <String, DisclosurePanel> disclosurePanels) {
      userForm.addPathToSpanMap (prefix, pathToSpanMap, disclosurePanels);
   }

   @Override
   protected Set <ConstraintViolationWrapper> validate (IUserProxy user) {
      Set <ConstraintViolationWrapper> violations = new HashSet <ConstraintViolationWrapper> ();
      runDefaultValidation (user, "", violations);
      return violations;
   }

   @Override
   protected void displayServerValidationError (ConstraintViolation <?> violation) {
      userForm.displayServerValidationError (violation);
   }

   @Override
   protected void clearServerErrors () {
      userForm.clearServerErrors ();
   }

   @Override
   protected IUserProxy loadFormIntoEntity (IUserProxy editableProxy) {
      return userForm.getEntity (entityProxy);
   }

   @Override
   protected Class <IUserProxy> getEntityProxyClass () {
      return IUserProxy.class;
   }

   @Override
   protected IStationRequestContext createNewSaveRequestContext () {
      return requestFactory.getStationContext ();
   }
   
   @Override
   protected Request <?> getSaveRequest (IUserProxy entityProxy) {
      return ((IStationRequestContext) getRequestContext ()).saveUser (entityProxy);
   }

   @Override
   protected Receiver <?> getSaveReceiver () {
      return new Receiver <IUserProxy> () {
         @Override
         public void onSuccess (IUserProxy response) {
            // fire config event and forward to success place
            placeController.goTo (new UserSetupPlace ());
         }

         @Override
         public void onFailure (ServerFailure serverFailure) {
            eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, MessageChangeEvent.getTextMessages ().saveElementError ("User"), serverFailure));
         }

         @Override
         public void onConstraintViolation (final Set <ConstraintViolation <?>> violations) {
            displayServerValidationErrors (violations);
         }
      };
   }

   @Override
   public void addDataFields (List <HasValueChangeHandlers <?>> dataFields) {
      userForm.addDataFields (dataFields);
   }

   @Override
   protected boolean monitorDirtyness () {
      return true;
   }
}
