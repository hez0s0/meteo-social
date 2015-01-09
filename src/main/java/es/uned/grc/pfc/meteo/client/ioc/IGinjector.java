package es.uned.grc.pfc.meteo.client.ioc;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;

import es.uned.grc.pfc.meteo.client.IBootstrap;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;
import es.uned.grc.pfc.meteo.client.resource.IApplicationResources;

@GinModules (MainGinModule.class)
public interface IGinjector extends Ginjector {
   
   /** get the singleton instance of the event communication bus */
   EventBus getEventBus ();
   /** get the singleton instance of the place controller */
   PlaceController getPlaceController ();
   /** get the singleton instance of the bootstrap class */
   IBootstrap getBootstrap ();
   /** get the singleton instance of resources for the app */
   IApplicationResources getApplicationResources ();
   
   /** get a new default place for the site */
   ObservationListPlace getDefaultPlace ();
}
