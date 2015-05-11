package es.uned.grc.pfc.meteo.client.view.action;

import com.google.gwt.user.client.ui.Panel;

import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;
import es.uned.grc.pfc.meteo.client.view.base.IView;

public interface IUserSetupActionsView extends IView {
   /** sets the editor mode to the action panels */
   void setEditorMode (EditorMode editorMode);
   /** get the element that the user clicks to go set a form editable */
   IHasActionHandlers getEditHandler ();
   /** get the element that the user clicks to save changes produced in the form */
   IHasActionHandlers getSaveHandler ();
   /** get the element that the user clicks to cancel changes produced in the form */
   IHasActionHandlers getCancelHandler ();
   /** get the container for viewing actions */
   Panel getViewActionsPanel ();
   /** get the container for edit actions */
   Panel getEditActionsPanel ();
}