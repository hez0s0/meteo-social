package es.uned.grc.pfc.meteo.server.model;

import java.util.List;

/**
 * Just a wrapper for client-server communications of paging, sort, filter options.
 */
public class RequestParam {
   
   private Integer length = null;
   private Integer start = null;
   private String sortField = null;
   private Boolean ascending = null;
   private String filter = null;
   private List <RequestParamFilter> filters = null;
   private String representation = null;
   
   public Integer getLength () {
      return length;
   }
   public void setLength (Integer length) {
      this.length = length;
   }
   public Integer getStart () {
      return start;
   }
   public void setStart (Integer start) {
      this.start = start;
   }
   public String getSortField () {
      return sortField;
   }
   public void setSortField (String sortField) {
      this.sortField = sortField;
   }
   public Boolean getAscending () {
      return ascending;
   }
   public void setAscending (Boolean ascending) {
      this.ascending = ascending;
   }
   public String getFilter () {
      return filter;
   }
   public void setFilter (String filter) {
      this.filter = filter;
   }
   public List <RequestParamFilter> getFilters () {
      return filters;
   }
   public void setFilters (List <RequestParamFilter> filters) {
      this.filters = filters;
   }
   public String getRepresentation () {
      return representation;
   }
   public void setRepresentation (String representation) {
      this.representation = representation;
   }
   
} //end of RequestParam
