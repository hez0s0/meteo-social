package es.uned.grc.pfc.meteo.client.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import es.uned.grc.pfc.meteo.client.model.IRequestParamProxy;
import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.model.paged.IStationPagedListProxy;
import es.uned.grc.pfc.meteo.client.model.paged.IStringPagedListProxy;
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
    * Obtain the own station with or without its last observatiosn
    */
   Request <IStationProxy> getOwnStation (boolean includeLastObservations);
   /**
    * Obtain the own station
    */
   Request <IStationProxy> getStationById (int stationId);
   /**
    * Obtained the list of stations contained within a rectagle
    */
   Request <List <IStationProxy>> getStationsInArea (double minLatitude, double minLongitude, double maxLatitude, double maxLongitude);
   /**
    * Obtains a list of stations for the given filter
    */
   Request <IStationPagedListProxy> getStations (IRequestParamProxy requestParam);
   /**
    * Obtains a list of cities for the given filter
    */
   Request <IStringPagedListProxy> getCities (String filter);
   /**
    * Obtains a list of countries for the given filter
    */
   Request <IStringPagedListProxy> getCountries (String filter);
}
