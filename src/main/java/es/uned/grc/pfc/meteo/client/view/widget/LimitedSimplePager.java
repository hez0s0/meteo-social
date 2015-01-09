package es.uned.grc.pfc.meteo.client.view.widget;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;

/**
 * Overwrite the default SimplePager to fix the last page count
 */
public class LimitedSimplePager extends SimplePager {
   
   public LimitedSimplePager () {
      this.setRangeLimited (true);
   }

   public LimitedSimplePager (TextLocation location, Resources resources, boolean showFastForwardButton, int fastForwardRows, boolean showLastPageButton) {
      super (location, resources, showFastForwardButton, fastForwardRows, showLastPageButton);
      
      this.setRangeLimited (true);
   }

   @Override
   public void setPageStart (int index) {
      HasRows display = getDisplay ();
      
      if (display != null) {
         Range range = display.getVisibleRange ();
         int pageSize = range.getLength ();
         if (!isRangeLimited () && display.isRowCountExact ()) {
            index = Math.min (index, display.getRowCount () - pageSize);
         }
         index = Math.max (0, index);
         if (index != range.getStart ()) {
            display.setVisibleRange (index, pageSize);
         }
      }
   }
   
}
