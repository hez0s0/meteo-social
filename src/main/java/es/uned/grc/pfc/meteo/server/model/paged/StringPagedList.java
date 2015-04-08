package es.uned.grc.pfc.meteo.server.model.paged;

import java.util.ArrayList;
import java.util.List;

/**
 * Unfortunately, RequestFactory does not support polymorphic
 * lists by now, so we need to extend PagedList for every
 * pageable entity (and declare its proxy).
 */
public class StringPagedList extends PagedList <Integer, String> {
   
   public StringPagedList () {
      super (new ArrayList <String> (), 0);
   }
   public StringPagedList (List <String> list) {
      super (list, list.size ());
   }
   
   public StringPagedList (PagedList <Integer, String> pagedList) {
      super (pagedList);
   }
}
