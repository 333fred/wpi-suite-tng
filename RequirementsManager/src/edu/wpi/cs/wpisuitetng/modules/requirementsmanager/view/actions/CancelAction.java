/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * @author Chris
 *
 * Action which concerns canceling the creation/editing of a requirement
 */
public class CancelAction extends AbstractAction {


	private DetailPanel parentView;

	public CancelAction(DetailPanel parentView) {
		super("Cancel");
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
