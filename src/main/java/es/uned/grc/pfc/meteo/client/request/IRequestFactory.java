package es.uned.grc.pfc.meteo.client.request;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface IRequestFactory extends RequestFactory {
   IObservationRequestContext getObservationContext ();
   IStationRequestContext getStationContext ();
}
