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
import es.uned.grc.pfc.meteo.client.event.StationSetupEvent;
import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.place.StationSetupPlace;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.request.IStationRequestContext;
import es.uned.grc.pfc.meteo.client.validator.ConstraintViolationWrapper;
import es.uned.grc.pfc.meteo.client.view.IStationSetupView;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.base.AbstractFormView;
import es.uned.grc.pfc.meteo.client.view.form.StationForm;

public class StationSetupViewImpl extends AbstractFormView <IStationProxy> implements IStationSetupView {
   interface ViewUiBinder extends UiBinder <HTMLPanel, StationSetupViewImpl> {
   }
   private static ViewUiBinder uiBinder = GWT.create (ViewUiBinder.class);
   
   @Inject
   EventBus eventBus = null;
   @Inject
   PlaceController placeController = null;

   @UiField
   StationForm stationForm = null;

   public StationSetupViewImpl () {
      initWidget (uiBinder.createAndBindUi (this));
      
      stationForm.setVisible (true);
   }

   @Override
   public Widget asWidget () {
      return this;
   }

   @Override
   public void setInput (IStationProxy station, IRequestFactory requestFactory, RequestContext requestContext, PlaceController placeController) {
      station = requestContext.edit (station);
      super.setInput (station, requestFactory, requestContext, placeController);

      stationForm.setInput (entityProxy, requestFactory, requestContext, placeController, eventBus);
      dirty = false;
   }

   @Override
   public void notifyEditMode (EditorMode editorMode) {
      stationForm.notifyEditMode (editorMode);
   }

   @Override
   public Place getCancelCreationPlace () {
      return new StationSetupPlace ();
   }

   @Override
   public void display (IStationProxy station) {
      stationForm.display (station);
      dirty = false;
   }

   @Override
   public void addPathToSpanMap (String prefix, Map <String, SpanElement> pathToSpanMap, Map <String, DisclosurePanel> disclosurePanels) {
      stationForm.addPathToSpanMap (prefix, pathToSpanMap, disclosurePanels);
   }

   @Override
   protected Set <ConstraintViolationWrapper> validate (IStationProxy station) {
      Set <ConstraintViolationWrapper> violations = new HashSet <ConstraintViolationWrapper> ();
      runDefaultValidation (station, "", violations);
      return violations;
   }

   @Override
   protected void displayServerValidationError (ConstraintViolation <?> violation) {
      stationForm.displayServerValidationError (violation);
   }

   @Override
   protected void clearServerErrors () {
      stationForm.clearServerErrors ();
   }

   @Override
   protected IStationProxy loadFormIntoEntity (IStationProxy editableProxy) {
      return stationForm.getEntity (entityProxy);
   }

   @Override
   protected Class <IStationProxy> getEntityProxyClass () {
      return IStationProxy.class;
   }

   @Override
   protected IStationRequestContext createNewSaveRequestContext () {
      return requestFactory.getStationContext ();
   }
   
   @Override
   protected Request <?> getSaveRequest (IStationProxy entityProxy) {
//      return ((IStationRequestContext) getRequestContext ()).saveStation (entityProxy); //TODO
      return null;
   }

   @Override
   protected Receiver <?> getSaveReceiver () {
      return new Receiver <List <String>> () {
         @Override
         public void onSuccess (List <String> response) {
            if (response.isEmpty ()) {
               // fire config event and forward to success place
               placeController.goTo (new StationSetupPlace ());
            } else {
               // display the errors and reset element (because it will not be saveable after error due to RequestFactory restrictions!!)
               eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, response, (Exception) null));
               eventBus.fireEvent (new StationSetupEvent ());
            }
         }

         @Override
         public void onFailure (ServerFailure serverFailure) {
            eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, MessageChangeEvent.getTextMessages ().saveElementError ("Station"), serverFailure));
         }

         @Override
         public void onConstraintViolation (final Set <ConstraintViolation <?>> violations) {
            displayServerValidationErrors (violations);
         }
      };
   }

   @Override
   public void addDataFields (List <HasValueChangeHandlers <?>> dataFields) {
      stationForm.addDataFields (dataFields);
   }

   @Override
   protected boolean monitorDirtyness () {
      return true;
   }
}
