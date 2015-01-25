package es.uned.grc.pfc.meteo.server.util;


public class IServerConstants {
   public static final String SERVICE_EXCEPTION_KEY = "serviceException";
   
   public static final String VALIDATION_KEY = "validation";

   /** size of buffer to be used on a general basis */
   public static final int BUF_SIZE = 1024 * 1024;
   
   /** convenience variable */
   public static final long ONE_MINUTE = 60 * 1000;
   
   /** time between executions of the collector, in milliseconds */
   public static final long COLLECTION_POLLING_TIME = 60000;
   
   /** time between executions of the quality control, in milliseconds */
   public static final long QUALITY_POLLING_TIME = 60000;
}
