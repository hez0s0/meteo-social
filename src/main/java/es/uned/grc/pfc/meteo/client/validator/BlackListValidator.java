package es.uned.grc.pfc.meteo.client.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BlackListValidator implements ConstraintValidator <BlackListValidationConstraint, String> {

   private String [] blacklist = null;

   @Override
   public void initialize (BlackListValidationConstraint constraintAnnotation) {
      this.blacklist = constraintAnnotation.blacklist ();
   }

   @Override
   public boolean isValid (String value, ConstraintValidatorContext context) {
      boolean forbidden = false;
      
      if (value != null) {
         for (String black : blacklist) {
            if (value.equalsIgnoreCase (black)) {
               forbidden = true;
               break;
            }
         }
      }
      
      return !forbidden;
   }

}
