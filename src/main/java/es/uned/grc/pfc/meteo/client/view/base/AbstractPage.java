package es.uned.grc.pfc.meteo.client.view.base;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractPage extends Composite implements IView {

   @Override
   public Widget asWidget () {
      return this;
   }
}
