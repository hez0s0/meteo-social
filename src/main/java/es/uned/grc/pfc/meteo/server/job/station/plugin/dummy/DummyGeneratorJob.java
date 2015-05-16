package es.uned.grc.pfc.meteo.server.job.station.plugin.dummy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.util.Calendar;

import es.uned.grc.pfc.meteo.client.util.DerivedUtils;
import es.uned.grc.pfc.meteo.server.job.station.IStationPlugin;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IParameterPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.util.IServerConstants;
import es.uned.grc.pfc.meteo.shared.ISharedConstants;

@Component
public class DummyGeneratorJob {

   private static final Integer STATION_ID = 1;
   
   protected static Logger logger = LoggerFactory.getLogger (DummyGeneratorJob.class);

   private enum MonthType {WINTER, SPRING, SUMMER};
   
   @Autowired
   private IStationPersistence stationPersistence = null;
   @Autowired
   private IParameterPersistence parameterPersistence = null;
   @Autowired
   private IObservationPersistence observationPersistence = null;
   @Autowired
   private IStationPlugin stationPlugin = null;
  
   private StationGenerationInfo stationGenerationInfo = new StationGenerationInfo (1.0, 1.0, 1.0, 1.0, 1.0);

   /**
    * To be executed periodically
    */
//   @Scheduled (fixedRate = 60000)
   public synchronized void timeout () {
      logger.info ("Executing task {}", getClass ().getSimpleName ());
      try {
         generate ();
         logger.info ("Observations generated");
      } catch (Exception e) {
         logger.error ("Error collecting observations", e);
      }
   }
   
   /**
    * Generates dummy observations
    * @throws IOException 
    */
   @Transactional (propagation = Propagation.REQUIRED)
   public void generate () throws IOException {
      Station station = null;
      Date generationIni = null;
      Date generationEnd = null;
      Date now = new Date ();
      GenerationStatus status = new GenerationStatus ();

      station = stationPersistence.findById (STATION_ID);
      if (station == null) {
         throw new RuntimeException ("unable to obtain own station, please review the system configuration");
      }
      if (stationPlugin == null) {
         throw new RuntimeException ("unable to obtain station plugin, please review the system configuration");
      }
      if (stationPlugin.getStationModelDescriptor () == null) {
         throw new RuntimeException (String.format ("unable to obtain a descriptor for '%s' station, please review the station plugin code", stationPlugin));
      }
      
      if (station.getLastCollectedPeriod () != null) {
         generationIni = DerivedUtils.getMonthIni (station.getLastCollectedPeriod ());
      } else {
         Calendar calendar = Calendar.getInstance ();
         calendar.setTime (now);
         calendar.set (Calendar.MONTH, 1);
         generationIni = DerivedUtils.getMonthIni (calendar.getTime ());
      }
      generationEnd = DerivedUtils.getMonthEnd (now);
      while (generationIni.getTime () < generationEnd.getTime ()) {
         generate (generationIni, status);
      }
   }

   private void generate (Date date, GenerationStatus status) throws IOException {
      SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMdd");
      SimpleDateFormat sdfLine = new SimpleDateFormat ("yyyyMMddHHmm");
      String day = sdf.format (date);
      StringBuilder line = null;
      FileOutputStream fos = null;
      DecimalFormat df = new DecimalFormat ("0.00");
      DecimalFormatSymbols custom = new DecimalFormatSymbols ();
      String filename = "/home/dummy/observations/new/dummy_observations_" + sdf.format (date) + ".csv";
      File file = new File (filename);
      MonthType monthType = getMonthType (date);
      Double [][] fullTAValues = getFullTAValues (monthType);
      Double [][] fullPRESValues = getFullPRESValues (monthType);
      Double [][] fullHUMValues = getFullHUMValues (monthType);
      Double [][] fullWINDSValues = getFullWINDSValues (monthType);
      Double [][] fullWINDDValues = getFullWINDDValues (monthType);
      
      try {
         if (file.exists ()) {
            file.delete ();
         }
         fos = new FileOutputStream (file);
         
         custom.setDecimalSeparator ('.');
         df.setDecimalFormatSymbols (custom);
         
         if (status.monthType == null || !status.monthType.equals (monthType)) {
            //restart the index
            status.index = 0;
         } else {
            status.index = getNextIndex (fullTAValues, status.index);
         }
         status.monthType = monthType;
         logger.info ("Creating file {} starting at index {} and timestamp {}", filename, status.index, date);

         while (sdf.format (date).equals (day)) {
            for (int i = 0; i < 144; i ++) {
               line = new StringBuilder ();
               line.append (sdfLine.format (date));
               line.append (ISharedConstants.WORD_LIST_SEPARATOR);
               
               line.append (IDummyConstants.TEMPERATURE_ACRONYM.toUpperCase ());
               line.append ("=");
               status.ta = getNext (i, fullTAValues, status.index, status.ta, stationGenerationInfo.taFactor);
               line.append (df.format (status.ta));
               line.append (ISharedConstants.WORD_LIST_SEPARATOR);
               
               line.append (IDummyConstants.PRESSURE_ACRONYM.toUpperCase ());
               line.append ("=");
               status.pres = getNext (i, fullPRESValues, status.index, status.pres, stationGenerationInfo.presFactor);
               line.append (df.format (status.pres));
               line.append (ISharedConstants.WORD_LIST_SEPARATOR);
               
               line.append (IDummyConstants.HUMIDITY_ACRONYM.toUpperCase ());
               line.append ("=");
               status.hum = getNext (i, fullHUMValues, status.index, status.hum, stationGenerationInfo.humFactor);
               line.append (df.format (status.hum));
               line.append (ISharedConstants.WORD_LIST_SEPARATOR);
               
               line.append (IDummyConstants.WIND_SPEED_ACRONYM.toUpperCase ());
               line.append ("=");
               status.winds = getNext (i, fullWINDSValues, status.index, status.winds, stationGenerationInfo.windsFactor);
               line.append (df.format (status.winds));
               line.append (ISharedConstants.WORD_LIST_SEPARATOR);
               
               line.append (IDummyConstants.WIND_DIRECTION_ACRONYM.toUpperCase ());
               line.append ("=");
               status.windd = getNext (i, fullWINDDValues, status.index, status.windd, stationGenerationInfo.winddFactor);
               line.append (df.format (status.windd));
               
//               logger.debug ("Adding row " + line.toString ());
               
               line.append ("\n");
               
               fos.write (line.toString ().getBytes ());
               
               date.setTime (date.getTime () + (10 * IServerConstants.ONE_MINUTE));
            }
         }
      } finally {
         if (fos != null) {
            fos.close ();
         }
      }
   }

