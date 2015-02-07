package es.uned.grc.pfc.meteo.server.decorator;

import java.lang.reflect.Method;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

/**
 * Audit user actions into a log
 */
@Component
public class LoggingServiceLayerDecorator extends ServiceLayerDecorator {
   
   private static Log log = LogFactory.getLog (LoggingServiceLayerDecorator.class);

   private boolean logAudit = false;
   private boolean logTime = false;
   private long logTimeThreshold = -1L;
   
   @Override
   public Object invoke (Method domainMethod, Object... args) {
      Object result = null;
      long startTime = -1;
      
      try {
         if (logAudit) {
            writeLogAudit (domainMethod, args);
         }
   
         if (logTime) {
            startTime = new Date ().getTime ();
         }
         result = super.invoke (domainMethod, args);
         
         if (logTime) {
            writeLogTime (startTime, domainMethod, args);
         }
      } catch (RuntimeException e) {
         log.error ("Error invoking action", e);
         throw e;
      } catch (Exception e) {
         throw new RuntimeException (e);
      }
      
      return result;
   }

   private void writeLogAudit (Method domainMethod, Object... args) {
      String userName = null;
      try {
         if (domainMethod != null) {
            log.info (String.format ("Invoked method '%s' with params '%s'", 
                                      userName,
                                      domainMethod.getName (),
                                      StringUtils.join (args, ", ")));
         }
      } catch (Exception e) {
         log.error ("Error logging method invokation", e);
      }
   }

   private void writeLogTime (long startTime, Method domainMethod, Object... args) {
      long diff = -1;
      try {
         if (domainMethod != null) {
            diff = new Date ().getTime () - startTime;
            
            if (diff >= logTimeThreshold) {
               log.info (String.format ("Method '%s' with params '%s' took %s ms.",
                                        domainMethod.getName (),
                                        StringUtils.join (args, ", "),
                                        diff));
            }
         }
      } catch (Exception e) {
         log.error ("Error logging method running time", e);
      }
   }

   public boolean isLogAudit () {
      return logAudit;
   }

   public void setLogAudit (boolean logAudit) {
      this.logAudit = logAudit;
   }

   public boolean isLogTime () {
      return logTime;
   }

   public void setLogTime (boolean logTime) {
      this.logTime = logTime;
   }

   public long getLogTimeThreshold () {
      return logTimeThreshold;
   }

   public void setLogTimeThreshold (long logTimeThreshold) {
      this.logTimeThreshold = logTimeThreshold;
   }
}
