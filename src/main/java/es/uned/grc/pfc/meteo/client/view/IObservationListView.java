package es.uned.grc.pfc.meteo.client.view;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Label;

import es.uned.grc.pfc.meteo.client.model.IDerivedRangeProxy;
import es.uned.grc.pfc.meteo.client.model.IObservationBlockProxy;
import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.model.IVariableObservationsProxy;
import es.uned.grc.pfc.meteo.client.model.IVariableProxy;
import es.uned.grc.pfc.meteo.client.request.IRequestFactory;
import es.uned.grc.pfc.meteo.client.view.base.IAsyncCellTableView;
import es.uned.grc.pfc.meteo.shared.ISharedConstants.DerivedRangeType;

public interface IObservationListView extends IAsyncCellTableView <IObservationBlockProxy> {
   void initTableColumns (List <IObservationBlockProxy> observationBlock);
   Date getStartDate ();
   Date getExactDate ();
   List <IVariableProxy> getVariables ();
   void setInput (IRequestFactory requestFactory);
   HasClickHandlers getSearchHandler ();
   void setStartDate (Date date);
   void setExactDate (Date date);
   void setTextVisible (boolean visible);
   void setGraphVisible (boolean visible);
   void setDerivedVisible (boolean visible);
   void generateGraphics (List <IVariableObservationsProxy> variableObservations);
   void appendDerived (DerivedRangeType derivedRangeType, IDerivedRangeProxy derivedRange);
   void appendDerivedGraphics (DerivedRangeType derivedRangeType, List <IDerivedRangeProxy> observations);
   void clear ();
   void setDerived (boolean derived);
   Label getNoResultsLabel ();
   void setStation (IStationProxy station);
}
