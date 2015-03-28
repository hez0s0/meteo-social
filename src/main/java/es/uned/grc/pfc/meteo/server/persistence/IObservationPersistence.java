package es.uned.grc.pfc.meteo.server.persistence;

import java.util.Date;
import java.util.List;

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.Variable;

public interface IObservationPersistence extends IPersistence <Integer, Observation> {
   
   /** obtains the observations of the own station whose quality controls have not yet been conducted */ 
   List <Observation> getUncontrolled (int max);
   /** obtains the observations of the own station whose derived calculations have not yet been conducted */ 
   List <Observation> getUnderived (int max);
   /** obtains the observations of the own station whose observed date is within given range */ 
   List <Observation> getObservedInRange (Date ini, Date end, boolean qualityControlled);
   /** obtains the observations of the own station whose derived range contains the given range */ 
   List <Observation> getDerivedInRange (Date ini, Date end, Variable ... variables);
}
