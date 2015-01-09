package es.uned.grc.pfc.meteo.server.persistence;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import es.uned.grc.pfc.meteo.server.model.IModelEnums;
import es.uned.grc.pfc.meteo.server.model.RequestParam;
import es.uned.grc.pfc.meteo.server.model.RequestParamFilter;
import es.uned.grc.pfc.meteo.server.model.base.IEntity;
import es.uned.grc.pfc.meteo.server.model.paged.PagedList;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

/**
 * Basic persistence features, to be extended by specific DAOs with 
 * given entity and key. Most of the actions should be provided by this class
 * only entity-related stuff should come to implementations.
 */
public abstract class AbstractPersistence <K extends Number, E extends IEntity <K>> 
   implements IPersistence <K, E> {

   private static Log log = LogFactory.getLog (AbstractPersistence.class);
   
   protected Class <K> keyClass = null;
   protected Class <E> entityClass = null;

   @Autowired
   private SessionFactory sessionFactory = null;

   @SuppressWarnings ("unchecked")
   public AbstractPersistence () {
      ParameterizedType genericSuperclass = (ParameterizedType) getClass ().getGenericSuperclass ();
      this.keyClass = (Class <K>) genericSuperclass.getActualTypeArguments () [0];
      this.entityClass = (Class <E>) genericSuperclass.getActualTypeArguments () [1];
   }

   /**
    * Provides access to the persistent current session.
    */
   protected final Session getCurrentSession () {
      return sessionFactory.getCurrentSession ();
   }

   /**
    * From the current session, creates a criteria of the entity class
    */
   protected Criteria getBaseCriteria () {
      return getCurrentSession ().createCriteria (entityClass);
   }
   
   @Override
   @Transactional
   public E saveOrMerge (E entity) {
      E result = null;
      if ( (entity.getId () != null) && (entity.getId ().longValue () > 0) ) {
         result = merge (entity);
      } else {
         entity.setVersion (IModelEnums.FIRST_VERSION);
         save (entity);
         result = entity;
      }
      return result;
   }
   
   @Override
   @Transactional
   public void save (E entity) {
      getCurrentSession ().save (entity);
   }

   @Override
   @Transactional
   public void delete (E entity) {
      getCurrentSession ().delete (entity);
   }

   @Override
   @Transactional
   public boolean deleteById (K ID) {
      E entity = findById (ID);
      if (entity != null) {
         getCurrentSession ().delete (entity);
         return true;
      } else {
         return false;
      }
   }

   @Override
   @Transactional
   public int deleteListById (Collection <K> idList) {
      return getCurrentSession ()
               .createQuery ("DELETE FROM " + entityClass.getName () + " WHERE ID in :idList")
               .setParameterList ("idList", idList)
               .executeUpdate ();
   }

   @Override
   @Transactional
   public int deleteList (Collection <E> entityList) {
      if ( (entityList != null) && (entityList.size () > 0) ) {
         List <K> idList = new ArrayList <K> (entityList.size ());
         for (E entity : entityList) {
            idList.add (entity.getId ());
         }
         return deleteListById (idList);
      } else {
         return 0;
      }
   }

   @Override
   @Transactional
   public int deleteAll () {
      return getCurrentSession ().createQuery ("DELETE FROM " + entityClass.getName ()).executeUpdate ();
   }

   @SuppressWarnings ("unchecked")
   @Override
   @Transactional
   public E merge (E entity) {
      return (E) getCurrentSession ().merge (entity);
   }

   @Override
   @Transactional
   public void refresh (E entity) {
      getCurrentSession ().refresh (entity);
   }

   @SuppressWarnings ("unchecked")
   @Override
   @Transactional
   public List <E> findByIdList (List <K> IDList) {
      if ( (IDList != null) && (IDList.size () > 0) ) {
         if (IDList.size () == 1) {
            E entity = findById (IDList.get (0));
            return (entity != null) ? Collections.<E> singletonList (entity) : Collections.<E> emptyList ();
         }
         Criteria criteria = getCurrentSession ().createCriteria (entityClass);
         criteria.add (new IdentifierInExpression (IDList));
         return (List <E>) criteria.list ();
      }
      return new ArrayList <E> (0);
   }

   @Override
   @Transactional
   public E findById (K ID) {
      return findById (ID, null, false, false);
   }

   @SuppressWarnings ("unchecked")
   @Override
   @Transactional
   public E findById (K ID, Map <String, FetchMode> fetchInfo, boolean explicitInitialize, boolean deep) {
      Criteria criteria = getCurrentSession ().createCriteria (entityClass);
      setFetchInfo (criteria, fetchInfo, explicitInitialize);
      criteria.add (Restrictions.idEq (ID));
      E result = (E) criteria.uniqueResult ();
      
      if (explicitInitialize) { //we must try to initialize first level of fetchInfo for this IEntity
         initialize (result, fetchInfo, deep);
      }
      return result;
   }
   
   @Transactional
   private void initialize (E entity, Map <String, FetchMode> fetchInfo, boolean deep) {
      try {
         if (fetchInfo != null) {
            for (Map.Entry <String, FetchMode> entry : fetchInfo.entrySet ()) {
               if (entry.getValue ().equals (FetchMode.JOIN)) {
                  initialize (entity, entry.getKey (), deep);
               }
            }
         }
      } catch (Exception e) {
         log.error (String.format ("Error initializing entity of class '%s'", entityClass), e);
      }
   }
   
   @SuppressWarnings ("rawtypes")
   private void initialize (IEntity entity, String property, boolean deep) {
      String [] propertiesGraph = null;
      String remainingPropertiesPath = null;
      String getterMethod = null;
      Method method = null;
      Object child = null;
      Iterator childIterator = null;
      Object subChild = null;
      
      if (entity != null) {
         //initialize first element in object graph
         propertiesGraph = property.split ("\\.");
         getterMethod = "get" + StringUtils.capitalize (propertiesGraph [0]);
         try {
            method = entity.getClass ().getMethod (getterMethod);
            child = method.invoke (entity);
            if (child != null) {
               Hibernate.initialize (child);
         
               if ( (deep) && (propertiesGraph.length > 1) ) {
                  //if element is a collection, iterate it to initialize its children
                  remainingPropertiesPath = property.substring (property.indexOf (".") + 1);
                  if ( (child instanceof PersistentBag) && (!((PersistentBag) child).isEmpty ()) ) {
                     childIterator = ((PersistentBag) child).iterator ();
                     while (childIterator.hasNext ()) {
                        subChild = childIterator.next ();
                        
                        if (subChild instanceof IEntity) {
                           initialize ((IEntity) subChild, remainingPropertiesPath, deep);
                        }
                     }
                  } else if (child instanceof IEntity){ //just initialize its children
                     initialize ((IEntity) child, remainingPropertiesPath, deep);
                  }
               }
            }

         } catch (NoSuchMethodException e) {
            throw new RuntimeException (e);
         } catch (SecurityException e) {
            throw new RuntimeException (e);
         } catch (IllegalAccessException e) {
            throw new RuntimeException (e);
         } catch (IllegalArgumentException e) {
            throw new RuntimeException (e);
         } catch (InvocationTargetException e) {
            throw new RuntimeException (e);
         }
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   @Transactional
   public List <E> findAll () {
      Criteria criteria = getCurrentSession ().createCriteria (entityClass);

      applyDefaultSort (criteria, new HashMap <String, Object> ());
      
      return (List <E>) criteria.list ();
   }

   @Override
   @Transactional
   public E flush (E entity) {
      getCurrentSession ().flush ();
      return entity;
   }

   @Override
   @Transactional
   public PagedList <K, E> getList (RequestParam requestParam) {
      return getList (requestParam, null);
   }
   
   @Override
   @Transactional
   public PagedList <K, E> getList (RequestParam requestParam, Map <String, FetchMode> fetchInfo) {
      return getList (requestParam, fetchInfo, false, false);
   }
   
   @Override
   @Transactional
   public PagedList <K, E> getList (RequestParam requestParam, Map <String, FetchMode> fetchInfo, boolean explicitInitialize, boolean deep) {
      return getList (requestParam, fetchInfo, explicitInitialize, deep, new HashMap <String, Object> ());
   }
   
   @SuppressWarnings ("unchecked")
   @Override
   @Transactional
   public PagedList <K, E> getList (RequestParam requestParam, 
                                    Map <String, FetchMode> fetchInfo, 
                                    boolean explicitInitialize, 
                                    boolean deep,
                                    Map <String, Object> contextParams) {
      if (contextParams == null) { 
         contextParams = new HashMap <String, Object> ();
      }
      Criteria criteria = getCurrentSession ().createCriteria (entityClass);
      criteria.setResultTransformer (Criteria.DISTINCT_ROOT_ENTITY);

      applyFilter (criteria, requestParam, contextParams);
      Long realSize = (Long) criteria.setProjection (Projections.projectionList ().add (Projections.rowCount ())).uniqueResult ();

      criteria = getSortedCriteria (requestParam, contextParams);

      setFetchInfo (criteria, fetchInfo, explicitInitialize);
      
      applyFilter (criteria, requestParam, contextParams);
      
      if ( (requestParam.getStart () != null) && (requestParam.getStart () > 0) ) {
         criteria.setFirstResult (requestParam.getStart ());
      }
      if ( (requestParam.getLength () != null) && (requestParam.getLength () > 0) ) {
         criteria.setMaxResults (requestParam.getLength ());
      }

      PagedList <K, E> pagedList = new PagedList <K, E>  (criteria.list (), (realSize != null) ? realSize : 0);
      
      if ( (explicitInitialize) && (pagedList != null) && (pagedList.getList () != null) ) {
         for (E entity : pagedList.getList ()) {
            initialize (entity, fetchInfo, deep);
         }
      }
      
      return pagedList;
   }

   /**
    * Sets the given fetchmode on given properties for the given criteria
    */
   private void setFetchInfo (Criteria criteria, Map <String, FetchMode> fetchInfo, boolean explicitInitialize) {
      FetchMode value = null;
      if (fetchInfo != null) {
         for (Map.Entry <String, FetchMode> entry : fetchInfo.entrySet ()) {
            value = entry.getValue ();
            if ( (explicitInitialize) && (value.equals (FetchMode.JOIN)) ) {
               criteria.setFetchMode (entry.getKey (), FetchMode.SELECT); //it shall be retrieved via Hibernate.initialize
            } else {
               criteria.setFetchMode (entry.getKey (), value);
            }
         }
      }
   }

   /**
    * Creates the criteria for the entityClass with an ASC/DESC sorting field
    * and adds the DISTINCT_ROOT_ENTITY resultTransformer
    */
   protected Criteria getSortedCriteria (RequestParam requestParam, Map <String, Object> contextParams) {
      return getSortedCriteria (getCurrentSession ().createCriteria (entityClass), requestParam, contextParams);
   }

   /**
    * Creates the criteria for the entityClass with an ASC/DESC sorting field
    * and adds the DISTINCT_ROOT_ENTITY resultTransformer
    */
   protected Criteria getSortedCriteria (Criteria criteria, RequestParam requestParam, Map <String, Object> contextParams) {
      String sorts [] = null;
      String sortField = null;
      Criteria sortCriteria = null;
      
      if (!StringUtils.isEmpty (requestParam.getSortField ())) {
         if (requestParam.getSortField ().startsWith (ISharedConstants.SPECIAL_ORDER_MARK)) {
            // the sort is marked as special: it is not a real field, but has a special meaning for implementing classes
            applySpecialSort (criteria, requestParam.getSortField ().substring (1), requestParam.getAscending (), contextParams);
         } else {
            sorts = requestParam.getSortField ().split ("\\.");
            sortCriteria = criteria;
            for (int i = 0; i < sorts.length - 1; i++) { //if there is a path expression, so let us add criterias
               sortCriteria = createCriteria (sortCriteria, sorts [i], null, JoinType.LEFT_OUTER_JOIN, contextParams);
            }
            sortField = sorts [sorts.length - 1];
            
            if (requestParam.getAscending ()) {
               sortCriteria.addOrder (Order.asc (sortField));
            } else {
               sortCriteria.addOrder (Order.desc (sortField));
            }
         }
      } else {
         applyDefaultSort (criteria, contextParams);
      }
      criteria.setResultTransformer (Criteria.DISTINCT_ROOT_ENTITY);
      
      return criteria;
   }

   protected void applySpecialSort (Criteria criteria, String sortField, boolean asc, Map <String, Object> contextParams) {
      throw new RuntimeException (String.format ("A special sorting criteria was provided ('%s') but no special implementation is provided by this persistence class", sortField));
   }

   /**
    * Creates a sub-criteria, but is meant for extension if a special mechanism is required
    * (for example, to prevent duplicate association path problems)
    */
   protected Criteria createCriteria (Criteria criteria, String property, String alias, JoinType joinType, Map <String, Object> contextParams) {
      return (alias == null) ? criteria.createCriteria (property, joinType) : criteria.createCriteria (property, alias, joinType);
   }

   /**
    * Creates a sub-criteria if non-existing, gets it from the contextParams if already existing
    */
   protected Criteria getCriteria (String paramName, String criteriaName, String alias, JoinType joinType, Criteria criteria, Map <String, Object> contextParams) {
      Criteria subCriteria = (Criteria) contextParams.get (paramName + "_" + String.valueOf (criteria.hashCode ()));
      
      if (subCriteria == null) {
         subCriteria = (alias != null) ? criteria.createCriteria (criteriaName, alias, joinType) : criteria.createCriteria (criteriaName, joinType);
         
         contextParams.put (paramName + "_" + String.valueOf (criteria.hashCode ()), subCriteria);
      }
      
      return subCriteria;
   }
   
   @SuppressWarnings ("unchecked")
   @Override
   public boolean exists (String propertyName, String name) {
      Criteria criteria = getBaseCriteria ();
      
      criteria.add (Restrictions.eq (propertyName, name));
      criteria.setProjection (Projections.rowCount ());
      
      List <IEntity <?>> results = (List <IEntity <?>>) criteria.list ();
      Number count = (Number) results.iterator ().next ();
      
      return count.intValue () > 0;
   }
   
   private void applyFilter (Criteria criteria, RequestParam requestParam, Map <String, Object> contextParams) {
      if ( (requestParam.getFilters () != null) && (!requestParam.getFilters ().isEmpty ()) ) {
         applyFilter (criteria, requestParam.getFilters (), contextParams);
      } else if ( (!StringUtils.isEmpty (requestParam.getFilter ())) || (isDefaultFilter ()) ) {
         applyFilter (criteria, requestParam.getFilter (), contextParams);
      }
   }
   
   /**
    * Override this method to support search of free text.
    */
   protected void applyFilter (Criteria criteria, String filter, Map <String, Object> contextParams) {
      throw new RuntimeException ("Search not implemented in the child class, please review your code.");
   }
   
   /**
    * Override this method to support search with different criteria.
    */
   protected void applyFilter (Criteria criteria, List <RequestParamFilter> filters, Map <String, Object> contextParams) {
      throw new RuntimeException ("Search not implemented in the child class, please review your code.");
   }
   
   /**
    * Override this method to support for specific sorting when not explicit from UI.
    */
   protected void applyDefaultSort (Criteria criteria, Map <String, Object> contextParams) {
      //to be implemented by child classes
   }

   /**
    * Override this method to support custom filters in sub-classes
    * no matter if there are external (user-provided) filters.
    */
   protected boolean isDefaultFilter () {
      return false;
   }
   
   /**
    * Create a searchable string via LIKE xxx%.
    */
   protected String asLike (String filter) {
      return filter + "%";
   }

   public E find (K ID, List <E> list) {
      E result = null;
      
      for (E element : list) {
         if (element.getId ().equals (ID)) {
            result = element;
            break;
         }
      }
      
      return result;
   }
   
   /**
    * Create a searchable string via LIKE %xxx%.
    */
   protected String asLikeWide (String filter) {
      return "%" + filter + "%";
   }

   protected void addIDListRestrictions (Criteria criteria, String subCriteriaName, String valueList) {
      addIDListRestrictions (criteria, subCriteriaName, parseIntegerList (valueList), false);
   }
   
   protected void addIDListRestrictions (Criteria criteria, String subCriteriaName, List <Integer> idList, boolean orNull) {
      if (idList.size () > 0) {
         criteria.createAlias (subCriteriaName, subCriteriaName);
         if (orNull) {
            criteria.add (Restrictions.or (Restrictions.isEmpty (subCriteriaName),
                                           Restrictions.in (subCriteriaName + ".ID", idList)));
         } else {
            criteria.add (Restrictions.in (subCriteriaName + ".ID", idList));
         }
      }
   }
   
   @Override
   public List <Integer> parseIntegerList (String encodedIDList) {
      List <Integer> result = new ArrayList <Integer> ();
      String [] idList = null;
      
      if (!StringUtils.isEmpty (encodedIDList)) {
         idList = encodedIDList.split (ISharedConstants.ID_LIST_ALTERNATIVE_SEPARATOR_RE);
         
         for (String id : idList) {
            if (!StringUtils.isEmpty (id)) {
               result.add (Integer.parseInt (id));
            }
         }
      }
      
      return result;
   }
   
   public List <K> getIdList (List <E> entities) {
      List <K> idList = new ArrayList <K> (entities != null ? entities.size () : 0);
      
      if (entities != null) {
         for (E entity : entities) {
            idList.add (entity.getId ());
         }
      }
      
      return idList;
   }

   protected RequestParamFilter getParam (String searchParam, List <RequestParamFilter> filters) {
      for (RequestParamFilter filter : filters) {
         if (filter.getParam ().equals (searchParam)) {
            return filter; 
         }
      }
      return null;
   }

   /**
    * Build an Order clause with asc or desc based on a property
    */
   protected Order getOrder (String property, boolean asc) {
      return (asc) ? Order.asc (property) : Order.desc (property);
   }
   
   public class IdentifierInExpression implements Criterion {

      private static final long serialVersionUID = -3120283684494528083L;
      private List <K> values = null;
      private Criterion delegate = null;

      public IdentifierInExpression (List <K> values) {
         this.values = values;
      }

      @Override
      public String toSqlString (Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
         String idProp = getIdProperty (criteria, criteriaQuery);
         delegate = Restrictions.in (idProp, values);
         return delegate.toSqlString (criteria, criteriaQuery);
      }

      @Override
      public TypedValue [] getTypedValues (Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
         return delegate.getTypedValues (criteria, criteriaQuery);
      }

      @Override
      public String toString () {
         return delegate.toString ();
      }

      /**
       * Retrieve propertyName that represents the Identifier of table
       */
      private String getIdProperty (Criteria criteria, CriteriaQuery criteriaQuery) {
         return criteriaQuery.getFactory ().getIdentifierPropertyName (criteriaQuery.getEntityName (criteria));
      }
   }
}
