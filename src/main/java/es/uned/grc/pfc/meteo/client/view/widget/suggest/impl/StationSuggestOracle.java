package es.uned.grc.pfc.meteo.client.view.widget.suggest.impl;

import es.uned.grc.pfc.meteo.client.model.IRequestParamProxy;
import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.model.paged.IStationPagedListProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.request.IStationRequestContext;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestOracle;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestion;

/**
 * Implements a suggest oracle box over the workSpaces of the system.
 */
public class StationSuggestOracle extends EntitySuggestOracle <IStationProxy, IStationRequestContext, IStationPagedListProxy> {
   
   @Override
   protected IStationRequestContext getRequestContext (IRequestFactory requestFactory) {
      return requestFactory.getStationContext ();
   }

   @Override
   protected EntitySuggestion <IStationProxy> createEntitySuggestion (IStationProxy entityProxy) {
      return new StationSuggestion (entityProxy);
   }

   @Override
   protected boolean isSameEntity (IStationProxy entityProxy, String text) {
      return text.equals (entityProxy.getName ());
   }
   
   @Override
   protected com.google.web.bindery.requestfactory.shared.Request <IStationPagedListProxy> getSuggestRequest (IStationRequestContext requestContext, IRequestParamProxy requestParamProxy) {
//      return requestContext.findWorkSpaces (requestParamProxy);
      return null;
   }

   @Override
   protected boolean canMakeRequest () {
      return true; //always perform requests
   }

   @Override
   protected int getMinimumCharsToSearch () {
      return 0;
   }
   
   @Override
   protected void loadDefaultValues (IRequestFactory requestFactory) {
//      IStationRequestContext requestContext = requestFactory.getAdminContext ();
//      IRequestParamProxy emptyRequest = requestContext.create (IRequestParamProxy.class);
//      requestContext.findWorkSpaces (emptyRequest)
//                    .fire (new Receiver <IStationPagedListProxy>() {
//         @SuppressWarnings ("unchecked")
//         public void onSuccess (IStationPagedListProxy response) {
//            setDefaultSuggestions ((List <SuggestOracle.Suggestion>) (List <?>) asSuggestionList (response.getList ()));
//         };
//      });
   }

}
