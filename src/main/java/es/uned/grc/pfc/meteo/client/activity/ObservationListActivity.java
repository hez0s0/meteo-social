package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

import es.uned.grc.pfc.meteo.client.model.IObservationProxy;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;
import es.uned.grc.pfc.meteo.client.view.IObservationListView;

public class ObservationListActivity extends AbstractAsyncDataActivity <IObservationProxy, IObservationListView, ObservationListPlace> {

   public ObservationListActivity (ObservationListPlace workSpaceListPlace, IObservationListView workSpaceListView, PlaceController placeController) {
      super (workSpaceListPlace, workSpaceListView, placeController);
   }
   @Override
   protected AsyncDataProvider <IObservationProxy> getAsyncDataProvider (final EventBus eventBus) {
      return new AsyncDataProvider <IObservationProxy> () {

         @Override
         protected void onRangeChanged (HasData <IObservationProxy> display) {
//            final Range range = display.getVisibleRange ();
//            final ColumnSortList sortList = listView.getDataTable ().getColumnSortList ();
//            IObservationRequestContext adminRequestContext = getRequestFactory (eventBus).getAdminContext ();
//            IRequestParamProxy requestParamProxy = adminRequestContext.create (IRequestParamProxy.class);
//
//            requestParamProxy.setStart (range.getStart ());
//            requestParamProxy.setLength (range.getLength ());
//
//            if ((sortList != null) && (sortList.size () > 0) && (sortList.get (0) != null) && (sortList.get (0).getColumn () != null)) {
//               requestParamProxy.setSortField (sortList.get (0).getColumn ().getDataStoreName ());
//               requestParamProxy.setAscending (sortList.get (0).isAscending ());
//            }
//
//            adminRequestContext.findWorkSpaces (requestParamProxy).fire (new Receiver <IObservationPagedListProxy> () {
//               @Override
//               public void onSuccess (IObservationPagedListProxy response) {
//                  // Push the data back into the list
//                  if (response.getRealSize () > Integer.MAX_VALUE) {
//                     throw new ClassCastException (PortableStringUtils.format ("Cannot cast long to int (%s)", response.getRealSize ()));
//                  }
//                  listView.getDataTable ().setRowCount (0);
//                  listView.getDataTable ().setRowCount (Long.valueOf (response.getRealSize ()).intValue ()); //we need to narrow it down (nastily): hibernate returns Long
//                  listView.getDataTable ().setRowData (range.getStart (), response.getList ());
//               }
//
//               @Override
//               public void onFailure (ServerFailure serverFailure) {
//                  eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, MessageChangeEvent.getTextMessages ().listError ("WorkSpace"), serverFailure));
//               }
//            });
         }
      };
   }
}
