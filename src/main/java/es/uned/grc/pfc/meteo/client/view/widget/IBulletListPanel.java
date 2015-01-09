package es.uned.grc.pfc.meteo.client.view.widget;

import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.requestfactory.shared.RequestContext;

public interface IBulletListPanel <E extends Object> {
   void display (List <E> entityProxies, PlaceController placeController, RequestContext requestContext);
}
