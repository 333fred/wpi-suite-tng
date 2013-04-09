package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

/*@author Steve Kordell*/

import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.Tab;

public class StatView extends Tab {

	/** The split pane that will display the chart and its control panel */
	private JSplitPane splitPane;
	
	/** The JPanel representing the charts control panel */
	private JPanel controlPanel;
	
	/** The button group to hold the radio buttons containing the type of chart */
	private ButtonGroup groupChartType;
	
	/** The button group to hold the radio buttons containing chart data */
	private ButtonGroup groupChartData;
	
	/** The JPanels that will hold the radio buttons */
	private JPanel panelChartType;
	private JPanel panelChartData;
	
	/** The radio buttons for the different types of charts */
	private JRadioButton rbutPieChart;
	private JRadioButton rbutBarChart;
	
	/** The raddio buttons for the different types of data to display in the chart */
	private JRadioButton rbutStatus;
	private JRadioButton rbutIteration;
	private JRadioButton rbutAssignee;
	
	/** The JPanel that holds the chart */
	private JPanel chartPanel;
	
	private static final int VERTICAL_PADDING = 8;
	private static final int HORIZONTAL_PADDING = 8;
	
	public StatView() {		
		
		//create the two panels
		createChartPanel();
		createControlPanel();
		
		//create the split pane
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,	chartPanel, controlPanel);
		
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return A chart.
	 */
	private static JFreeChart createChart() {
		StatusRequirementStatistics statusRequirementStatistics = new StatusRequirementStatistics();
		return statusRequirementStatistics.buildPieChart();
	}
	
	private void createChartPanel() {
		chartPanel = new JPanel();
		chartPanel.setLayout(new BorderLayout());
		JPanel chart = new ChartPanel(createChart());
		chartPanel.add(chart, BorderLayout.CENTER);
	}
	
	private void createControlPanel() {
		controlPanel = new JPanel();
		
		groupChartType = new ButtonGroup();
		groupChartData = new ButtonGroup();
		
		panelChartType = new JPanel();
		panelChartData = new JPanel();
		
		rbutPieChart = new JRadioButton("Pie Chart");
		rbutBarChart = new JRadioButton("Bar Graph");
		
		rbutStatus = new JRadioButton("Status");
		rbutIteration = new JRadioButton("Iteration");
		rbutAssignee = new JRadioButton("Assignee");
		
		//add the buttons to the button groups
		groupChartType.add(rbutPieChart);
		groupChartType.add(rbutBarChart);
		
		groupChartData.add(rbutStatus);
		groupChartData.add(rbutIteration);
		groupChartData.add(rbutAssignee);
		
		//add the buttons to thier JPanels
		panelChartType.add(rbutPieChart);
		panelChartType.add(rbutBarChart);
		
		panelChartData.add(rbutStatus);
		panelChartData.add(rbutIteration);
		panelChartData.add(rbutAssignee);

		
		SpringLayout controlLayout = new SpringLayout();
		
		controlLayout.putConstraint(SpringLayout.NORTH, panelChartType, VERTICAL_PADDING, SpringLayout.NORTH, controlPanel);
		controlLayout.putConstraint(SpringLayout.WEST, panelChartType, HORIZONTAL_PADDING, SpringLayout.WEST, controlPanel);
		
		controlLayout.putConstraint(SpringLayout.NORTH, panelChartData, VERTICAL_PADDING, SpringLayout.SOUTH, panelChartType);
		controlLayout.putConstraint(SpringLayout.WEST, panelChartData, HORIZONTAL_PADDING, SpringLayout.WEST, controlPanel);
		
		//add the elements to the panel
		controlPanel.add(panelChartType);
		controlPanel.add(panelChartData);
		
		controlPanel.setLayout(controlLayout);
		
	}
}