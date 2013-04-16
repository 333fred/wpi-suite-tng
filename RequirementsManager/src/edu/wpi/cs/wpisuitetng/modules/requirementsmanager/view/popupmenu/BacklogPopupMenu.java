package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.popupmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

public class BacklogPopupMenu extends JPopupMenu implements ActionListener {

	/** Menu options for the PopupMenu */
	private JMenuItem menuCreateRequirement;
	private JMenuItem menuFilterIteration;

	/** The tab controller used to create new tabs */
	private MainTabController tabController;
	
	/** The iterations that were selected when this was pressed */
	private List<Iteration> selectedIterations;

	/**
	 * Creates an BacklogPopupMenu with the given tab controller
	 * 
	 * @param tabController
	 *            The tab controller to open tabs in
	 */

	public BacklogPopupMenu(MainTabController tabController,
			List<Iteration> selectedIterations) {
		this.tabController = tabController;
		this.selectedIterations = selectedIterations;

		menuCreateRequirement = new JMenuItem("New Requirement");
		menuFilterIteration = new JMenuItem("Filter By Backlog");
		
		menuCreateRequirement.addActionListener(this);
		menuFilterIteration.addActionListener(this);
	
		add(menuCreateRequirement);
		add(menuFilterIteration);

	}

	/**
	 * The action listener that is called when the user selects a menu option
	 * 
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(menuFilterIteration)) {
			Iteration iter = selectedIterations.get(0);
			RequirementTableView tableView = RequirementTableView.getInstance();
			tableView.IterationFilter(iter.getName());
			tableView.displayFilterInformation("Filtering by " + iter.getName());
		} else {
			tabController.addCreateRequirementTab();
		}
	}

}
