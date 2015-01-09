package es.uned.grc.pfc.meteo.client.view.widget.cell;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Button;

import es.uned.grc.pfc.meteo.client.util.IStyleConstants;

/**
 * A cell that includes a button with a text an allows to click on it. (By
 * default, widgets added to a cell do not include the events, so clicking on
 * buttons manually added to a cell does not work.
 * See here:
 * http://stackoverflow.com/questions/12595925/how-to-add-clickhandler-to-the-subrow-of-the-celltablebuilder
 */
public class ClickableButtonCell extends AbstractCell <String> {

   protected String buttonText = null;
   protected ClickHandler clickHandler = null;

   public ClickableButtonCell (String buttonText, ClickHandler clickHandler) {
      super (CLICK);

      this.buttonText = buttonText;
      this.clickHandler = clickHandler;
   } //end of ClickableButtonCell

   @Override
   public void render (Context context, String value, SafeHtmlBuilder sb) {
      Button button = new Button ();
      
      button.setText (buttonText);
      button.setStyleName (IStyleConstants.BUTTON);
   
      sb.appendHtmlConstant (button.toString ());
   } //end of render

   @Override
   public void onBrowserEvent (Context context, Element parent, String value, NativeEvent event, ValueUpdater <String> valueUpdater) {
//      AlertDialogBox.showWarning ("onBrowserEvent");
      super.onBrowserEvent (context, parent, value, event, valueUpdater);
      if ( (CLICK.equals (event.getType ())) && (clickHandler != null) ) {
         clickHandler.onClick (null);
      }
   } //end of onBrowserEvent
} //end of ClickableButtonCell
