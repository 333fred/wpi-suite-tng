/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * @author Chris
 *
 */
public class CancelAction extends AbstractAction {

	private Requirement requirement;
	private DetailPanel parentView;

	CancelAction(Requirement requirement, DetailPanel parentView) {
		super("Cancel");
		this.requirement = requirement;
		this.parentView = parentView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.parentView.getMainTabController().closeCurrentTab();
	}

}
