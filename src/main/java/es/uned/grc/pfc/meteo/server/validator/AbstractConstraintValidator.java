package es.uned.grc.pfc.meteo.server.validator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.lang.StringUtils;

import es.uned.grc.pfc.meteo.server.model.base.IEntity;

public abstract class AbstractConstraintValidator <T> {

   protected String getMessage (Class <?> clazz, String defaultMessage) {
      return defaultMessage;
   }

   protected Object getPropertyValue (IEntity <?> entity, String field) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      Method method = entity.getClass().getMethod ("get" + StringUtils.capitalize (field));
      
      return method.invoke (entity);
   }
   
   public <E extends IEntity <?>> String getIdProperty (EntityManager entityManager, Class <E> entityClass) {
      Metamodel metamodel = entityManager.getMetamodel ();
      EntityType <E> entity = metamodel.entity (entityClass);
      Set <SingularAttribute <? super E, ?>> singularAttributes = entity.getSingularAttributes ();
      for (SingularAttribute <? super E, ?> singularAttribute : singularAttributes) {
         if (singularAttribute.isId ()) {
            return singularAttribute.getName ();
         }
      }
      throw new RuntimeException (String.format ("id field not found in entity %s", entityClass.getName ()));
   }
}