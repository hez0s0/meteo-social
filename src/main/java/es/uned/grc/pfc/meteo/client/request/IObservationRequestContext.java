package es.uned.grc.pfc.meteo.client.request;

import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import es.uned.grc.pfc.meteo.server.service.ObservationService;
import es.uned.grc.pfc.meteo.shared.locators.CdiServiceLocator;

/**
 * Client-side wrapper for ObservationService methods.
 */
@Service (value = ObservationService.class, locator = CdiServiceLocator.class)
public interface IObservationRequestContext extends RequestContext {
   
}
