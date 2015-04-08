package es.uned.grc.pfc.meteo.client.view.widget.suggest.impl;

import com.google.gwt.user.client.ui.TextBox;

import es.uned.grc.pfc.meteo.client.model.paged.IStringPagedListProxy;
import es.uned.grc.pfc.meteo.client.request.IStationRequestContext;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestBox;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestInputListBox;

public class CountrySuggestInputListBox extends EntitySuggestInputListBox <String, IStationRequestContext, IStringPagedListProxy> {

   @Override
   protected EntitySuggestBox <String, IStationRequestContext, IStringPagedListProxy> createEntitySuggestBox (TextBox textBox) {
      return new CountrySuggestBox (textBox);
   }

   @Override
   protected boolean allowFreeEntry (String freeText) {
      return false;
   }

   @Override
   protected String createFreeEntry (String freeText) throws Exception {
      throw new Exception ("NOT ALLOWED");
   }

   @Override
   protected String getDisplay (String entity) {
      return entity;
   }

   @Override
   protected boolean allowDuplicated () {
      return false;
   }

   @Override
   protected boolean areEqual (String e1, String e2) {
      return e1.equalsIgnoreCase (e2);
   }
}
