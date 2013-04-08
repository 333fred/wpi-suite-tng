package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;


public class StatView extends Tab {

	public StatView() {
        add(createDemoPanel());	
	}
	
    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    private static PieDataset createDataset() {
    	
    	List<Requirement> requirements = RequirementDatabase.getInstance().getAllRequirements();
    	
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<Status, Integer> requirementsPerStatus = new HashMap<Status, Integer>();
        
		// for every possible status
		for(Status status: Status.values()){
			requirementsPerStatus.put(status, 0);	// insert the status in the data set with zero counted requirements
		}
        
		// for every requirement in this project
		for(Requirement requirement : requirements){			
			Status status = requirement.getStatus();
			Integer oldValue = requirementsPerStatus.get(status);
			requirementsPerStatus.put(status, new Integer(oldValue.intValue() + 1));	// increment the number of requirements for a given status			
		}
        
		//format the status
		for(Status status: Status.values()){
			try {
				dataset.setValue(status, new Double((requirementsPerStatus.get(status)/requirements.size())*100));
			} finally {
			}
		}
		
        return dataset;        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(PieDataset dataset) {
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Pie Chart Demo 1",  // chart title
            dataset,             // data
            true,               // include legend
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;        
    }
    
    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        PieChartDemo1 demo = new PieChartDemo1("Pie Chart Demo 1");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}