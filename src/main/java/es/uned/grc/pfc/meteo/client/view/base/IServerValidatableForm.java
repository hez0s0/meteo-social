package es.uned.grc.pfc.meteo.client.view.base;

public interface IServerValidatableForm {
   /** display a server validation error */
   void displayServerValidationError (int baseElement, String [] propertyPath, String message);
   /** clears the spans of server-side validation errors */
   void clearServerErrors ();
}
