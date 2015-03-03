package es.uned.grc.pfc.meteo.client.model;

import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import es.uned.grc.pfc.meteo.server.dto.ObservationBlockDTO;

@ProxyFor (ObservationBlockDTO.class)
public interface IObservationBlockProxy extends ValueProxy {
   IStationProxy getStation ();
   void setStation (IStationProxy station);
   Date getObserved ();
   void setObserved (Date observed);
   List <IObservationProxy> getObservations ();
   void setObservations (List <IObservationProxy> observations);
}
