package es.uned.grc.pfc.meteo.server.job.station;

import java.util.List;

public interface IParser {
   List <RawObservation> parseBlock (byte [] block);
}
