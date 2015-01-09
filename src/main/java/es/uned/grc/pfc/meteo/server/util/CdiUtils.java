package es.uned.grc.pfc.meteo.server.util;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CdiUtils {

   private static BeanManager beanManager = null;
   
   private CdiUtils () {
      
   }
   
   public static <T extends Object> T getReference (Class <T> clazz) {
      try {
         if (beanManager == null) {
               beanManager = (BeanManager) new InitialContext ().lookup ("java:comp/BeanManager");
         }
         return getReference (beanManager, clazz);
      } catch (NamingException e) {
         throw new RuntimeException (e);
      }
   }
   
   @SuppressWarnings ("unchecked")
   public static <T extends Object> T getReference (BeanManager beanManager, Class <T> clazz) {
      Bean <?> bean = null;
      Set <Bean <?>> beans = beanManager.getBeans (clazz);
      
      if (beans.size () != 1) {
         throw new RuntimeException (String.format ("1 implementation for class %s expected, but %s found", clazz.getName (), beans.size ()));
      }
      bean = beans.iterator ().next ();
      
      return (T) beanManager.getReference (bean, clazz, beanManager.createCreationalContext (bean));
   }
}
