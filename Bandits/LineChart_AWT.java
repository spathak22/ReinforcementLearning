package assignment5;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChart_AWT extends ApplicationFrame {

   public LineChart_AWT( String applicationTitle , String chartTitle, DefaultCategoryDataset dataset ) {
      super(applicationTitle);
      JFreeChart lineChart = ChartFactory.createLineChart(
         chartTitle,
         "Number of Pulls","Regret",
         dataset,
         PlotOrientation.VERTICAL,
         true,true,false);
         
      ChartPanel chartPanel = new ChartPanel( lineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      setContentPane( chartPanel );
   }

   public DefaultCategoryDataset createDataset( ) {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
      dataset.addValue( 15 , "Regret" , "1970" );
      dataset.addValue( 30 , "Regret" , "1980" );
      dataset.addValue( 60 , "Regret" ,  "1990" );
      dataset.addValue( 120 , "Regret" , "2000" );
      dataset.addValue( 240 , "Regret" , "2010" );
      dataset.addValue( 300 , "Regret" , "2014" );
      return dataset;
   }
   
   public static void main( String[ ] args ) {
//      LineChart_AWT chart = new LineChart_AWT(
//         "Bandits" ,
//         "Regret vs No of Pulls");
//
//      chart.pack( );
//      RefineryUtilities.centerFrameOnScreen( chart );
//      chart.setVisible( true );
   }
}