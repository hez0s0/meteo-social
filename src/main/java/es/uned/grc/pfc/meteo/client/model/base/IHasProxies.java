package es.uned.grc.pfc.meteo.client.model.base;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.BaseProxy;

/**
 * Interface for a class that has a list of values that extend a {@link BaseProxy}
 */
public interface IHasProxies <E extends Object> {
   /** the list of values contained */
   List <E> getValues ();
}
