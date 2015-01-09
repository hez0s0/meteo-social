package es.uned.grc.pfc.meteo.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.LocalizableResource.GenerateKeys;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

import es.uned.grc.pfc.meteo.client.resource.IApplicationResources;
import es.uned.grc.pfc.meteo.client.util.ClientGlobals;
import es.uned.grc.pfc.meteo.client.view.base.AbstractEntityEditor.EditorMode;
import es.uned.grc.pfc.meteo.client.view.base.IActionHandler;
import es.uned.grc.pfc.meteo.client.view.util.FormUtils;

public class ButtonPanel extends HorizontalPanel {
   /**
    * The constants used in this Widget.
    */
   @Generate (format = "com.google.gwt.i18n.rebind.format.PropertiesFormat", locales = {"default"})
   @GenerateKeys ("com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator")
   public interface TextConstants extends Constants {
      @DefaultStringValue ("Delete") @Meaning ("A small sized image button")
      String delete ();
      @DefaultStringValue ("Up") @Meaning ("A small sized image button")
      String up ();
      @DefaultStringValue ("Down") @Meaning ("A small sized image button")
      String down();
      @DefaultStringValue ("Strike out selected versions") @Meaning ("A small sized image button")
      String deleteVersions ();
      @DefaultStringValue ("Remove strike out for selected versions") @Meaning ("A small sized image button")
      String undeleteVersions ();
      @DefaultStringValue ("Compare two selected versions") @Meaning ("A small sized image button")
      String compareVersions ();
      @DefaultStringValue ("Synchronize selected version into VMobil") @Meaning ("A small sized image button")
      String syncVersion ();
      @DefaultStringValue ("Edit") @Meaning ("A small sized image button")
      String edit ();
      @DefaultStringValue ("Save") @Meaning ("A small sized image button")
      String save ();
      @DefaultStringValue ("Commit for approval") @Meaning ("A small sized image button")
      String commit ();
      @DefaultStringValue ("Discard changes") @Meaning ("A small sized image button")
      String discard ();
      @DefaultStringValue ("Approve changes") @Meaning ("A small sized image button")
      String approve ();
      @DefaultStringValue ("Deny changes") @Meaning ("A small sized image button")
      String deny ();
      @DefaultStringValue ("Show/hide versions") @Meaning ("A small sized image button")
      String showVersions ();
      @DefaultStringValue ("Expropriate version (change owner to myself)") @Meaning ("A small sized image button")
      String expropriateVersion ();
      @DefaultStringValue ("Restore as current version") @Meaning ("A small sized image button")
      String restoreVersion ();
   }
   private static TextConstants textConstants = GWT.create (TextConstants.class);

   private static IApplicationResources applicationResources = ClientGlobals.getInstance ().getApplicationResources ();
   
   public enum ButtonType {
      DELETE (1), UP (2), DOWN (4), 
      EDIT (8), SAVE (16), COMMIT (32), DISCARD (64), APPROVE (128), DENY (256), SHOW_VERSIONS (512), 
      DELETE_VERSIONS (1024), UNDELETE_VERSIONS (2048), COMPARE_VERSIONS (4096), SYNC_VERSION (8192),
      EXPROPRIATE_VERSION (16384), RESTORE_VERSION (32768);

      public int value;

      ButtonType (int value) {
         this.value = value;
      }

      public int value () {
         return value;
      }
   }

   // should be in-sync with ButtonType enum
   private enum DefaultButtonInfo {Delete (applicationResources.close (), textConstants.delete ()), 
                                   Up (applicationResources.up (), textConstants.up ()), 
                                   Down (applicationResources.down (), textConstants.down ()),  
                                   Edit (applicationResources.edit (), textConstants.edit ()),  
                                   Save (applicationResources.save (), textConstants.save ()),  
                                   Commit (applicationResources.commit (), textConstants.commit ()),  
                                   Discard (applicationResources.discard (), textConstants.discard ()),  
                                   Approve (applicationResources.approve (), textConstants.approve ()),  
                                   Deny (applicationResources.deny (), textConstants.deny ()),  
                                   ShowVersions (applicationResources.showVersions (), textConstants.showVersions ()), 
                                   DeleteVersion (applicationResources.deleteVersion (), textConstants.deleteVersions ()), 
                                   UndeleteVersion (applicationResources.undeleteVersion (), textConstants.undeleteVersions ()), 
                                   CompareVersions (applicationResources.compareVersions (), textConstants.compareVersions ()),  
                                   SyncVersion (applicationResources.syncVersion (), textConstants.syncVersion ()), 
                                   ExpropriateVersion (applicationResources.expropriateVersion (), textConstants.expropriateVersion ()),  
                                   RestoreVersion (applicationResources.restoreVersion (), textConstants.restoreVersion ());
      
