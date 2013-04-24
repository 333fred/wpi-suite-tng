/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Steve Kordell, Alex Gorowara
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.Tab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter.FilterUpdateListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter.FilterView;

@SuppressWarnings("serial")
public class StatView extends Tab implements ActionListener,
		FilterUpdateListener {

	private static StatView sv;
	private JComboBox comboBoxStatisticType;
	private JRadioButton makePieRadio;
	private JRadioButton makeBarRadio;
	private JRadioButton makeLineRadio;
	/*
	 * private JButton generateChart;
	 */

	/** Enums representing the type of chart and type of data */
	private ChartType chartType;
	private DataType chartDataType;

	private StatView() {

		chartType = ChartType.PIE;
		chartDataType = DataType.STATUS;
		FilterView.getInstance().addFilterUpdateListener(this);

		this.setLayout(new BorderLayout());
		JPanel chart = new ChartPanel(createPieStatusChart());

		JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				chart, buildSidePanel());
		mainPane.setResizeWeight(0.40);
		add(mainPane);
	}

	public static StatView getInstance() {
		if (sv == null) {
			sv = new StatView();
		}
		return sv;
	}

	/**
	 * Reinitialized the stat view. Used for testing
	 */
	public static void init() {
		sv = new StatView();
	}

	public JPanel buildSidePanel() {
		final int VERTICAL_PADDING = 5;
		final int HORIZONTAL_PADDING = 5;
		final int FAR = 5;

		SpringLayout sidePanelLayout = new SpringLayout();
		JPanel sidePanel = new JPanel(sidePanelLayout);

		JLabel lblStatisticType = new JLabel("Statistic Type");

		String[] availableStatisticTypes = { "Status", "Assignees",
				"Iterations", "Velocity" };
		// TODO: Add Estimates, Effort, Tasks charts for future use.
		comboBoxStatisticType = new JComboBox(availableStatisticTypes);
		comboBoxStatisticType.addActionListener(this);

		makePieRadio = new JRadioButton("Pie Chart");
		makePieRadio.setMnemonic(KeyEvent.VK_P);
		makePieRadio.setActionCommand("Pie Chart");
		makePieRadio.addActionListener(this);

		makeBarRadio = new JRadioButton("Bar Chart");
		makeBarRadio.setMnemonic(KeyEvent.VK_B);
		makeBarRadio.setActionCommand("Bar Chart");
		makeBarRadio.addActionListener(this);

		makeLineRadio = new JRadioButton("Line Chart");
		makeLineRadio.setMnemonic(KeyEvent.VK_B);
		makeLineRadio.setActionCommand("Line Chart");
		makeLineRadio.addActionListener(this);
		makeLineRadio.setEnabled(false);

		/*
		 * generateChart = new JButton("Generate Chart");
		 * generateChart.setMnemonic(KeyEvent.VK_ENTER);
		 * generateChart.setActionCommand("Generate Chart");
		 * generateChart.addActionListener(this);
		 */

		ButtonGroup group = new ButtonGroup();
		group.add(makePieRadio);
		group.add(makeBarRadio);
		group.add(makeLineRadio);
		updateSelectedItems();

		JPanel radioPanel = new JPanel(new GridLayout(3, 1));
		radioPanel.add(makePieRadio);
		radioPanel.add(makeBarRadio);
		radioPanel.add(makeLineRadio);
		radioPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Chart Type"));

		sidePanel.add(lblStatisticType);
		sidePanel.add(comboBoxStatisticType);
		sidePanel.add(radioPanel);

		/*
		 * sidePanel.add(generateChart);
		 */

		sidePanelLayout.putConstraint(SpringLayout.NORTH, lblStatisticType,
				VERTICAL_PADDING, SpringLayout.NORTH, sidePanel);
		sidePanelLayout.putConstraint(SpringLayout.WEST, lblStatisticType,
				HORIZONTAL_PADDING, SpringLayout.WEST, sidePanel);

		sidePanelLayout.putConstraint(SpringLayout.NORTH,
				comboBoxStatisticType, VERTICAL_PADDING, SpringLayout.SOUTH,
				lblStatisticType);
		sidePanelLayout.putConstraint(SpringLayout.WEST, comboBoxStatisticType,
				HORIZONTAL_PADDING, SpringLayout.WEST, sidePanel);

		sidePanelLayout.putConstraint(SpringLayout.NORTH, radioPanel,
				VERTICAL_PADDING + FAR, SpringLayout.SOUTH,
				comboBoxStatisticType);
		sidePanelLayout.putConstraint(SpringLayout.WEST, radioPanel,
				HORIZONTAL_PADDING, SpringLayout.WEST, sidePanel);

		/*
		 * sidePanelLayout.putConstraint(SpringLayout.NORTH,makeBarRadio,
		 * VERTICAL_PADDING,SpringLayout.SOUTH,makePieRadio);
		 * sidePanelLayout.putConstraint
		 * (SpringLayout.WEST,makeBarRadio,HORIZONTAL_PADDING
		 * ,SpringLayout.WEST,sidePanel);
		 * 
		 * sidePanelLayout.putConstraint(SpringLayout.NORTH,makeLineRadio,
		 * VERTICAL_PADDING,SpringLayout.SOUTH,makeBarRadio);
		 * sidePanelLayout.putConstraint
		 * (SpringLayout.WEST,makeLineRadio,HORIZONTAL_PADDING
		 * ,SpringLayout.WEST,sidePanel);
		 */

		return sidePanel;
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return A chart.
	 */
	private static JFreeChart createPieStatusChart() {
		StatusRequirementStatistics statusRequirementStatistics = new StatusRequirementStatistics();
		JFreeChart chart = statusRequirementStatistics.buildPieChart();
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		plot.setNoDataMessage("No data available");
		plot.setCircular(true);
		plot.setLabelGap(0.02);
		return chart;
	}

	/**
	 * method to update this object upon receiving word of an action updates
	 * user preference for data type and chart type and creates a new chart
	 * based on those new preferences
	 */
	public void actionPerformed(ActionEvent e) {

		String source = e.getActionCommand();

		// TODO: the above is what is causing the exception

		if (source.equals("Pie Chart")) {
			if (chartDataType == DataType.VELOCITY) {
				updateChartDataType(DataType.STATUS);
				comboBoxStatisticType.setSelectedItem("Status");
			}
			updateChartType(ChartType.PIE);

		} else if (source.equals("Bar Chart")) {
			if (chartDataType == DataType.VELOCITY) {
				updateChartDataType(DataType.STATUS);
				comboBoxStatisticType.setSelectedItem("Status");
			}
			updateChartType(ChartType.BAR);
		} else if (source.equals("Line Chart")) {
			updateChartType(ChartType.LINE);
			updateChartDataType(DataType.VELOCITY);
			comboBoxStatisticType.setSelectedItem("Velocity");
		} else if (comboBoxStatisticType.getSelectedItem().equals("Status")) {
			if (chartType == ChartType.LINE) {
				updateChartType(ChartType.PIE);
				makePieRadio.setSelected(true);
				makePieRadio.setEnabled(true);
				makeBarRadio.setEnabled(true);
				makeLineRadio.setEnabled(false);
			}
			updateChartDataType(DataType.STATUS);

		} else if (comboBoxStatisticType.getSelectedItem().equals("Iterations")) {
			if (chartType == ChartType.LINE) {
				updateChartType(ChartType.PIE);
				makePieRadio.setSelected(true);
				makePieRadio.setEnabled(true);
				makeBarRadio.setEnabled(true);
				makeLineRadio.setEnabled(false);
			}
			updateChartDataType(DataType.ITERATION);
		} else if (comboBoxStatisticType.getSelectedItem().equals("Assignees")) {
			if (chartType == ChartType.LINE) {
				updateChartType(ChartType.PIE);
				makePieRadio.setSelected(true);
				makePieRadio.setEnabled(true);
				makeBarRadio.setEnabled(true);
				makeLineRadio.setEnabled(false);
			}
			updateChartDataType(DataType.ASSIGNEE);
		} else if (comboBoxStatisticType.getSelectedItem().equals("Estimates")) {
			updateChartDataType(DataType.ESTIMATES);
		} else if (comboBoxStatisticType.getSelectedItem().equals("Effort")) {
			updateChartDataType(DataType.EFFORT);
		} else if (comboBoxStatisticType.getSelectedItem().equals("Velocity")) {
			updateChartDataType(DataType.VELOCITY);
			updateChartType(ChartType.LINE);
			makeLineRadio.setSelected(true);
			makePieRadio.setEnabled(false);
			makeBarRadio.setEnabled(false);
			makeLineRadio.setEnabled(true);

		} else if (comboBoxStatisticType.getSelectedItem().equals("Tasks")) {
			updateChartDataType(DataType.TASK);
		}
		this.updateChart();

	}

	// public for testing purposes
	public void updateSelectedItems() {
		// set the chart type radio buttons
		switch (chartType) {
		case PIE:
			makePieRadio.setSelected(true);
			makeBarRadio.setSelected(false);
			makeLineRadio.setSelected(false);
			break;
		case BAR:
			makeBarRadio.setSelected(true);
			makePieRadio.setSelected(false);
			makeLineRadio.setSelected(false);
			break;
		case LINE:
			makeBarRadio.setSelected(false);
			makePieRadio.setSelected(false);
			makeLineRadio.setSelected(true);
		}
		comboBoxStatisticType.setSelectedItem(this.chartDataType.toString()
				.substring(0, 1)
				.concat(this.chartDataType.toString().toLowerCase()));
	}

	/**
	 * Called by the action listener to update the chart type
	 * 
	 * @param type
	 *            The new chart type
	 */
	// public for testing purposes
	public void updateChartType(ChartType type) {
		chartType = type;
	}

	/**
	 * Called by the action listener to update the chart data type
	 * 
	 * @param type
	 *            The new chart data type
	 */
	// public for testing purposes
	public void updateChartDataType(DataType type) {
		chartDataType = type;
	}

	// public only for testing
	public enum ChartType {
		BAR, PIE, LINE
	}

	// public only for testing
	public enum DataType {
		STATUS, ITERATION, ASSIGNEE, ESTIMATES, EFFORT, TASK, VELOCITY
	}

	/**
	 * method to update the displayed chart based on the user's selection
	 */
	public void updateChart() {

		JFreeChart chart = null;
		AbstractRequirementStatistics stats = null;

		// initialize the stats based on the type of data which the user has
		// selected
		switch (this.chartDataType) {

		case STATUS:
			stats = new StatusRequirementStatistics();
			break;

		case ITERATION:
			stats = new IterationRequirementStatistics();
			break;

		case ASSIGNEE:
			stats = new AssigneeRequirementStatistics();
			break;

		case ESTIMATES:
			stats = new EstimateRequirementStatistics();
			break;

		case EFFORT:
			stats = new ActualRequirementStatistics();
			break;
		case VELOCITY:
			stats = new VelocityIterationStatistics();
			break;
		case TASK:
			stats = new TaskRequirementStatistics();
			break;
		default:
			// if you encounter this default statement, it means that new values
			// have been
			// added to the DataType enum, but nobody has modified this poor
			// little method

		}
		
		// build the chart based on the type of chart the user has selected
		switch (chartType) {

		case BAR: // TODO: determine if additional modifications need to be made
					// to the plot
			chart = stats.buildBarChart();
			CategoryPlot barPlot = (CategoryPlot) chart.getPlot();
			barPlot.setNoDataMessage("No data available");
			break;

		case PIE:
			chart = stats.buildPieChart();
			PiePlot3D piePlot = (PiePlot3D) chart.getPlot();
			piePlot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
			piePlot.setNoDataMessage("No data available");
			piePlot.setCircular(true);
			piePlot.setLabelGap(0.02);
			piePlot.setForegroundAlpha(0.7f);
			PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator(
					"{0} = {1} ({2})");
			piePlot.setLabelGenerator(generator);
			break;

		case LINE:
			chart = stats.buildLineChart();
			CategoryPlot linePlot = (CategoryPlot) chart.getPlot();
			linePlot.setNoDataMessage("No data available");

		default:
			// if you encounter this default statement, it means that new values
			// have been
			// added to the ChartType enum, but nobody has modified this poor
			// little method

		}

		// add the newly generated chart to the panel
		JSplitPane mainPane = (JSplitPane) this.getComponent(0);
		ChartPanel chartPanel = (ChartPanel) mainPane.getLeftComponent();
		chartPanel.setChart(chart);

		return;

	}

	@Override
	public void onGainedFocus() {
		updateChart();
	}

	/**
	 * @return the chartType
	 */
	public ChartType getChartType() {
		return chartType;
	}

	public DataType getChartDataType() {
		return chartDataType;
	}

	@Override
	public void filtersUpdated() {
		updateChart();
	}

}
