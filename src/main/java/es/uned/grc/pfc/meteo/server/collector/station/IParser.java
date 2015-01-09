package es.uned.grc.pfc.meteo.server.collector.station;

import java.util.List;

public interface IParser {
   List <RawObservation> parseBlock (byte [] block);
}
