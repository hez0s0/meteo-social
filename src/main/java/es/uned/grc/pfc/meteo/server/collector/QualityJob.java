package es.uned.grc.pfc.meteo.server.collector;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.util.IServerConstants;

@Component
public class QualityJob {

   protected static Logger logger = LoggerFactory.getLogger (QualityJob.class);

   /** get a copy of the application context */
   @Autowired
   private ConfigurableApplicationContext context = null;

   @JmsListener (destination = IServerConstants.OBSERVATIONS_QUEUE_NAME)
   public void processObservations (List <Observation> observations) {
      try {
         logger.info ("Quality to be checked on %s observations", observations.size ());

         // TODO Auto-generated method stub
      } catch (Exception e) {
         logger.error ("Error collecting observations", e);
      }
   }
}
