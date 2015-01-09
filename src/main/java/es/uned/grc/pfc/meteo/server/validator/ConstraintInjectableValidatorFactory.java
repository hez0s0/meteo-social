package es.uned.grc.pfc.meteo.server.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

import es.uned.grc.pfc.meteo.server.util.CdiUtils;

public class ConstraintInjectableValidatorFactory implements ConstraintValidatorFactory {

   @Override
   public <T extends ConstraintValidator <?, ?>> T getInstance (Class <T> clazz) {
      return CdiUtils.getReference (clazz);
   }
}