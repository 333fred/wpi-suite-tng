package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;

public class FilterTableView extends JPanel {

	/** List of all the filters this is displaying */
	List<Filter> filters;
	
	/** the table view for the filters */
	private JTable tableView;
	
	/** The table model for the table view */
	private FilterTableModel tableModel;
	
	public FilterTableView() {

		ArrayList<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter());
		filters.add(new Filter());
		tableModel = new FilterTableModel(filters);
		
		tableView = new JTable(tableModel);		
		
		setLayout(new BorderLayout());
		
		add(tableView, BorderLayout.CENTER);
		
		
	}
}
