package es.uned.grc.pfc.meteo.server.model.paged;

import java.util.ArrayList;
import java.util.List;

import es.uned.grc.pfc.meteo.server.model.Station;

/**
 * Unfortunately, RequestFactory does not support polymorphic
 * lists by now, so we need to extend PagedList for every
 * pageable entity (and declare its proxy).
 */
public class StationPagedList extends PagedList <Integer, Station> {
   
   public StationPagedList () {
      super (new ArrayList <Station> (), 0);
   }
   public StationPagedList (List <Station> list) {
      super (list, list.size ());
   }
   
   public StationPagedList (PagedList <Integer, Station> pagedList) {
      super (pagedList);
   }
}
