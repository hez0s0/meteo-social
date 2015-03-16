package es.uned.grc.pfc.meteo.client.model;

import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import es.uned.grc.pfc.meteo.server.dto.DerivedRangeDTO;

@ProxyFor (DerivedRangeDTO.class)
public interface IDerivedRangeProxy extends ValueProxy {
   IStationProxy getStation ();
   void setStation (IStationProxy station);
   Date getIni ();
   void setIni (Date ini);
   Date getEnd ();
   void setEnd (Date end);
   List <IDerivedVariableProxy> getDerivedVariables ();
   void setDerivedVariables (List <IDerivedVariableProxy> derivedVariables);
}
