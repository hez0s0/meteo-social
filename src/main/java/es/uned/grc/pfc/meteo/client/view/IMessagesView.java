package es.uned.grc.pfc.meteo.client.view;

import com.google.gwt.user.client.ui.HTMLPanel;

import es.uned.grc.pfc.meteo.client.view.base.IView;

public interface IMessagesView extends IView {
   /** gets access to an HTML that is able to represent a message */
   HTMLPanel getMessagePanel ();
}
