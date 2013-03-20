/**
 * Action invoked upon use of the Refresh key
 * Heavily adapted from CreateDefectAction in the DefectTracker module
 * 
 * @author Alex Gorowara 
 * 
 */

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementListView;

/**
 * Action that calls {@link MainTabController#addCreateDefectTab()}, default mnemonic key is C. 
 */
@SuppressWarnings("serial")
public class RefreshAction extends AbstractAction {

	private final RequirementListView requirementList;
	
	/**
	 * Create a CreateDefectAction
	 * @param controller When the action is performed, controller.addCreateDefectTab() is called
	 */
	public RefreshAction(RequirementListView requirementList) {
		super("Refresh");
		this.requirementList = requirementList;
		putValue(MNEMONIC_KEY, KeyEvent.VK_F5);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		requirementList.refresh();
	}

}
