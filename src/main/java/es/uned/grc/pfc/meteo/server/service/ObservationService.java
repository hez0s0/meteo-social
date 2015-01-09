package es.uned.grc.pfc.meteo.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.grc.pfc.meteo.server.service.helper.ObservationServiceHelper;

/**
 * Service to manage observations.
 * To be wrapped and invoked through RequestFactory via a twin context.
 */
@Service
public class ObservationService {
   
   @Autowired
   private ObservationServiceHelper observationServiceHelper = null;
}
