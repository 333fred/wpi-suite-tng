package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;

public class FilterTableView extends JPanel {

	/** List of all the filters this is displaying */
	List<Filter> filters;
	
	/** the table view for the filters */
	private JTable tableView;
	
	public FilterTableView() {
		
	}
}
