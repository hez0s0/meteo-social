package es.uned.grc.pfc.meteo.client.place;

import java.util.ArrayList;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.RequestContext;

import es.uned.grc.pfc.meteo.client.model.IRequestParamFilterProxy;
import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

public abstract class AbstractFilterPlace extends AbstractPlace {
   
   protected List <IRequestParamFilterProxy> filters = null;
   protected String filterRepresentation = null; //used to persist the request filter, since no *Proxy objects can be CREATED here
   
   public List <IRequestParamFilterProxy> getFilters () {
      return filters;
   }

   public void setFilters (List <IRequestParamFilterProxy> filters) {
      this.filters = filters;
   }

   public String getFilterRepresentation () {
      return filterRepresentation;
   }

   public void setFilterRepresentation (String filterRepresentation) {
      this.filterRepresentation = filterRepresentation;
   }
   
   public static <P extends AbstractFilterPlace> String serialize (P place) {
      String result = serialize (place.getFilters ());
      
      place.setFilterRepresentation (result);
      
      return result;
   } //end of serialize
   
   public static String serialize (List <IRequestParamFilterProxy> filters) {
      StringBuffer token = new StringBuffer ();
      
      if (filters != null) {
         for (IRequestParamFilterProxy filter : filters) {
            if (token.length () > 0) {
               token.append (ISharedConstants.PARAM_SEP);
            }
            token.append (filter.getParam ());
            token.append (ISharedConstants.MULTIPLE_SEP);
            token.append (filter.getValue ());
            if (!PortableStringUtils.isEmpty (filter.getRepresentation ())) {
               token.append (ISharedConstants.MULTIPLE_SEP);
               token.append (filter.getRepresentation ());
            }
            if (filter.getMustNull () != null) {
               token.append (ISharedConstants.MULTIPLE_SEP);
               token.append (filter.getMustNull ());
            }
         }
      }
      
      return token.toString ();
   } //end of serialize
   
   public static <P extends AbstractFilterPlace, R extends RequestContext> void deserialize (P place, String token, R requestContext) {
      String [] params = null;
      String [] values = null;
      IRequestParamFilterProxy filter = null;
      List <IRequestParamFilterProxy> filters = null;
      
      place.setFilterRepresentation (token);
      
      if ( (!PortableStringUtils.isEmpty (token)) && (requestContext != null) ) {
         params = token.split (ISharedConstants.PARAM_SEP);
         filters = new ArrayList <IRequestParamFilterProxy> (params.length);
         
         for (String param : params) {
            filter = requestContext.create (IRequestParamFilterProxy.class);
            values = param.split (ISharedConstants.MULTIPLE_SEP);
            
            if (values.length > 0) {
               filter.setParam (values [0]);
               if (values.length > 1) {
                  filter.setValue (values [1]);
                  if (values.length > 2) {
                     filter.setRepresentation (values [2]);
                     if (values.length > 3) {
                        try {
                           filter.setMustNull (Boolean.parseBoolean (values [3]));
                        } catch (Exception e) {}
                     } //has mustnull
                  } //has representation
               } //has value
               filters.add (filter);
            } //has param
         } //end of loop over params
         place.setFilters (filters);
      }
   } //end of deserialize
} //end of AbstractFilterPlace
