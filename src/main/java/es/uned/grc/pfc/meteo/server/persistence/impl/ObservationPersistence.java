package es.uned.grc.pfc.meteo.server.persistence.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.RequestParamFilter;
import es.uned.grc.pfc.meteo.server.persistence.AbstractPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

@Repository
@Transactional (propagation = Propagation.REQUIRED)
public class ObservationPersistence extends AbstractPersistence <Integer, Observation> implements IObservationPersistence {
   
   @Override
   protected void applyDefaultSort (Criteria criteria, Map <String, Object> contextParams) {
      criteria.addOrder (Order.asc ("observed"));
   }
   
   @Override
   protected void applyFilter (Criteria criteria, List <RequestParamFilter> filters, Map <String, Object> contextParams) {
      ISharedConstants.ObservationFilter property = null;
      Date parsedDate = null;
      Criteria stationCriteria = null;
      Criteria variableCriteria = null;
      
      stationCriteria = criteria.createCriteria ("station");
      
      //iterate and apply all the filters
      for (RequestParamFilter filter : filters) {
         if (filter.getParam () != null) {
            property = ISharedConstants.ObservationFilter.valueOf (filter.getParam ());
            
            if (!StringUtils.isEmpty (filter.getValue ())) {
               switch (property) {
                  case OWN:
                     stationCriteria.add (Restrictions.eq ("own", true));
                     break;
                  case START_DATE:
                     try {
                        parsedDate = new SimpleDateFormat (ISharedConstants.SHARED_SHORT_DATE_FORMAT).parse (filter.getValue ());
                     } catch (ParseException e) {
                        throw new RuntimeException (String.format ("Start date cannot be parsed: '%s'", filter.getValue ()));
                     }
                     criteria.add (Restrictions.ge ("observed", parsedDate));
                     break;
                  case END_DATE:
                     try {
                        parsedDate = new SimpleDateFormat (ISharedConstants.SHARED_SHORT_DATE_FORMAT).parse (filter.getValue ());
                     } catch (ParseException e) {
                        throw new RuntimeException (String.format ("End date cannot be parsed: '%s'", filter.getValue ()));
                     }
                     criteria.add (Restrictions.lt ("observed", parsedDate));
                     break;
                  case STATION_ID:
                     stationCriteria.add (Restrictions.idEq (Integer.valueOf (filter.getValue ())));
                     break;
                  case VARIABLE_IDS:
                     if (variableCriteria == null) {
                        variableCriteria = criteria.createCriteria ("variable");
                     }
                     variableCriteria.add (Restrictions.in ("id", parseIntegerList (filter.getValue ()))); 
                     break;
                  case DERIVED_ONLY:
                     if (variableCriteria == null) {
                        variableCriteria = criteria.createCriteria ("variable");
                     }
                     variableCriteria.add (Restrictions.eq ("internal", true));
                     break;
                  case MEASURED_ONLY:
                     if (variableCriteria == null) {
                        variableCriteria = criteria.createCriteria ("variable");
                     }
                     variableCriteria.add (Restrictions.eq ("internal", false)); 
                     break;
               }
            }
         }
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List <Observation> getUncontrolled (int max) {
      if (max <= 0) {
         max = Integer.MAX_VALUE;
      }
      return getBaseCriteria ().add (Restrictions.isNull ("quality"))
                               .add (Restrictions.isNull ("rangeIni"))
                               .addOrder (Order.asc ("observed"))
                               .setMaxResults (max)
                               .list ();
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List <Observation> getUnderived (int max) {
      if (max <= 0) {
         max = Integer.MAX_VALUE;
      }
      return getBaseCriteria ().add (Restrictions.isNull ("derived"))
                               .add (Restrictions.isNull ("rangeIni"))
                               .addOrder (Order.asc ("observed"))
                               .setMaxResults (max)
                               .list ();
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List <Observation> getObservedInRange (Date ini, Date end) {
      return getBaseCriteria ().add (Restrictions.between ("observed", ini, end))
                               .addOrder (Order.asc ("observed"))
                               .list ();
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List <Observation> getDerivedInRange (Date ini, Date end) {
      return getBaseCriteria ().add (Restrictions.between ("rangeIni", ini, end))
                               .add (Restrictions.between ("rangeEnd", ini, end))
                               .list ();
   }
}
