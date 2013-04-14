package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

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

	public FilterTableView() {

		ArrayList<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter());
		filters.add(new Filter());
		tableModel = new FilterTableModel(filters);

		tableView = new JTable(tableModel);
		tableView.setFillsViewportHeight(true);

		scrollPane = new JScrollPane(tableView,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// scrollPane.getViewport().add(tableView);

		setLayout(new BorderLayout());

		add(scrollPane, BorderLayout.CENTER);

	}
}
