package es.uned.grc.pfc.meteo.client.view.widget.suggest.impl;

import com.google.gwt.user.client.ui.TextBox;

import es.uned.grc.pfc.meteo.client.model.IVariableProxy;
import es.uned.grc.pfc.meteo.client.model.paged.IVariablePagedListProxy;
import es.uned.grc.pfc.meteo.client.request.IObservationRequestContext;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestBox;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestOracle;

public class VariableSuggestBox extends EntitySuggestBox <IVariableProxy, IObservationRequestContext, IVariablePagedListProxy> {
   
   public VariableSuggestBox (TextBox textBox) {
      super (textBox);
   }
   
   @Override
   public void setValue (IVariableProxy value) {
      suggestBox.setText ((value != null) ? value.getAcronym () + " (" + value.getName () + ")" : "");
   }

   @Override
   public IVariableProxy getValue () {
      return entitySuggestOracle.getValue (suggestBox.getText ());
   }

   @Override
   protected EntitySuggestOracle <IVariableProxy, IObservationRequestContext, IVariablePagedListProxy> createEntitySuggestOracle () {
      return new VariableSuggestOracle ();
   }
}
