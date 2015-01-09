package es.uned.grc.pfc.meteo.server.model.paged;

import java.util.ArrayList;
import java.util.List;

import es.uned.grc.pfc.meteo.server.model.Observation;

/**
 * Unfortunately, RequestFactory does not support polymorphic
 * lists by now, so we need to extend PagedList for every
 * pageable entity (and declare its proxy).
 */
public class ObservationPagedList extends PagedList <Integer, Observation> {
   
   public ObservationPagedList () {
      super (new ArrayList <Observation> (), 0);
   }
   public ObservationPagedList (List <Observation> list) {
      super (list, list.size ());
   }
   
   public ObservationPagedList (PagedList <Integer, Observation> pagedList) {
      super (pagedList);
   }
}
