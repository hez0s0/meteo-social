package es.uned.grc.pfc.meteo.server.persistence;

import java.util.List;

import es.uned.grc.pfc.meteo.server.model.Observation;

public interface IObservationPersistence extends IPersistence <Integer, Observation> {
   
   /** obtains the observations of the own station whose quality controls have not yet been conducted */ 
   List <Observation> getUncontrolled ();
   /** obtains the observations of the own station whose derived calculations have not yet been conducted */ 
   List <Observation> getUnderived ();
}
