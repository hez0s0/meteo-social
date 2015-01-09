package es.uned.grc.pfc.meteo.server.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;

/**
 * Supports non-client invokable actions of the AdminService module
 */
@Service
public class ObservationServiceHelper {

   @Autowired
   private IObservationPersistence observationPersistence = null;

}
