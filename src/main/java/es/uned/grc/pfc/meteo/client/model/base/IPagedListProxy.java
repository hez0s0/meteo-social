package es.uned.grc.pfc.meteo.client.model.base;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.BaseProxy;

/**
 * Proxy for the server-side PagedList.
 * 
 * Unfortunately, RequestFactory does not support polymorphic
 * lists by now, so it will have to be extended for every listed
 * entity (grffff).
 */
public interface IPagedListProxy <T extends BaseProxy> {
   List <T> getList ();
   long getRealSize ();
}
