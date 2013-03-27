/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * @author Chris
 *
 * Action which concerns cancelling the creation/editing of a requirement
 */
public class CancelAction extends AbstractAction {

	private Requirement requirement;
	private DetailPanel parentView;

	public CancelAction(Requirement requirement, DetailPanel parentView) {
		super("Cancel");
		this.requirement = requirement;
		this.parentView = parentView;
	}
	
	/**
	 * Method to cancel any edits made but not yet saved
	 * or unsaved requirement creation
	 * 
	 * @e any ActionEvent object
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.parentView.getMainTabController().closeCurrentTab();
	}

}
