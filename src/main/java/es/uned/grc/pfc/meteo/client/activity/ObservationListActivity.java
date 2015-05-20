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
import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.model.IVariableObservationsProxy;
import es.uned.grc.pfc.meteo.client.model.IVariableProxy;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;
import es.uned.grc.pfc.meteo.client.place.ObservationListPlace.ObservationType;
import es.uned.grc.pfc.meteo.client.request.IObservationRequestContext;
import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.client.view.IObservationListView;
import es.uned.grc.pfc.meteo.client.view.widget.GlassPanel;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;
import es.uned.grc.pfc.meteo.shared.ISharedConstants.DerivedRangeType;

public class ObservationListActivity extends AbstractAsyncDataActivity <IObservationBlockProxy, IObservationListView, ObservationListPlace> {

   private AcceptsOneWidget panel = null;
   private ObservationListPlace listPlace = null;
   
   public ObservationListActivity (ObservationListPlace listPlace, IObservationListView listView, PlaceController placeController) {
      super (listPlace, listView, placeController);
      
      this.listPlace = listPlace;
   }
   
   @Override
   public void start (final AcceptsOneWidget panel, final EventBus eventBus) {
      this.panel = panel;
      listView.setDerived (listPlace.getObservationType ().equals (ObservationType.DERIVED));
      listView.setStation (null);
      getRequestFactory (eventBus).getObservationContext ().getTodayStart ().fire (new Receiver <Date> () {

         @Override
         public void onSuccess (Date response) {
            //set default interval between 00 and 24 of today
            listView.setExactDate (response, listPlace.isEmptyParams ());
            
            ObservationListActivity.super.start (panel, eventBus);
         }
      });
      if (listPlace.getStationId () == null) {
         //load own station
         getRequestFactory (eventBus).getStationContext ().getOwnStation ().fire (new Receiver <IStationProxy> () {
            @Override
            public void onSuccess (IStationProxy response) {
               listView.setStation (response);
            }
         });
      } else {
         //load a given station
         getRequestFactory (eventBus).getStationContext ().getStationById (listPlace.getStationId ()).fire (new Receiver <IStationProxy> () {
            @Override
            public void onSuccess (IStationProxy response) {
               listView.setStation (response);
            }
         });
      }
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

      requestParamProxy.setFilters (new ArrayList <IRequestParamFilterProxy> ());
      
      if (listPlace.getStationId () == null) {
         //own station
         paramFilter = observationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.ObservationFilter.OWN.toString ());
         paramFilter.setValue ("true");
         requestParamProxy.getFilters ().add (paramFilter);
      } else {
         //a given station
         paramFilter = observationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.ObservationFilter.STATION_ID.toString ());
         paramFilter.setValue (String.valueOf (listPlace.getStationId ()));
         requestParamProxy.getFilters ().add (paramFilter);
      }

      if (!listPlace.getObservationType ().equals (ObservationType.DERIVED)) {
         //exact date filter, if present
         if (listView.getExactDate () != null) {
            paramFilter = observationRequestContext.create (IRequestParamFilterProxy.class);
            paramFilter.setParam (ISharedConstants.ObservationFilter.START_DATE.toString ());
            paramFilter.setValue (dateTimeFormat.format (listView.getExactDate ()));
            requestParamProxy.getFilters ().add (paramFilter);
            
            paramFilter = observationRequestContext.create (IRequestParamFilterProxy.class);
            paramFilter.setParam (ISharedConstants.ObservationFilter.END_DATE.toString ());
            paramFilter.setValue (dateTimeFormat.format (new Date (listView.getExactDate ().getTime () + ISharedConstants.ONE_DAY_MILLIS)));
            requestParamProxy.getFilters ().add (paramFilter);
         }
         //variable list filter, if present
         if (listView.getVariables () != null && !listView.getVariables ().isEmpty ()) {
            paramFilter = observationRequestContext.create (IRequestParamFilterProxy.class);
            paramFilter.setParam (ISharedConstants.ObservationFilter.VARIABLE_IDS.toString ());
            paramFilter.setValue (getVariableIdList (listView.getVariables ()));
            requestParamProxy.getFilters ().add (paramFilter);
         }
         //only measured filter, always use
         paramFilter = observationRequestContext.create (IRequestParamFilterProxy.class);
         paramFilter.setParam (ISharedConstants.ObservationFilter.MEASURED_ONLY.toString ());
         paramFilter.setValue ("true");
         requestParamProxy.getFilters ().add (paramFilter);
         
         if ((sortList != null) && (sortList.size () > 0) && (sortList.get (0) != null) && (sortList.get (0).getColumn () != null)) {
            requestParamProxy.setSortField (sortList.get (0).getColumn ().getDataStoreName ());
            requestParamProxy.setAscending (sortList.get (0).isAscending ());
         }
      }

      if (listPlace.getObservationType ().equals (ObservationType.DERIVED)) {
         if (listPlace.getRepresentation ().equals (ObservationListPlace.Representation.TEXT)) {
            //search for derived variables
            listView.clear ();
            for (DerivedRangeType derivedRangeType : DerivedRangeType.values ()) {
               getDerivedText (derivedRangeType, listPlace.getStationId (), observationRequestContext, eventBus);
            }
            observationRequestContext.fire ();
         } else {
            //search for derived variables
            listView.clear ();
            for (final DerivedRangeType derivedRangeType : DerivedRangeType.values ()) {
               getDerivedGraphic (derivedRangeType, listPlace.getStationId (), observationRequestContext, eventBus);
            }
            observationRequestContext.fire ();
         }
      } else {
         if (listPlace.getRepresentation ().equals (ObservationListPlace.Representation.TEXT)) {
            //search for table representation
            GlassPanel.fireDistraction (panel,
                                        listView.asWidget (), 
                                        observationRequestContext.getObservationBlocks (requestParamProxy)
                                                                 .with ("station", "observations", "observations.variable"),
                                        new Receiver <List <IObservationBlockProxy>> () {
                                           @Override
                                           public void onSuccess (List <IObservationBlockProxy> response) {
                                              listView.setTextVisible (true);
                                              listView.setGraphVisible (false);
                                              listView.initTableColumns (response);
                                              listView.getDataTable ().setRowCount (0);
                                              listView.getDataTable ().setRowCount (response.size ());
                                              listView.getDataTable ().setRowData (0, response);
                                              listView.getNoResultsLabel ().setVisible (response.isEmpty ());
                                           }
                                  
                                           @Override
                                           public void onFailure (ServerFailure serverFailure) {
                                              eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, MessageChangeEvent.getTextMessages ().listError ("Observation"), serverFailure));
                                           }
                                        });
         } else {
            //search for graphic representation
            GlassPanel.fireDistraction (panel,
                                        listView.asWidget (), 
                                        observationRequestContext.getVariableObservations (requestParamProxy)
                                                                 .with ("observations", "observations.variable", "stationVariable", "stationVariable.station", "stationVariable.variable"),
                                        new Receiver <List <IVariableObservationsProxy>> () {
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
   }

   private void getDerivedGraphic (final DerivedRangeType derivedRangeType, final Integer stationId, IObservationRequestContext observationRequestContext, final EventBus eventBus) {
      GlassPanel.toDistraction (panel,
                                listView.asWidget (), 
                                observationRequestContext.getDerivedInRangeForGraphics (derivedRangeType, listView.getExactDate (), stationId)
                                                         .with ("station", "derivedVariables", "derivedVariables.stationVariable", "derivedVariables.stationVariable.variable"),
                                new Receiver <List <IDerivedRangeProxy>> () {
                                   @Override
                                   public void onSuccess (List <IDerivedRangeProxy> response) {
                                      listView.setDerivedVisible (true);
                                      
                                      listView.appendDerivedGraphics (derivedRangeType, response);
                                   }
                                   
                                   @Override
                                   public void onFailure (ServerFailure serverFailure) {
                                      eventBus.fireEvent (new MessageChangeEvent (MessageChangeEvent.Level.ERROR, MessageChangeEvent.getTextMessages ().listError ("Derived Observation"), serverFailure));
                                   }
                                });
    }

   private void getDerivedText (final DerivedRangeType derivedRangeType, final Integer stationId, IObservationRequestContext observationRequestContext, final EventBus eventBus) {
      GlassPanel.toDistraction (panel,
                                listView.asWidget (), 
                                observationRequestContext.getDerivedInRange (derivedRangeType, listView.getExactDate (), stationId)
                                                         .with ("station", "derivedVariables", "derivedVariables.stationVariable", "derivedVariables.stationVariable.variable"),
                                new Receiver <IDerivedRangeProxy> () {
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

   private String getVariableIdList (List <IVariableProxy> variables) {
      String [] ids = new String [variables.size ()];
      int i = 0;
      
      for (IVariableProxy variable : variables) {
         ids [i ++] = String.valueOf (variable.getId ());
      }
      
      return PortableStringUtils.join (ids, ISharedConstants.ID_LIST_ALTERNATIVE_SEPARATOR);
   }
}
