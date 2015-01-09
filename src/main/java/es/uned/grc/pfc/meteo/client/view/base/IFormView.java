package es.uned.grc.pfc.meteo.client.view.base;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;

public interface IFormView <E extends IEntityProxy> extends IView, IValidatableForm {
   /** gets the mode for edition/display */
   EditorMode getEditorMode ();
   /** sets the mode for edition/display */
   void setEditorMode (EditorMode editorMode);
   /** clear validation errors */
   void clearErrors ();
   /** cancel all creation-related info */
   void cancelCreation ();
   /** cancels an EDIT operation (by reloading the bean info) */
   void cancelEdition ();
   /** get the internally edited entity */
   E getEntityProxy ();
   /** persist the entity server-side. If null is returned, validation WAS NOT SUCCESSFUL!! */
   E save () throws Exception;
   /** if a form has unsaved local changes */
   boolean isDirty ();
}
