/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		@author Alex Gorowara, Alex Woodyard
 ********************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.network.Network;

/**
 * The Requirement tab's toolbar panel. Always has a group of global commands
 * (Create Requirement, Search).
 */
@SuppressWarnings("serial")
public class ToolbarView extends DefaultToolbarView {

	private JButton createHelp;

	/** Button for creating a requirement */
	private JButton createRequirement;

	/** Button for creating iteration */
	private JButton createIteration;

	/** Button for creating statistics panel */
	private JButton createStatistics;

	/** Button for creating a permissions panel */
	private JButton createPermissions;

	/**
	 * Create a ToolbarView.
	 * 
	 * @param tabController
	 *            The MainTabController this view should open tabs with
	 */
	public ToolbarView(MainTabController tabController) {
		// Note: User Manual button is being deprecated in favor of it's own
		// toolbar
		// Construct the content panel
		JPanel content = new JPanel();
		JPanel resourcePanel = new JPanel();
		JPanel permissionPanel = new JPanel();
		SpringLayout layout = new SpringLayout();
		SpringLayout resourceLayout = new SpringLayout();
		SpringLayout permissionLayout = new SpringLayout();
		content.setLayout(layout);
		content.setOpaque(false);
		resourcePanel.setLayout(resourceLayout);
		resourcePanel.setOpaque(false);
		permissionPanel.setLayout(permissionLayout);
		permissionPanel.setOpaque(false);

		// Construct the create Requirement button
		createRequirement = new JButton("Create Requirement");
		createRequirement.setAction(new CreateRequirementAction(tabController));

		createIteration = new JButton("Create Iteration");
		createIteration.setAction(new CreateIterationAction(tabController));

		// Construct the User Manual button
		createHelp = new JButton("User Manual");
		createHelp.setAction(new CreateHelpPanelAction(tabController));

		// Construct the Stat button
		createStatistics = new JButton("Show Statistics");
		createStatistics.setAction(new CreateStatPanelAction(tabController));

		// Construct the permissions button
		createPermissions = new JButton("Show Permissions");
		createPermissions.setAction(new CreatePermissionPanelAction(
				tabController));

		// Construct the search field
		// searchField = new JPlaceholderTextField("Lookup by ID", 15);
		// searchField.addActionListener(new
		// LookupRequirementController(tabController, searchField, this));

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
		/*
		 * layout.putConstraint(SpringLayout.WEST, createHelpPanel, 8,
		 * SpringLayout.WEST, content); layout.putConstraint(SpringLayout.SOUTH,
		 * createHelpPanel, 17, SpringLayout.SOUTH, createIteration);
		 */

		// Add buttons to the content panel
		// content.add(createIteration);
		content.add(createRequirement);
		// content.add(createHelpPanel);
		content.add(createIteration);

		resourcePanel.add(createHelp);
		resourcePanel.add(createStatistics);

		// content.add(searchField);

		// create objects for user permission panel
		JLabel nameLabel = new JLabel();
		JLabel userName = new JLabel();
		JLabel permissionLabel = new JLabel();
		JLabel userLevel = new JLabel();

		nameLabel.setText("The current user is: ");
		userName.setText(ConfigManager.getConfig().getUserName());
		permissionLabel.setText("your current permission is: ");
		userLevel.setText(PermissionModel.getInstance().getPermission()
				.toString());

		// stack all the labels on top of each other
		permissionLayout.putConstraint(SpringLayout.NORTH, nameLabel, 3,
				SpringLayout.NORTH, permissionPanel);
		permissionLayout.putConstraint(SpringLayout.NORTH, userName, 3,
				SpringLayout.SOUTH, nameLabel);
		permissionLayout.putConstraint(SpringLayout.NORTH, permissionLabel, 3,
				SpringLayout.SOUTH, userName);
		permissionLayout.putConstraint(SpringLayout.NORTH, userLevel, 3,
				SpringLayout.SOUTH, permissionLabel);
		permissionLayout.putConstraint(SpringLayout.NORTH, createPermissions,
				3, SpringLayout.SOUTH, userLevel);

		// center everything horizontally
		permissionLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
				nameLabel, 0, SpringLayout.HORIZONTAL_CENTER, permissionPanel);
		permissionLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
				userName, 0, SpringLayout.HORIZONTAL_CENTER, permissionPanel);
		permissionLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
				userLevel, 0, SpringLayout.HORIZONTAL_CENTER, permissionPanel);
		permissionLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
				permissionLabel, 0, SpringLayout.HORIZONTAL_CENTER, permissionPanel);
		permissionLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
				createPermissions, 0, SpringLayout.HORIZONTAL_CENTER, permissionPanel);
		
		// match everything to nameLabel width
		permissionLayout.putConstraint(SpringLayout.EAST, nameLabel, 0,
				SpringLayout.EAST, permissionLabel);
		permissionLayout.putConstraint(SpringLayout.WEST, nameLabel, 0,
				SpringLayout.WEST, permissionLabel);
		permissionLayout.putConstraint(SpringLayout.EAST, userName, 0,
				SpringLayout.EAST, permissionLabel);
		permissionLayout.putConstraint(SpringLayout.WEST, userName, 0,
				SpringLayout.WEST, permissionLabel);
		permissionLayout.putConstraint(SpringLayout.EAST, userLevel, 0,
				SpringLayout.EAST, permissionLabel);
		permissionLayout.putConstraint(SpringLayout.WEST, userLevel, 0,
				SpringLayout.WEST, permissionLabel);
		permissionLayout.putConstraint(SpringLayout.EAST, createPermissions, 0,
				SpringLayout.EAST, permissionLabel);
		permissionLayout.putConstraint(SpringLayout.WEST, createPermissions, 0,
				SpringLayout.WEST, permissionLabel);

		permissionPanel.add(nameLabel);
		permissionPanel.add(userName);
		permissionPanel.add(permissionLabel);
		permissionPanel.add(userLevel);
		permissionPanel.add(createPermissions);

		// Construct a new toolbar group to be added to the end of the toolbar
		ToolbarGroupView toolbarGroup = new ToolbarGroupView("Home", content);
		ToolbarGroupView helpToolbar = new ToolbarGroupView("Resources",
				resourcePanel);
		ToolbarGroupView permissionToolbar = new ToolbarGroupView(
				"Permissions", permissionPanel);

		// Calculate the width of the toolbar
		Double toolbarGroupWidth = Math.max(createRequirement
				.getPreferredSize().getWidth() + 40,
		// 40 accounts for margins between the buttons
				permissionLabel.getPreferredSize().getWidth() + 10);

		toolbarGroup.setPreferredWidth(toolbarGroupWidth.intValue());
		addGroup(toolbarGroup);
		helpToolbar.setPreferredWidth(toolbarGroupWidth.intValue());
		addGroup(helpToolbar);
		permissionToolbar.setPreferredWidth(toolbarGroupWidth.intValue());
		addGroup(permissionToolbar);

		// This is the help toolbar
		ToolbarGroupView toolbarView = new ToolbarGroupView("Help");
		JButton butHelp = new JButton("User Manual");
		butHelp.setAction(new CreateHelpPanelAction(tabController));
		// Create and add the buttons that will be displayed in the help
		toolbarView.getContent().add(butHelp);
		// Set the width of the group so it is not too long
		toolbarView.setPreferredWidth((int) (butHelp.getPreferredSize()
				.getWidth() + 40));
		// Calculate the width of the toolbar
		toolbarGroup.setPreferredWidth(toolbarGroupWidth.intValue());
		// addGroup(toolbarView);

	}
}
