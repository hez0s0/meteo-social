package es.uned.grc.pfc.meteo.client.view;

import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.view.base.IView;

public interface IStationMapView extends IView {
   void setCenterStation (IStationProxy station);
}
