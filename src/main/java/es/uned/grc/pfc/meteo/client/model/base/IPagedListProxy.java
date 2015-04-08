package es.uned.grc.pfc.meteo.client.model.base;

import java.util.List;

/**
 * Proxy for the server-side PagedList.
 * 
 * Unfortunately, RequestFactory does not support polymorphic
 * lists by now, so it will have to be extended for every listed
 * entity (grffff).
 */
public interface IPagedListProxy <T extends Object> {
   List <T> getList ();
   long getRealSize ();
}
