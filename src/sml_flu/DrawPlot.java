package sml_flu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Transparency;
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

public class DrawPlot {

    public DrawPlot() {
		// TODO Auto-generated constructor stub
	}

	public XYDataset DrawPlot(final String title,final String x_label,final String y_label,
			ArrayList<?>[] result_set, String[] names) {
        
        //ArrayList<?>[] result_set= new ArrayList<?>[]{gasdm,nphgs,eventtree,lgta};
		//new String[]{"DMGraphScan","NPHGS","EventTree","LGTA"}
        final XYDataset dataset = createDataset(result_set, names);
        /*
        final JFreeChart chart = createChart(dataset, title, x_label, y_label);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 480));
        setContentPane(chartPanel);
        */
        return dataset;
    }

    private XYDataset createDataset(ArrayList<?>[] pointset, String[] names) {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        for (int j=0; j<names.length;j++){
	        final XYSeries series = new XYSeries(names[j]);
	        for(int i=0;i<pointset[j].size();i++)
	        	series.add(((ArrayList<Point>)pointset[j]).get(i).x, 
	        			((ArrayList<Point>)pointset[j]).get(i).y);
	        dataset.addSeries(series);
	    }
        return dataset;
    }



    public static XYDataset draw(final String title,final String x_label,final String y_label,
    		ArrayList<?>[] result_set, String[] names){        
        final DrawPlot demo = new DrawPlot();
        return demo.DrawPlot(title,x_label,y_label,result_set, names);
        
    }
    public static void main(final String[] args) {

    }

}
