package es.uned.grc.pfc.meteo.client.view.widget.suggest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import es.uned.grc.pfc.meteo.client.model.IRequestParamProxy;
import es.uned.grc.pfc.meteo.client.model.base.IPagedListProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;

/**
 * Implements a generic suggest oracle for any entity of the system.
 */
public abstract class EntitySuggestOracle <E extends BaseProxy, R extends RequestContext, P extends IPagedListProxy <E>> extends MultiWordSuggestOracle {

   /** Number of suggestions to request from the server. */
   protected static final int MAX_SUGGESTIONS = 20;
   
   /** the suggestbox that is bound with this oracle */
   protected SuggestBox suggestBox = null;
   /** Is there a request in progress */
   protected boolean requestInProgress = false;
   /** The most recent request made by the client.  */
   protected Request mostRecentClientRequest = null;
   /** the request factory for async access to server-side model */
   protected IRequestFactory requestFactory = null;
   /** the current entity list retrieved from the server */
   protected IPagedListProxy <E> currentEntityPagedList = null;
   
   /** creates a request context that can create a {@link IRequestParamProxy} and utterly fire a server-side request for suggestions */
   protected abstract R getRequestContext (IRequestFactory requestFactory);
   /** creates a Request to be fired so that suggestions can be obtained from the server */
   protected abstract com.google.web.bindery.requestfactory.shared.Request <P> getSuggestRequest (R requestContext, IRequestParamProxy requestParamProxy);
   /** creates an entity suggestion, given an entity itself */
   protected abstract EntitySuggestion <E> createEntitySuggestion (E entityProxy) ;
   /** checks if a text identifies a given entity */
   protected abstract boolean isSameEntity (E entityProxy, String text) ;
   /** if a request to the server can be performed according to extending classes logic */
   protected abstract boolean canMakeRequest ();
   /** if a search should be performed with less than N characters */
   protected abstract int getMinimumCharsToSearch ();
   
   public IRequestFactory getRequestFactory () {
      return requestFactory;
   }
   
   public void setRequestFactory (IRequestFactory requestFactory) {
      this.requestFactory = requestFactory;
      loadDefaultValues (requestFactory);
   }

   /**
    * Called by the SuggestBox to get some suggestions.
    */
   @Override
   public void requestSuggestions (final Request request, final Callback callback) {
      // Record this request as the most recent one.
      mostRecentClientRequest = request;
      // If there is not currently a request in progress return some suggestions. If there is a request in progress
      // suggestions will be returned when it completes.
      if (!requestInProgress) {
         returnSuggestions (callback);
      }
   }
   
   /** load default values if needed */
   protected void loadDefaultValues (IRequestFactory requestFactory) {
      //this default implementation does nothing, implement in children classes
   }

   /**
    * Return some suggestions to the SuggestBox. At this point we know that
    * there is no call to the server currently in progress and we try to satisfy
    * the request from the most recent results from the server before we call
    * the server.
    */
   private void returnSuggestions (Callback callback) {
      final String mostRecentQuery = mostRecentClientRequest.getQuery ();
      if (mostRecentQuery.length () < getMinimumCharsToSearch ()) {
         // for single character queries return an empty list.
         callback.onSuggestionsReady (mostRecentClientRequest, new Response (Collections.<Suggestion> emptyList ()));
         currentEntityPagedList = null;
         return;
      }
      makeRequest (mostRecentClientRequest, callback);
   }
   
   /**
    * Send a request to the server.
    */
   private void makeRequest (final Request request, final Callback callback) {
      if (requestFactory == null) {
         throw new RuntimeException ("RequestFactory not set"); //should be seen at dev time
      }
      if (canMakeRequest ()) {
         requestInProgress = true;
         R requestContext = getRequestContext (requestFactory);
         IRequestParamProxy requestParamProxy = requestContext.create (IRequestParamProxy.class);
   
         requestParamProxy.setStart (0);
         requestParamProxy.setLength (MAX_SUGGESTIONS);
         requestParamProxy.setFilter (request.getQuery ());
         getSuggestRequest (requestContext, requestParamProxy).fire (new Receiver <IPagedListProxy <E>> () {
            @Override
            public void onSuccess (IPagedListProxy <E> entityPagedList) {
               Response suggestResponse = new Response ();
               suggestResponse.setMoreSuggestionsCount ((int) entityPagedList.getRealSize () - entityPagedList.getList ().size ());
               suggestResponse.setSuggestions (asSuggestionList (entityPagedList.getList ()));
               callback.onSuggestionsReady (request, suggestResponse);
               EntitySuggestOracle.this.currentEntityPagedList = entityPagedList;
               EntitySuggestOracle.this.requestInProgress = false;
            } //end of onSuccess
   
            @Override
            public void onFailure (ServerFailure serverFailure) {
               EntitySuggestOracle.this.currentEntityPagedList = null;
               EntitySuggestOracle.this.requestInProgress = false;
            } //end of onFailure
         });
      }
   }
   
   /**
    * Given a paged list, transforms it into a suggestion structure acceptable
    * for the suggest box.
    */
   protected List <EntitySuggestion <E>> asSuggestionList (List <E> entityList) {
      List <EntitySuggestion <E>> result = null;
      if (entityList != null) {
         result = new ArrayList <EntitySuggestion <E>> (entityList.size ());
         for (E entityProxy : entityList) {
            result.add (createEntitySuggestion (entityProxy));
         }
      }
      return result;
   }

   /**
    * Given a text representation (eventually from the contents of the SuggestBox)
    * finds the right element as an entity.
    */
   public E getValue (String text) {
      if (currentEntityPagedList != null) {
         for (E entityProxy : currentEntityPagedList.getList ()) {
            if (isSameEntity (entityProxy, text)) {
               return entityProxy;
            }
         }
      }
      return null;
   }
   
   public SuggestBox getSuggestBox () {
      return suggestBox;
   }
   public void setSuggestBox (SuggestBox suggestBox) {
      this.suggestBox = suggestBox;
   }
}
