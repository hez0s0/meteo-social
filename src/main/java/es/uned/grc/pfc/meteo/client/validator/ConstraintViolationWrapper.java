package es.uned.grc.pfc.meteo.client.validator;

import javax.validation.ConstraintViolation;

public class ConstraintViolationWrapper {
   String prefix = null;
   ConstraintViolation <?> violation = null;
   
   public ConstraintViolationWrapper (String prefix, ConstraintViolation <?> violation) {
      this.prefix = prefix;
      this.violation = violation;
   } //end of ConstraintViolationWrapper
   
   public String getPrefix () {
      return prefix;
   }
   
   public ConstraintViolation <?> getViolation () {
      return violation;
   }
} //end of ConstraintViolationWrapper
