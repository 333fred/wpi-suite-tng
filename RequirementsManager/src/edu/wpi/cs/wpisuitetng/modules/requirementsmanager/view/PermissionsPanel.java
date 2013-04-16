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
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.StringListModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.Tab;

@SuppressWarnings("serial")
public class PermissionsPanel extends Tab {

	private JList userList;
    private String[] users = new String[10];

	private JRadioButton adminButton;
	private JRadioButton updateButton;
	private JRadioButton noPermissionButton;

	public PermissionsPanel() {
		JPanel radioPanel = new JPanel();
		
		for(int i = 0; i<5; i++){
			users[i] = "demo" + i;
		}

		SpringLayout layout = new SpringLayout();
		SpringLayout radioLayout = new SpringLayout();
		
		radioPanel.setLayout(radioLayout);
		setLayout(layout);

		// construct the list of users
		userList = new JList(users);
	

		/** Construct the admin button */
		adminButton = new JRadioButton("Admin", false);
		adminButton.setEnabled(true);

		/** Construct the update button */
		updateButton = new JRadioButton("Update", false);

		/** Construct the none button */
		noPermissionButton = new JRadioButton("None", false);

		JScrollPane userScroll = new JScrollPane();
		userScroll.setBorder(null);
		userScroll.getViewport().add(userList);
		
		
		// set constraints for the overall panel
		layout.putConstraint(SpringLayout.WEST, radioPanel, 15,
				SpringLayout.EAST, userScroll);
		layout.putConstraint(SpringLayout.WEST, radioPanel, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, userScroll, 0,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, radioPanel, 0,
				SpringLayout.NORTH, this);

		// set constraints for the radio panel
		radioLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, radioPanel,
				0, SpringLayout.HORIZONTAL_CENTER, adminButton);
		radioLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, radioPanel,
				0, SpringLayout.HORIZONTAL_CENTER, updateButton);
		radioLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, radioPanel,
				0, SpringLayout.HORIZONTAL_CENTER, noPermissionButton);
		radioLayout.putConstraint(SpringLayout.NORTH, adminButton, 15, SpringLayout.NORTH, radioPanel);
		radioLayout.putConstraint(SpringLayout.NORTH, updateButton, 15, SpringLayout.SOUTH, adminButton);
		radioLayout.putConstraint(SpringLayout.NORTH, noPermissionButton, 15, SpringLayout.SOUTH, updateButton);
		
		radioPanel.add(adminButton);
		radioPanel.add(updateButton);
		radioPanel.add(noPermissionButton);
		radioPanel.add(new JLabel("TEST"));
		
		add(userScroll);
		add(radioPanel);
	}
}
