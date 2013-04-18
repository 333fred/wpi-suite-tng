/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevels;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.PermissionsDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

public class PermissionToolbarPane extends JPanel {

	private static PermissionToolbarPane singleton;
	
	public static PermissionToolbarPane createSingleton(MainTabController tabController) {
		if (singleton == null) {
			singleton = new PermissionToolbarPane(tabController);
		}
		return singleton;
	}
	
	public static PermissionToolbarPane getInstance(){
		return singleton;
	}
	
	/** Button for creating a permissions panel */
	private JButton createPermissions;

	private JLabel nameLabel;
	private JLabel userName;
	private JLabel permissionLabel;
	private JLabel userLevel;

	/**
	 * 
	 */
	public PermissionToolbarPane(MainTabController tabController) {

		SpringLayout permissionLayout = new SpringLayout();
		this.setLayout(permissionLayout);
		this.setOpaque(false);

		// create objects for user permission panel
		nameLabel = new JLabel();
		userName = new JLabel();
		permissionLabel = new JLabel();
		userLevel = new JLabel();

		// Construct the permissions button
		createPermissions = new JButton("Show Permissions");
		createPermissions.setAction(new CreatePermissionPanelAction(
				tabController));
		if (PermissionModel.getInstance().getPermission() == UserPermissionLevels.ADMIN) {
			createPermissions.setEnabled(true);
			PermissionsDatabase.getInstance().start();
		} else {
			createPermissions.setEnabled(false);
		}
		User usr = PermissionModel.getInstance().getUser();
		
		nameLabel.setText("The current user is: ");
		if(!(usr == null)) {
			if(!(usr.getName() == null)) {
				userName.setText(usr.getName()); }}
		else
			userName.setText("User is NULL");
		permissionLabel.setText("your current permission is: ");
		userLevel.setText(PermissionModel.getInstance().getPermission()
				.toString());

		// stack all the labels on top of each other
		permissionLayout.putConstraint(SpringLayout.NORTH, nameLabel, 3,
				SpringLayout.NORTH, this);
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
				nameLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
		permissionLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
				userName, 0, SpringLayout.HORIZONTAL_CENTER, this);
		permissionLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
				userLevel, 0, SpringLayout.HORIZONTAL_CENTER, this);
		permissionLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
				permissionLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
		permissionLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
				createPermissions, 0, SpringLayout.HORIZONTAL_CENTER, this);

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

		this.add(nameLabel);
		this.add(userName);
		this.add(permissionLabel);
		this.add(userLevel);
		this.add(createPermissions);
	}

	/**
	 * Updates the status bar with the current permission
	 */
	public void refreshPermission() {
		userLevel.setText(PermissionModel.getInstance().getPermission()
				.toString());
		if (PermissionModel.getInstance().getPermission() == UserPermissionLevels.ADMIN) {
			createPermissions.setEnabled(true);
			if(!(PermissionsDatabase.getInstance().isAlive()))
					PermissionsDatabase.getInstance().start();
		} else {
			createPermissions.setEnabled(false);
			PermissionsDatabase.getInstance().interrupt();
		}
		User usr = PermissionModel.getInstance().getUser();
		if(!(usr == null)) {
			if(!(usr.getName() == null)) {
				userName.setText(usr.getName()); }}
		else
			userName.setText("User is NULL");
		userLevel.setText(PermissionModel.getInstance().getPermission()
				.toString());
	}

	public double getLabelWidth() {
		return permissionLabel.getPreferredSize().getWidth();
	}

}
