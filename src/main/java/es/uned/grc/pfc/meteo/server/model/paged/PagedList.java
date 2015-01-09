package es.uned.grc.pfc.meteo.server.model.paged;

import java.util.List;

/**
 * Wrapper for a list of partial results from a wider
 * resultset (meant for pagination).
 * 
 * Unfortunately, RequestFactory does not support polymorphic
 * lists by now, so it will have to be extended for every listed
 * entity (grffff).
 */
public class PagedList  <K extends Number, E> {

   private List <E> list = null;
   private long realSize = 0;
   
   /** DO NOT USE: for serialization purposes only */
   @Deprecated
   public PagedList () {
   }
   
   public PagedList (List <E> list, long realSize) {
      this.list = list;
      this.realSize = realSize;
   }
   
   public PagedList (PagedList <K, E> pagedList) {
      this (pagedList.getList (), pagedList != null ? pagedList.getRealSize () : 0);
   }

   public List <E> getList () {
      return list;
   }

   public void setList (List <E> list) {
      this.list = list;
   }

   public long getRealSize () {
      return realSize;
   }
   
   public void setRealSize (long realSize) {
      this.realSize = realSize;
   }
}
