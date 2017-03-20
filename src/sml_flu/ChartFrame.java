package sml_flu;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

public class ChartFrame extends JFrame{
	/*
	final JFreeChart chart = createChart(dataset, title, x_label, y_label);
    final ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 480));
    setContentPane(chartPanel);
    */
	public ChartFrame(XYDataset[] datasets, String[] titles, String[] x_labels, String[]y_labels){
		JPanel jp= new JPanel();
	    jp.setLayout(new GridLayout(2,2));
	    JFreeChart chart;
	    ChartPanel chartPanel;
		for (int i = 0; i < datasets.length; i++) {
			chart = createChart(datasets[i], titles[i], x_labels[i], y_labels[i]);
			chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new java.awt.Dimension(500, 480));
			jp.add(chartPanel);
		}
		this.setLocationRelativeTo(null);
		this.setSize(1000, 700);
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setContentPane(jp);
		this.setVisible(true);
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

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final NumberAxis domainAxis = new NumberAxis("X-Axis");
//        domainAxis.setRange(0.0,1.0);
//        domainAxis.setTickUnit(new NumberTickUnit(1.0));
//        domainAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 30));  
//        // 璁剧疆y杞翠笂鐨勬爣棰樺瓧浣�  
//        domainAxis.setLabelFont(new Font("SansSerif", Font.PLAIN, 30)); 
        chart.getTitle().setFont(new Font("SansSerif", Font.PLAIN, 22));
        chart.getLegend().setItemFont(new Font("SansSerif", Font.PLAIN, 18)); 
        
//        final NumberAxis rangeAxis = new NumberAxis("Y-Axis");
//        rangeAxis.setRange(0.0,1.0);
//        rangeAxis.setTickUnit(new NumberTickUnit(0.1));
       
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
        plot.setRenderer(renderer);
                
        return chart;
    }
}
