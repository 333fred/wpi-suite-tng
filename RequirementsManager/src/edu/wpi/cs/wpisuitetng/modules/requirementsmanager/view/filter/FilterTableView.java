package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;

public class FilterTableView extends JPanel {

	/** List of all the filters this is displaying */
	List<Filter> filters;

	/** the table view for the filters */
	private JTable tableView;

	/** The table model for the table view */
	private FilterTableModel tableModel;

	/** The scroll pane for the filter table */
	private JScrollPane scrollPane;
	
	/** Button used to enable or disable a filter */
	private JButton butEnable;

	/** Panel to hold stuff in the scrollPane */
	private JPanel panel;
	
	public FilterTableView() {
		ArrayList<Filter> filters = new ArrayList<Filter>();
		
		panel = new JPanel();
		
		butEnable = new JButton("Disable");
		butEnable.setEnabled(false);
		
		tableModel = new FilterTableModel(filters);

		tableView = new JTable(tableModel);
		tableView.setFillsViewportHeight(true);

		scrollPane = new JScrollPane(tableView,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// scrollPane.getViewport().add(tableView);

		setLayout(new BorderLayout());

		add(scrollPane, BorderLayout.CENTER);
		add(butEnable, BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(100,500));

	}
}
