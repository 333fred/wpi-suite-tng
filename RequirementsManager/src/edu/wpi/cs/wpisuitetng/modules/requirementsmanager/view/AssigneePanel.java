package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
		String[] test = {"Value 1", "Value 2", "Value 3", "Value 4"};
		String[] testa = {"Value 1a", "Value 2a", "Value 3a", "Value 4a"};
		unassignedUsers = new JList(test);
		assignedUsers = new JList(testa);
		
		/*
		JPanel unassignedPanel = new JPanel();
		JPanel assignedPanel = new JPanel();
		*/
		
		unassignedLabel = new JLabel("Unassigned");
		assignedLabel = new JLabel("Assigned");
		
		unassignedScroll.add(unassignedLabel);
		unassignedScroll.add(unassignedUsers);
		
		assignedScroll.add(assignedLabel);
		assignedScroll.add(assignedUsers);
		
		assignedUsers.setBorder(BorderFactory.createEtchedBorder());
		unassignedUsers.setBorder(BorderFactory.createEtchedBorder());
		
		assignSelectedUsers = new JButton(">>");
		unassignSelectedUsers = new JButton("<<");
		
		
		SpringLayout layout = new SpringLayout();
		
		
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel,BoxLayout.Y_AXIS));
		buttonsPanel.add(assignSelectedUsers);
		buttonsPanel.add(unassignSelectedUsers);	
		
		
		int halfButton = (int)(buttonsPanel.getPreferredSize().getWidth() / 2);
		
		layout.putConstraint(SpringLayout.WIDTH, unassignedScroll, 0, SpringLayout.WIDTH, assignedScroll);
		
		layout.putConstraint(SpringLayout.WEST, unassignedScroll, 0, SpringLayout.WEST, this);
		//layout.putConstraint(SpringLayout.EAST, unassignedUsers, 0, SpringLayout.WEST, buttonsPanel);		
		layout.putConstraint(SpringLayout.NORTH, unassignedScroll, 0, SpringLayout.NORTH, this);
		
		//layout.putConstraint(SpringLayout.EAST, buttonsPanel, 0, SpringLayout.WEST, assignedUsers);
		layout.putConstraint(SpringLayout.EAST, unassignedScroll, -halfButton, SpringLayout.HORIZONTAL_CENTER, this);
		
		layout.putConstraint(SpringLayout.WEST, assignedScroll, halfButton, SpringLayout.HORIZONTAL_CENTER, this);
		
		layout.putConstraint(SpringLayout.EAST, assignedScroll, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, assignedScroll, 0, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.SOUTH, unassignedScroll, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.SOUTH, assignedScroll, 0, SpringLayout.SOUTH, this);
		
		layout.putConstraint(SpringLayout.VERTICAL_CENTER,buttonsPanel, 0, SpringLayout.VERTICAL_CENTER, this);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER,buttonsPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);
		
		
		setLayout(layout);
		
		add(unassignedScroll);
		add(buttonsPanel);
		add(assignedScroll);
		
	}
}
