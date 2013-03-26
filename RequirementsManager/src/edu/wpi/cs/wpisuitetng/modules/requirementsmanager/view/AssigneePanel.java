package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

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
	
	public AssigneePanel() {
		
		//initialize the two test string arrays
		String[] test = new String[50];
		
		for (int i=0;i<test.length;i++) {
			test[i] = "Value " + (i+1);
		}
		
		String[] testa = {"Value 1a", "Value 2a", "Value 3a", "Value 4a"};
		
		//initialize the list of the assigned and unassigned users
		unassignedUsers = new JList(test);
		assignedUsers = new JList(testa);		
	
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
		
		//initialize the buttons
		assignSelectedUsers = new JButton(">>");
		unassignSelectedUsers = new JButton("<<");
		
		//create the Jpanel for holding the buttons
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel,BoxLayout.Y_AXIS));
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
}
