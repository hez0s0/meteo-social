package es.uned.grc.pfc.meteo.client.model.paged;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.model.base.IPagedListProxy;
import es.uned.grc.pfc.meteo.server.model.paged.StationPagedList;

/**
 * Unfortunately, RequestFactory does not support polymorphic
 * lists by now, so this is extending it for a specific entity.
 */
@ProxyFor (value = StationPagedList.class)
public interface IStationPagedListProxy extends ValueProxy, IPagedListProxy <IStationProxy>  {
   List <IStationProxy> getList ();
}
