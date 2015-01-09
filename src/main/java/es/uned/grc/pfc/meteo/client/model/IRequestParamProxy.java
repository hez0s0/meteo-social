package es.uned.grc.pfc.meteo.client.model;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import es.uned.grc.pfc.meteo.server.model.RequestParam;

@ProxyFor (RequestParam.class)
public interface IRequestParamProxy extends ValueProxy {
   Integer getLength ();
   void setLength (Integer length);
   Integer getStart ();
   void setStart (Integer start);
   String getSortField ();
   void setSortField (String sortField);
   Boolean getAscending ();
   void setAscending (Boolean ascending);
   void setFilter (String filter);
   String getFilter ();
   List <IRequestParamFilterProxy> getFilters ();
   void setFilters (List <IRequestParamFilterProxy> filters);
   
   void setRepresentation (String representation);
   String getRepresentation ();
}
