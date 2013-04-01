/**
 * Class for viewing a toolbar
 * 
 * Adapted from ToolbarView in the DefectModule
 * 
 * @author Alex
 *
 */

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.FocusableTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import java.awt.BorderLayout;

/**
 * @author Nick Massa
 * The Help Panel on the top right. CURRENTLY NOT IMPLEMENTED. DONE AS NEXT TO HOME IN TOOLBARVIEW
 */
@SuppressWarnings("serial")
public class HelpPanelView extends FocusableTab implements IToolbarGroupProvider {
	
	ToolbarGroupView toolbarView;
	JButton butHelp;
	ToolbarGroupView toolbarGroup;
	private JButton createHelpPanel;
	
	/**
	 * Create a ToolbarView.
	 * @param tabController The MainTabController this view should open tabs with
	 */
	public HelpPanelView(MainTabController tabController) {

		toolbarView = new ToolbarGroupView("Help");
		butHelp = new JButton("User Manual");

		butHelp.setAction(new CreateHelpPanelAction(tabController));
		// create and add the buttons that will be displayed
		toolbarView.getContent().add(butHelp);
		// set the width of the group so it is not too long
		toolbarView.setPreferredWidth((int) (butHelp.getPreferredSize()
				.getWidth() + butHelp.getPreferredSize().getWidth() + 40));
		
		// Calculate the width of the toolbar
		Double toolbarGroupWidth = createHelpPanel.getPreferredSize().getWidth() + 40; // 40 accounts for margins between the buttons
		
		toolbarGroup.setPreferredWidth(toolbarGroupWidth.intValue());
	}
	
	@Override
	public ToolbarGroupView getGroup() {
		return toolbarView;
	}

}
