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
	
	private final RequirementTableView tableView;
	TableRowSorter<TableModel> sorter;
	
	public SaveEditingTableAction(final RequirementTableView tableView,
			final TableRowSorter<TableModel> sorter) {
		super("Save Changes");
		this.tableView = tableView;
		this.sorter = sorter;
	}
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		// TODO Auto-generated method stub
		final RequirementsController saveController = new RequirementsController();
		final RequirementDatabase rdb = RequirementDatabase.getInstance();
		
		final boolean[] changedRows = tableView.getTable().getEditedRows();
		
		// if the user is still currently editing a cell, and they try to save
		if (tableView.getTable().getCellEditor() != null) {
			// stop editing first before saving
			tableView.getTable().getCellEditor().stopCellEditing();
		}
		
		for (int i = 0; i < changedRows.length; i++) {
			if (changedRows[i]) {
				final int id = Integer.parseInt((String) tableView.getTable()
						.getModel().getValueAt(i, 0));
				final int newEstimate = Integer.parseInt((String) tableView
						.getTable().getModel().getValueAt(i, 6));
				try {
					final Requirement reqToChange = rdb.get(id);
					reqToChange.setEstimate(newEstimate);
					final UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
							this);
					saveController.save(reqToChange, observer);
				} catch (final RequirementNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		if (tableView.isEditable()) {
			tableView.setEditable(false);
			tableView.displayEditInformation("");
			tableView.changeButtonStatus();
			// tableView.getTable().setRowSorter(sorter);
			// tableView.refresh();
			tableView.getTable().clearUpdated();
		}
	}
	
	@Override
	public void fail(final Exception exception) {
		tableView.displayEditInformation("Unable to complete request: "
				+ exception.getMessage());
		tableView.refresh();
	}
	
	@Override
	public void responseError(final int statusCode, final String statusMessage) {
		tableView.displayEditInformation("Received " + statusCode
				+ " error from server: " + statusMessage);
		tableView.refresh();
	}
	
	@Override
	public void responseSuccess() {
		// TODO Auto-generated method stub
	}
}
