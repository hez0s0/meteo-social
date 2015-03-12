package es.uned.grc.pfc.meteo.client.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.server.service.StationService;
import es.uned.grc.pfc.meteo.shared.locators.SpringServiceLocator;

/**
 * Client-side wrapper for StationService methods.
 */
@Service (value = StationService.class, locator = SpringServiceLocator.class)
public interface IStationRequestContext extends RequestContext {

   /**
    * Obtain the own station
    */
   Request <IStationProxy> getOwnStation ();

   /**
    * Obtained the list of stations contained within a rectagle
    */
   Request <List <IStationProxy>> getStationsInArea (double minLatitude, double minLongitude, double maxLatitude, double maxLongitude);
}
