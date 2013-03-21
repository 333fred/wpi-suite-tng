/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * @author Alex
 *
 */
public class MainView extends JPanel {

	Requirement requirement;
	
	/** The panel containing the detail view */
	private DetailPanel detailView;
	
	/**
	 * Construct the panel.
	 * @param boardModel 
	 */
	public MainView(DetailPanel detailView,Requirement requirement) {
		this.requirement = requirement;
		// Add the board panel to this view
		detailView = new DetailPanel(requirement);
		add(detailView);
	}
}
