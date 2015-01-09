package es.uned.grc.pfc.meteo.client.view.base;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import es.uned.grc.pfc.meteo.client.event.MessageChangeEvent;
import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.util.IStyleConstants;
import es.uned.grc.pfc.meteo.client.view.util.DisplayUtils;
import es.uned.grc.pfc.meteo.client.view.util.FormUtils;
import es.uned.grc.pfc.meteo.shared.validator.ClientValidationGroup;

public abstract class AbstractEntityEditor <T extends IEntityProxy> extends Composite implements Editor <T>, IView, HasEditorErrors <T> {
   public enum EditorMode {VIEW, EDIT, CREATE};

   protected T entityProxy = null;
   protected RequestFactoryEditorDriver <T, Editor <T>> editorDriver = null;
   protected IRequestFactory requestFactory = null;
   protected RequestContext requestContext = null;
   protected EditorMode editorMode = null;
   
   protected PlaceController placeController = null;
   protected EventBus eventBus = null;

   protected Map <String, SpanElement> pathToSpanMap = null;

   /** provide the mapping of fields to span elements so that validation can be performed and displayed correctly */
   public abstract Map <String, SpanElement> getPathToSpanMap ();
   /** provide an instance of the persist operation in your RequestContext */
   public abstract void save (T entityProxy) throws Exception;
   /** provide the Place where the app must forward after saving entity */
   public abstract Place getSavePlace (T entityProxy) throws Exception;
   /** provide the Place where the app must forward after canceling the creation of a (new) entity */
   public abstract Place getCancelCreatePlace ();
   /** set the proxy info into internal fields */
   protected abstract void setFields (T entityProxy) ;
   /** provide a user presentable name for the managed entity */
   protected abstract String getEntityName ();
   
   protected Receiver <T> updateReceiver = new Receiver <T> () {
      @Override
      public void onSuccess (T response) {
         clearErrors ();
         try {
            placeController.goTo (getSavePlace (response));
         } catch (Exception ex) {
            eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, 
                  MessageChangeEvent.getTextMessages ().saveElementError (getEntityName ()), ex));
         }
      }

      public void onConstraintViolation (Set <ConstraintViolation <?>> violations) {
         //not using built-in validation mechanisms
      }
   };
   
   public void setEditorDriver (RequestFactoryEditorDriver <T, Editor <T>> editorDriver) {
      this.editorDriver = editorDriver;
   }

   public void setInput (T entityProxy, IRequestFactory requestFactory, RequestContext requestContext, EditorMode editorMode, PlaceController placeController, EventBus eventBus) {
      this.entityProxy = entityProxy;
      this.placeController = placeController;
      this.requestFactory = requestFactory;
      this.requestContext = requestContext;
      this.eventBus = eventBus;

      editorDriver.initialize (this);
      setEditorMode (editorMode);
      setVisible (true);
      setFields (entityProxy);

      pathToSpanMap = getPathToSpanMapInstance ();
      
      clearErrors ();
   }
   
   public void setEditorMode (EditorMode editorMode) {
      this.editorMode = editorMode;
      
      if (editorMode == EditorMode.EDIT || editorMode == EditorMode.CREATE) {
         editorDriver.edit (entityProxy, requestContext);
      } else if (editorMode == EditorMode.VIEW) {
         editorDriver.display (entityProxy);
      }

      notifyEditMode (editorMode);
   }
   
   public EditorMode getEditorMode () {
      return this.editorMode;
   }

   public void notifyEditMode (EditorMode editorMode) {
      //nothing to do in the default implementation: to be handled by children
   }

   public void clearErrors () {
      if (pathToSpanMap != null) {
         for (SpanElement span : pathToSpanMap.values ()) {
            span.setInnerText ("");
            FormUtils.setElementVisible (span, false);
         }
      }
      notifyErrorsCleared ();
   } //end of clearErrors

   public void save () throws Exception {
      Set <ConstraintViolation <T>> violations = null;
      
      clearErrors ();
      editorDriver.flush ();
      
      violations = runDefaultValidation (entityProxy);
      if (violations.isEmpty()) {
         save (entityProxy);
      } else { //validation errors
         displayValidationErrors (violations);
      }
   } //end of save
   
   protected Set <ConstraintViolation <T>> runDefaultValidation (T entityProxy) {
      Validator validator = Validation.buildDefaultValidatorFactory ().getValidator ();
      return validator.validate (entityProxy, Default.class, ClientValidationGroup.class);
   } //end of runDefaultValidation
   
   protected void displayValidationErrors (Set <ConstraintViolation <T>> violations) {
      if (pathToSpanMap != null) {
         for (ConstraintViolation <?> violation : violations) {
            DisplayUtils.showSpan (pathToSpanMap.get (violation.getPropertyPath ().toString ()), 
                                   IClientConstants.textMessages.fieldMessage (violation.getMessage ()));
         }
      }
   } //end of displayValidationErrors
   
   public void cancelCreation () {
      placeController.goTo (getCancelCreatePlace ());
   }
   
   @Override
   public void showErrors (List <EditorError> errors) {
      //not using built-in validation of Editor FW, but custom made
   }

   public void setWidgetError (Widget w) {
      if (w != null) {
         w.addStyleName (IStyleConstants.ERROR_WIDGET);
      }
   }

   public void notifyErrorsCleared () {
      //nothing to do in the default implementation: to be handled by children
   }

   public T getEntityProxy () {
      return entityProxy;
   } //end of getEntityProxy
   
   protected Map <String, SpanElement> getPathToSpanMapInstance () {
      if (pathToSpanMap == null) {
         return getPathToSpanMap ();
      }
      return pathToSpanMap;
   } //end of getPathToSpanMapInstance
} //end of AbstractEntityEditor
