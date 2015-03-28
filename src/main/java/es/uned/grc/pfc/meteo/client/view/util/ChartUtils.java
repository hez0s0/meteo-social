package es.uned.grc.pfc.meteo.client.view.util;

import com.google.gwt.visualization.client.visualizations.AreaChart;
import com.google.gwt.visualization.client.visualizations.BarChart;
import com.google.gwt.visualization.client.visualizations.ColumnChart;
import com.google.gwt.visualization.client.visualizations.LineChart;

import es.uned.grc.pfc.meteo.client.model.IVariableProxy;

public class ChartUtils {

   private static final int GRAPH_WIDTH = 500;
   private static final int GRAPH_HEIGHT = 380;
   
   private ChartUtils () {
      
   }
   
   public static ColumnChart.Options createColumnOptions (IVariableProxy variable) {
      ColumnChart.Options options = ColumnChart.Options.create ();
      options.setWidth (GRAPH_WIDTH);
      options.setHeight (GRAPH_HEIGHT);
      options.setTitle (variable.getName ());
//      options.setEnableTooltip (true);
      return options;
   }

   public static BarChart.Options createBarOptions (IVariableProxy variable) {
      BarChart.Options options = BarChart.Options.create ();
      options.setWidth (GRAPH_WIDTH);
      options.setHeight (GRAPH_HEIGHT);
      options.setTitle (variable.getName ());
//      options.setEnableTooltip (true);
      return options;
   }

   public static AreaChart.Options createAreaOptions (IVariableProxy variable) {
      AreaChart.Options options = AreaChart.Options.create ();
      options.setWidth (GRAPH_WIDTH);
      options.setHeight (GRAPH_HEIGHT);
      options.setTitle (variable.getName ());
//      options.setEnableTooltip (true);
      return options;
   }

   public static LineChart.Options createLineOptions (IVariableProxy variable) {
      LineChart.Options options = LineChart.Options.create ();
      options.setWidth (GRAPH_WIDTH);
      options.setHeight (GRAPH_HEIGHT);
      options.setTitle (variable.getName ());
//      options.setEnableTooltip (true);
      options.setSmoothLine (true);
      return options;
   }
}
