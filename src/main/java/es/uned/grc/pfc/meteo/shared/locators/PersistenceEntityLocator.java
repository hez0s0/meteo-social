package es.uned.grc.pfc.meteo.shared.locators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.web.bindery.requestfactory.shared.Locator;

import es.uned.grc.pfc.meteo.server.model.base.IEntity;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IPersistence;
import es.uned.grc.pfc.meteo.server.util.CdiUtils;

/**
 * A locator for entities to be translated into proxies. Note that it is 
 * written for Integer-keyed entities.
 */
public class PersistenceEntityLocator extends Locator <IEntity <Integer>, Integer> {

   private static final String PERSISTENCE_MASK = "%s.I%sPersistence";

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
   public IEntity <Integer> find (Class <? extends IEntity <Integer>> clazz, Integer id) {
      try {
         IEntity <Integer> result = null;
         String persistenceName = getPersistenceClassName (clazz);
         IPersistence <Integer, IEntity <Integer>> persistence = (IPersistence <Integer, IEntity <Integer>>) CdiUtils.getReference (Class.forName (persistenceName));
         
         try {
            result = persistence.findById (id);
         } catch (Exception e) {
            log.error (String.format ("Error retrieving item of class '%s' with ID '%s'", clazz.getName (), id), e);
         }
         
         return result;
      } catch (Exception e) {
         log.error (String.format ("Error finding entity of type %s with ID %s", clazz.getName (), id), e);
         throw new RuntimeException (e);
      }
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
   
   private String getPersistenceClassName (Class <? extends IEntity <Integer>> entityClazz) {
      return String.format (PERSISTENCE_MASK, IObservationPersistence.class.getPackage ().getName (), entityClazz.getSimpleName ());
   }

   public boolean isLive (IEntity <Integer> domainObject) {
      return true; //by default, super.isLive invokes find again, so causing poor performance
   }
}
