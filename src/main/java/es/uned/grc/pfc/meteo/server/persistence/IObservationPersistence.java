package es.uned.grc.pfc.meteo.server.persistence;

import java.util.Date;
import java.util.List;

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.Variable;

public interface IObservationPersistence extends IPersistence <Integer, Observation> {
   
   /** obtains the observations of the given station whose quality controls have not yet been conducted */ 
   List <Observation> getUncontrolled (Integer stationId, int max);
   /** obtains the observations of the given station whose derived calculations have not yet been conducted */ 
   List <Observation> getUnderived (Integer stationId, int max);
   /** obtains the observations of the given station whose observed date is within given range */ 
   List <Observation> getObservedInRange (Integer stationId, Date ini, Date end, boolean qualityControlled);
   /** obtains the observations of the given station whose derived range contains the given range */ 
   List <Observation> getDerivedInRange (Integer stationId, Date ini, Date end, Variable ... variables);
   /** obtains the last observations received by given station */
   List <Observation> getLastReceived (Integer stationId);
   /** obtains the observations of the own station that could be published and were not yet */
   List <Observation> getUnsent (Integer stationId, List <Integer> variableIds);
}
