package es.uned.grc.pfc.meteo.client.util;

public abstract class SearchParamRepresentationGetter <E extends Object> {
   public abstract String getRepresentationGetter (E entity);
   
   public String getValue (E entity) {
      return (entity != null) ? String.valueOf (entity).trim () : "";
   }
}
