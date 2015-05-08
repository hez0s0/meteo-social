package es.uned.grc.pfc.meteo.server.job.station;

import java.util.List;

public interface IStationPlugin {
   StationModelDescriptor getStationModelDescriptor ();
   List <ParameterDescriptor> getParameterDescriptors ();
   List <VariableDescriptor> getVariableDescriptors ();
   short getObservationPeriod ();
   IParser getParser ();
   ICollector getCollector ();
}
