package es.uned.grc.pfc.meteo.client.view.widget.suggest.impl;

import es.uned.grc.pfc.meteo.client.model.IRequestParamProxy;
import es.uned.grc.pfc.meteo.client.model.paged.IStringPagedListProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.request.IStationRequestContext;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestOracle;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestion;

/**
 * Implements a suggest oracle box over the cities of the system.
 */
public class CitySuggestOracle extends EntitySuggestOracle <String, IStationRequestContext, IStringPagedListProxy> {
   
   @Override
   protected IStationRequestContext getRequestContext (IRequestFactory requestFactory) {
      return requestFactory.getStationContext ();
   }

   @Override
   protected EntitySuggestion <String> createEntitySuggestion (String entityProxy) {
      return new CitySuggestion (entityProxy);
   }

   @Override
   protected boolean isSameEntity (String entityProxy, String text) {
      return text.equals (entityProxy);
   }
   
   @Override
   protected com.google.web.bindery.requestfactory.shared.Request <IStringPagedListProxy> getSuggestRequest (IStationRequestContext requestContext, IRequestParamProxy requestParamProxy) {
      return requestContext.getCities (requestParamProxy.getFilter ());
   }

   @Override
   protected boolean canMakeRequest () {
      //allow only when no value is already given (this field is for one element only)
      return entitySuggestInputListBox.getValues () == null || entitySuggestInputListBox.getValues ().isEmpty ();
   }

   @Override
   protected int getMinimumCharsToSearch () {
      return 0;
   }
}
