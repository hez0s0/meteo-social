package es.uned.grc.pfc.meteo.client.view.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.validator.ConstraintViolationWrapper;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.util.DisplayUtils;
import es.uned.grc.pfc.meteo.client.view.widget.GlassPanel;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.ValidationErrorsDialogBox;
import es.uned.grc.pfc.meteo.shared.validator.ClientValidationGroup;

public abstract class AbstractFormView <E extends IEntityProxy> extends AbstractPage implements IFormView <E>, IValidatableForm {

   protected Request <?> saveRequest = null;
   
   protected EditorMode editorMode = null;
   protected E originalEntityProxy = null;
   protected E entityProxy = null;
   protected IRequestFactory requestFactory = null;
   protected RequestContext requestContext = null;
   protected PlaceController placeController = null;
   protected Map <String, SpanElement> pathToSpanMap = null;
   protected Map <String, DisclosurePanel> disclosurePanels = null;
   protected List <HasValueChangeHandlers <?>> dataFields = null;
   protected boolean dirty = false;
   protected boolean changeListenersAdded = false;
//   protected boolean registeredSave = false;
   protected boolean mustClone = false;

   /** sets visible and enable status of internal widgets */
   public abstract void notifyEditMode (EditorMode editorMode);
   /** provides the Place to redirect upon cancel of a creation */
   protected abstract Place getCancelCreationPlace ();
   /** loads info from the bean proxy into the widgets */
   protected abstract void display (E entityProxy);
   /** validate if fields in the forms are correct for entity proxy */
   protected abstract Set <ConstraintViolationWrapper> validate (E entityProxy);
   /** remove the validation errors for server-side errors */
   protected abstract void clearServerErrors ();
   /** from the info provided in the forms, fill the entity */ 
   protected abstract E loadFormIntoEntity (E editableProxy);
   
   protected abstract Request <?> getSaveRequest (E entityProxy);
   protected abstract Receiver <?> getSaveReceiver ();
   
   protected abstract Class <E> getEntityProxyClass ();
   protected abstract RequestContext createNewSaveRequestContext ();
   
   /** 
    * get the list of form fields that should be scanned for changes to consider
    * that a form "isDirty", that is, contains unsaved changes
    */
   public abstract void addDataFields (List <HasValueChangeHandlers <?>> dataFields);
   /** if event handlers should be added to dataFields to check for dirty forms */
   protected abstract boolean monitorDirtyness ();
   
   @Override
   public boolean isDirty () {
      return dirty;
   }
   
   /**
    * Prepare for edition (unfreeze bean) or view, and notify editing mode to children.
    */
   public void setInput (E entityProxy, IRequestFactory requestFactory, RequestContext requestContext, PlaceController placeController) {
      this.requestContext = requestContext;
      this.requestFactory = requestFactory;
      this.entityProxy = entityProxy;
      this.originalEntityProxy = entityProxy;
      this.placeController = placeController;
      
      //initialize internal structures
      editorMode = null;
      pathToSpanMap = null;
      disclosurePanels = null;
      dataFields = null;
      dirty = false;
      changeListenersAdded = false;
      mustClone = false;
      
      pathToSpanMap = getPathToSpanMapInstance ();
      disclosurePanels = getDisclosurePanelsInstance ();
      dataFields = getDataFieldsInstance ();
      
      clearErrors ();
//      registeredSave = false;
      mustClone = false;
      saveRequest = null;
      
      setEditorMode ((entityProxy.getId () == null) ? EditorMode.CREATE : EditorMode.VIEW);
   }
   
   @Override
   public EditorMode getEditorMode () {
      return editorMode;
   }

   @Override
   public void setEditorMode (EditorMode editorMode) {
      this.editorMode = editorMode;
      if ( (!this.editorMode.equals (EditorMode.VIEW)) && (entityProxy != null) ) {
         entityProxy = requestContext.edit (entityProxy); //important: we must use the returned proxy from edit !!
      }
      notifyEditMode (editorMode);
   }

   @Override
   public E getEntityProxy () {
      return entityProxy;
   }

   public void setEntityProxy (E entityProxy) {
      this.entityProxy = entityProxy;
   }

   public RequestContext getRequestContext () {
      return requestContext;
   }

   public void setRequestContext (RequestContext requestContext) {
      this.requestContext = requestContext;
   }
   
   @Override
   public void clearErrors () {
      DisplayUtils.clearErrors (pathToSpanMap);
      clearServerErrors ();
   }
   
   protected E fireSave (E entityProxy) {
      this.entityProxy = entityProxy;
      this.entityProxy.setValidate (true); //the root entity must be validated
      
      if (saveRequest == null) {
         saveRequest = getSaveRequest (this.entityProxy);
      }
      
      // fire request with a distraction
      GlassPanel.fireDistraction ((AcceptsOneWidget) getParent (),
                                  (IsWidget) asWidget (), 
                                  saveRequest,
                                  getSaveReceiver ());
      
      return this.entityProxy;
   }
   
   @Override
   public void cancelCreation () {
      placeController.goTo (getCancelCreationPlace ());
   }
   
   @Override
   public void cancelEdition () {
      clearErrors ();
      setEditorMode (EditorMode.VIEW);
      entityProxy = originalEntityProxy;
      display (entityProxy);
      dirty = false;
   }

   protected List <HasValueChangeHandlers <?>> getDataFieldsInstance () {
      if (dataFields == null) {
         dataFields = new ArrayList <HasValueChangeHandlers <?>> ();

         addDataFields (dataFields);
      }
      
      if ( (!changeListenersAdded) && (monitorDirtyness ()) ) {
         addChangeListeners (dataFields);
         changeListenersAdded = true;
      }
      
      return dataFields;
   }
   
