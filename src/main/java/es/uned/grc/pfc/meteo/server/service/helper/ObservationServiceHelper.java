package es.uned.grc.pfc.meteo.server.service.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.grc.pfc.meteo.server.collector.station.IStationPlugin;
import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.RequestParam;
import es.uned.grc.pfc.meteo.server.model.RequestParamFilter;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.model.Variable;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IVariablePersistence;
import es.uned.grc.pfc.meteo.server.util.IServerConstants;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

/**
 * Supports non-client invokable actions of the AdminService module
 */
@Service
public class ObservationServiceHelper {

   @Autowired
   private IObservationPersistence observationPersistence = null;
   @Autowired
   private IStationPersistence stationPersistence = null;
   @Autowired
   private IVariablePersistence variablePersistence = null;
   
   @Autowired
   private IStationPlugin stationPlugin = null;

   public List <Observation> fillGaps (List <Observation> observations, RequestParam requestParam) throws ParseException {
      Observation  observation = null;
      List <Observation> result = new ArrayList <Observation> ();
      Date startDate = getStartDate (requestParam);
      Date endDate = getEndDate (requestParam);
      List <Variable> variables = null;
      Station station = null;
      
      if (startDate != null && endDate != null) {
         //iterate all the periods and variables to fill the gaps with "empty" observations
         station = getStation (requestParam, observations);
         variables = getVariables (station, requestParam);
         
         long stationPeriod = (stationPlugin.getObservationPeriod () * IServerConstants.ONE_MINUTE);
         
         for (long observed = startDate.getTime (); observed <= endDate.getTime (); observed += stationPeriod) {
            for (Variable variable : variables) {
               observation = findObservation (observations, observed, variable.getId ());
               if (observation == null) {
                  observation = createEmptyObservation (observed, variable, station);
               }
               result.add (observation);
            }
         }
      } else {
         //just get the results as-is
         result.addAll (observations);
      }
      
      return result;
   }
   
   private List <Variable> getVariables (Station station, RequestParam requestParam) {
      List <Variable> result = null;
      String filterValue = findFilterValue (requestParam, ISharedConstants.ObservationFilter.VARIABLE_IDS);
      
      if (!StringUtils.isEmpty (filterValue)) {
         //a list of variables was provided
         result = variablePersistence.findByIdList (variablePersistence.parseIntegerList (filterValue));
      } else {
         //use the variables of the station
         result = new ArrayList <Variable> (station.getVariables ());
      }
      
      return result;
   }

   private Station getStation (RequestParam requestParam, List <Observation> observations) {
      Station result = null;
      String filterValue = null;
      // if there is any observation at all, it must contain the station
      if (!observations.isEmpty ()) {
         result = observations.get (0).getStation ();
      } else {
         //get it from the filter, if no observation at all was found
         filterValue = findFilterValue (requestParam, ISharedConstants.ObservationFilter.OWN);
         if (!StringUtils.isEmpty (filterValue)) {
            //own station
            result = stationPersistence.getOwnStation ();
            if (result == null) {
               throw new RuntimeException ("unable to obtain own station, please review the system configuration");
            }
         } else {
            //id-specified station
            filterValue = findFilterValue (requestParam, ISharedConstants.ObservationFilter.STATION_ID);
            if (!StringUtils.isEmpty (filterValue)) {
               result = stationPersistence.findById (Integer.valueOf (filterValue));
            }
         }
      }
      
      if (result == null) {
         throw new RuntimeException ("No station found, but it is mandatory");
      }
      
      return result;
   }

   private Observation createEmptyObservation (long observed, Variable variable, Station station) {
      Observation observation = new Observation ();
      
      observation.setStation (station);
      observation.setVariable (variable);
      observation.setObserved (new Date (observed));
      
      return observation;
   }

   private Observation findObservation (List <Observation> observations, long observed, int variableId) {
      for (Observation observation : observations) {
         if (observation.getObserved ().getTime () == observed && observation.getVariable ().getId () == variableId) {
            return observation;
         }
      }
      return null;
   }

   private Date getStartDate (RequestParam requestParam) throws ParseException {
      String value = findFilterValue (requestParam, ISharedConstants.ObservationFilter.START_DATE);
      return !StringUtils.isEmpty (value) ? new SimpleDateFormat (ISharedConstants.SHARED_SHORT_DATE_FORMAT).parse (value) : null;
   }

   private Date getEndDate (RequestParam requestParam) throws ParseException {
      String value = findFilterValue (requestParam, ISharedConstants.ObservationFilter.END_DATE);
      return !StringUtils.isEmpty (value) ? new SimpleDateFormat (ISharedConstants.SHARED_SHORT_DATE_FORMAT).parse (value) : null;
   }
   
   public String findFilterValue (RequestParam requestParam, ISharedConstants.ObservationFilter searchedFilter) {
      for (RequestParamFilter filter : requestParam.getFilters ()) {
         if (filter.getParam () != null) {
            if (ISharedConstants.ObservationFilter.valueOf (filter.getParam ()).equals (searchedFilter)) {
               return filter.getValue ();
            }
         }
      }
      return null;
   }

}
