package es.uned.grc.pfc.meteo.client.activity.action;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import es.uned.grc.pfc.meteo.client.activity.AbstractBaseActivity;
import es.uned.grc.pfc.meteo.client.event.MessageChangeEvent;
import es.uned.grc.pfc.meteo.client.event.StationSetupEvent;
import es.uned.grc.pfc.meteo.client.place.StationSetupPlace;
import es.uned.grc.pfc.meteo.client.view.IStationSetupView;
import es.uned.grc.pfc.meteo.client.view.action.IStationSetupActionsView;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.base.IActionHandler;
import es.uned.grc.pfc.meteo.client.view.base.IHasActionHandlers;

public class StationSetupActionsActivity extends AbstractBaseActivity {
   protected IStationSetupView editView = null;
   protected IStationSetupActionsView editActionsView = null;
   protected StationSetupPlace place = null;

   public StationSetupActionsActivity (StationSetupPlace place, 
                                    IStationSetupView editView, 
                                    IStationSetupActionsView editActionsView, 
                                    PlaceController placeController) {
      super (placeController, place);

      this.place = place;
      this.editView = editView;
      this.editActionsView = editActionsView;
   }

   @Override
   public void start (AcceptsOneWidget panel, final EventBus eventBus) {
      panel.setWidget (editActionsView.asWidget ());
      applyRoleVisibility ();
      bind (eventBus);
      editActionsView.setEditorMode (AbstractEntityEditor.EditorMode.VIEW);
   }

   /**
    * Binds the event handlers.
    */
   private synchronized void bind (final EventBus eventBus) {
      /** handle click on edit */ 
      registerHandler (editActionsView.getEditHandler (), new IActionHandler () {
         @Override
         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
            editView.setEditorMode (EditorMode.EDIT);
            editActionsView.getViewActionsPanel ().setVisible (false);
            editActionsView.getEditActionsPanel ().setVisible (true);
            source.setCompleted ();
         }
      });
      
      /** handle click on save */ 
      registerHandler (editActionsView.getSaveHandler (), new IActionHandler () {
         @Override
         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
            try {
               if (editView.save () != null) {
                  editActionsView.getViewActionsPanel ().setVisible (false);
                  editActionsView.getEditActionsPanel ().setVisible (true);
               }
            } catch (Exception ex) {
               eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, MessageChangeEvent.getTextMessages ().saveElementError ("Station"), ex));
            } finally {
               source.setCompleted ();
            }
         }
      });
      
      /** handle click on cancel */ 
      registerHandler (editActionsView.getCancelHandler (), new IActionHandler () {
         @Override
         public void onAction (DomEvent <?> event, final IHasActionHandlers source) {
            if (!editView.getEditorMode ().equals (EditorMode.CREATE)) { 
               //it was not a new element, let us clear up and return to view mode
               eventBus.fireEvent (new StationSetupEvent ());
            } else { 
               //we were creating new, let us allow the editor to decide were to go
               editView.cancelCreation ();
            }
            source.setCompleted ();
         }
      });
   }
}
