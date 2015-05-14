package es.uned.grc.pfc.meteo.client.validator;

import javax.validation.GroupSequence;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;

import es.uned.grc.pfc.meteo.client.model.IParameterProxy;
import es.uned.grc.pfc.meteo.client.model.IStationProxy;
import es.uned.grc.pfc.meteo.client.model.IUserProxy;
import es.uned.grc.pfc.meteo.client.model.IVariableProxy;
import es.uned.grc.pfc.meteo.shared.validator.ClientValidationGroup;
import es.uned.grc.pfc.meteo.shared.validator.MaximalValidationGroup;

public class BeanValidatorFactory extends AbstractGwtValidatorFactory {

   /**
    * Validator marker for the Validation Sample project. Only the classes and
    * groups listed in the {@link GwtValidation} annotation can be validated.
    */
   @GwtValidation (value = {IStationProxy.class, IParameterProxy.class, IVariableProxy.class, IUserProxy.class},
                   groups = {Default.class, MaximalValidationGroup.class, ClientValidationGroup.class})
   @GroupSequence ({Default.class, MaximalValidationGroup.class, ClientValidationGroup.class})
   public interface GwtValidator extends Validator {
   }

   @Override
   public AbstractGwtValidator createValidator () {
      return GWT.create (GwtValidator.class);
   }
}
