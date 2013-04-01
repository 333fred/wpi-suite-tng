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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/**
 * The Requirement tab's toolbar panel.
 * Always has a group of global commands (Create Requirement, Search).
 */
@SuppressWarnings("serial")
public class ToolbarView extends DefaultToolbarView {

	private JButton createHelpPanel;

	/** Button for creating a requirement */
	private JButton createRequirement;
	
	/** Button for creating iteration */
	private JButton createIteration;


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
				
		// Construct the create Requirement button
		createRequirement = new JButton("Create Requirement");
		createRequirement.setAction(new CreateRequirementAction(tabController));
		
		createIteration = new JButton("Create Iteration");
		createIteration.setAction(new CreateIterationAction(tabController));

		// Construct the User Manual button
		createHelpPanel = new JButton("?");
	    createHelpPanel.setAction(new CreateHelpPanelAction(tabController));
		
		// Construct the search field
		// searchField = new JPlaceholderTextField("Lookup by ID", 15);
		// searchField.addActionListener(new LookupRequirementController(tabController, searchField, this));
		
		// Configure the layout of the buttons on the content panel
		layout.putConstraint(SpringLayout.NORTH, createRequirement, 5, SpringLayout.NORTH, content);
		layout.putConstraint(SpringLayout.WEST, createRequirement, 8, SpringLayout.WEST, content);
		
		layout.putConstraint(SpringLayout.NORTH, createIteration, 5, SpringLayout.SOUTH, createRequirement);
		layout.putConstraint(SpringLayout.WEST, createIteration, 8, SpringLayout.WEST, content);
		layout.putConstraint(SpringLayout.EAST, createIteration, 0, SpringLayout.EAST, createRequirement);
		
		layout.putConstraint(SpringLayout.NORTH, createHelpPanel, 5, SpringLayout.SOUTH, createIteration);
		layout.putConstraint(SpringLayout.WEST, createHelpPanel, 8, SpringLayout.WEST, content);
		layout.putConstraint(SpringLayout.SOUTH, createHelpPanel, 17, SpringLayout.SOUTH, createIteration);
		
		// Add buttons to the content panel
		content.add(createIteration);
		content.add(createRequirement);;
		content.add(createHelpPanel);;

		//content.add(searchField);
		
		// Construct a new toolbar group to be added to the end of the toolbar
		ToolbarGroupView toolbarGroup = new ToolbarGroupView("Home", content);
		
		// Calculate the width of the toolbar
		Double toolbarGroupWidth = createRequirement.getPreferredSize().getWidth() + 40; // 40 accounts for margins between the buttons
		toolbarGroup.setPreferredWidth(toolbarGroupWidth.intValue());
		addGroup(toolbarGroup);
	}

}
