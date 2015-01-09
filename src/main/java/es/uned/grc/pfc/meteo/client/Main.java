package es.uned.grc.pfc.meteo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import es.uned.grc.pfc.meteo.client.ioc.IGinjector;

public class Main implements EntryPoint {

   private final IGinjector injector = GWT.create (IGinjector.class);

   @Override
   public void onModuleLoad () {
      injector.getBootstrap ().start (injector);
   }

}
