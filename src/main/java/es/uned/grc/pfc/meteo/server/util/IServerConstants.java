package es.uned.grc.pfc.meteo.server.util;


public class IServerConstants {
   
   public static final String OBSERVATIONS_QUEUE_NAME = "java:jboss/jms/queue/observationsQueue";
   
   public static final String SERVICE_EXCEPTION_KEY = "serviceException";
   
   public static final String VALIDATION_KEY = "validation";

   public static final int BUF_SIZE = 1024 * 1024;

   public static final String JBOSS_FOLDER_PROPERTY_NAME = "jboss.server.data.dir";
   public static final String TEMP_FOLDER_PROPERTY_NAME = "java.io.tmpdir";
}
