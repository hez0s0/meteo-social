package es.uned.grc.pfc.meteo.server.job.station;

import java.util.Date;
import java.util.Map;

public interface ICollector {
   byte [] getObservationBlock (Date observationTime, Map <String, String> params);
}
