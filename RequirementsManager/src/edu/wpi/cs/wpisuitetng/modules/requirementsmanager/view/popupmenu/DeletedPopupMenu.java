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
 * Popup menu for the deleted iteration. It displays options for viewing only
 * deletec requirements in the requirements table view
 * 
 * @author Alex C
 */
@SuppressWarnings ("serial")
public class DeletedPopupMenu extends JPopupMenu implements ActionListener {
	
	private final JMenuItem menuFilterDeleted;
	
	/**
	 * Creates a DeletedPopupMenu with the given tab controller
	 * 
	 * @param tabController
	 *            The tab controller to open tabs in
	 */
	public DeletedPopupMenu(final MainTabController tabController) {
		menuFilterDeleted = new JMenuItem("View Table By Deleted");
		
		menuFilterDeleted.addActionListener(this);
		
		add(menuFilterDeleted);
		
	}
	
	/**
	 * The action listener is called when the user selects a menu option
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource().equals(menuFilterDeleted)) {
			Iteration iter;
			try {
				// deleted has ID -2
				iter = IterationDatabase.getInstance().get(-2);
				final RequirementTableView tableView = RequirementTableView
						.getInstance();
				tableView.IterationFilter(iter.getName());
				tableView.displayFilterInformation("Viewing by "
						+ iter.getName());
			} catch (final IterationNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
}
