package es.uned.grc.pfc.meteo.client.view.base;

import es.uned.grc.pfc.meteo.client.model.base.IEntityProxy;

public interface IEditorView <T extends IEntityProxy> {
   /** get a view's internal editor */
   AbstractEntityEditor <T> getEditor ();
} //end of IEditorView
