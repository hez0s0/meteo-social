package es.uned.grc.pfc.meteo.client;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

import es.uned.grc.pfc.meteo.client.place.ObservationListPlace;

@WithTokenizers ({ObservationListPlace.Tokenizer.class})
public interface IHistoryMapper extends PlaceHistoryMapper {
   //just a container to add the places that our history shall handle via WithTokenizers
}
