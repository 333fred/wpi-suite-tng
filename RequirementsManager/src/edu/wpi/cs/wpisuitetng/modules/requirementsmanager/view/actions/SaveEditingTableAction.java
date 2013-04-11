/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SaveRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * @author Alex
 *
 */
public class SaveEditingTableAction extends AbstractAction implements ISaveNotifier {
	private RequirementTableView tableView;
	TableRowSorter<TableModel> sorter;
	
	public SaveEditingTableAction(RequirementTableView tableView, TableRowSorter<TableModel> sorter) {
		super("Save Changes");
		this.tableView = tableView;
		this.sorter = sorter;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		SaveRequirementController saveController = new SaveRequirementController(this);
		RequirementDatabase rdb = RequirementDatabase.getInstance();
		
		boolean[] changedRows = tableView.getTable().getEditedRows();
		
		for (int i = 0; i < changedRows.length; i++) {
			if (changedRows[i]) {
				int id = Integer.parseInt((String)this.tableView.getTable().getModel().getValueAt(i, 0));
				int newEstimate = Integer.parseInt((String)this.tableView.getTable().getModel().getValueAt(i, 6));
				try {
					Requirement reqToChange = rdb.getRequirement(id);
					reqToChange.setEstimate(newEstimate);
					saveController.SaveRequirement(reqToChange, false);
				} catch (RequirementNotFoundException e1) {
					// TODO Auto-generated catch block
				}			
			}
		}
		
		// TODO Auto-generated method stub
		if (tableView.getIsEditable()) {
			tableView.setIsEditable(false);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fail(Exception exception) {
		// TODO Auto-generated method stub
		
	}
}
