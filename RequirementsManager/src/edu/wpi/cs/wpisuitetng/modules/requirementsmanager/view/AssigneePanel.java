package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.QueryUserController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.StringListModel;

//import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.AssignUserAction;
//import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.UnassignUserAction;

@SuppressWarnings("serial")
public class AssigneePanel extends JPanel {

	/** A JList for the unassigned users, and assigned users */
	private JList unassignedUsers;
	private JList assignedUsers;

	/** Buttons for assigning and unassigning users */
	private JButton assignSelectedUsers;
	private JButton unassignSelectedUsers;

	/** Labels for the JLists */
	private JLabel unassignedLabel;
	private JLabel assignedLabel;

	/** Scroll panes for the JLists */
	private JScrollPane unassignedScroll;
	private JScrollPane assignedScroll;

	/** Lists to store the assigned and unassigned users*/
	private DefaultListModel assignedUsersList;
	private DefaultListModel unassignedUsersList;

	/** The requirement that this view will operate on */
	private Requirement requirement;
	private DetailPanel detailPanel;
	
	
	public AssigneePanel(Requirement requirement, DetailPanel detailPanel) {
		this.requirement = requirement;
		this.detailPanel = detailPanel;
		
		QueryUserController userController = new QueryUserController(this);
		
		//initialize the two test string arrays
		unassignedUsersList = new DefaultListModel();

	/*	for (int i=0;i<50;i++) {
			unassignedUsersList.add(i, "Value " + (i+1));
		}*/

		assignedUsersList = new DefaultListModel();
		//assignedUsersList.addElement("Value 1a");
		//assignedUsersList.addElement("Value 2a");
		//assignedUsersList.addElement("Value 3a");
		//assignedUsersList.addElement("Value 4a");

		//initialize the list of the assigned and unassigned users
		unassignedUsers = new JList(unassignedUsersList);
		assignedUsers = new JList(assignedUsersList);		

		//initialize the labels
		unassignedLabel = new JLabel("Unassigned");
		assignedLabel = new JLabel("Assigned");

		//initialize the scrolls
		unassignedScroll = new JScrollPane();
		assignedScroll = new JScrollPane();		

		//set the borders
		assignedScroll.setBorder(null);
		unassignedScroll.setBorder(null);

		//add the lists to the scroll panes
		assignedScroll.getViewport().add(assignedUsers);
		unassignedScroll.getViewport().add(unassignedUsers);

		//set the border of the list views
		assignedUsers.setBorder(BorderFactory.createEtchedBorder());
		unassignedUsers.setBorder(BorderFactory.createEtchedBorder());

		//create the JPanel for holding the buttons
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel,BoxLayout.Y_AXIS));


		//initialize the buttons
		assignSelectedUsers = new JButton(">");
		assignSelectedUsers.addActionListener(new AssignUserAction());
		unassignSelectedUsers = new JButton("<");
		unassignSelectedUsers.addActionListener(new UnassignUserAction());
		

		//add buttons to buttonsPanel
		buttonsPanel.add(assignSelectedUsers);
		buttonsPanel.add(unassignSelectedUsers);

		//initialize the layout
		SpringLayout layout = new SpringLayout();		

		//half the width of the button pane, for use in centering the lists
		int halfButton = (int)(buttonsPanel.getPreferredSize().getWidth() / 2);

		//set up the constraints of the spring layout

		layout.putConstraint(SpringLayout.NORTH, unassignedLabel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, assignedLabel, 0, SpringLayout.NORTH, this);	


		layout.putConstraint(SpringLayout.WIDTH, unassignedScroll, 0, SpringLayout.WIDTH, assignedScroll);

		layout.putConstraint(SpringLayout.WEST, unassignedScroll, 0, SpringLayout.WEST, this);
		//layout.putConstraint(SpringLayout.EAST, unassignedUsers, 0, SpringLayout.WEST, buttonsPanel);		
		layout.putConstraint(SpringLayout.NORTH, unassignedScroll, 0, SpringLayout.SOUTH, unassignedLabel);

		//layout.putConstraint(SpringLayout.EAST, buttonsPanel, 0, SpringLayout.WEST, assignedUsers);
		layout.putConstraint(SpringLayout.EAST, unassignedScroll, -halfButton, SpringLayout.HORIZONTAL_CENTER, this);

		layout.putConstraint(SpringLayout.WEST, assignedScroll, halfButton, SpringLayout.HORIZONTAL_CENTER, this);

		layout.putConstraint(SpringLayout.EAST, assignedScroll, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, assignedScroll, 0, SpringLayout.SOUTH, assignedLabel);

		layout.putConstraint(SpringLayout.SOUTH, unassignedScroll, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.SOUTH, assignedScroll, 0, SpringLayout.SOUTH, this);

		layout.putConstraint(SpringLayout.VERTICAL_CENTER,buttonsPanel, 0, SpringLayout.VERTICAL_CENTER, this);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER,buttonsPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);


		layout.putConstraint(SpringLayout.WEST, unassignedLabel, 0, SpringLayout.WEST, unassignedScroll);
		layout.putConstraint(SpringLayout.WEST, assignedLabel, 0, SpringLayout.WEST, assignedScroll);

		setLayout(layout);

		add(unassignedLabel);
		add(unassignedScroll);
		add(buttonsPanel);
		add(assignedLabel);
		add(assignedScroll);

		
		this.initializeLists(new ArrayList<String>());
		userController.getUsers();
	}
	
	/** Initializes the unassigned and assign lists.
	 * Assigned USers list will be populated from this requirement
	 * Unassigned Users list will be pulled from the sever, then filtered to filter out the assigned users from
	 * 	the assigned users list
	 */
	
	public void initializeLists(List<String> allUsers) {
		//lists for assignedUsers and unassigned users
		List<String> assignedUsers = requirement.getUsers();
		
		//create a new list to store the unassigned users;
		List<String> unassignedUsers = new ArrayList<String>();
		
		//loop through all users, and filter out the unassigned users
		for (String user: allUsers) {
			//check if this user is contained in assignedUsers
			if (!assignedUsers.contains(user)) {
				//if not add it to unassigned users list
				unassignedUsers.add(user);
			}
		}
		
		//clear the old values from the list, and add the new values
		assignedUsersList.clear();
		unassignedUsersList.clear();
		
		//iterate through and add them to the list
		for (String user: assignedUsers) {
			assignedUsersList.addElement(user);
		}
		
		for (String user: unassignedUsers) {
			unassignedUsersList.addElement(user);
		}
		
		
	}
	
	/** Updates the requirement with an updated version
	 * 
	 * @param newRequirement The updated requirement
	 */
	
	public void updateRequirement(Requirement newRequirement) {
		this.requirement = newRequirement;
		
		//initialize the user lists
		//TODO: uncomment this when requirements can be fetched from the server
		//initializeLists();
	}
	
	
	/** Returns a list of all the users on the server, in string format 
	 * 
	 * @return The list of all users from the server
	 * 
	 * TODO: Implement this
	 */
	public List<String> getUsersFromServer() {
		return null;
	}
	
	

	public DefaultListModel getUnassignedUsersList(){
		return unassignedUsersList;
	}
	
	
	public void setUnassignedUsersList(StringListModel list){
		/*
		unassignedUsersList.clear();
		for (int i = 0; i < list.getUsers().size(); i++) {
			unassignedUsersList.addElement(list.getUsers().get(i));
		}
		*/
		initializeLists(list.getUsers());
	}
	
	
	public List<String> getAssignedUsersList(){
		List<String> users = new ArrayList<String>();
		for (Object aUser : this.assignedUsersList.toArray()) {
			users.add(aUser.toString());
		}
		return users;
	}
	
	public void setAssignedUsersList(DefaultListModel list){
		assignedUsersList = list;
	}
	
	
	
	/**
	 *  Class used as the Action Listener for the Assigned User button
	 *  
	 *  Moves users from the unassigned list to the Assigned list when the assign button is pressed
	 * 
	 * 
	 *
	 */

	private class AssignUserAction implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			for(Object m: unassignedUsers.getSelectedValues()){
				detailPanel.enableSaveButton();
				//add the selected element(s) in alphabetical order
				int i = 0;
				while(i < (assignedUsersList.getSize() - 1) && assignedUsersList.get(i).toString().compareTo(m.toString()) < 0){
					i++;
				}
				assignedUsersList.add(i,m);
			}		
			//remove selected element(s)
			for(Object m: unassignedUsers.getSelectedValues()){
				unassignedUsersList.removeElement(m);
			}
		}
	}
	
	
	/**
	 *  Class used as the Action Listener for the Unassigned Uuser button
	 *  
	 *  Moves users from the assigned list to the unassigned list when the unassign button is pressed
	 * 
	 * 
	 *
	 */
	private class UnassignUserAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			for(Object m : assignedUsers.getSelectedValues()){
				detailPanel.enableSaveButton();
				//add the selected element(s) in alphabetical order
				int i = 0;
				while(i < (unassignedUsersList.getSize() - 1) && unassignedUsersList.get(i).toString().compareTo(m.toString()) < 0 ){
					i++;
				}
				unassignedUsersList.add(i, m);
			}	
			//remove selected element(s)
			for(Object m: assignedUsers.getSelectedValues()){
				assignedUsersList.removeElement(m);
			}
		}
	}
	
	public void disableUserButtons(){
		assignSelectedUsers.setEnabled(false);
		unassignSelectedUsers.setEnabled(false);
		unassignedUsers.setEnabled(false);
		assignedUsers.setEnabled(false);
	}
}

