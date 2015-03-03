package es.uned.grc.pfc.meteo.client.request;

import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.ExtraTypes;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import es.uned.grc.pfc.meteo.client.model.IObservationBlockProxy;
import es.uned.grc.pfc.meteo.client.model.IRequestParamFilterProxy;
import es.uned.grc.pfc.meteo.client.model.IRequestParamProxy;
import es.uned.grc.pfc.meteo.client.model.paged.IVariablePagedListProxy;
import es.uned.grc.pfc.meteo.server.service.ObservationService;
import es.uned.grc.pfc.meteo.shared.locators.SpringServiceLocator;

/**
 * Client-side wrapper for ObservationService methods.
 */
@ExtraTypes ({IRequestParamFilterProxy.class})
@Service (value = ObservationService.class, locator = SpringServiceLocator.class)
public interface IObservationRequestContext extends RequestContext {

   /**
    * Obtains a list of observations for the given filter. If a start and end date
    * is provided, it fills all the possible gaps by pushing Observations with null values
    * for every period within the range and every variable.
    * IMPORTANT: this method assumes that 1 station is provided as filter, either by id
    * or by marking the own station flag !! 
    */
   Request <List <IObservationBlockProxy>> getObservations (IRequestParamProxy requestParam);
   /**
    * Get the variables that a given station is able to measure
    * Specify null as the stationId to get the variables of your own station
    */
   Request <IVariablePagedListProxy> getStationVariables (String filer, Integer stationId, boolean measuredOnly, boolean internalOnly);
   /**
    * Obtain the start of the current day
    */
   Request <Date> getTodayStart ();
}
