package es.uned.grc.pfc.meteo.client.view.table;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.user.cellview.client.Column;

import es.uned.grc.pfc.meteo.client.model.IObservationBlockProxy;
import es.uned.grc.pfc.meteo.client.util.IClientConstants;

public class IndexedObservationColumn extends Column <IObservationBlockProxy, String> {
   private final int index;

   public IndexedObservationColumn (int index) {
      super (new EditTextCell ());
      this.index = index;
   }

   @Override
   public String getValue (IObservationBlockProxy observationBlock) {
      return observationBlock.getObservations ().get (index) != null && observationBlock.getObservations ().get (index).getValue () != null ? observationBlock.getObservations ().get (index).getValue () : IClientConstants.TEXT_CONSTANTS.emptyValue ();
   }
}