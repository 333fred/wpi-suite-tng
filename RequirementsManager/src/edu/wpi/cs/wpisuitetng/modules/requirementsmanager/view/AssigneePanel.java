/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.UserController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.StringListModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.QueryUserRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;

/**
 * The assignee panel which shows the assignees for an individual requirement
 */

@SuppressWarnings ({ "deprecation", "unchecked", "serial", "rawtypes" })
public class AssigneePanel extends JPanel {
	
	/**
	 * Class used as the Action Listener for the Assigned User button
	 * 
	 * Moves users from the unassigned list to the Assigned list when the assign
	 * button is pressed
	 * 
	 * 
	 * 
	 */
	private class AssignUserAction implements ActionListener {
		
		@Override
		public void actionPerformed(final ActionEvent e) {
			for (final Object m : unassignedUsers.getSelectedValues()) {
				// add the selected element(s) in alphabetical order
				int i = 0;
				while ((i < (assignedUsersList.getSize() - 1))
						&& (assignedUsersList.get(i).toString()
								.compareTo(m.toString()) < 0)) {
					i++;
				}
				assignedUsersList.add(i, m);
			}
			// remove selected element(s)
			for (final Object m : unassignedUsers.getSelectedValues()) {
				unassignedUsersList.removeElement(m);
			}
			final UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
					detailPanel);
			
			controller = new RequirementsController();
			
			requirement.setUsers(getAssignedUsersList());
			
