package es.uned.grc.pfc.meteo.client.view.widget;

import java.util.List;

import es.uned.grc.pfc.meteo.client.model.IRequestParamFilterProxy;

public interface IFilterPanel extends IBulletListPanel <IRequestParamFilterProxy> {
   public List <IRequestParamFilterProxy> getFilters ();
}
