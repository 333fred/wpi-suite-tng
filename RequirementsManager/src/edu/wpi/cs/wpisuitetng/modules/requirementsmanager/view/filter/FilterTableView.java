package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IRetrieveAllFiltersNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllFiltersController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;

public class FilterTableView extends JPanel implements IRetrieveAllFiltersNotifier {

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
	
	/** Button used to delete a filter */
	private JButton butDelete;

	/** Panel to hold stuff in the scrollPane */
	private JPanel butPanel;
	
	/** THe controller to retrieve filters */
	private RetrieveAllFiltersController filterController;
	
	public FilterTableView() {
		ArrayList<Filter> filters = new ArrayList<Filter>();
		
		filterController = new RetrieveAllFiltersController(this);
		
		butPanel = new JPanel();
		
		butEnable = new JButton("Disable");
		butEnable.setEnabled(false);
		
		butDelete = new JButton("Delete");
		butDelete.setEnabled(false);
		
		butPanel.add(butEnable);
		butPanel.add(butDelete);
		
		tableModel = new FilterTableModel(filters);

		tableView = new JTable(tableModel);
		tableView.setFillsViewportHeight(true);

		scrollPane = new JScrollPane(tableView,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// scrollPane.getViewport().add(tableView);

		setLayout(new BorderLayout());

		add(scrollPane, BorderLayout.CENTER);
		add(butPanel, BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(100,500));
		
		refresh();

	}
	
	/** Refreshes the table view 
	 * 
	 */
	
	public void refresh() {	
		
		//get the filters from the server 
		filterController.getAll();
		
		List<Filter> filters = FilterDatabase.getInstance().getFilters();
		
		System.out.println("Refreshing filter table: " + filters.size());
		
		tableModel.updateFilters(filters);
	}

	@Override
	public void receivedData(Filter[] filters) {
		//put the filters in the table model
		tableModel.updateFilters(Arrays.asList(filters));	
	}
}
