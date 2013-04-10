package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

/*@author Steve Kordell*/

import java.awt.BorderLayout;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.Tab;

@SuppressWarnings("serial")
public class StatView extends Tab implements ActionListener {

	private JComboBox comboBoxStatisticType;
	private JRadioButton makePieRadio;
	private JRadioButton makeBarRadio;
	
	/** Enums representing the type of chart and type of data */
	private ChartType chartType;
	private DataType chartDataType;

	
	public StatView() {
		chartType = ChartType.PIE;
		chartDataType = DataType.STATUS;
		
		this.setLayout(new BorderLayout());
		JPanel chart = new ChartPanel(createPieStatusChart());
		
		JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,chart,buildSidePanel());
		mainPane.setResizeWeight(0.75);
		add(mainPane);
	}


	public JPanel buildSidePanel() {
		final int VERTICAL_PADDING = 5;
		final int HORIZONTAL_PADDING = 5;
		final int FAR = 5;
		
		SpringLayout sidePanelLayout = new SpringLayout();
		JPanel sidePanel = new JPanel(sidePanelLayout);		
		
		JLabel lblStatisticType = new JLabel("Statistic Type");
		JLabel lblChartType = new JLabel("Chart Type");
		
		String[] availableStatisticTypes = {"Assignees","Iterations","Status"};
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
	
		ButtonGroup group = new ButtonGroup();
		group.add(makePieRadio);
		group.add(makeBarRadio);
		setSelectedRadioButtons();
		
		sidePanel.add(lblStatisticType);
		sidePanel.add(lblChartType);
		sidePanel.add(comboBoxStatisticType);
		sidePanel.add(makePieRadio);
		sidePanel.add(makeBarRadio);
		
		sidePanelLayout.putConstraint(SpringLayout.NORTH,lblStatisticType,VERTICAL_PADDING,SpringLayout.NORTH,sidePanel);
		sidePanelLayout.putConstraint(SpringLayout.WEST,lblStatisticType,HORIZONTAL_PADDING,SpringLayout.WEST,sidePanel);
		
		sidePanelLayout.putConstraint(SpringLayout.NORTH,comboBoxStatisticType,VERTICAL_PADDING,SpringLayout.SOUTH,lblStatisticType);
		sidePanelLayout.putConstraint(SpringLayout.WEST,comboBoxStatisticType,HORIZONTAL_PADDING,SpringLayout.WEST,sidePanel);
		
		sidePanelLayout.putConstraint(SpringLayout.NORTH,lblChartType,VERTICAL_PADDING+FAR,SpringLayout.SOUTH,comboBoxStatisticType);
		sidePanelLayout.putConstraint(SpringLayout.WEST,lblChartType,HORIZONTAL_PADDING,SpringLayout.WEST,sidePanel);	
		
		sidePanelLayout.putConstraint(SpringLayout.NORTH,makePieRadio,VERTICAL_PADDING+FAR,SpringLayout.SOUTH,lblChartType);
		sidePanelLayout.putConstraint(SpringLayout.WEST,makePieRadio,HORIZONTAL_PADDING,SpringLayout.WEST,sidePanel);
		
		sidePanelLayout.putConstraint(SpringLayout.NORTH,makeBarRadio,VERTICAL_PADDING,SpringLayout.SOUTH,makePieRadio);
		sidePanelLayout.putConstraint(SpringLayout.WEST,makeBarRadio,HORIZONTAL_PADDING,SpringLayout.WEST,sidePanel);
		

		
		return sidePanel;
	}
    
    /**
* Creates a chart.
*
* @param dataset the dataset.
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
    
    public void actionPerformed(ActionEvent e) {
    	//System.out.println(e.getActionCommand());
		JRadioButton source = (JRadioButton)e.getSource();
		if (source.equals(makePieRadio)) {
			updateChartType(ChartType.PIE);
		} else if (source.equals(makeBarRadio)) {
			updateChartType(ChartType.BAR);
		} else if (comboBoxStatisticType.getSelectedItem().equals("Status")) {
			updateChartDataType(DataType.STATUS);
		} else if (comboBoxStatisticType.getSelectedItem().equals("Iterations")) {
			updateChartDataType(DataType.ITERATION);
		} else if (comboBoxStatisticType.getSelectedItem().equals("Assignees")) {
			updateChartDataType(DataType.ASSIGNEE);
		}
    }
    
	private void setSelectedRadioButtons() {
		//set the chart type radio buttons
		switch (chartType) {
		case PIE:
			makePieRadio.setSelected(true);
		case BAR:
			makeBarRadio.setSelected(true);
		}
		//todo set the combo box
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

