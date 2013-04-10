/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * @author Alex C
 *
 */
public class EnableEditingAction extends AbstractAction {
	
	private RequirementTableView tableView;

	/**
	 * Constructor for EnableEditingAction 
	 * 
	 * @param tableView
	 */
	public EnableEditingAction( RequirementTableView tableView) {
		super("Enable Editing");
		this.tableView = tableView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		// Turn off row sorting (it breaks editing right now)
		tableView.getTable().setRowSorter(null);
		
		// set isEditable to true
		tableView.setIsEditable(true);
		
		tableView.changeButtonStatus();
		
		tableView.displayEditInformation("Editing Enabled");		

	}

}
