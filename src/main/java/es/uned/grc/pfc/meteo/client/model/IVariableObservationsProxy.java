package es.uned.grc.pfc.meteo.client.model;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import es.uned.grc.pfc.meteo.server.dto.VariableObservationsDTO;

@ProxyFor (VariableObservationsDTO.class)
public interface IVariableObservationsProxy extends ValueProxy {
   IStationProxy getStation ();
   void setStation (IStationProxy station);
   IVariableProxy getVariable ();
   void setVariable (IVariableProxy variable);
   List <IObservationProxy> getObservations ();
   void setObservations (List <IObservationProxy> observations);
}