			controller.save(requirement, observer);
		}
	}
	
	/**
	 * Class used as the Action Listener for the Unassigned Uuser button
	 * 
	 * Moves users from the assigned list to the unassigned list when the
	 * unassign button is pressed
	 */
	private class UnassignUserAction implements ActionListener {
		
		@Override
		public void actionPerformed(final ActionEvent e) {
			for (final Object m : assignedUsers.getSelectedValues()) {
				// add the selected element(s) in alphabetical order
				int i = 0;
				while ((i < (unassignedUsersList.getSize() - 1))
						&& (unassignedUsersList.get(i).toString()
								.compareTo(m.toString()) < 0)) {
					i++;
				}
				unassignedUsersList.add(i, m);
			}
			// remove selected element(s)
			for (final Object m : assignedUsers.getSelectedValues()) {
				assignedUsersList.removeElement(m);
			}
			final UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
					detailPanel);
			
			controller = new RequirementsController();
			
			requirement.setUsers(getAssignedUsersList());
			
			controller.save(requirement, observer);
		}
	}
	
	/** A JList for the unassigned users, and assigned users */
	private final JList unassignedUsers;
	private final JList assignedUsers;
	
	/** Buttons for assigning and unassigning users */
	private final JButton assignSelectedUsers;
	private final JButton unassignSelectedUsers;
	
	/** Labels for the JLists */
	private final JLabel unassignedLabel;
	private final JLabel assignedLabel;
	
	/** Scroll panes for the JLists */
	private final JScrollPane unassignedScroll;
	private final JScrollPane assignedScroll;
	
	/** Lists to store the assigned and unassigned users */
	private DefaultListModel assignedUsersList;
	private final DefaultListModel unassignedUsersList;
	
	/** The requirement that this view will operate on */
	private Requirement requirement;
	
	private final DetailPanel detailPanel;
	
	private RequirementsController controller;
	
	/**
	 * Creates a new assignee panel for the given requirement and parent view
	 * 
	 * @param requirement
	 *            the requirement to assign people
	 * @param detailPanel
	 *            the panel to display in
	 */
	public AssigneePanel(final Requirement requirement,
			final DetailPanel detailPanel) {
		this.requirement = requirement;
		this.detailPanel = detailPanel;
		
		final UserController userController = new UserController();
		
		// initialize the two test string arrays
		unassignedUsersList = new DefaultListModel();
		
		assignedUsersList = new DefaultListModel();
		
		// initialize the list of the assigned and unassigned users
		unassignedUsers = new JList(unassignedUsersList);
		assignedUsers = new JList(assignedUsersList);
		
		// initialize the labels
		unassignedLabel = new JLabel("Unassigned");
		assignedLabel = new JLabel("Assigned");
		
		// initialize the scrolls
		unassignedScroll = new JScrollPane();
		assignedScroll = new JScrollPane();
		
		// set the borders
		assignedScroll.setBorder(null);
		unassignedScroll.setBorder(null);
		
		// add the lists to the scroll panes
		assignedScroll.getViewport().add(assignedUsers);
		unassignedScroll.getViewport().add(unassignedUsers);
		
		// set the border of the list views
		assignedUsers.setBorder(BorderFactory.createEtchedBorder());
		unassignedUsers.setBorder(BorderFactory.createEtchedBorder());
		
		// create the JPanel for holding the buttons
		final JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		
		// initialize the buttons
		assignSelectedUsers = new JButton(">");
		assignSelectedUsers.addActionListener(new AssignUserAction());
		unassignSelectedUsers = new JButton("<");
		unassignSelectedUsers.addActionListener(new UnassignUserAction());
		
		// add buttons to buttonsPanel
		buttonsPanel.add(assignSelectedUsers);
		buttonsPanel.add(unassignSelectedUsers);
		
		// initialize the layout
		final SpringLayout layout = new SpringLayout();
		
		// half the width of the button pane, for use in centering the lists
		final int halfButton = (int) (buttonsPanel.getPreferredSize()
				.getWidth() / 2);
		
		// set up the constraints of the spring layout
		
		layout.putConstraint(SpringLayout.NORTH, unassignedLabel, 0,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, assignedLabel, 0,
				SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WIDTH, unassignedScroll, 0,
				SpringLayout.WIDTH, assignedScroll);
		
		layout.putConstraint(SpringLayout.WEST, unassignedScroll, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, unassignedScroll, 0,
				SpringLayout.SOUTH, unassignedLabel);
		
		layout.putConstraint(SpringLayout.EAST, unassignedScroll, -halfButton,
				SpringLayout.HORIZONTAL_CENTER, this);
		
		layout.putConstraint(SpringLayout.WEST, assignedScroll, halfButton,
				SpringLayout.HORIZONTAL_CENTER, this);
		
		layout.putConstraint(SpringLayout.EAST, assignedScroll, 0,
				SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, assignedScroll, 0,
				SpringLayout.SOUTH, assignedLabel);
		
		layout.putConstraint(SpringLayout.SOUTH, unassignedScroll, 0,
				SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.SOUTH, assignedScroll, 0,
				SpringLayout.SOUTH, this);
		
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, buttonsPanel, 0,
				SpringLayout.VERTICAL_CENTER, this);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, buttonsPanel, 0,
				SpringLayout.HORIZONTAL_CENTER, this);
		
		layout.putConstraint(SpringLayout.WEST, unassignedLabel, 0,
				SpringLayout.WEST, unassignedScroll);
		layout.putConstraint(SpringLayout.WEST, assignedLabel, 0,
				SpringLayout.WEST, assignedScroll);
		
		setLayout(layout);
		
		add(unassignedLabel);
		add(unassignedScroll);
		add(buttonsPanel);
		add(assignedLabel);
		add(assignedScroll);
		
		initializeLists(new ArrayList<String>());
		final QueryUserRequestObserver observer = new QueryUserRequestObserver(
				this);
		userController.getAll(observer);
	}
	
	/**
	 * This function disables interaction with the assignee panel
	 */
	public void disableUserButtons() {
		final JPanel panel = new JPanel();
		final Color defaultColor = panel.getBackground();
		
		assignSelectedUsers.setEnabled(false);
		unassignSelectedUsers.setEnabled(false);
		unassignedUsers.setEnabled(false);
		unassignedUsers.setBackground(defaultColor);
		assignedUsers.setEnabled(false);
		assignedUsers.setBackground(defaultColor);
	}
	
	/**
	 * Gets the list of users assigned to the requirement
	 * 
	 * @return the users
	 */
	public List<String> getAssignedUsersList() {
		final List<String> users = new ArrayList<String>();
		for (final Object aUser : assignedUsersList.toArray()) {
			users.add(aUser.toString());
		}
		return users;
	}
	
	/**
	 * Gets all the users that aren't assigned to the requirement
	 * 
	 * @return the model with the unassigned users
	 */
	public DefaultListModel getUnassignedUsersList() {
		return unassignedUsersList;
	}
	
	/**
	 * Returns a list of all the users on the server, in string format
	 * 
	 * @return The list of all users from the server
	 * 
	 *         TODO: Implement this
	 */
	public List<String> getUsersFromServer() {
		return null;
	}
	
	/**
	 * Initializes the unassigned and assign lists. Assigned USers list will be
	 * populated from this requirement Unassigned Users list will be pulled from
	 * the sever, then filtered to filter out the assigned users from the
	 * assigned users list
	 * 
	 * @param allUsers
	 *            all of the unsigned users
	 */
	
	public void initializeLists(final List<String> allUsers) {
		// lists for assignedUsers and unassigned users
		final List<String> newAssignedUsers1 = requirement.getUsers();
		
		// create a new list to store the unassigned users;
		final List<String> newUnassignedUsers = new ArrayList<String>();
		
		// loop through all users, and filter out the unassigned users
		for (final String user : allUsers) {
			// check if this user is contained in assignedUsers
			if (!newAssignedUsers1.contains(user)) {
				// if not add it to unassigned users list
				newUnassignedUsers.add(user);
			}
		}
		
		// clear the old values from the list, and add the new values
		assignedUsersList.clear();
		unassignedUsersList.clear();
		
		// iterate through and add them to the list
		for (final String user : newAssignedUsers1) {
			assignedUsersList.addElement(user);
		}
		
		for (final String user : newUnassignedUsers) {
			unassignedUsersList.addElement(user);
		}
		
	}
	
	/**
	 * Sets the already assigned users list
	 * 
	 * @param list
	 *            the new assigned users
	 */
	public void setAssignedUsersList(final DefaultListModel list) {
		assignedUsersList = list;
	}
	
	/**
	 * Sets the unassigned users list
	 * 
	 * @param list
	 *            the new unassigned users
	 */
	public void setUnassignedUsersList(final StringListModel list) {
		initializeLists(list.getUsers());
	}
	
	/**
	 * Updates the requirement with an updated version
	 * 
	 * @param newRequirement
	 *            The updated requirement
	 */
	
	public void updateRequirement(final Requirement newRequirement) {
		requirement = newRequirement;
	}
}
