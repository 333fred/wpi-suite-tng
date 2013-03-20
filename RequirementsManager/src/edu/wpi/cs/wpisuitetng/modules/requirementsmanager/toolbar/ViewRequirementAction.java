/**
 * Action invoked upon use of the View Requirement key
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

/**
 * Action that calls {@link MainTabController#addCreateDefectTab()}, default mnemonic key is C. 
 */
@SuppressWarnings("serial")
public class ViewRequirementAction extends AbstractAction {

	private final MainTabController controller;
	
	/**
	 * Create a CreateDefectAction
	 * @param controller When the action is performed, controller.addCreateDefectTab() is called
	 */
	public ViewRequirementAction(MainTabController controller) {
		super("View Requirement");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_C);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO: replace the parameter with the currently selected requirement
		controller.addViewRequirementTab(null);
	}

}
