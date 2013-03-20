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

/**
 * Action that calls {@link MainTabController#addCreateDefectTab()}, default mnemonic key is C. 
 */
@SuppressWarnings("serial")
public class RefreshAction extends AbstractAction {

	private final MainTabController controller;
	
	/**
	 * Create a CreateDefectAction
	 * @param controller When the action is performed, controller.addCreateDefectTab() is called
	 */
	public RefreshAction(MainTabController controller) {
		super("Refresh");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_C);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO: implement code for actually refreshing the set of requirements
	}

}
