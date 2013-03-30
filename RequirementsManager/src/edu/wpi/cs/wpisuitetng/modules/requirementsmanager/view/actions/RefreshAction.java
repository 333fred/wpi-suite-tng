/**
 * Action invoked upon use of the Refresh key
 * Heavily adapted from CreateDefectAction in the DefectTracker module
 * 
 * @author Alex Gorowara 
 * 
 */

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * Action that calls {@link MainTabController#addCreateRequirementTab()}, default mnemonic key is C. 
 */
@SuppressWarnings("serial")
public class RefreshAction extends AbstractAction {

	private final RequirementTableView requirementList;
	
	/**
	 * Create a RefreshAction object which operates on a specific RequirementListView
	 * @param RequirementTableView the current view, which is to be refreshed
	 * 
	 */
	public RefreshAction(RequirementTableView requirementList) {
		super("Refresh");
		this.requirementList = requirementList;
		putValue(MNEMONIC_KEY, KeyEvent.VK_F5);
	}
	
	/**
	 * Method to refresh the view upon receiving any ActionEvent
	 * @e any ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		requirementList.refresh();
	}

}
