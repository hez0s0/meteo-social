package es.uned.grc.pfc.meteo.client.request;

import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import es.uned.grc.pfc.meteo.server.service.StationService;
import es.uned.grc.pfc.meteo.shared.locators.CdiServiceLocator;

/**
 * Client-side wrapper for StationService methods.
 */
@Service (value = StationService.class, locator = CdiServiceLocator.class)
public interface IStationRequestContext extends RequestContext {
   
}
