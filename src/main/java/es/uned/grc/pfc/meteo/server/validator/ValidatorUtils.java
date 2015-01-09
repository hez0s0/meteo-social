package es.uned.grc.pfc.meteo.server.validator;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ValidatorUtils {

   public static Validator getValidator () {
      Configuration <?> configuration = null;
      ValidatorFactory validatorFactory = null;
      Validator validator = null;
      
      configuration = Validation.byDefaultProvider ().configure ();
      validatorFactory = configuration.constraintValidatorFactory (new ConstraintInjectableValidatorFactory ()).buildValidatorFactory ();
      validator = validatorFactory.getValidator ();
      
      return validator;
   }
}
