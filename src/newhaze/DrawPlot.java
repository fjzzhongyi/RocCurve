package newhaze;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.*;
//import org.jfree.ui.Spacer;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DrawPlot extends ApplicationFrame {

    public DrawPlot(final String title,final String x_label,final String y_label,ArrayList<Point> gasdm,ArrayList<Point> nphgs,
    		ArrayList<Point> eventtree, ArrayList<Point> lgta, ArrayList<Point> fss) {
        super(title);
        final XYDataset dataset = createDataset(gasdm, nphgs, eventtree, lgta, fss);
        final JFreeChart chart = createChart(dataset, title, x_label, y_label);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 480));
        setContentPane(chartPanel);
    }

    private XYDataset createDataset(ArrayList<Point> gasdm, ArrayList<Point> nphgs, 
    		ArrayList<Point> eventtree, ArrayList<Point> lgta, ArrayList<Point> fss) {
    	final XYSeries series1 = new XYSeries("MASS");
        for(int i=0;i<gasdm.size();i++)
        	series1.add(gasdm.get(i).x, gasdm.get(i).y);
        
        final XYSeries series2 = new XYSeries("NPHGS");
        for(int i=0;i<nphgs.size();i++)
        	series2.add(nphgs.get(i).x, nphgs.get(i).y);
        
        final XYSeries series3 = new XYSeries("EventTree");
        for(int i=0;i<eventtree.size();i++)
        	series3.add(eventtree.get(i).x, eventtree.get(i).y);
        
        final XYSeries series4 = new XYSeries("LGTA");
        for(int i=0;i<lgta.size();i++)
        	series4.add(lgta.get(i).x, lgta.get(i).y);
        
        final XYSeries series5 = new XYSeries("FSS");
        for(int i=0;i<fss.size();i++)
        	series5.add(fss.get(i).x, fss.get(i).y);
        
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
        dataset.addSeries(series4);
        dataset.addSeries(series5);
        return dataset;
    }

    private JFreeChart createChart(final XYDataset dataset, final String title,final String x_label,final String y_label) {
        
        final JFreeChart chart = ChartFactory.createXYLineChart(
            title,      // chart title
            x_label,  	// x axis label
            y_label,    // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        chart.setBackgroundPaint(Color.white);

//      final NumberAxis domainAxis = new NumberAxis("X-Axis");
//      domainAxis.setRange(0.0,1.0);
//      domainAxis.setTickUnit(new NumberTickUnit(1.0));
//      domainAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 30));  
//      // 设置y轴上的标题字体  
//      domainAxis.setLabelFont(new Font("SansSerif", Font.PLAIN, 30)); 
      chart.getTitle().setFont(new Font("SansSerif", Font.PLAIN, 22));
      chart.getLegend().setItemFont(new Font("SansSerif", Font.PLAIN, 18)); 
      
//      final NumberAxis rangeAxis = new NumberAxis("Y-Axis");
//      rangeAxis.setRange(0.0,1.0);
//      rangeAxis.setTickUnit(new NumberTickUnit(0.1));
     
      final XYPlot plot = chart.getXYPlot();
      
      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setLabelFont(new Font("SansSerif", Font.PLAIN, 18));
      rangeAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 18));
      
      NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
      domainAxis.setLabelFont(new Font("SansSerif", Font.PLAIN, 18));
      domainAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 18));
      
      final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
      renderer.setSeriesPaint(0, new Color(220,20,60));
      renderer.setSeriesPaint(1, new Color(0,0,205));
      renderer.setSeriesPaint(2, new Color(50,205,50));
      renderer.setSeriesPaint(3, new Color(255,140,0));
      renderer.setSeriesPaint(4, new Color(192,14,235));
      plot.setRenderer(renderer);
                
        return chart;
    }

    public static void draw(final String title,final String x_label,final String y_label,ArrayList<Point> gasdm, 
    		ArrayList<Point> nphgs, ArrayList<Point> eventtree, ArrayList<Point> lgta, ArrayList<Point> fss){        
        final DrawPlot demo = new DrawPlot(title,x_label,y_label,gasdm, nphgs, eventtree, lgta, fss);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
    public static void main(final String[] args) {

    }

}
