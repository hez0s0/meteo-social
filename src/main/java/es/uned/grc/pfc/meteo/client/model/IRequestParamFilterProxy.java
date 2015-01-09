package es.uned.grc.pfc.meteo.client.model;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import es.uned.grc.pfc.meteo.server.model.RequestParamFilter;

@ProxyFor (RequestParamFilter.class)
public interface IRequestParamFilterProxy extends ValueProxy {
   String getParam ();
   void setParam (String param);
   String getValue ();
   void setValue (String value);
   Boolean getMustNull ();
   void setMustNull (Boolean mustNull);
   String getRepresentation ();
   void setRepresentation (String representation);
}
