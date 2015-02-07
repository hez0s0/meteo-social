package es.uned.grc.pfc.meteo.server.collector.station.plugin.dummy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uned.grc.pfc.meteo.server.collector.station.ICollector;

public class DummyCollector implements ICollector {

   protected static Logger logger = LoggerFactory.getLogger (DummyCollector.class);
   
   private static final String FILE_MASK = "%s" + File.separator + "%s" + "_%s.csv";
   private static final String FILE_DATE_FORMAT = "yyyyMMdd";
   
   @Override
   public byte [] getObservationBlock (Date observationTime, Map <String, String> params) {
      byte [] block = null;
      File folder = null;
      File file = null;
      BufferedReader br = null;
      String folderName = getParamValue (IDummyConstants.FOLDER_PARAM, params);
      String fileName = getParamValue (IDummyConstants.FILE_PARAM, params);
      
      //validate params
      if (StringUtils.isEmpty (folderName)) {
         throw new RuntimeException (String.format ("Param %s not configured or empty", IDummyConstants.FOLDER_PARAM));
      }
      if (StringUtils.isEmpty (fileName)) {
         throw new RuntimeException (String.format ("Param %s not configured or empty", IDummyConstants.FILE_PARAM));
      }
      
      //locate file for the right date
      folder = new File (folderName);
      if (!folder.exists () || !folder.isDirectory () || !folder.canExecute ()) {
         throw new RuntimeException (String.format ("Folder '%s' does not exist or is not accessible", folderName));
      }
      
      fileName = String.format (FILE_MASK, folderName, fileName, new SimpleDateFormat (FILE_DATE_FORMAT).format (observationTime));
      file = new File (fileName);
      if (!file.exists () || !file.isFile () || !file.canRead ()) {
         logger.info ("File '{}' does not exist or is not accessible", fileName);
      } else {
         try {
            //if the file for given date is there, find the row that includes the info for the given period
            br = new BufferedReader (new FileReader (file));
            for (String line; (line = br.readLine ()) != null; ) {
               if (isObservationLine (line, observationTime)) {
                  //we found the line for the date
                  block = line.getBytes ();
                  break;
               }
            }
         } catch (Exception e) {
            throw new RuntimeException (String.format ("Read error on file '%s'", fileName));
         } finally {
            if (br != null) {
               try {
                  br.close ();
               } catch (IOException e) {
                  //silent, unimportant
               }
            }
         }
      }
      
      return block;
   }

   private boolean isObservationLine (String line, Date observationTime) {
      return (line != null && line.startsWith (new SimpleDateFormat (IDummyConstants.OBSERVATION_DATE_FORMAT).format (observationTime)));
   }

   private String getParamValue (String key, Map <String, String> params) {
      for (Map.Entry <String, String> param : params.entrySet ()) {
         if (param.getKey ().equals (key)) {
            return param.getValue ();
         }
      }
      return null;
   }
}
