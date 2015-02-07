package es.uned.grc.pfc.meteo.shared.locators;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.google.web.bindery.requestfactory.shared.Locator;

import es.uned.grc.pfc.meteo.server.model.base.IEntity;
import es.uned.grc.pfc.meteo.server.persistence.IPersistence;
import es.uned.grc.pfc.meteo.server.util.ApplicationContextProvider;

/**
 * A locator for entities to be translated into proxies. Note that it is 
 * written for Integer-keyed entities.
 */
@Component
public class PersistenceEntityLocator extends Locator <IEntity <Integer>, Integer> {

   private static final String PERSISTENCE_SUFIX = "Persistence";

   private static Log log = LogFactory.getLog (PersistenceEntityLocator.class);

   @Override
   public IEntity <Integer> create (Class <? extends IEntity <Integer>> clazz) {
      try {
         return clazz.newInstance ();
      } catch (InstantiationException e) {
         throw new RuntimeException (e);
      } catch (IllegalAccessException e) {
         throw new RuntimeException (e);
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public IEntity <Integer> find (Class <? extends IEntity <Integer>> clazz, Integer ID) {
      IEntity <Integer> result = null;
      String persistenceName = getPersistenceBeanName (clazz);
      IPersistence <Integer, IEntity <Integer>> persistence = (IPersistence <Integer, IEntity <Integer>>) ApplicationContextProvider.getApplicationContext ().getBean (persistenceName);
      
      try {
         result = persistence.findById (ID);
      } catch (Exception e) {
         log.error (String.format ("Error retrieving item of class '%s' with ID '%s'", clazz.getName (), ID), e);
      }
      
      return result;
   }

   @Override
   public Class <IEntity <Integer>> getDomainType () {
      throw new UnsupportedOperationException();
   }

   @Override
   public Integer getId (IEntity <Integer> domainObject) {
      return domainObject.getId ();
   }

   @Override
   public Class <Integer> getIdType () {
      return Integer.class;
   }

   @Override
   public Object getVersion (IEntity <Integer> domainObject) {
      return domainObject.getVersion ();
   }
   
   private String getPersistenceBeanName (Class <? extends IEntity <Integer>> entityClazz) {
      StringBuffer name = new StringBuffer ();
      name.append (entityClazz.getSimpleName ());
      name.append (PERSISTENCE_SUFIX);
      return WordUtils.uncapitalize (name.toString ());
   }

   public boolean isLive (IEntity <Integer> domainObject) {
      //by default, super.isLive invokes find again, so causing poor performance
      return true;
   }
}
