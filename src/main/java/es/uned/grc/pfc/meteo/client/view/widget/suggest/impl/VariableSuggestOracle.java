package es.uned.grc.pfc.meteo.client.view.widget.suggest.impl;

import java.util.List;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.web.bindery.requestfactory.shared.Receiver;

import es.uned.grc.pfc.meteo.client.model.IRequestParamProxy;
import es.uned.grc.pfc.meteo.client.model.IVariableProxy;
import es.uned.grc.pfc.meteo.client.model.paged.IVariablePagedListProxy;
import es.uned.grc.pfc.meteo.client.request.IObservationRequestContext;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestOracle;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.EntitySuggestion;

/**
 * Implements a suggest oracle box over the workSpaces of the system.
 */
public class VariableSuggestOracle extends EntitySuggestOracle <IVariableProxy, IObservationRequestContext, IVariablePagedListProxy> {
   
   @Override
   protected IObservationRequestContext getRequestContext (IRequestFactory requestFactory) {
      return requestFactory.getObservationContext ();
   }

   @Override
   protected EntitySuggestion <IVariableProxy> createEntitySuggestion (IVariableProxy entityProxy) {
      return new VariableSuggestion (entityProxy);
   }

   @Override
   protected boolean isSameEntity (IVariableProxy entityProxy, String text) {
      return text.equals (entityProxy.getName ());
   }
   
   @Override
   protected com.google.web.bindery.requestfactory.shared.Request <IVariablePagedListProxy> getSuggestRequest (IObservationRequestContext requestContext, IRequestParamProxy requestParamProxy) {
      return requestContext.getStationVariables (requestParamProxy.getFilter (), null, true, false);
   }

   @Override
   protected boolean canMakeRequest () {
      //always perform requests
      return true;
   }

   @Override
   protected int getMinimumCharsToSearch () {
      return 0;
   }
   
   @Override
   protected void loadDefaultValues (IRequestFactory requestFactory) {
      IObservationRequestContext requestContext = requestFactory.getObservationContext ();
      requestContext.getStationVariables (null, null, true, false)
                    .fire (new Receiver <IVariablePagedListProxy>() {
         @SuppressWarnings ("unchecked")
         public void onSuccess (IVariablePagedListProxy response) {
            setDefaultSuggestions ((List <SuggestOracle.Suggestion>) (List <?>) asSuggestionList (response.getList ()));
         };
      });
   }

}
