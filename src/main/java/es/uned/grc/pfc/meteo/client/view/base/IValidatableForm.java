package es.uned.grc.pfc.meteo.client.view.base;

import java.util.Map;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.ui.DisclosurePanel;

public interface IValidatableForm {
   /** 
    * Path to the span elements that display validation error messages.
    * 
    * If the elements are contained within a DisclosurePanel, it is included in the provided map as well
    */
   void addPathToSpanMap (String prefix, Map <String, SpanElement> pathToSpanMap, Map <String, DisclosurePanel> disclosurePanels);
}
