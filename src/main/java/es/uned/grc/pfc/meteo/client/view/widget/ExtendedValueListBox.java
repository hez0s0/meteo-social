package es.uned.grc.pfc.meteo.client.view.widget;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.view.client.ProvidesKey;

public class ExtendedValueListBox<T> extends ValueListBox <T> {

   public ExtendedValueListBox (Renderer <T> renderer) {
      super (renderer);
   }

   public ExtendedValueListBox (Renderer <T> renderer, ProvidesKey <T> keyProvider) {
      super (renderer, keyProvider);
   }

   public HandlerRegistration addChangeHandler (ChangeHandler handler) {
      return getListBox ().addChangeHandler (handler);
   }

   public ListBox getListBox () {
      return (ListBox) getWidget ();
   }
}
