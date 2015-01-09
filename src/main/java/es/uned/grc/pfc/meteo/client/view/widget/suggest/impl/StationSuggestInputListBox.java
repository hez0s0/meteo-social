package es.uned.grc.pfc.meteo.client.view.widget.suggest.impl;

import com.google.gwt.user.client.ui.TextBox;

import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.model.paged.IStationPagedListProxy;
import es.uned.grc.pfc.meteo.client.request.IStationRequestContext;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestBox;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestInputListBox;

public class StationSuggestInputListBox extends EntitySuggestInputListBox <IStationProxy, IStationRequestContext, IStationPagedListProxy> {

   @Override
   protected EntitySuggestBox <IStationProxy, IStationRequestContext, IStationPagedListProxy> createEntitySuggestBox (TextBox textBox) {
      return new StationSuggestBox (textBox);
   }

   @Override
   protected boolean allowFreeEntry (String freeText) {
      return false;
   }

   @Override
   protected IStationProxy createFreeEntry (String freeText) throws Exception {
      throw new Exception ("NOT ALLOWED");
   }

   @Override
   protected String getDisplay (IStationProxy entity) {
      return entity.getName ();
   }

   @Override
   protected boolean allowDuplicated () {
      return false;
   }

   @Override
   protected boolean areEqual (IStationProxy e1, IStationProxy e2) {
      return ( (e1.getId () != null) && (e2.getId () != null) && (e1.getId ().equals (e2.getId ())) );
   }
}
