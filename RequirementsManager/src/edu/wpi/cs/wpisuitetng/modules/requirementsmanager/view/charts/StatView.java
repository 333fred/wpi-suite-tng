package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

/*@author Steve Kordell*/

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.demo.PieChartDemo1;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.Tab;

public class StatView extends Tab {

public StatView() {
		this.setLayout(new BorderLayout());
		JPanel chart = new ChartPanel(createChart());
        add(chart,BorderLayout.CENTER);
}
    
    /**
* Creates a chart.
*
* @param dataset the dataset.
*
* @return A chart.
*/
    private static JFreeChart createChart() {
        /*
        JFreeChart chart = ChartFactory.createPieChart(
            "Pie Chart Demo 1", // chart title
            dataset, // data
            true, // include legend
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;
        */
    	StatusRequirementStatistics statusRequirementStatistics = new StatusRequirementStatistics();
    	return statusRequirementStatistics.buildPieChart();
    }
}