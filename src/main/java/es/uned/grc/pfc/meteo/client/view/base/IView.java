package es.uned.grc.pfc.meteo.client.view.base;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface IView {
   IsWidget asWidget ();
   Widget getParent ();
   void setVisible (boolean visible);
}
