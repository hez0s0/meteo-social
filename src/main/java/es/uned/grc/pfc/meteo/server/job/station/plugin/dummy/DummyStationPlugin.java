package es.uned.grc.pfc.meteo.server.job.station.plugin.dummy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import es.uned.grc.pfc.meteo.server.job.station.ICollector;
import es.uned.grc.pfc.meteo.server.job.station.IParser;
import es.uned.grc.pfc.meteo.server.job.station.IStationPlugin;
import es.uned.grc.pfc.meteo.server.job.station.ParameterDescriptor;
import es.uned.grc.pfc.meteo.server.job.station.StationModelDescriptor;
import es.uned.grc.pfc.meteo.server.job.station.VariableDescriptor;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

@Component
public class DummyStationPlugin implements IStationPlugin {

   @Override
   public StationModelDescriptor getStationModelDescriptor () {
      StationModelDescriptor descriptor = new StationModelDescriptor ();
      descriptor.setName ("Dummy station");
      descriptor.setDescription ("A non real station meant for debugging based on simple CSV files accesible via local folder");
      return descriptor;
   }

   @Override
   public List <ParameterDescriptor> getParameterDescriptors () {
      List <ParameterDescriptor> parameters = new ArrayList <ParameterDescriptor> ();
      ParameterDescriptor parameter = null;
      
      parameter = new ParameterDescriptor ();
      parameter.setName (IDummyConstants.FOLDER_PARAM);
      parameter.setDescription ("The folder where the observations file is to be located");
      parameter.setDefaultValue ("/home/dummy/observations");
      parameters.add (parameter);
      
      parameter = new ParameterDescriptor ();
      parameter.setName (IDummyConstants.FILE_PARAM);
      parameter.setDescription ("Name of the file where the observations are included");
      parameter.setDefaultValue ("dummy_observations.csv");
      parameters.add (parameter);
      
      return parameters;
   }

   @Override
   public List <VariableDescriptor> getVariableDescriptors () {
      List <VariableDescriptor> variables = new ArrayList <VariableDescriptor> ();
      VariableDescriptor variable = null;

      variable = new VariableDescriptor ();
      variable.setName ("Temperature");
      variable.setAcronym (IDummyConstants.TEMPERATURE_ACRONYM);
      variable.setDescription ("The air temperature");
      variable.setUnit ("ºC");
      variable.setDefaultMaximum (50.0);
      variable.setDefaultMinimum (-30.0);
      variable.setPhysicalMaximum (80.0);
      variable.setPhysicalMinimum (-80.0);
      variable.setPosition (0);
      variable.setDisplayGroup (0);
      variable.setGraphType (ISharedConstants.GraphType.LINE);
      variables.add (variable);
      
      variable = new VariableDescriptor ();
      variable.setName ("Pressure");
      variable.setAcronym (IDummyConstants.PRESSURE_ACRONYM);
      variable.setDescription ("The air pressure");
      variable.setUnit ("mbar");
      variable.setDefaultMaximum (1100.0);
      variable.setDefaultMinimum (850.0);
      variable.setPhysicalMaximum (1500.0);
      variable.setPhysicalMinimum (500.0);
      variable.setPosition (1);
      variable.setDisplayGroup (1);
      variable.setGraphType (ISharedConstants.GraphType.BAR);
      variables.add (variable);
      
      variable = new VariableDescriptor ();
      variable.setName ("Humidity");
      variable.setAcronym (IDummyConstants.HUMIDITY_ACRONYM);
      variable.setUnit ("%");
      variable.setDescription ("Relative humidity of the air");
      variable.setDefaultMaximum (90.0);
      variable.setDefaultMinimum (0.0);
      variable.setPhysicalMaximum (100.0);
      variable.setPhysicalMinimum (0.0);
      variable.setPosition (2);
      variable.setDisplayGroup (2);
      variable.setGraphType (ISharedConstants.GraphType.AREA);
      variables.add (variable);
      
      variable = new VariableDescriptor ();
      variable.setName ("Wind speed");
      variable.setAcronym (IDummyConstants.WIND_SPEED_ACRONYM);
      variable.setDescription ("Speed of the wind");
      variable.setUnit ("m/s");
      variable.setDefaultMaximum (50.0);
      variable.setDefaultMinimum (0.0);
      variable.setPhysicalMaximum (200.0);
      variable.setPhysicalMinimum (0.0);
      variable.setPosition (3);
      variable.setDisplayGroup (3);
      variable.setGraphType (ISharedConstants.GraphType.LINE);
      variables.add (variable);

      variable = new VariableDescriptor ();
      variable.setName ("Wind direction");
      variable.setAcronym (IDummyConstants.WIND_DIRECTION_ACRONYM);
      variable.setDescription ("Direction of the wind in grades");
      variable.setUnit ("º");
      variable.setDefaultMaximum (360.0);
      variable.setDefaultMinimum (0.0);
      variable.setPhysicalMaximum (360.0);
      variable.setPhysicalMinimum (0.0);
      variable.setPosition (4);
      variable.setDisplayGroup (4);
      variable.setGraphType (ISharedConstants.GraphType.NONE);
      variables.add (variable);
      
      return variables;
   }

   @Override
   public short getObservationPeriod () {
      return 10;
   }

   @Override
   public IParser getParser () {
      return new DummyParser ();
   }

   @Override
   public ICollector getCollector () {
      return new DummyCollector ();
   }

}
