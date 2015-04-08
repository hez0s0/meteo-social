package es.uned.grc.pfc.meteo.client.model.paged;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import es.uned.grc.pfc.meteo.client.model.base.IPagedListProxy;
import es.uned.grc.pfc.meteo.server.model.paged.StringPagedList;

/**
 * Unfortunately, RequestFactory does not support polymorphic
 * lists by now, so this is extending it for a specific entity.
 */
@ProxyFor (value = StringPagedList.class)
public interface IStringPagedListProxy extends ValueProxy, IPagedListProxy <String>  {
   List <String> getList ();
}
