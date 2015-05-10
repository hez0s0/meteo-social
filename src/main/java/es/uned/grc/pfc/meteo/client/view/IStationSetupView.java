package es.uned.grc.pfc.meteo.client.view;

import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.view.base.IView;

public interface IStationSetupView extends IView {
   void setStation (IStationProxy response);
}
