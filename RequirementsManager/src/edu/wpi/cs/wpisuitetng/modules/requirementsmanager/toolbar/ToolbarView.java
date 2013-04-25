/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		@author Alex Gorowara
 *		@author Alex Woodyard
 ********************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/**
 * The Requirement tab's toolbar panel. Always has a group of global commands
 * (Create Requirement, Search).
 */
@SuppressWarnings ("serial")
public class ToolbarView extends DefaultToolbarView {
	
	/** The singleton instance of this class */
	private static ToolbarView _instance;
	
	/**
	 * Returns the instance of the toolbar view, if tab controller was not set,
	 * returns null
	 * 
	 * @return the ToolbarView instance
	 */
	
	public static ToolbarView getInstance() {
		return ToolbarView._instance;
	}
	
	/**
	 * Returns the instance of this toolbar view, and sets the tab controller
	 * 
	 * @param tabController
	 * @return the ToolbarView instance
	 */
	
	public static ToolbarView getInstance(final MainTabController tabController) {
		if (ToolbarView._instance == null) {
			ToolbarView._instance = new ToolbarView(tabController);
		}
		return ToolbarView._instance;
	}
	
	private final JButton createHelp;
	
	/** Button for creating a requirement */
	private final JButton createRequirement;
	
	/** Button for creating iteration */
	private final JButton createIteration;
	
	/** Button for creating statistics panel */
	private final JButton createStatistics;
	
	/**
	 * Create a ToolbarView.
	 * 
	 * @param tabController
	 *            The MainTabController this view should open tabs with
	 */
	private ToolbarView(final MainTabController tabController) {
		
		// Note: User Manual button is being deprecated in favor of it's own
		// toolbar
		// Construct the content panel
		final JPanel content = new JPanel();
		final JPanel resourcePanel = new JPanel();
		final PermissionToolbarPane permissionPanel = PermissionToolbarPane
				.createSingleton(tabController);
		final SpringLayout layout = new SpringLayout();
		final SpringLayout resourceLayout = new SpringLayout();
		content.setLayout(layout);
		content.setOpaque(false);
		resourcePanel.setLayout(resourceLayout);
		resourcePanel.setOpaque(false);
		
		// Construct the create Requirement button
		createRequirement = new JButton("Create Requirement");
		createRequirement.setAction(new CreateRequirementAction(tabController));
		createRequirement.setEnabled(PermissionModel.getInstance()
				.getUserPermissions().canCreateRequirement());
		
		createIteration = new JButton("Create Iteration");
		createIteration.setAction(new CreateIterationAction(tabController));
		createIteration.setEnabled(PermissionModel.getInstance()
				.getUserPermissions().canCreateIteration());
		
		// Construct the User Manual button
		createHelp = new JButton("User Manual");
		createHelp.setAction(new CreateHelpPanelAction());
		
		// Construct the Stat button
		createStatistics = new JButton("Show Statistics");
		createStatistics.setAction(new CreateStatPanelAction(tabController));
		
		// Configure the layout of the buttons on the content panel
		layout.putConstraint(SpringLayout.NORTH, createRequirement, 5,
				SpringLayout.NORTH, content);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, createRequirement,
				0, SpringLayout.HORIZONTAL_CENTER, content);
		
		layout.putConstraint(SpringLayout.NORTH, createIteration, 10,
				SpringLayout.SOUTH, createRequirement);
		layout.putConstraint(SpringLayout.WEST, createIteration, 0,
				SpringLayout.WEST, createRequirement);
		layout.putConstraint(SpringLayout.EAST, createIteration, 0,
				SpringLayout.EAST, createRequirement);
		
		resourceLayout.putConstraint(SpringLayout.NORTH, createHelp, 5,
				SpringLayout.NORTH, resourcePanel);
		resourceLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
				createHelp, 0, SpringLayout.HORIZONTAL_CENTER, resourcePanel);
		
		resourceLayout.putConstraint(SpringLayout.NORTH, createStatistics, 10,
				SpringLayout.SOUTH, createHelp);
		resourceLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
				createStatistics, 0, SpringLayout.HORIZONTAL_CENTER,
				resourcePanel);
		
		resourceLayout.putConstraint(SpringLayout.WEST, createHelp, 0,
				SpringLayout.WEST, createStatistics);
		resourceLayout.putConstraint(SpringLayout.EAST, createHelp, 0,
				SpringLayout.EAST, createStatistics);
		
		// Add buttons to the content panel
		content.add(createRequirement);
		content.add(createIteration);
		
		resourcePanel.add(createHelp);
		resourcePanel.add(createStatistics);
		
		// Construct a new toolbar group to be added to the end of the toolbar
		final ToolbarGroupView toolbarGroup = new ToolbarGroupView("Home",
				content);
		final ToolbarGroupView helpToolbar = new ToolbarGroupView("Resources",
				resourcePanel);
		final ToolbarGroupView permissionToolbar = new ToolbarGroupView(
				"Permissions", permissionPanel);
		
		// Calculate the width of the toolbar
		final Double toolbarGroupWidth = Math.max(createRequirement
				.getPreferredSize().getWidth() + 40,
		// 40 accounts for margins between the buttons
				permissionPanel.getLabelWidth() + 10);
		
		toolbarGroup.setPreferredWidth(toolbarGroupWidth.intValue());
		addGroup(toolbarGroup);
		helpToolbar.setPreferredWidth(toolbarGroupWidth.intValue());
		addGroup(helpToolbar);
		permissionToolbar.setPreferredWidth(toolbarGroupWidth.intValue());
		addGroup(permissionToolbar);
		
		// This is the help toolbar
		final ToolbarGroupView toolbarView = new ToolbarGroupView("Help");
		final JButton butHelp = new JButton("User Manual");
		butHelp.setAction(new CreateHelpPanelAction());
		// Create and add the buttons that will be displayed in the help
		toolbarView.getContent().add(butHelp);
		// Set the width of the group so it is not too long
		toolbarView.setPreferredWidth((int) (butHelp.getPreferredSize()
				.getWidth() + 40));
		// Calculate the width of the toolbar
		toolbarGroup.setPreferredWidth(toolbarGroupWidth.intValue());
		
	}
	
	/**
	 * Refreshes the user permissions
	 * 
	 */
	
	public void refreshPermissions() {
		createRequirement.setEnabled(PermissionModel.getInstance()
				.getUserPermissions().canCreateRequirement());
		createIteration.setEnabled(PermissionModel.getInstance()
				.getUserPermissions().canCreateIteration());
	}
}
