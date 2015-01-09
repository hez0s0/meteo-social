package es.uned.grc.pfc.meteo.shared.locators;

import javax.inject.Singleton;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;

import es.uned.grc.pfc.meteo.server.util.CdiUtils;

@Singleton
public class CdiServiceLocator implements ServiceLocator {

   @Override
   public Object getInstance (Class <?> clazz) {
      try {
         return CdiUtils.getReference (clazz);
      } catch (Exception e) {
         throw new RuntimeException (e);
      }
   }
}
