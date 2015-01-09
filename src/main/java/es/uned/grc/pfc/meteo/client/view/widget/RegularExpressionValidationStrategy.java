package es.uned.grc.pfc.meteo.client.view.widget;


public class RegularExpressionValidationStrategy implements IValidationStrategy {
   private String regularExpression;

   public RegularExpressionValidationStrategy (String regularExpression) {
      this.regularExpression = regularExpression;
   }

   @Override
   public boolean matches (String valueToCheck) {
      return valueToCheck.matches (regularExpression);
   }
}