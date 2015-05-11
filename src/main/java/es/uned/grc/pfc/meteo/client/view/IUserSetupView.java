package es.uned.grc.pfc.meteo.client.view;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import es.uned.grc.pfc.meteo.client.model.IUserProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.view.base.IFormView;

public interface IUserSetupView extends IFormView <IUserProxy> {
   /** sets the element and info to be edited */
   void setInput (IUserProxy user, IRequestFactory requestFactory, RequestContext requestContext, PlaceController placeController);
}