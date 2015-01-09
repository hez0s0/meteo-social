package es.uned.grc.pfc.meteo.client.validator;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Target ({ ElementType.METHOD })
@Retention (RUNTIME)
@Constraint (validatedBy = BlackListValidator.class)
@Documented
public @interface BlackListValidationConstraint {
   String message () default "contains a forbidden value";
   String [] blacklist ();
   Class <?> [] groups () default {};
   Class <? extends Payload> [] payload () default {};
}
