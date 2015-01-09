package es.uned.grc.pfc.meteo.client.view;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

import es.uned.grc.pfc.meteo.client.activity.mapper.MainActivityMapper;
import es.uned.grc.pfc.meteo.client.view.base.IView;

public interface IMainLayoutView extends IView {
   SimplePanel getHeaderPanel () ;
   SimplePanel getMenuPanel ();
   SimplePanel getSearchPanel ();
   SimplePanel getMessagesPanel ();
   SimplePanel getMainPanel ();
   SimplePanel getActionsPanel ();
   
   DivElement getMainDiv ();
   DivElement getActionsDiv ();
   
   Label getDetailsTitle ();
   Label getBackToList ();
   
   void setMainActivityMapper (MainActivityMapper mainActivityMapper);
}
