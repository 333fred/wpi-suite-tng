package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.AssignUserAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.UnassignUserAction;

@SuppressWarnings("serial")
public class AssigneePanel extends JPanel {

	/** A JList for the unassigned users, and assigned users */
	private JList<String> unassignedUsers;
	private JList<String> assignedUsers;

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
	private DefaultListModel<String> assignedUsersList;
	private DefaultListModel<String> unassignedUsersList;

	public AssigneePanel() {

		//initialize the two test string arrays
		unassignedUsersList = new DefaultListModel<String>();

		for (int i=0;i<50;i++) {
			unassignedUsersList.add(i, "Value " + (i+1));
		}

		assignedUsersList = new DefaultListModel<String>();
		assignedUsersList.addElement("Value 1a");
		assignedUsersList.addElement("Value 2a");
		assignedUsersList.addElement("Value 3a");
		assignedUsersList.addElement("Value 4a");

		//initialize the list of the assigned and unassigned users
		unassignedUsers = new JList<String>(unassignedUsersList);
		assignedUsers = new JList<String>(assignedUsersList);		

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
		assignSelectedUsers = new JButton(">>");
		assignSelectedUsers.addActionListener(new AssignUserAction());
		unassignSelectedUsers = new JButton("<<");
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

	}

	public DefaultListModel<String> getUnassignedUsersList(){
		return unassignedUsersList;
	}
	public void setUnassignedUsersList(DefaultListModel<String> list){
		unassignedUsersList = list;
	}
	public DefaultListModel<String> getAssignedUsersList(){
		return assignedUsersList;
	}
	public void setAssignedUsersList(DefaultListModel<String> list){
		assignedUsersList = list;
	}

	class AssignUserAction implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			for(String m: unassignedUsers.getSelectedValuesList()){
				//add the selected element(s) in alphabetical order
				int i = 0;
				while(i < (assignedUsersList.getSize() - 1) && assignedUsersList.get(i).compareTo(m) < 0){
					i++;
				}
				assignedUsersList.add(i,m);
			}		
			//remove selected element(s)
			for(String m: unassignedUsers.getSelectedValuesList()){
				unassignedUsersList.removeElement(m);
			}
		}
	}
	class UnassignUserAction implements ActionListener{
	
		@Override
		public void actionPerformed(ActionEvent e){
			for(String m : assignedUsers.getSelectedValuesList()){
				//add the selected element(s) in alphabetical order
				int i = 0;
				while(i < (unassignedUsersList.getSize() - 1) && unassignedUsersList.get(i).compareTo(m) < 0 ){
					i++;
				}
				unassignedUsersList.add(i, m);
			}	
			//remove selected element(s)
			for(String m: assignedUsers.getSelectedValuesList()){
				assignedUsersList.removeElement(m);
			}
		}
	}
}

