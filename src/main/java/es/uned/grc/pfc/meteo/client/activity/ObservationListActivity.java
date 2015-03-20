package es.uned.grc.pfc.meteo.client.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import es.uned.grc.pfc.meteo.client.event.MessageChangeEvent;
import es.uned.grc.pfc.meteo.client.model.IDerivedRangeProxy;
import es.uned.grc.pfc.meteo.client.model.IObservationBlockProxy;
import es.uned.grc.pfc.meteo.client.model.IRequestParamFilterProxy;
import es.uned.grc.pfc.meteo.client.model.IRequestParamProxy;
import es.uned.grc.pfc.meteo.client.model.IVariableObservationsProxy;
import es.uned.grc.pfc.meteo.client.model.IVariableProxy;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace.ObservationType;
import es.uned.grc.pfc.meteo.client.request.IObservationRequestContext;
import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.client.view.IObservationListView;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;
import es.uned.grc.pfc.meteo.shared.ISharedConstants.DerivedRangeType;

public class ObservationListActivity extends AbstractAsyncDataActivity <IObservationBlockProxy, IObservationListView, ObservationListPlace> {

   private ObservationListPlace listPlace = null;
   
   public ObservationListActivity (ObservationListPlace listPlace, IObservationListView listView, PlaceController placeController) {
      super (listPlace, listView, placeController);
      
      this.listPlace = listPlace;
   }
   
   @Override
   public void start (final AcceptsOneWidget panel, final EventBus eventBus) {
      getRequestFactory (eventBus).getObservationContext ().getTodayStart ().fire (new Receiver <Date> () {

         @Override
         public void onSuccess (Date response) {
          //set default interval between 00 and 24 of today
            listView.setStartDate (response);
            listView.setEndDate (new Date (response.getTime () + ISharedConstants.ONE_DAY_MILLIS));
            
            ObservationListActivity.super.start (panel, eventBus);
         }
      });
   }
   
   @Override
   protected AsyncDataProvider <IObservationBlockProxy> getAsyncDataProvider (final EventBus eventBus) {
      listView.setInput (getRequestFactory (eventBus));

      listView.setTextVisible (false);
      listView.setGraphVisible (false);
      listView.setDerivedVisible (false);
      listView.clear ();
      
      registerHandler (listView.getSearchHandler (), new ClickHandler () {
         @Override
         public void onClick (ClickEvent event) {
            doSearch (eventBus);
         }
      });
      
      return new AsyncDataProvider <IObservationBlockProxy> () {
         @Override
         protected void onRangeChanged (HasData <IObservationBlockProxy> display) {
            doSearch (eventBus);
         }
      };
   }

   protected void doSearch (final EventBus eventBus) {
      IRequestParamFilterProxy paramFilter = null;
      DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat (ISharedConstants.SHARED_SHORT_DATE_FORMAT);
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

      //start date filter, if present
      if (listView.getStartDate () != null) {
         paramFilter = observationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.ObservationFilter.START_DATE.toString ());
         paramFilter.setValue (dateTimeFormat.format (listView.getStartDate ()));
         requestParamProxy.getFilters ().add (paramFilter);
      }
      //end date filter, if present
      if (listView.getEndDate () != null) {
         paramFilter = observationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.ObservationFilter.END_DATE.toString ());
         paramFilter.setValue (dateTimeFormat.format (listView.getEndDate ()));
         requestParamProxy.getFilters ().add (paramFilter);
      }
      //variable list filter, if present
      if (listView.getVariables () != null && !listView.getVariables ().isEmpty ()) {
         paramFilter = observationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.ObservationFilter.VARIABLE_IDS.toString ());
         paramFilter.setValue (getVariableIdList (listView.getVariables ()));
         requestParamProxy.getFilters ().add (paramFilter);
      }
      //only measured filter, if active
      if (listView.getOnlyMeasured ()) {
         paramFilter = observationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.ObservationFilter.MEASURED_ONLY.toString ());
         paramFilter.setValue ("true");
         requestParamProxy.getFilters ().add (paramFilter);
      }
      //only derived filter, if active
      if (listView.getOnlyDerived ()) {
         paramFilter = observationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.ObservationFilter.DERIVED_ONLY.toString ());
         paramFilter.setValue ("true");
         requestParamProxy.getFilters ().add (paramFilter);
      }
      
      if ((sortList != null) && (sortList.size () > 0) && (sortList.get (0) != null) && (sortList.get (0).getColumn () != null)) {
         requestParamProxy.setSortField (sortList.get (0).getColumn ().getDataStoreName ());
         requestParamProxy.setAscending (sortList.get (0).isAscending ());
      }

      if (listPlace.getObservationType ().equals (ObservationType.DERIVED)) {
         //search for derived variables
         listView.clear ();
         for (final DerivedRangeType derivedRangeType : DerivedRangeType.values ()) {
            observationRequestContext.getDerivedInRange (derivedRangeType, listView.getStartDate (), null)
                                     .with ("station", "derivedVariables", "derivedVariables.variable")
                                     .to (new Receiver <IDerivedRangeProxy> () {
               @Override
               public void onSuccess (IDerivedRangeProxy response) {
                  listView.setDerivedVisible (true);
                  listView.appendDerived (derivedRangeType, response);
               }
      
               @Override
               public void onFailure (ServerFailure serverFailure) {
                  eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, MessageChangeEvent.getTextMessages ().listError ("Derived Observation"), serverFailure));
               }
            });
         }
         observationRequestContext.fire ();
      } else if (listPlace.getRepresentation ().equals (ObservationListPlace.Representation.TEXT)) {
         //search for table representation
         observationRequestContext.getObservationBlocks (requestParamProxy)
                                  .with ("station", "observations", "observations.variable")
                                  .fire (new Receiver <List <IObservationBlockProxy>> () {
            @Override
            public void onSuccess (List <IObservationBlockProxy> response) {
               listView.setTextVisible (true);
               listView.setGraphVisible (false);
               listView.initTableColumns (response);
               listView.getDataTable ().setRowCount (0);
               listView.getDataTable ().setRowCount (response.size ());
               listView.getDataTable ().setRowData (0, response);
            }
   
            @Override
            public void onFailure (ServerFailure serverFailure) {
               eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, MessageChangeEvent.getTextMessages ().listError ("Observation"), serverFailure));
            }
         });
      } else {
         //search for graphic representation
         observationRequestContext.getVariableObservations (requestParamProxy)
                                  .with ("station", "observations", "observations.variable")
                                  .fire (new Receiver <List <IVariableObservationsProxy>> () {
            @Override
            public void onSuccess (List <IVariableObservationsProxy> response) {
               listView.setTextVisible (false);
               listView.setGraphVisible (true);
               
               listView.generateGraphics (response);
            }
   
            @Override
            public void onFailure (ServerFailure serverFailure) {
               eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, MessageChangeEvent.getTextMessages ().listError ("Observation"), serverFailure));
            }
         });
      }
   }

   private String getVariableIdList (List <IVariableProxy> variables) {
      String [] ids = new String [variables.size ()];
      int i = 0;
      
      for (IVariableProxy variable : variables) {
         ids [i ++] = String.valueOf (variable.getId ());
      }
      
      return PortableStringUtils.join (ids, ISharedConstants.ID_LIST_ALTERNATIVE_SEPARATOR);
   }
}
