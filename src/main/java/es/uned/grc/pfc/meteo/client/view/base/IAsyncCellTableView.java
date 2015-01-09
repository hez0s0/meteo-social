package es.uned.grc.pfc.meteo.client.view.base;

import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.web.bindery.requestfactory.shared.BaseProxy;

public interface IAsyncCellTableView <E extends BaseProxy> extends IView {
   AbstractCellTable <E> getDataTable ();
}
