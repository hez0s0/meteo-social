package es.uned.grc.pfc.meteo.client.view.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import es.uned.grc.pfc.meteo.client.model.IUserProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.base.AbstractFormView;
import es.uned.grc.pfc.meteo.client.view.base.IEditNotifiableWidget;
import es.uned.grc.pfc.meteo.client.view.base.IValidatableForm;
import es.uned.grc.pfc.meteo.client.view.base.IView;
import es.uned.grc.pfc.meteo.client.view.util.DisplayUtils;
import es.uned.grc.pfc.meteo.client.view.util.FormUtils;

public class UserForm  extends Composite implements IView, IEditNotifiableWidget, IValidatableForm {
   interface FormUiBinder extends UiBinder <HTMLPanel, UserForm> {
   }
   private static FormUiBinder uiBinder = GWT.create (FormUiBinder.class);

   protected RequestContext requestContext = null;
   protected EventBus eventBus = null;
   protected IUserProxy user = null;
   protected Map <String, SpanElement> propertySpanMap = new HashMap <String, SpanElement> ();
   
   @UiField
   protected TextBox nameTextBox = null;
   @UiField
   protected TextBox familyNameTextBox = null;

   // NOW COME THE VALIDATION DISPLAY SPANS
   @UiField
   protected SpanElement nameSpan = null;
   @UiField
   protected SpanElement familyNameSpan = null;
   
   public UserForm () {
      initWidget (uiBinder.createAndBindUi (this));
      
      propertySpanMap.put ("firstName", nameSpan);
      propertySpanMap.put ("familyName", familyNameSpan);
   }

   protected void init (IUserProxy user,
                        IRequestFactory requestFactory, 
                        RequestContext requestContext, 
                        PlaceController placeController,
                        EventBus eventBus) {
      this.requestContext = requestContext;
      this.user = user;
      this.eventBus = eventBus;
   }
   
   public void setInput (IUserProxy user, IRequestFactory requestFactory, RequestContext requestContext, PlaceController placeController, EventBus eventBus) {
      init (user, requestFactory, requestContext, placeController, eventBus);
      
      display (user);
   }
   
   @Override
   public void notifyEditMode (EditorMode editorMode) {
      FormUtils.notifyEditMode (editorMode, nameTextBox);
      FormUtils.notifyEditMode (editorMode, familyNameTextBox);
   }

   public void display (IUserProxy user) {
      nameTextBox.setText (user.getFirstName ());
      familyNameTextBox.setText (user.getFamilyName ());
   }

   @Override
   public void addPathToSpanMap (String prefix, Map <String, SpanElement> pathToSpanMap, Map <String, DisclosurePanel> disclosurePanels) {
      pathToSpanMap.put (AbstractFormView.getPropertyPath (prefix, "firstName"), nameSpan);
      pathToSpanMap.put (AbstractFormView.getPropertyPath (prefix, "familyName"), familyNameSpan);
   }
   
   /**
    * Composes the entity from the fields.
    */
   public IUserProxy getEntity (IUserProxy user) {
      user.setFirstName (nameTextBox.getText ());
      user.setFamilyName (familyNameTextBox.getText ());
      
      return user;
   }
   
   public void displayServerValidationError (ConstraintViolation <?> violation) {
      DisplayUtils.showSpan (propertySpanMap.get (violation.getPropertyPath ().toString ()), violation.getMessage ());
   }

   public void clearServerErrors () {
      DisplayUtils.clearErrors (propertySpanMap);
   }
   
   public void addDataFields (List <HasValueChangeHandlers <?>> dataFields) {
      dataFields.add (nameTextBox);
      dataFields.add (familyNameTextBox);
   }
}
