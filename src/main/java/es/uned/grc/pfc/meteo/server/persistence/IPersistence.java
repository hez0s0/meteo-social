package es.uned.grc.pfc.meteo.server.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import es.uned.grc.pfc.meteo.server.model.RequestParam;
import es.uned.grc.pfc.meteo.server.model.base.IEntity;
import es.uned.grc.pfc.meteo.server.model.paged.PagedList;

/**
 * Interface for abstract management of most common persistence operations.
 * 
 * @param <K> Specify the type of the key identifier of the IEntity
 * @param <E> Specify an IEntity (the Value Object).
 */
public interface IPersistence <K extends Number, E extends IEntity <K>> {
   /**
    * Creates a new entity (must be new)
    */
   void save (E entity);
   /**
    * Updates a given entity (must previously exist)
    */
   E merge (E entity);
   /**
    * Creates or updates a given entity
    */
   E saveOrMerge (E entity);
   /**
    * Removes given entity from storage
    */
   void delete (E entity);
   /**
    * Removes one single entity from storage with given Id
    */
   boolean deleteById (K Id);
   /**
    * Removes a list of entities from storage
    */
   int deleteListById (Collection <K> idList);
   /**
    * Removes a list of entities from storage
    */
   int deleteList (Collection <E> entityList);
   /**
    * Removes the whole table
    */
   int deleteAll ();
   /**
    * Re-reads an entity from DB
    */
   void refresh (E entity);
   /**
    * Retrieves one entity by given Id
    */
   E findById (K Id);
   /**
    * Retrieves one entity by given Id applying given fetchInfo. If the explicitInitialize
    * param is true, the fetching will be done via Hibernate.initialize (that is
    * separate queries) instead of via EAGER fetching, for the JOIN-marked properties.
    * If deep is true, the initialize shall consider all the properties separated by . and
    * recursively initialize all of them.
    */
   E findById (K Id, Map <String, FetchMode> fetchInfo, boolean explicitInitialize, boolean deep);
   /** retrieves a list of entities by given Id list */
   List <E> findByIdList (List <K> IdList);
   /**
    * Gets with no conditions, sorting, etc., the whole list of entities
    */
   List <E> findAll ();
   /**
    * Retrieves a paginated list with the given requestParam as guidance for
    * page size
    */
   PagedList <K, E> getList (RequestParam requestParam);
   /**
    * Retrieves a paginated list with the given requestParam as guidance for
    * page size applying given fetchInfo
    */
   PagedList <K, E> getList (RequestParam requestParam, Map <String, FetchMode> fetchInfo);
   /**
    * Retrieves a paginated list with the given requestParam as guidance for
    * page size applying given fetchInfo and initializes subcollections is
    * required
    */
   PagedList <K, E> getList (RequestParam requestParam, Map <String, FetchMode> fetchInfo, boolean explicitInitialize, boolean deep);
   /**
    * Retrieves a paginated list with the given requestParam as guidance for
    * page size applying given fetchInfo and initializes subcollections is
    * required. It may contain as well a map of contextParams
    */
   PagedList <K, E> getList (RequestParam requestParam, Map <String, FetchMode> fetchInfo, boolean explicitInitialize, boolean deep, Map <String, Object> contextParams);
   /** check if the element exists (same attribute-value) */
   boolean exists (String propertyName, String name);
   /** finds a given element within a list */
   E find (K Id, List <E> list);
   /** given a list of entities, extract their Ids into a list */
   List <K> getIdList (List <E> entities);
   /** given a string representing a list of Integer, create a List <Integer> */
   List <Integer> parseIntegerList (String encodedIdList);
   E flush (E entity);
}
