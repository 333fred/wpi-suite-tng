/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview;

import javax.swing.JPanel;

/**
 * @author Alex
 *
 */
public class MainView extends JPanel {

	/** The panel containing the post board */
	private DetailView detailView;
	
	/**
	 * Construct the panel.
	 * @param boardModel 
	 */
	public MainView(DetailView detailView) {
		// Add the board panel to this view
		detailView = new DetailView(detailView);
		add(detailView);
	}
}
