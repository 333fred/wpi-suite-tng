/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * @author Alex C
 *
 */
public class EnableEditingAction extends AbstractAction {

	private RequirementTableView tableView;
	TableRowSorter<TableModel> sorter;

	/**
	 * Constructor for EnableEditingAction 
	 * 
	 * @param tableView
	 */
	public EnableEditingAction(RequirementTableView tableView, TableRowSorter<TableModel> sorter) {
		super("Enable Editing");
		this.tableView = tableView;
		this.sorter = sorter;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// TODO Auto-generated method stub
		if (tableView.isEditable()) {
			tableView.setEditable(false);
			tableView.displayEditInformation("");
			tableView.changeButtonStatus();
			tableView.getTable().setRowSorter(sorter);
			tableView.getTable().clearUpdated();
			tableView.refresh();
		} else {

			// Turn off row sorting (it breaks editing right now)
			tableView.getTable().setRowSorter(null);

			// set isEditable to true
			tableView.setEditable(true);

			tableView.changeButtonStatus();

			tableView.displayEditInformation("Editing Enabled");		

		}
	}

}
