package es.uned.grc.pfc.meteo.client.util;

import com.google.gwt.core.client.GWT;

import es.uned.grc.pfc.meteo.client.resource.IApplicationResources;

public class ClientGlobals {
   
   private static ClientGlobals instance = new ClientGlobals ();
   
   private IApplicationResources applicationResources = null;

   private ClientGlobals () {
      //do not construct: singleton
   }
   
   public static ClientGlobals getInstance () {
      return instance;
   }
      
   /**
    * Provides access to the app global bundled resources
    */
   public IApplicationResources getApplicationResources () {
      if (applicationResources == null) {
         applicationResources = GWT.create (IApplicationResources.class);
      }
      return applicationResources;
   }
}
