/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Alex Woodyard
 *    @author Conor Geary
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.PermissionsDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.Tab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.PermissionSelectionChangedListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.SavePermissionsAction;

@SuppressWarnings("serial")
public class PermissionsPanel extends Tab {

	/** List of locally stored permissions */
	private List<PermissionModel> localPermissions;

	/** A list to display */
	private JList userList;

	/** A list of users to display */
	private String[] users;

	/** A button to select administrative permission level */
	private JRadioButton adminButton;

	/** A button to select update permission level */
	private JRadioButton updateButton;

	/** A button to select the minimal permission level */
	private JRadioButton noPermissionButton;

	/** A button to save changes in permissions */
	private JButton saveButton;

	@SuppressWarnings("unchecked")
	public PermissionsPanel() {
		PermissionModel model = PermissionModel.getInstance();
		SpringLayout layout = new SpringLayout();
		SpringLayout radioLayout = new SpringLayout();
		saveButton = new JButton("Save Changes");

		JPanel radioPanel = new JPanel();

		radioPanel.setLayout(radioLayout);
		setLayout(layout);
		/** Initialize the list of local permissions */
		localPermissions = PermissionsDatabase.getInstance().getAll();
		System.out.println(localPermissions.size());
		users = new String[localPermissions.size()];

		for (int i = 0; i < users.length; i++) {
			users[i] = localPermissions.get(i).getUser().getName();
		}

		// construct the list of users
		userList = new JList(users);
		//userList.setSelectedIndex(0); //default to the 
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.addListSelectionListener(new PermissionSelectionChangedListener(this));

		/** Construct the admin button */
		adminButton = new JRadioButton("Admin", false);

		/** Construct the update button */
		updateButton = new JRadioButton("Update", false);

		/** Construct the none button */
		noPermissionButton = new JRadioButton("None", false);

		JScrollPane userScroll = new JScrollPane();
		userScroll.setBorder(null);
		userScroll.getViewport().add(userList);

		// Create a group for all the buttons
		ButtonGroup group = new ButtonGroup();
		group.add(adminButton);
		group.add(updateButton);
		group.add(noPermissionButton);
		if (!userList.isSelectionEmpty()) {
			String name = (String) userList.getSelectedValue();
			for (PermissionModel mod : localPermissions) {
				if (name.equals(mod.getUser().getName())) {
					setSelectedButtons(mod.getPermLevel());
				}
			}
		}

		// set constraints for the overall panel
		layout.putConstraint(SpringLayout.WEST, userScroll, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, userScroll, 0,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, userScroll, 10,
				SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.SOUTH, userScroll, 0,
				SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.NORTH, radioPanel, 0,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, radioPanel, 0,
				SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, radioPanel, 10,
				SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.SOUTH, radioPanel, 0,
				SpringLayout.SOUTH, this);

		// set constraints for the radio panel
		radioLayout.putConstraint(SpringLayout.WEST, adminButton, 0,
				SpringLayout.WEST, radioPanel);
		radioLayout.putConstraint(SpringLayout.WEST, updateButton, 0,
				SpringLayout.WEST, radioPanel);
		radioLayout.putConstraint(SpringLayout.NORTH, adminButton, 15,
				SpringLayout.NORTH, radioPanel);
		radioLayout.putConstraint(SpringLayout.NORTH, updateButton, 15,
				SpringLayout.SOUTH, adminButton);
		radioLayout.putConstraint(SpringLayout.NORTH, noPermissionButton, 15,
				SpringLayout.SOUTH, updateButton);
		radioLayout.putConstraint(SpringLayout.NORTH, saveButton, 10,
				SpringLayout.SOUTH, noPermissionButton);
		radioLayout.putConstraint(SpringLayout.WEST, radioPanel, 0,
				SpringLayout.WEST, saveButton);
		radioLayout.putConstraint(SpringLayout.EAST, saveButton,
				(int) saveButton.getPreferredSize().getWidth(),
				SpringLayout.WEST, radioPanel);
		
		radioLayout.putConstraint(SpringLayout.SOUTH, saveButton,
				(int) saveButton.getPreferredSize().getHeight(),
				SpringLayout.NORTH, saveButton);

		// assign an action to the save button
	

		// add the buttons to the panel
		radioPanel.add(adminButton);
		radioPanel.add(updateButton);
		radioPanel.add(noPermissionButton);
		radioPanel.add(saveButton);
		radioPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Permission Level"));

		JPanel test = new JPanel();
		test.add(new JLabel("Test"));
		this.add(userScroll);
		this.add(radioPanel);

	}

	public void setSelectedButtons(UserPermissionLevel level) {
		switch (level) {
		case ADMIN:
			adminButton.setSelected(true);
			updateButton.setSelected(false);
			noPermissionButton.setSelected(false);
			saveButton.setAction(new SavePermissionsAction(this, localPermissions.get(userList.getSelectedIndex())));
			break;
		case UPDATE:
			adminButton.setSelected(false);
			updateButton.setSelected(true);
			noPermissionButton.setSelected(false);
			saveButton.setAction(new SavePermissionsAction(this, localPermissions.get(userList.getSelectedIndex())));
			break;
		case NONE:
			adminButton.setSelected(false);
			updateButton.setSelected(false);
			noPermissionButton.setSelected(true);
			saveButton.setAction(new SavePermissionsAction(this, localPermissions.get(userList.getSelectedIndex())));
			break;
		default:
			adminButton.setSelected(false);
			updateButton.setSelected(false);
			noPermissionButton.setSelected(false);
			saveButton.setEnabled(false);
		}
	}

	public UserPermissionLevel getPermission() {
		if (adminButton.isSelected())
			return UserPermissionLevel.ADMIN;
		else if (updateButton.isSelected())
			return UserPermissionLevel.UPDATE;
		else if (noPermissionButton.isSelected())
			return UserPermissionLevel.NONE;
		return null;
	}
	
	public JList getUserList(){
		return userList;
	}

	public List<PermissionModel> getLocalDatabase() {
		return localPermissions;
	}
}
