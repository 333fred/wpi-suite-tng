/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * @author Alex
 * 
 */
public class SaveEditingTableAction extends AbstractAction implements
		ISaveNotifier {
	private RequirementTableView tableView;
	TableRowSorter<TableModel> sorter;

	public SaveEditingTableAction(RequirementTableView tableView,
			TableRowSorter<TableModel> sorter) {
		super("Save Changes");
		this.tableView = tableView;
		this.sorter = sorter;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		RequirementsController saveController = new RequirementsController();
		RequirementDatabase rdb = RequirementDatabase.getInstance();

		boolean[] changedRows = tableView.getTable().getEditedRows();

		// if the user is still currently editing a cell, and they try to save
		if (tableView.getTable().getCellEditor() != null) {
			// stop editing first before saving
			tableView.getTable().getCellEditor().stopCellEditing();
		}

		for (int i = 0; i < changedRows.length; i++) {
			if (changedRows[i]) {
				int id = Integer.parseInt((String) this.tableView.getTable()
						.getModel().getValueAt(i, 0));
				int newEstimate = Integer.parseInt((String) this.tableView
						.getTable().getModel().getValueAt(i, 6));
				try {
					Requirement reqToChange = rdb.get(id);
					reqToChange.setEstimate(newEstimate);
					UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
							this);
					saveController.save(reqToChange, observer);
				} catch (RequirementNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}

		if (tableView.isEditable()) {
			tableView.setEditable(false);
			tableView.displayEditInformation("");
			tableView.changeButtonStatus();
			tableView.getTable().setRowSorter(sorter);
			// tableView.refresh();
			tableView.getTable().clearUpdated();
		}
	}

	@Override
	public void responseSuccess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void responseError(int statusCode, String statusMessage) {
		this.tableView.displayEditInformation("Received " + statusCode
				+ " error from server: " + statusMessage);
		this.tableView.refresh();
	}

	@Override
	public void fail(Exception exception) {
		this.tableView.displayEditInformation("Unable to complete request: "
				+ exception.getMessage());
		this.tableView.refresh();
	}
}