   private Double getNext (int i, Double [][] fullValues, int index, Double last, Double factor) {
      Double result = null;
      Double target = null;
      Integer steps = null;
      Double averageStep = null;
      Double currentStep = null;
      Double [] row = fullValues [index];
      Double [] nextRow = fullValues [getNextIndex (fullValues, index)];

      if (i == 0) {
         //just take the 00h column
         result = row [0];
      } else if (i == 6*6) {
         //just take the 06h column
         result = row [1];
      } else if (i == 12*6) {
         //just take the 12h column
         result = row [2];
      } else if (i == 18*6) {
         //just take the 18h column
         result = row [3];
      } else if (i < 6*6) {
         //somewhere between 00h and 06h, calculate according to the last value
         target = row [1];
         steps = 6*6 - i;
      } else if (i < 12*6) {
         //somewhere between 06h and 12h, calculate according to the last value
         target = row [2];
         steps = 12*6 - i;
      } else if (i < 18*6) {
         //somewhere between 12h and 18h, calculate according to the last value
         target = row [3];
         steps = 18*6 - i;
      } else {
         //somewhere between 18h and 00h, calculate according to the last value
         target = nextRow [0];
         steps = 24*6 - i;
      }
      
      if (result == null) {
         averageStep = (target - last) / (steps);
         currentStep = getRandomDouble (averageStep - averageStep * 0.25, averageStep + averageStep * 0.25);
         result = last + currentStep;
      }
      
      return result * factor;
   }

   private Double [][] getFullTAValues (MonthType monthType) {
      switch (monthType) {
         case SPRING:
            return IDummyGeneratorTAConstants.SPRING;
         case SUMMER:
            return IDummyGeneratorTAConstants.SUMMER;
         case WINTER:
            return IDummyGeneratorTAConstants.WINTER;
      }
      return null;
   }
   private Double [][] getFullPRESValues (MonthType monthType) {
      switch (monthType) {
         case SPRING:
            return IDummyGeneratorPRESConstants.SPRING;
         case SUMMER:
            return IDummyGeneratorPRESConstants.SUMMER;
         case WINTER:
            return IDummyGeneratorPRESConstants.WINTER;
      }
      return null;
   }
   private Double [][] getFullHUMValues (MonthType monthType) {
      switch (monthType) {
         case SPRING:
            return IDummyGeneratorHUMConstants.SPRING;
         case SUMMER:
            return IDummyGeneratorHUMConstants.SUMMER;
         case WINTER:
            return IDummyGeneratorHUMConstants.WINTER;
      }
      return null;
   }
   private Double [][] getFullWINDSValues (MonthType monthType) {
      switch (monthType) {
         case SPRING:
            return IDummyGeneratorWINDSConstants.SPRING;
         case SUMMER:
            return IDummyGeneratorWINDSConstants.SUMMER;
         case WINTER:
            return IDummyGeneratorWINDSConstants.WINTER;
      }
      return null;
   }
   private Double [][] getFullWINDDValues (MonthType monthType) {
      switch (monthType) {
         case SPRING:
            return IDummyGeneratorWINDDConstants.SPRING;
         case SUMMER:
            return IDummyGeneratorWINDDConstants.SUMMER;
         case WINTER:
            return IDummyGeneratorWINDDConstants.WINTER;
      }
      return null;
   }

   private MonthType getMonthType (Date date) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (date);
      int month = calendar.get (Calendar.MONTH) + 1;
      if (month <= 2 || month >= 11) {
         //nov, dec, jan, feb are winter
         return MonthType.WINTER;
      } else if ((month > 2 && month <= 5) || (month > 8 && month <= 10)) {
         //mar, apr, may, sep, oct are spring/fall
         return MonthType.SPRING;
      } else {
         //the rest is summer
         return MonthType.SUMMER;
      }
   }

   private class StationGenerationInfo {
      private Double taFactor = null;
      private Double presFactor = null;
      private Double humFactor = null;
      private Double windsFactor = null;
      private Double winddFactor = null;
      
      public StationGenerationInfo (Double taFactor, Double presFactor, Double humFactor, Double windsFactor, Double winddFactor) {
         this.taFactor = taFactor;
         this.presFactor = presFactor;
         this.humFactor = humFactor;
         this.windsFactor = windsFactor;
         this.winddFactor = winddFactor;
      }
   }
   
   private class GenerationStatus {
      private Double ta = null;
      private Double pres = null;
      private Double hum = null;
      private Double winds = null;
      private Double windd = null;
      private MonthType monthType = null;
      private Integer index = 0;
   }

   private double getRandomDouble (double min, double max) {
      return min + (double) (new Random ().nextDouble () * (max - min));
   }

   private int getNextIndex (Double [][] fullValues, int index) {
      return (index < fullValues.length - 1) ? index + 1 : 0;
   }
}
