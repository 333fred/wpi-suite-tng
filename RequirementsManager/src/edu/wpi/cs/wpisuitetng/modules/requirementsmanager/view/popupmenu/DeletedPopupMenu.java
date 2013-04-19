/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.popupmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * @author Alex C
 *
 */
public class DeletedPopupMenu extends JPopupMenu implements ActionListener {

	private JMenuItem menuFilterDeleted;

	/** The tab controller used to create new tabs */
	private MainTabController tabController;
		
	/**
	 * Creates a DeletedPopupMenu with the given tab controller
	 * 
	 * @param tabController 
	 * 		The tab controller to open tabs in
	 */
	public DeletedPopupMenu(MainTabController tabController) {
		this.tabController = tabController;

		menuFilterDeleted = new JMenuItem("Filter By Deleted");
		
		menuFilterDeleted.addActionListener(this);

		add(menuFilterDeleted);

	}
	
	/**
	 * The action listener is called when the user selects a menu option
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(menuFilterDeleted)) {
			Iteration iter;
			try {
				// deleted has ID -2
				iter = IterationDatabase.getInstance().get(-2);
				RequirementTableView tableView = RequirementTableView.getInstance();
				tableView.IterationFilter(iter.getName());
				tableView.displayFilterInformation("Filtering by " + iter.getName());
			} catch (IterationNotFoundException e1) {
				e1.printStackTrace();
			} 
		}		
	}
}
