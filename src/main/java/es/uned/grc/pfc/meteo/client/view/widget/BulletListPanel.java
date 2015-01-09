package es.uned.grc.pfc.meteo.client.view.widget;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import es.uned.grc.pfc.meteo.client.util.IStyleConstants;
import es.uned.grc.pfc.meteo.client.util.PortableStringUtils;
import es.uned.grc.pfc.meteo.client.util.SearchParamRepresentationGetter;
import es.uned.grc.pfc.meteo.client.view.util.FormUtils;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.BulletList;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.ListItem;
import es.uned.grc.pfc.meteo.client.view.widget.suggest.Paragraph;

public abstract class BulletListPanel <E extends Object> extends Composite implements IBulletListPanel <E> {
   
   @UiField
   protected Label noneLabel = null;
   @UiField
   protected BulletList bulletList = null;
   
   protected abstract Integer getEntityID (E entityProxy);
   protected abstract String getDisplayText (E entityProxy);
   protected abstract SearchParamRepresentationGetter <E> getFilterParamRepresentationGetter ();
   
   protected void init () {
      bulletList.setStyleName (IStyleConstants.TOKEN_READ_ONLY_LIST);
      
      clearAll ();
   }

   @Override
   public void display (List <E> entityProxies, PlaceController placeController, RequestContext requestContext) {
      clearAll ();

      if (entityProxies != null) {
         noneLabel.setVisible (entityProxies.size () == 0);
         
         for (E entityProxy : entityProxies) {
            if (entityProxy != null) {
               display (entityProxy, placeController, requestContext);
            }
         }
      } else {
         noneLabel.setVisible (true);
      }
   }

   protected void display (final E entityProxy, final PlaceController placeController, final RequestContext requestContext) {
      display (entityProxy, placeController, requestContext, IStyleConstants.TOKEN_TOKEN, null);
   }
   
   protected void display (final E entityProxy, final PlaceController placeController, final RequestContext requestContext, String styleName, String popupText) {
      Paragraph p = null;
      FocusPanel focusPanel = new FocusPanel ();
      ListItem <E> displayItem = new ListItem <E> (entityProxy);
      ClickHandler customClickHandler = null;
      
      // prepare the item
      p = new Paragraph (getDisplayText (entityProxy));
      
      displayItem.setStyleName (styleName);
      displayItem.add (p);
      
      // prepare a panel that the user can click on
      focusPanel.add (displayItem);
      
      customClickHandler = getCustomClickHandler ();
      
      if (customClickHandler != null) {
         focusPanel.addClickHandler (customClickHandler);
      } else { //add the standard clickHandler
         focusPanel.addClickHandler (new ClickHandler () {
            
            @Override
            public void onClick (ClickEvent event) {
//               DrugItemFilter filterParam = getFilterParam ();
//               if (filterParam != null) {
//                  List <E> entityList = new ArrayList <E> (1);
//                  ObservationListPlace observationListPlace = new ObservationListPlace ();
//                  List <IRequestParamFilterProxy> filters = new ArrayList <IRequestParamFilterProxy> ();
//                  SearchParamRepresentationGetter <E> representationGetter = getFilterParamRepresentationGetter (); 
//   
//                  entityList.add (entityProxy);
//                  SearchUtils.appendListFilter (filters, filterParam, entityList, representationGetter, requestContext);
//                  
//                  observationListPlace.setFilters (filters);
//                  observationListPlace.setFilterRepresentation (ObservationListPlace.serialize (observationListPlace));
//                  placeController.goTo (observationListPlace);
//               }
            }
         });
      }
      
      // add a tooltip if required
      if (!PortableStringUtils.isEmpty (popupText)) {
         FormUtils.addAltHandler (focusPanel, popupText);
      }
      
      // add it to the displayed list
      bulletList.add (focusPanel);
   } //end of display

   protected void clearAll () {
      bulletList.clear ();
   }

   /**
    * If a custom clickHandler is required on every bullet, specifiy it by overriding this method.
    */
   protected ClickHandler getCustomClickHandler () {
      return null;
   }
} //end of BulletListPanel
