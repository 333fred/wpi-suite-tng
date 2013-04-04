package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationTreeView;


public class EditIterationAction extends AbstractAction {

	/** The iteration view to receive the iteration from */
	private IterationTreeView iterationTreeView;
	
	/** The tab controller to open the new tab in */
	private MainTabController tabController;
	
	public EditIterationAction(IterationTreeView iterationTreeView, MainTabController tabController) {
		this.iterationTreeView = iterationTreeView;
		this.tabController = tabController;
	}	


	/** Opens the currently selected iterations in the IterationTreeView */
	@Override
	public void actionPerformed(ActionEvent e) {
		Iteration[] iterationsToOpen = iterationTreeView.getSelectedIterations();
		for (Iteration i : iterationsToOpen) {
			if (i == null || i.getName().equals("Backlog")) continue;
			tabController.addEditIterationTab(i);
		}
		
	}
}
