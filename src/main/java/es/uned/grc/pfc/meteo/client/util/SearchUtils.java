package es.uned.grc.pfc.meteo.client.util;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.RequestContext;

import es.uned.grc.pfc.meteo.client.model.IRequestParamFilterProxy;
import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

public class SearchUtils {
   
   public static <E extends Object, P extends Enum <?>, R extends RequestContext> 
          void appendListFilter (List <IRequestParamFilterProxy> filters, 
                                 P param, 
                                 List <E> entities,
                                 SearchParamRepresentationGetter <E> representationGetter,
                                 R requestContext) {
      if ( (entities != null && (!entities.isEmpty ())) && (param != null) ) {
         appendFilter (filters, 
                       param, 
                       encodeEntityList (entities, representationGetter), 
                       encodeEntityRepresentationList (entities, representationGetter), 
                       requestContext);
      }
   } //end of appendListFilter
   
   public static <E extends Object, P extends Enum <?>, R extends RequestContext> 
          void appendFilter (List <IRequestParamFilterProxy> filters, 
                             P param, 
                             E entity, 
                             R requestContext) {
      appendFilter (filters,
                    param, 
                    entity, 
                    (SearchParamRepresentationGetter <E>) null,
                    requestContext);
   }
   
   public static <E extends Object, P extends Enum <?>, R extends RequestContext> 
          void appendFilter (List <IRequestParamFilterProxy> filters, 
                             P param, 
                             E entity, 
                             SearchParamRepresentationGetter <E> representationGetter,
                             R requestContext) {
      String representation = null;
      String valueString = null;

      if ( (entity != null) && (param != null) ) {
         if (representationGetter != null) {
            valueString = representationGetter.getValue (entity);
            
            representation = representationGetter.getRepresentationGetter (entity);
         } else {
            valueString = String.valueOf (entity).trim ();
         }
         
         appendFilter (filters, param, valueString, representation, requestContext);
      }
   } //end of appendFilter
   
   private static <P extends Enum <?>, R extends RequestContext> void appendFilter (List <IRequestParamFilterProxy> filters, 
                                                                                    P param, 
                                                                                    String value, 
                                                                                    String representation,
                                                                                    R requestContext) {
      IRequestParamFilterProxy filter = null;

      filter = requestContext.create (IRequestParamFilterProxy.class);
      
      filter.setParam (param.toString ());
      filter.setValue (value);
      if (representation != null) {
         filter.setRepresentation (representation);
      }
      
      filters.add (filter);
   } //end of appendFilter
   
   private static <E extends Object> String encodeEntityList (List <E> entities, SearchParamRepresentationGetter <E> representationGetter) {
      StringBuffer result = new StringBuffer ();
      
      for (E entity : entities) {
         if (result.length () > 0) {
            result.append (ISharedConstants.ID_LIST_ALTERNATIVE_SEPARATOR);
         }
         if (entity instanceof IEntityProxy) {
            result.append (((IEntityProxy) entity).getId ());
         } else {
            result.append (representationGetter != null ? representationGetter.getValue (entity) : String.valueOf (entity).trim ());
         }
      }
      
      return result.toString ();
   } //end of encodeEntityList
   
   private static <E extends Object> String encodeEntityRepresentationList (List <E> entities, SearchParamRepresentationGetter <E> representationGetter) {
      
      if (representationGetter != null) {
         StringBuffer result = new StringBuffer ();
         
         for (E entity : entities) {
            if (result.length () > 0) {
               result.append (ISharedConstants.WORD_LIST_SEPARATOR);
               result.append (ISharedConstants.WORD_SEPARATOR);
            }
            result.append (representationGetter.getRepresentationGetter (entity));
         }
         
         return result.toString ();
      } else {
         return null;
      }
   } //end of encodeEntityRepresentationList
   
} //end of SearchUtils
