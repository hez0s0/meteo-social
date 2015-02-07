package es.uned.grc.pfc.meteo.client.view.widget.suggest;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings ("deprecation")
public class ListItem <E extends Object> extends ComplexPanel implements HasText, HasHTML, HasClickHandlers, HasKeyDownHandlers, HasBlurHandlers {

   private E entity = null;
   private boolean selected = false;
   
   public ListItem (E entity) {
      this.entity = entity;
      setElement (DOM.createElement ("LI"));
   }

   public E getEntity () {
      return entity;
   }

   public void setEntity (E entity) {
      this.entity = entity;
   }
   
   public void add (Widget w) {
      super.add (w, getElement ());
   }

   public void insert (Widget w, int beforeIndex) {
      super.insert (w, getElement (), beforeIndex, true);
   }

   public String getText () {
      return DOM.getInnerText (getElement ());
   }

   public void setText (String text) {
      DOM.setInnerText (getElement (), (text == null) ? "" : text);
   }

   public void setId (String id) {
      DOM.setElementAttribute (getElement (), "id", id);
   }

   public String getHTML () {
      return DOM.getInnerHTML (getElement ());
   }

   public void setHTML (String html) {
      DOM.setInnerHTML (getElement (), (html == null) ? "" : html);
   }

   public HandlerRegistration addClickHandler (ClickHandler handler) {
      return addDomHandler (handler, ClickEvent.getType ());
   }

   public HandlerRegistration addKeyDownHandler (KeyDownHandler handler) {
      return addDomHandler (handler, KeyDownEvent.getType ());
   }

   public HandlerRegistration addBlurHandler (BlurHandler handler) {
      return addDomHandler (handler, BlurEvent.getType ());
   }

   public boolean isSelected () {
      return selected;
   }

   public void setSelected (boolean selected) {
      this.selected = selected;
   }
}