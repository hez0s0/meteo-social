package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceChangeRequestEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import es.uned.grc.pfc.meteo.client.activity.mapper.MainActivityMapper;
import es.uned.grc.pfc.meteo.client.place.AbstractPlace;
import es.uned.grc.pfc.meteo.client.place.StationSetupPlace;
import es.uned.grc.pfc.meteo.client.place.UserSetupPlace;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;
import es.uned.grc.pfc.meteo.client.view.IMenuView;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.ActionDialogBox;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.ActionDialogBoxClickEvent;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.ConfirmationDialogBox;
import es.uned.grc.pfc.meteo.client.view.widget.dialog.IActionDialogBoxEventHandler;

/**
 * Activity (presenter) that acts as a bridge between the view and the
 * server-side services. Manages the logic for menu display
 */
public class MenuActivity extends AbstractBaseActivity {
   private static boolean bound = false;

   private MainActivityMapper mainActivityMapper = null;
   private IMenuView menuView = null;
   
   public MenuActivity (MainActivityMapper mainActivityMapper, AbstractPlace place, IMenuView menuView, PlaceController placeController) {
      super (placeController, place);

      this.mainActivityMapper = mainActivityMapper;
      this.menuView = menuView;
   }

   @Override
   public void start (AcceptsOneWidget panel, final EventBus eventBus) {
      panel.setWidget (menuView.asWidget ());
      applyRoleVisibility ();
      bind (eventBus);
   }

   /**
    * Wire all click handlers
    */
   private void bind (final EventBus eventBus) {
      if (!bound) {
         menuView.getRefreshMenuItem ().setScheduledCommand (new Command () {
            public void execute () {
               if (mainActivityMapper.isMainViewDirty ()) { 
                  //if there are unsaved changes, confirmation is needed to proceed
                  ConfirmationDialogBox.askConfirmation (IClientConstants.TEXT_CONSTANTS.pendingChangesQuestion ()).addClickHandler (ActionDialogBox.ButtonType.ACCEPT, new IActionDialogBoxEventHandler () {
                     @Override
                     public void onActionClicked (ActionDialogBoxClickEvent event) {
                        refreshView (eventBus);
                     }
                  });
               } else {
                  refreshView (eventBus);
               }
            }
         });
         menuView.getStationConfigurationMenuItem ().setScheduledCommand (new Command () {
            public void execute () {
               if (mainActivityMapper.isMainViewDirty ()) { 
                  //if there are unsaved changes, confirmation is needed to proceed
                  ConfirmationDialogBox.askConfirmation (IClientConstants.TEXT_CONSTANTS.pendingChangesQuestion ()).addClickHandler (ActionDialogBox.ButtonType.ACCEPT, new IActionDialogBoxEventHandler () {
                     @Override
                     public void onActionClicked (ActionDialogBoxClickEvent event) {
                        placeController.goTo (new StationSetupPlace ());
                     }
                  });
               } else {
                  placeController.goTo (new StationSetupPlace ());
               }
            }
         });
         menuView.getUserConfigurationMenuItem ().setScheduledCommand (new Command () {
            public void execute () {
               if (mainActivityMapper.isMainViewDirty ()) { 
                  //if there are unsaved changes, confirmation is needed to proceed
                  ConfirmationDialogBox.askConfirmation (IClientConstants.TEXT_CONSTANTS.pendingChangesQuestion ()).addClickHandler (ActionDialogBox.ButtonType.ACCEPT, new IActionDialogBoxEventHandler () {
                     @Override
                     public void onActionClicked (ActionDialogBoxClickEvent event) {
                        placeController.goTo (new UserSetupPlace ());
                     }
                  });
               } else {
                  placeController.goTo (new UserSetupPlace ());
               }
            }
         });

         bound = true;
      }
   }

   private void refreshView (final EventBus eventBus) {
      Place currentPlace = placeController.getWhere ();
      //Taken from PlaceController.maybeGoTo()
      PlaceChangeRequestEvent willChange = new PlaceChangeRequestEvent (currentPlace);
      eventBus.fireEvent (willChange);
      String warning = willChange.getWarning ();
      //Taken from PlaceController.goTo ()
      if (warning == null) {
         eventBus.fireEvent (new PlaceChangeEvent (currentPlace));
      }
   }
}
