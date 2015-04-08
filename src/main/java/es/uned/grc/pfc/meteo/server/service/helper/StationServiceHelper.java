package es.uned.grc.pfc.meteo.server.service.helper;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.grc.pfc.meteo.server.model.paged.StringPagedList;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

/**
 * Supports non-client invokable actions of the StationService module
 */
@Service
public class StationServiceHelper {

   private static Logger logger = LoggerFactory.getLogger (StationServiceHelper.class);
   
   @Autowired
   private SessionFactory sessionFactory = null;
   
   /**
    * Obtains a list of properties of stations for the given filter
    */
   public StringPagedList getStationProperties (String value, ISharedConstants.StationFilter filter) {
      try {
         StringPagedList result = new StringPagedList ();

         String property = filter.equals (ISharedConstants.StationFilter.CITY) ? "city" : "country";
       
         String countQueryString = "SELECT COUNT(DISTINCT " + property + ") FROM metStation " +
                                   "WHERE " + property + " LIKE '" + value + "%' ";
         @SuppressWarnings ("unchecked")
         List <Object> countResult = sessionFactory.getCurrentSession ().createSQLQuery (countQueryString).list ();
         result.setRealSize (((Number) countResult.get (0)).longValue ());
         
         String queryString = "SELECT DISTINCT " + property + " FROM metStation " +
                              "WHERE " + property + " LIKE '" + value + "%'" + 
                              "ORDER BY " + property;
         @SuppressWarnings ("unchecked")
         List <Object> queryResults = sessionFactory.getCurrentSession ().createSQLQuery (queryString).list ();
         result.setList (new ArrayList <String> (queryResults.size ()));
         for (Object queryResult : queryResults) {
            result.getList ().add (queryResult.toString ());
         }
         
         return result;
      } catch (Exception e) {
         logger.error ("Error listing stations", e);
         throw new RuntimeException ("Could not list stations. See server logs.");
      }
   }
}
