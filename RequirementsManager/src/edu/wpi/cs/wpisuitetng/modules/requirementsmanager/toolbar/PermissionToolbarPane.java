/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric, Alex Woodyard, Conor Geary
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.PermissionModelController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.PermissionsDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllPermissionsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

public class PermissionToolbarPane extends JPanel {

	private static PermissionToolbarPane singleton;

	public static PermissionToolbarPane createSingleton(
			MainTabController tabController) {
		if (singleton == null) {
			singleton = new PermissionToolbarPane(tabController);
		}
		return singleton;
	}

	public static PermissionToolbarPane getInstance() {
		return singleton;
	}

	/** Button for creating a permissions panel */
	private JButton createPermissions;

	private JLabel nameLabel;
	private JLabel userName;
	private JLabel permissionLabel;
	private JLabel userLevel;

	/**
	 * Default constructor that creates the pane for the toolbar
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
		if (PermissionModel.getInstance().getPermLevel() == UserPermissionLevel.ADMIN) {
			createPermissions.setEnabled(true);
			PermissionsDatabase.getInstance().start();
		} else {
			createPermissions.setEnabled(false);
		}
		User usr = PermissionModel.getInstance().getUser();

		nameLabel.setText("Current User: ");
		if (!(usr == null)) {
			if (!(usr.getName() == null)) {
				userName.setText(usr.getName());
			}
		} else {
			userName.setText("User is NULL");
		}
		permissionLabel.setText("Permission: ");
		String permLevel = PermissionModel.getInstance().getPermLevel().toString();
		userLevel.setText(permLevel.substring(0, 1).concat(
				permLevel.substring(1).toLowerCase()));

		// stack all the labels on top of each other
		permissionLayout.putConstraint(SpringLayout.NORTH, nameLabel, 3,
				SpringLayout.NORTH, this);
		permissionLayout.putConstraint(SpringLayout.NORTH, userName, 3,
				SpringLayout.NORTH, this);
		permissionLayout.putConstraint(SpringLayout.NORTH, permissionLabel, 3,
				SpringLayout.SOUTH, nameLabel);
		permissionLayout.putConstraint(SpringLayout.NORTH, userLevel, 3,
				SpringLayout.SOUTH, userName);
		permissionLayout.putConstraint(SpringLayout.NORTH, createPermissions,
				3, SpringLayout.SOUTH, userLevel);

		// center everything horizontally
		permissionLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
				createPermissions, 0, SpringLayout.HORIZONTAL_CENTER, this);

		permissionLayout.putConstraint(SpringLayout.WEST, nameLabel, 8,
				SpringLayout.WEST, this);
		permissionLayout.putConstraint(SpringLayout.EAST, nameLabel, 0,
				SpringLayout.WEST, userName);
		permissionLayout.putConstraint(SpringLayout.EAST, userName, -8,
				SpringLayout.EAST, this);
		permissionLayout.putConstraint(SpringLayout.WEST, permissionLabel, 8,
				SpringLayout.WEST, this);
		permissionLayout.putConstraint(SpringLayout.EAST, permissionLabel, 0,
				SpringLayout.WEST, userLevel);
		permissionLayout.putConstraint(SpringLayout.EAST, userLevel, -8,
				SpringLayout.EAST, this);

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
		userLevel.setText(PermissionModel.getInstance().getPermLevel()
				.toString());
		if (PermissionModel.getInstance().getPermLevel() == UserPermissionLevel.ADMIN) {
			createPermissions.setEnabled(true);
			RetrieveAllPermissionsRequestObserver observer = new RetrieveAllPermissionsRequestObserver();
			PermissionModelController controller = new PermissionModelController();
			controller.getAll(observer);

		} else {
			createPermissions.setEnabled(false);
		}
		User usr = PermissionModel.getInstance().getUser();
		if (!(usr == null)) {
			if (!(usr.getName() == null)) {
				userName.setText(usr.getName());
			}
		} else
			userName.setText("NULL");
		String permLevel = PermissionModel.getInstance().getPermLevel().toString();
		userLevel.setText(permLevel.substring(0, 1).concat(
				permLevel.substring(1).toLowerCase()));
	}

	/**
	 * 
	 * @return The width of the widest label
	 */
	public double getLabelWidth() {
		return Math.max(
				(permissionLabel.getPreferredSize().getWidth() + userLevel
						.getPreferredSize().getWidth()), (nameLabel
						.getPreferredSize().getWidth() + userName
						.getPreferredSize().getWidth()));
	}

}
