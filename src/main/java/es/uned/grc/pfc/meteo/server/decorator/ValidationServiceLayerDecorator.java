package es.uned.grc.pfc.meteo.server.decorator;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.stereotype.Component;

import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

import es.uned.grc.pfc.meteo.server.model.base.IEntity;
import es.uned.grc.pfc.meteo.server.model.base.IValidatable;
import es.uned.grc.pfc.meteo.server.validator.ValidatorUtils;
import es.uned.grc.pfc.meteo.shared.validator.ServerValidationGroup;

@Component
public class ValidationServiceLayerDecorator extends ServiceLayerDecorator {
   
   @Override
   public <T extends Object> Set <ConstraintViolation <T>> validate (T domainObject) {
      Set <ConstraintViolation <T>> violations = null;
      
      if ( (domainObject instanceof IValidatable) 
        && (((IValidatable) domainObject).getValidate () != null)
        && (((IValidatable) domainObject).getValidate ()) ) { //validate a bean only if requested

         violations = ValidatorUtils.getValidator ().validate (domainObject, ServerValidationGroup.class);
      }
      
      return violations;
   }

   protected <E extends IEntity <?>> Set <ConstraintViolation <E>> runDefaultValidation (Validator validator, E entity) {
      return entity != null ? validator.validate (entity, ServerValidationGroup.class) : new HashSet <ConstraintViolation <E>> (0);
   }
   
}
