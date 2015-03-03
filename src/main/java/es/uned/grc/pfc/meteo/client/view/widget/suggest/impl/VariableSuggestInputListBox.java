package es.uned.grc.pfc.meteo.client.view.widget.suggest.impl;

import com.google.gwt.user.client.ui.TextBox;

import es.uned.grc.pfc.meteo.client.model.IVariableProxy;
import es.uned.grc.pfc.meteo.client.model.paged.IVariablePagedListProxy;
import es.uned.grc.pfc.meteo.client.request.IObservationRequestContext;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestBox;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestInputListBox;

public class VariableSuggestInputListBox extends EntitySuggestInputListBox <IVariableProxy, IObservationRequestContext, IVariablePagedListProxy> {

   @Override
   protected EntitySuggestBox <IVariableProxy, IObservationRequestContext, IVariablePagedListProxy> createEntitySuggestBox (TextBox textBox) {
      return new VariableSuggestBox (textBox);
   }

   @Override
   protected boolean allowFreeEntry (String freeText) {
      return false;
   }

   @Override
   protected IVariableProxy createFreeEntry (String freeText) throws Exception {
      throw new Exception ("NOT ALLOWED");
   }

   @Override
   protected String getDisplay (IVariableProxy entity) {
      return entity.getName ();
   }

   @Override
   protected boolean allowDuplicated () {
      return false;
   }

   @Override
   protected boolean areEqual (IVariableProxy e1, IVariableProxy e2) {
      return ( (e1.getId () != null) && (e2.getId () != null) && (e1.getId ().equals (e2.getId ())) );
   }
}
