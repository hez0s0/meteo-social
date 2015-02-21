package es.uned.grc.pfc.meteo.client.view;

import java.util.List;

import es.uned.grc.pfc.meteo.client.model.IObservationBlockProxy;
import es.uned.grc.pfc.meteo.client.view.base.IAsyncCellTableView;

public interface IObservationListView extends IAsyncCellTableView <IObservationBlockProxy> {

   void initTableColumns (List <IObservationBlockProxy> observationBlock);
}
