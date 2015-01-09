package es.uned.grc.pfc.meteo.client.view.util;

import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.cellview.client.CellTable;

public interface CustomCellTableResources extends CellTable.Resources {
   public interface CellTableStyle extends CellTable.Style {
   };

   @Override
   @NotStrict
   @Source ({CellTable.Style.DEFAULT_CSS, "cellTableStyle.css"})
   CellTableStyle cellTableStyle();
}
