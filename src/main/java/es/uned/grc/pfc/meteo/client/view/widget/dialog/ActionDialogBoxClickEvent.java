package es.uned.grc.pfc.meteo.client.view.widget.dialog;

import com.google.gwt.event.dom.client.ClickEvent;

public class ActionDialogBoxClickEvent extends ClickEvent {
   private ActionDialogBox actionDialogBox = null;
   
   ActionDialogBoxClickEvent (ActionDialogBox actionDialogBox) {
      this.actionDialogBox = actionDialogBox;
   }
   
   public ActionDialogBox getActionDialogBox () {
      return actionDialogBox;
   }
}