   @SuppressWarnings ({ "unchecked", "rawtypes" })
   private void addChangeListeners (List <HasValueChangeHandlers <?>> dataFields) {
      for (HasValueChangeHandlers <?> dataField : dataFields) {
         dataField.addValueChangeHandler (new ValueChangeHandler () {
            @Override
            public void onValueChange (ValueChangeEvent event) {
               dirty = true;
            }
         });
      }
   }
   
   protected Map <String, SpanElement> getPathToSpanMapInstance () {
      if (pathToSpanMap == null) {
         pathToSpanMap = new HashMap <String, SpanElement> ();
         disclosurePanels = new HashMap <String, DisclosurePanel> ();
         
         addPathToSpanMap ("", pathToSpanMap, disclosurePanels);
      }
      return pathToSpanMap;
   }
   
   protected Map <String, DisclosurePanel> getDisclosurePanelsInstance () {
      getPathToSpanMapInstance ();
      return disclosurePanels;
   }
   
   protected void wrapViolations (Set <ConstraintViolation <IEntityProxy>> violations, String prefix, Set <ConstraintViolationWrapper> result) {
      if ( (result != null) && (violations != null) ) {
         for (ConstraintViolation <IEntityProxy> violation : violations) {
            result.add (new ConstraintViolationWrapper (prefix, violation));
         }
      }
   }
   
   protected void runDefaultValidation (IEntityProxy entityProxy, String prefix, Set <ConstraintViolationWrapper> result) {
      Validator validator = Validation.buildDefaultValidatorFactory ().getValidator ();
      Set <ConstraintViolation <IEntityProxy>> violations = validator.validate (entityProxy, Default.class, ClientValidationGroup.class);
      
      wrapViolations (violations, prefix, result);
   }
   
   public void displayValidationErrors (Set <ConstraintViolationWrapper> violationWrappers) {
      SpanElement span = null;
      String property = null;
      SpanElement firstSpan = null;
//      StringBuffer sb = new StringBuffer ();
      if (pathToSpanMap != null) {
         for (ConstraintViolationWrapper violationWrapper : violationWrappers) {
            property = violationWrapper.getPrefix () + violationWrapper.getViolation ().getPropertyPath ().toString ();

            expandDisclosurePanels (property);
            
            span = pathToSpanMap.get (property);
//            sb.append (property + "\\n");
            DisplayUtils.showSpan (span, IClientConstants.textMessages.fieldMessage (violationWrapper.getViolation ().getMessage ()));
            
            if (firstSpan == null) {
               firstSpan = span;
            }
         }
         
         if (firstSpan != null) { 
            firstSpan.scrollIntoView ();
         }
      }
      ValidationErrorsDialogBox.showDialog ();
//      AlertDialogBox.showWarning (sb.toString ());
   }
   
   protected void expandDisclosurePanels (String property) {
      for (Map.Entry <String, DisclosurePanel> entry : disclosurePanels.entrySet ()) {
         if (property.startsWith (entry.getKey ())) {
            entry.getValue ().setOpen (true);
         }
      }
   }
   
   public void displayServerValidationErrors (Set <ConstraintViolation <?>> violations) {
      for (ConstraintViolation <?> violation : violations) {
//         AlertDialogBox.showWarning ("'" + violation.getPropertyPath ().toString () + "' " + violation.getMessage () + "; ");
         displayServerValidationError (violation);
      }
      ValidationErrorsDialogBox.showDialog ();
   }
   
   protected void displayServerValidationError (ConstraintViolation <?> violation) {
      //to be implemented by children, since this requires knowledge of the composition of the beans
   }
   
   public static String getPropertyPath (String prefix, String property) {
      StringBuffer result = new StringBuffer ();
      
      if ( (prefix != null) && (!prefix.trim ().isEmpty ()) ) {
         result.append (prefix);
      }
      result.append (property);
      
      return result.toString ();
   }
   
   @Override
   public E save () {
      E entityProxy = this.entityProxy;
      Set <ConstraintViolationWrapper> violations = null;
      
      if (requestContext == null) {
         throw new RuntimeException ("Entity proxy not set for edition");
      }
      clearErrors ();

      // load the info provided by different fields into an entity proxy, and flush it to the server-side
      entityProxy = loadFormIntoEntity (entityProxy); //load info
      violations = validate (entityProxy);
      if (violations.isEmpty()) {
         setEntityProxy (entityProxy);
//         if (mustClone) {
//            //a new request context was created, the proxy must be cloned and bound to it
//            entityProxy = EntityUtils.cloneProxyToNewContext (getEntityProxyClass (), entityProxy, requestContext);
//         }
         
         return fireSave (entityProxy);
      } else { //validation errors
         displayValidationErrors (violations);
         return null;
      }
   }
   
   /**
    * Implementations of this class MUST call this method in their onFailure handler for save if they
    * want to be able to retry a save after server-side error (requestContext is not reusable by
    * default after such).
    */
   protected void handleServerFailure (Class <E> entityClass, RequestContext newRequestContext) {
      //a new request context was created, the proxy must be cloned and bound to it
//      entityProxy = EntityUtils.cloneProxyToNewContext (entityClass, entityProxy, newRequestContext);
//
//      requestContext = newRequestContext; //old requestContext is discarded

//      registeredSave = false;
//      mustClone = true;
   }
}
