package es.uned.grc.pfc.meteo.client.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import es.uned.grc.pfc.meteo.client.event.MessageChangeEvent;
import es.uned.grc.pfc.meteo.client.model.IObservationProxy;
import es.uned.grc.pfc.meteo.client.model.IRequestParamFilterProxy;
import es.uned.grc.pfc.meteo.client.model.IRequestParamProxy;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;
import es.uned.grc.pfc.meteo.client.request.IObservationRequestContext;
import es.uned.grc.pfc.meteo.client.view.IObservationListView;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

public class ObservationListActivity extends AbstractAsyncDataActivity <IObservationProxy, IObservationListView, ObservationListPlace> {

   public ObservationListActivity (ObservationListPlace workSpaceListPlace, IObservationListView workSpaceListView, PlaceController placeController) {
      super (workSpaceListPlace, workSpaceListView, placeController);
   }
   @Override
   protected AsyncDataProvider <IObservationProxy> getAsyncDataProvider (final EventBus eventBus) {
      return new AsyncDataProvider <IObservationProxy> () {

         @Override
         protected void onRangeChanged (HasData <IObservationProxy> display) {
            IRequestParamFilterProxy paramFilter = null;
            final Range range = display.getVisibleRange ();
            final ColumnSortList sortList = listView.getDataTable ().getColumnSortList ();
            IObservationRequestContext observationRequestContext = getRequestFactory (eventBus).getObservationContext ();
            IRequestParamProxy requestParamProxy = observationRequestContext.create (IRequestParamProxy.class);

            requestParamProxy.setStart (0);
            requestParamProxy.setLength (Integer.MAX_VALUE);

            //own station
            requestParamProxy.setFilters (new ArrayList <IRequestParamFilterProxy> ());
            paramFilter = observationRequestContext.create (IRequestParamFilterProxy.class);
            paramFilter.setParam (ISharedConstants.ObservationFilter.OWN.toString ());
            paramFilter.setValue ("true");
            requestParamProxy.getFilters ().add (paramFilter);
            
            if ((sortList != null) && (sortList.size () > 0) && (sortList.get (0) != null) && (sortList.get (0).getColumn () != null)) {
               requestParamProxy.setSortField (sortList.get (0).getColumn ().getDataStoreName ());
               requestParamProxy.setAscending (sortList.get (0).isAscending ());
            }

            observationRequestContext.getObservations (requestParamProxy)
                                     .with ("variable", "station")
                                     .fire (new Receiver <List <IObservationProxy>> () {
               @Override
               public void onSuccess (List <IObservationProxy> response) {
                  listView.getDataTable ().setRowCount (0);
                  listView.getDataTable ().setRowCount (response.size ());
                  listView.getDataTable ().setRowData (range.getStart (), response);
               }

               @Override
               public void onFailure (ServerFailure serverFailure) {
                  eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, MessageChangeEvent.getTextMessages ().listError ("Observation"), serverFailure));
               }
            });
         }
      };
   }
}
