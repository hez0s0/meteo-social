package es.uned.grc.pfc.meteo.server.collector.station.plugin.dummy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.uned.grc.pfc.meteo.server.collector.station.IParser;
import es.uned.grc.pfc.meteo.server.collector.station.RawObservation;

public class DummyParser implements IParser {

   private static final String OBSERVATION_DATE_FORMAT = "yyyyMMddHHmmss";
   
   /**
    * Parses a line in the format yyyyMMddHHmmss,P1=V1,P2=V2,...
    * where Px identifies the param acronym and Vx is its value at the given period
    */
   @Override
   public List <RawObservation> parseBlock (byte [] block) {
      List <RawObservation> result = null;
      RawObservation rawObservation = null;
      Date dateObserved = null;
      SimpleDateFormat sdf = new SimpleDateFormat (OBSERVATION_DATE_FORMAT);
      String line = null;
      String [] columns = null;
      String [] observation = null;
      int columnPosition = 0;
      String observationAcronym = null;
      
      if (block != null) {
         line = new String (block);
         columns = line.split (",");
         
         if (columns.length >= 2) {
            result = new ArrayList <RawObservation> (columns.length - 1);
            for (String column : columns) {
               if (columnPosition == 0) {
                  //the first column contains the observation date
                  try {
                     dateObserved = sdf.parse (column);
                  } catch (Exception e) {
                     throw new RuntimeException (String.format ("Column %s with value '%s' does not denote the observation date correctly", columnPosition, column));
                  }
               } else {
                  //any other column contains a pair param=value
                  observation = column.split ("=");
                  if (observation.length != 2) {
                     throw new RuntimeException (String.format ("Column %s with value '%s' does not denote an observation correctly", columnPosition, column));
                  }
               }

               observationAcronym = observation [0] != null ? observation [0].trim () : "";
               
               if (!observationAcronym.equals (IDummyConstants.TEMPERATURE_ACRONYM)
                   && !observationAcronym.equals (IDummyConstants.PRESSURE_ACRONYM)
                   && !observationAcronym.equals (IDummyConstants.HUMIDITY_ACRONYM)
                   && !observationAcronym.equals (IDummyConstants.WIND_SPEED_ACRONYM)
                   && !observationAcronym.equals (IDummyConstants.WIND_DIRECTION_ACRONYM)) {
                  throw new RuntimeException (String.format ("Column %s with value '%s' does not contain a well-known acronym [%s, %s, %s, %s, %s]", 
                                              columnPosition, 
                                              column,
                                              IDummyConstants.TEMPERATURE_ACRONYM,
                                              IDummyConstants.PRESSURE_ACRONYM,
                                              IDummyConstants.HUMIDITY_ACRONYM,
                                              IDummyConstants.WIND_SPEED_ACRONYM,
                                              IDummyConstants.WIND_DIRECTION_ACRONYM));
               }
               //create the representation of the parsed values and add it to results
               rawObservation = new RawObservation ();
               rawObservation.setObserved (dateObserved);
               rawObservation.setVariableName (observation [0]);
               rawObservation.setValue (observation [1]);
               result.add (rawObservation);
               
               columnPosition ++;
            }
         } else {
            throw new RuntimeException (String.format ("Line '%s' does not contain date + observations", line));
         }
      }
      
      return result;
   }

}
