package es.uned.grc.pfc.meteo.server.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import es.uned.grc.pfc.meteo.server.decorator.LoggingServiceLayerDecorator;
import es.uned.grc.pfc.meteo.server.decorator.ValidationServiceLayerDecorator;
import es.uned.grc.pfc.meteo.server.util.ApplicationContextProvider;

@WebServlet (name = "requestFactoryServlet", 
             urlPatterns = "/gwtRequest", 
             initParams = {@WebInitParam (name = CustomRequestFactoryServlet.LOG_AUDIT_PARAM, value = "false"),
                           @WebInitParam (name = CustomRequestFactoryServlet.LOG_TIME_PARAM, value = "true"),
                           @WebInitParam (name = CustomRequestFactoryServlet.LOG_TIME_THRESHOLD_PARAM, value = "300")})
public class CustomRequestFactoryServlet extends RequestFactoryServlet {

   private static final long serialVersionUID = 8638452289629204222L;

   protected static final String LOG_AUDIT_PARAM = "logAudit";
   protected static final String LOG_TIME_PARAM = "logTime";
   protected static final String LOG_TIME_THRESHOLD_PARAM = "logTimeThreshold";

   protected static Logger logger = LoggerFactory.getLogger (CustomRequestFactoryServlet.class);   
   
   static class LoquaciousExceptionHandler implements ExceptionHandler {
      @Override
      public ServerFailure createServerFailure (Throwable throwable) {
         logger.error ("Server error", throwable);
         return new ServerFailure (throwable.getMessage (), throwable.getClass ().getName (), null, true);
      }
   }

   private LoggingServiceLayerDecorator loggingServiceLayerDecorator = ApplicationContextProvider.getApplicationContext ().getBean (LoggingServiceLayerDecorator.class);
   
   @Override
   public void init (ServletConfig servletConfig) throws ServletException {
      super.init (servletConfig);

      loggingServiceLayerDecorator.setLogAudit (getBooleanParam (servletConfig, LOG_AUDIT_PARAM, false));
      loggingServiceLayerDecorator.setLogTime (getBooleanParam (servletConfig, LOG_TIME_PARAM, false));
      loggingServiceLayerDecorator.setLogTimeThreshold (getLongParam (servletConfig, LOG_TIME_THRESHOLD_PARAM, -1L));
   }

   public CustomRequestFactoryServlet () {
      //note decorators are added in proper order (they will behave like a filter chain)
      this (new LoquaciousExceptionHandler(),
            ApplicationContextProvider.getApplicationContext ().getBean (LoggingServiceLayerDecorator.class),
            ApplicationContextProvider.getApplicationContext ().getBean (ValidationServiceLayerDecorator.class));
   }

   public CustomRequestFactoryServlet (ExceptionHandler exceptionHandler, LoggingServiceLayerDecorator loggingServiceLayerDecorator, ValidationServiceLayerDecorator validationServiceLayerDecorator) {
      super (exceptionHandler, new ServiceLayerDecorator [] {loggingServiceLayerDecorator, validationServiceLayerDecorator});
      this.loggingServiceLayerDecorator = loggingServiceLayerDecorator;
   }

   private boolean getBooleanParam (ServletConfig servletConfig, String paramName, boolean defaultValue) {
      boolean result = defaultValue;

      try {
         result = Boolean.valueOf (servletConfig.getInitParameter (paramName));
      } catch (Exception e) {
         result = defaultValue;

         logger.warn (String.format ("%s param does not contain must contain a value of 'true' for 'false'. Default value of '%s' is set", paramName, defaultValue));
      }

      return result;
   }

   private long getLongParam (ServletConfig servletConfig, String paramName, long defaultValue) {
      long result = defaultValue;

      try {
         result = Long.valueOf (servletConfig.getInitParameter (paramName));
      } catch (Exception e) {
         result = defaultValue;

         logger.warn (String.format ("%s param does not contain a long value. Default value of '%s' is set", paramName, defaultValue));
      }

      return result;
   }
}
