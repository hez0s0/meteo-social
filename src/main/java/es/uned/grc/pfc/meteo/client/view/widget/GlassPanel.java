package es.uned.grc.pfc.meteo.client.view.widget;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import es.uned.grc.pfc.meteo.client.view.widget.dialog.AlertDialogBox;

public class GlassPanel extends Composite implements NativePreviewHandler {
   interface GlassPanelUiBinder extends UiBinder <VerticalPanel, GlassPanel> {
   }
   private static GlassPanelUiBinder uiBinder = GWT.create (GlassPanelUiBinder.class);

   private static final boolean DEBUG = false;
   @UiField
   protected Image loadingImage = null;
   
   @UiConstructor
   public GlassPanel () {
      initWidget (uiBinder.createAndBindUi (this));
   }

   @Override
   public void onPreviewNativeEvent (NativePreviewEvent event) {
      event.consume ();
      event.cancel ();
   }
   
   public static <V> void fireDistraction (final AcceptsOneWidget parent, final IsWidget finalChild, final Request <V> request, final Receiver <?> receiver) {
      fireDistraction (parent, finalChild, request, receiver, true);
   }

   public static <V> void toDistraction (final AcceptsOneWidget parent, final IsWidget finalChild, final Request <V> request, final Receiver <?> receiver) {
      fireDistraction (parent, finalChild, request, receiver, false);
   }
   
   @SuppressWarnings ({ "rawtypes", "unchecked" })
   private static <V> void fireDistraction (final AcceptsOneWidget parent, final IsWidget finalChild, final Request <V> request, final Receiver receiver, boolean fire) {
      final GlassPanel glassPanel = new GlassPanel ();
      parent.setWidget (glassPanel);

      if (DEBUG) {
         AlertDialogBox.showWarning ("FIRING REQUEST: " + request);
      }
      final long init = new Date ().getTime ();
      request.to (new Receiver <V> () {
         @Override
         public void onSuccess (V response) {
            parent.setWidget (finalChild);
            long end1 = new Date ().getTime ();
            receiver.onSuccess (response);
            long end2 = new Date ().getTime ();
            if (DEBUG) {
               AlertDialogBox.showWarning ("Load time: " + ((end1 - init)));
               AlertDialogBox.showWarning ("Display time: " + ((end2 - init)));
            }
         }
         
         @Override
         public void onFailure (ServerFailure serverFailure) {
            parent.setWidget (finalChild);
            receiver.onFailure (serverFailure);
         }
         
         @Override
         public void onConstraintViolation (Set<ConstraintViolation <?>> violations) {
            parent.setWidget (finalChild);
            receiver.onConstraintViolation (violations);
         }
      });
      if (fire) {
         request.fire ();
      }
   }
}
