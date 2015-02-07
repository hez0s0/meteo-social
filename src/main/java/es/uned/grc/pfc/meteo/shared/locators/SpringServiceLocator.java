package es.uned.grc.pfc.meteo.shared.locators;

import org.apache.commons.lang.WordUtils;
import org.springframework.stereotype.Component;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;

import es.uned.grc.pfc.meteo.server.util.ApplicationContextProvider;

@Component
public class SpringServiceLocator implements ServiceLocator {

   @Override
   public Object getInstance (Class <?> clazz) {
      return ApplicationContextProvider.getApplicationContext ().getBean (WordUtils.uncapitalize (clazz.getSimpleName ()));
   }
}
