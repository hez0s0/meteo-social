package es.uned.grc.pfc.meteo.client.view.widget.suggest.impl;

import com.google.gwt.user.client.ui.TextBox;

import es.uned.grc.pfc.meteo.client.model.paged.IStringPagedListProxy;
import es.uned.grc.pfc.meteo.client.request.IStationRequestContext;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestBox;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestOracle;

public class CountrySuggestBox extends EntitySuggestBox <String, IStationRequestContext, IStringPagedListProxy> {
   
   public CountrySuggestBox (TextBox textBox) {
      super (textBox);
   }
   
   @Override
   public void setValue (String value) {
      suggestBox.setText ((value != null) ? value : "");
   }

   @Override
   public String getValue () {
      return entitySuggestOracle.getValue (suggestBox.getText ());
   }

   @Override
   protected EntitySuggestOracle <String, IStationRequestContext, IStringPagedListProxy> createEntitySuggestOracle () {
      return new CountrySuggestOracle ();
   }
}
