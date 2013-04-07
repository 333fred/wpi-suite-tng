package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.popupmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

public class BacklogPopupMenu extends JPopupMenu implements ActionListener {

	/** Menu options for the PopupMenu */
	private JMenuItem menuCreateRequirement;
	
	/** The tab controller used to create new tabs */
	private MainTabController tabController;
	
	
	/** Creates an BacklogPopupMenu with the given tab controller
	 * 
	 * @param tabController The tab controller to open tabs in
	 */
	
	public BacklogPopupMenu(MainTabController tabController) {
		this.tabController = tabController;

		menuCreateRequirement = new JMenuItem("New Requirement");		

		menuCreateRequirement.addActionListener(this);
		
		add(menuCreateRequirement);			
	

	}
	
	/** The action listener that is called when the user selects a menu option
	 * 
	 */
	
	public void actionPerformed(ActionEvent e) {
		tabController.addCreateRequirementTab();
	}
	
}
