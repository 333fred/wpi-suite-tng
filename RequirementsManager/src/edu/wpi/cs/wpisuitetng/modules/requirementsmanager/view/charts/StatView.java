package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

/*@author Steve Kordell*/

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.Tab;

public class StatView extends Tab implements ActionListener {

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
	
	/** Enums representing hte type of chart and type of data */
	private ChartType chartType;
	private DataType chartDataType;
	
	private static final int VERTICAL_PADDING = 8;
	private static final int HORIZONTAL_PADDING = 8;
	
	public StatView() {		
		
		chartType = ChartType.PIE;
		chartDataType = DataType.STATUS;
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
		
		//set up the panels containing the radio buttons
		panelChartType = new JPanel();
		panelChartData = new JPanel();
		
		panelChartType.setBorder(BorderFactory.createTitledBorder("Chart type"));
		panelChartData.setBorder(BorderFactory.createTitledBorder("Chart data type"));
		
		
		//creat the radio buttons
		rbutPieChart = new JRadioButton("Pie Chart");
		rbutBarChart = new JRadioButton("Bar Graph");		
		
		rbutStatus = new JRadioButton("Status");
		rbutIteration = new JRadioButton("Iteration");
		rbutAssignee = new JRadioButton("Assignee");
		
		//ste the selected radio buttons
		setSelectedRadioButtons();
		
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
		
		//add the action listeners
		rbutPieChart.addActionListener(this);
		rbutBarChart.addActionListener(this);
		
		rbutStatus.addActionListener(this);
		rbutIteration.addActionListener(this);
		rbutAssignee.addActionListener(this);
		
	}
	/** Sets the selected radio buttons based upon the enums
	 * 
	 */
	
	private void setSelectedRadioButtons() {
		//set the chart type radio buttons
		switch (chartType) {
		case PIE:
			rbutPieChart.setSelected(true);
		case BAR:
			rbutBarChart.setSelected(true);
		}
		
		//set the chart data type radio buttons
		switch(chartDataType) {
		case STATUS:
			rbutStatus.setSelected(true);
		case ITERATION:
			rbutIteration.setSelected(true);
		case ASSIGNEE:
			rbutAssignee.setSelected(true);
		}
	}
		
	public void actionPerformed(ActionEvent e) {
		JRadioButton source = (JRadioButton)e.getSource();
		if (source.equals(rbutPieChart)) {
			updateChartType(ChartType.PIE);
		}
		else if (source.equals(rbutBarChart)) {
			updateChartType(ChartType.BAR);
		}
		else if (source.equals(rbutStatus)) {
			updateChartDataType(DataType.STATUS);
		}
		else if (source.equals(rbutIteration)) {
			updateChartDataType(DataType.ITERATION);
		}
		else if (source.equals(rbutAssignee)) {
			updateChartDataType(DataType.ASSIGNEE);
		}
	}
	
	/** Called by the action listener to update the chart type
	 * 
	 * @param type The new chart type
	 */
	
	private void updateChartType(ChartType type) {
		chartType = type;
	}
	
	/** Called by the action listener to update the chart data type
	 * 
	 * @param type The new chart data type
	 */
	
	private void updateChartDataType(DataType type) {
		chartDataType = type;
	}
	
	private enum ChartType {
		BAR, PIE
	}
	
	private enum DataType {
		STATUS, ITERATION, ASSIGNEE
	}
}