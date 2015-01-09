package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import es.uned.grc.pfc.meteo.client.activity.mapper.MainActivityMapper;
import es.uned.grc.pfc.meteo.client.view.IMainLayoutView;

public class ActivityManagerInitializer {
   
   @Inject
   public ActivityManagerInitializer (IMainLayoutView mainLayoutView, 
         @Named ("header") ActivityManager headerActivityManager, 
         @Named ("menu") ActivityManager menuActivityManager, 
         @Named ("search") ActivityManager searchActivityManager, 
         @Named ("messages") ActivityManager messagesActivityManager, 
         @Named ("main") ActivityManager mainActivityManager, 
         @Named ("actions") ActivityManager actionsActivityManager) {

      mainLayoutView.setMainActivityMapper ((MainActivityMapper) ((CustomActivityManager) mainActivityManager).getMapper ());
      
      headerActivityManager.setDisplay (mainLayoutView.getHeaderPanel ());
      menuActivityManager.setDisplay (mainLayoutView.getMenuPanel ());
      searchActivityManager.setDisplay (mainLayoutView.getSearchPanel ());
      messagesActivityManager.setDisplay (mainLayoutView.getMessagesPanel ());
      mainActivityManager.setDisplay (mainLayoutView.getMainPanel ());
      actionsActivityManager.setDisplay (mainLayoutView.getActionsPanel ());
   }
   
}
