package es.uned.grc.pfc.meteo.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;

import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.view.base.IView;

public interface IHeaderView extends IView {
   /** sets the input */
   void setInput (IRequestFactory requestFactory);
   
   /** gets access to the element that must be clicked to select DE language */
   HasClickHandlers getEsButton ();
   /** gets access to the element that must be clicked to select GB language */
   HasClickHandlers getEnButton ();
}