      private ImageResource imageResource = null;
      private String tooltip = null;
      
      DefaultButtonInfo (ImageResource imageResource, String tooltip) {
         this.imageResource = imageResource;
         this.tooltip = tooltip;
      }

      public ImageResource getImageResource () {
         return imageResource;
      }

      public String getTooltip () {
         return tooltip;
      }
   }

   public class ButtonLayout {

      private int layout = 0; // no button

      public ButtonLayout add (ButtonType buttonType) {
         layout |= buttonType.value ();
         return this;
      }

      public boolean isButtonInLayout (ButtonType buttonType) {
         int result = layout & buttonType.value ();
         return result != 0;
      }
   }

   private ButtonLayout buttonLayout = null;
   private ActionPushButton [] buttons = null;
   private EditorMode editorMode = null;
   
   public ButtonPanel () {
      init ();
   }

   private void init () {
      // create default buttons
      DefaultButtonInfo [] defaultButtonInfos = DefaultButtonInfo.values ();
      buttons = new ActionPushButton [defaultButtonInfos.length];
      for (int buttonCounter = 0; buttonCounter < defaultButtonInfos.length; buttonCounter ++) {
         ActionPushButton button = new ActionPushButton (new Image (defaultButtonInfos [buttonCounter].getImageResource ().getSafeUri ()));
         FormUtils.addAltHandler (button, defaultButtonInfos [buttonCounter].getTooltip ());
         button.setVisible (false); // no button visible initially
         add (button);
         buttons [buttonCounter] = button;
      }
   }

   private void applyButtonLayout () {
      boolean visible = false;
      for (ButtonType buttonType : ButtonType.values ()) {
         visible = ( (buttonLayout != null) 
                  && (buttonLayout.isButtonInLayout (buttonType)) 
                  && ((editorMode == null) || (!editorMode.equals (EditorMode.VIEW))) );
         setVisibleButton (buttonType, visible);
      }
   }

   public ButtonLayout getButtonLayout () {
      return buttonLayout;
   }

   public void setButtonLayout (ButtonLayout buttonLayout) {
      this.buttonLayout = buttonLayout;

      applyButtonLayout ();
   }
   
   public void setEnableAll (boolean enable) {
      if (buttonLayout != null) {
         for (ButtonType buttonType : ButtonType.values ()) {
            if (buttonLayout.isButtonInLayout (buttonType)) {
               buttons [buttonType.ordinal ()].setEnabled (enable);
            }
         }
      }
   }

   public void setEnableButton (ButtonType buttonType, boolean enable) {
      if (buttonLayout.isButtonInLayout (buttonType)) {
         buttons [buttonType.ordinal ()].setEnabled (enable);
      }
   }

   public void setVisibleButton (ButtonType buttonType) {
      if ( (buttonLayout != null) && (buttonLayout.isButtonInLayout (buttonType)) ) {
         buttons [buttonType.ordinal ()].setVisible (true);
      } else {
         buttons [buttonType.ordinal ()].setVisible (false);
      }
   }

   public void setVisibleButton (ButtonType buttonType, boolean visible) {
      buttons [buttonType.ordinal ()].setVisible (visible);
   }


   public ActionPushButton getButton (ButtonType buttonType) {
      return buttons [buttonType.ordinal ()];
   }

   public HandlerRegistration addButtonActionHandler (ButtonType buttonType, final IActionHandler handler) {
      ActionPushButton button = buttons [buttonType.ordinal ()];
      
      return button.addActionHandler (handler);
   }
 
   /**
    * Change field visibility and readability according the editorMode.
    */
   public void notifyEditMode (EditorMode editorMode) {
      this.editorMode = editorMode;
      applyButtonLayout ();
   }
   
   @UiChild (tagname = "buttonStyleName")
   public void setButtonStyleName (String buttonStyleName) {
      for (ButtonType buttonType : ButtonType.values ()) {
         buttons [buttonType.ordinal ()].addStyleName (buttonStyleName);
      }
   }
   
   @UiChild (tagname = "styleName")
   public void setStyleName (String styleName) {
      super.setStyleName (styleName);
   }
}
