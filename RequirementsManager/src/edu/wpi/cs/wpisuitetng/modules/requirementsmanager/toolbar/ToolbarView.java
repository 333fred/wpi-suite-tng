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
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.JPlaceholderTextField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/**
 * The Defect tab's toolbar panel.
 * Always has a group of global commands (Create Defect, Search).
 */
@SuppressWarnings("serial")
public class ToolbarView extends DefaultToolbarView {

	private JButton createRequirement;
	private JButton viewRequirement;
	private JButton refresh;
	// private JPlaceholderTextField searchField;
	
	/**
	 * Create a ToolbarView.
	 * @param tabController The MainTabController this view should open tabs with
	 */
	public ToolbarView(MainTabController tabController) {

		// Construct the content panel
		JPanel content = new JPanel();
		SpringLayout layout  = new SpringLayout();
		content.setLayout(layout);
		content.setOpaque(false);
				
		// Construct the create defect button
		createRequirement = new JButton("Create Requirement");
		createRequirement.setAction(new CreateRequirementAction(tabController));
		
		// Construct the search button
		viewRequirement = new JButton("View Requirement");
		viewRequirement.setAction(new ViewRequirementAction(tabController));
		
		// Construct the refresh button
		refresh = new JButton("Refresh");
		refresh.setAction(new RefreshAction(tabController));
		
		// Construct the search field
		// searchField = new JPlaceholderTextField("Lookup by ID", 15);
		// searchField.addActionListener(new LookupDefectController(tabController, searchField, this));
		
		// Configure the layout of the buttons on the content panel
		layout.putConstraint(SpringLayout.NORTH, createRequirement, 5, SpringLayout.NORTH, content);
		layout.putConstraint(SpringLayout.WEST, createRequirement, 8, SpringLayout.WEST, content);
		layout.putConstraint(SpringLayout.WEST, viewRequirement, 10, SpringLayout.EAST, createRequirement);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, viewRequirement, 0, SpringLayout.VERTICAL_CENTER, createRequirement);
		// TODO: place location constraints on Refresh button
		// layout.putConstraint(SpringLayout.NORTH, searchField, 15, SpringLayout.SOUTH, createRequirement);
		// layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, searchField, 5, SpringLayout.EAST, createRequirement);
		
		// Add buttons to the content panel
		content.add(createRequirement);
		content.add(viewRequirement);
		content.add(refresh);
		//content.add(searchField);
		
		// Construct a new toolbar group to be added to the end of the toolbar
		ToolbarGroupView toolbarGroup = new ToolbarGroupView("Home", content);
		
		// Calculate the width of the toolbar
		Double toolbarGroupWidth = createRequirement.getPreferredSize().getWidth() + viewRequirement.getPreferredSize().getWidth() + 40; // 40 accounts for margins between the buttons
		toolbarGroup.setPreferredWidth(toolbarGroupWidth.intValue());
		addGroup(toolbarGroup);
	}

}
