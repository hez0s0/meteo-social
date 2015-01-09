package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.web.bindery.requestfactory.shared.BaseProxy;

import es.uned.grc.pfc.meteo.client.place.AbstractPlace;
import es.uned.grc.pfc.meteo.client.view.base.IAsyncCellTableView;

public abstract class AbstractAsyncDataActivity <E extends BaseProxy, V extends IAsyncCellTableView <E>, P extends AbstractPlace> extends AbstractBaseActivity {
   
   protected V listView = null;
   protected AsyncDataProvider <E> listAsyncDataProvider = null;
   
   public AbstractAsyncDataActivity (P place, V listView, PlaceController placeController) {
      super (placeController, place);
      this.listView = listView;
   }
   
   protected abstract AsyncDataProvider <E> getAsyncDataProvider (final EventBus eventBus);
   
   @Override
   public void start (AcceptsOneWidget panel, final EventBus eventBus) {
      panel.setWidget (listView.asWidget ());
       
      listAsyncDataProvider = getAsyncDataProvider (eventBus);

      listAsyncDataProvider.addDataDisplay (listView.getDataTable ());
   }

   @Override
   public void onStop () {
      super.onStop ();
      
      if (listAsyncDataProvider != null) {
         listAsyncDataProvider.removeDataDisplay (listView.getDataTable ());
      }
   }
}
