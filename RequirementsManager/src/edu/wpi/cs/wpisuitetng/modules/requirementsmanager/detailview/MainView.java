/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.postboard.model.PostBoardModel;
import edu.wpi.cs.wpisuitetng.modules.postboard.view.BoardPanel;

/**
 * @author Alex
 *
 */
public class MainView extends JPanel {

	/** The panel containing the post board */
	private final DetailView detailView;
	
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
