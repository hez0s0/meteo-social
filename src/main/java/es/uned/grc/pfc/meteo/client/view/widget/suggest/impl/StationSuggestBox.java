package es.uned.grc.pfc.meteo.client.view.widget.suggest.impl;

import com.google.gwt.user.client.ui.TextBox;

import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.model.paged.IStationPagedListProxy;
import es.uned.grc.pfc.meteo.client.request.IStationRequestContext;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestBox;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestOracle;

public class StationSuggestBox extends EntitySuggestBox <IStationProxy, IStationRequestContext, IStationPagedListProxy> {
   
   public StationSuggestBox (TextBox textBox) {
      super (textBox);
   }
   
   @Override
   public void setValue (IStationProxy value) {
      suggestBox.setText ((value != null) ? value.getName () : "");
   }

   @Override
   public IStationProxy getValue () {
      return entitySuggestOracle.getValue (suggestBox.getText ());
   }

   @Override
   protected EntitySuggestOracle <IStationProxy, IStationRequestContext, IStationPagedListProxy> createEntitySuggestOracle () {
      return new StationSuggestOracle ();
   }
}
