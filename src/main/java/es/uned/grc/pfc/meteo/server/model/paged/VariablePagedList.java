package es.uned.grc.pfc.meteo.server.model.paged;

import java.util.ArrayList;
import java.util.List;

import es.uned.grc.pfc.meteo.server.model.Variable;

/**
 * Unfortunately, RequestFactory does not support polymorphic
 * lists by now, so we need to extend PagedList for every
 * pageable entity (and declare its proxy).
 */
public class VariablePagedList extends PagedList <Integer, Variable> {
   
   public VariablePagedList () {
      super (new ArrayList <Variable> (), 0);
   }
   public VariablePagedList (List <Variable> list) {
      super (list, list.size ());
   }
   
   public VariablePagedList (PagedList <Integer, Variable> pagedList) {
      super (pagedList);
   }
}
