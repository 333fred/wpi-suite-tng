/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Nick, Kyle, Matt, Chris
 * 
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * @author Kyle, Matt, Chris
 * 
 */
public class SubRequirementPanel extends JPanel {

	/** The list of requirements that the view is displaying */

	private Requirement requirement;
	private DetailPanel panel;
	private JPanel editSubReqPanel; // Lower panel for adding sub requirements
									// and parents

	// fields for the top half of sub requirement panel
	private JLabel parentReq;
	private DefaultListModel childrenList; // List of subrequirements
	private JList topReqNames;
	private JScrollPane topScrollPane; // ScrollPane for subreqs/parents

	// fields for the bottom half of sub requirement panel
	private DefaultListModel validChildList; // List of requirements available
	// to add as children
	private DefaultListModel validParentList; // List of requirements available
	// to add as parents
	private JList bottomReqNames;
	private JScrollPane bottomScrollPane;

	private JLabel parentLabel; // Parent and child labels
	private JLabel childLabel;

	private JRadioButton radioParent; // Radio buttons to choose between adding
										// children/parents
	private JRadioButton radioChild;
	private ButtonGroup btnGroup;

	private JButton addReq; // Button to add child or parent
	private JButton removeChild; // Buttons to remove child or parent
	private JButton removeParent;

	public Boolean parentSelected; // Boolean for which radio button is selected

	public SubRequirementPanel(Requirement requirement, DetailPanel panel) {

		this.requirement = requirement; // Load in the requirement and detail
										// panel
		this.panel = panel;

		parentSelected = false;

		validChildList = new DefaultListModel(); // Initialize lists
		bottomReqNames = new JList();

		childrenList = new DefaultListModel();
		validParentList = new DefaultListModel();

		editSubReqPanel = new JPanel(); // Initialize lower panel
		editSubReqPanel.setBorder(BorderFactory
				.createTitledBorder("Add to SubRequirement Hierarchy"));

		SpringLayout layout = new SpringLayout(); // Initialize layout
		SpringLayout subreqPanelLayout = new SpringLayout();

		addReq = new JButton("Add"); // Add the buttons and labels
		removeChild = new JButton("Remove Children");

		removeParent = new JButton("Remove Parent");
		parentLabel = new JLabel("Parent:");
		childLabel = new JLabel("Children:");
		parentReq = new JLabel();

		JRadioButton radioChild = new JRadioButton("Add Children"); // Add radio buttons
		JRadioButton radioParent = new JRadioButton("Add Parent");

		radioChild.setSelected(true); // Set the radio button for children true first

		btnGroup = new ButtonGroup(); // Add the buttons to the group
		btnGroup.add(radioChild);
		btnGroup.add(radioParent);

		topScrollPane = new JScrollPane(); // Create the top pane that will hold
											// sub requirements
		topScrollPane.setViewportView(topReqNames);
		topScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		topScrollPane.setBorder(BorderFactory.createEtchedBorder());

		bottomScrollPane = new JScrollPane(); // Create the lower pane that will
												// hold requirements to add
		bottomScrollPane.setViewportView(bottomReqNames);
		bottomScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		bottomScrollPane.setBorder(BorderFactory.createEtchedBorder());

		// Create the constraints for the GUI
		layout.putConstraint(SpringLayout.NORTH, parentLabel, 5,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, parentLabel, 16,
				SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, childLabel, 5,
				SpringLayout.SOUTH, parentLabel);
		layout.putConstraint(SpringLayout.WEST, childLabel, 16,
				SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, parentReq, 5,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, parentReq, 14,
				SpringLayout.EAST, parentLabel);

		layout.putConstraint(SpringLayout.NORTH, topScrollPane, 5,
				SpringLayout.SOUTH, parentLabel);
		layout.putConstraint(SpringLayout.WEST, topScrollPane, 5,
				SpringLayout.EAST, childLabel);
		layout.putConstraint(SpringLayout.EAST, topScrollPane, -64,
				SpringLayout.EAST, this);

		layout.putConstraint(SpringLayout.NORTH, removeChild, 5,
				SpringLayout.SOUTH, topScrollPane);
		layout.putConstraint(SpringLayout.WEST, removeChild, 16,
				SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, removeParent, 5,
				SpringLayout.SOUTH, topScrollPane);
		layout.putConstraint(SpringLayout.WEST, removeParent, 5,
				SpringLayout.EAST, removeChild);

		layout.putConstraint(SpringLayout.NORTH, editSubReqPanel, 10,
				SpringLayout.SOUTH, removeChild);
		layout.putConstraint(SpringLayout.SOUTH, editSubReqPanel, -1,
				SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.WIDTH, editSubReqPanel, -1,
				SpringLayout.WIDTH, this);

		subreqPanelLayout.putConstraint(SpringLayout.NORTH, radioChild, 5,
				SpringLayout.NORTH, editSubReqPanel);
		subreqPanelLayout.putConstraint(SpringLayout.WEST, radioChild, 16,
				SpringLayout.WEST, editSubReqPanel);

		subreqPanelLayout.putConstraint(SpringLayout.NORTH, radioParent, 5,
				SpringLayout.NORTH, editSubReqPanel);
		subreqPanelLayout.putConstraint(SpringLayout.WEST, radioParent, 5,
				SpringLayout.EAST, radioChild);

		subreqPanelLayout.putConstraint(SpringLayout.NORTH, bottomScrollPane,
				5, SpringLayout.SOUTH, radioParent);
		subreqPanelLayout.putConstraint(SpringLayout.WEST, bottomScrollPane,
				13, SpringLayout.EAST, childLabel);
		subreqPanelLayout.putConstraint(SpringLayout.EAST, bottomScrollPane,
				-56, SpringLayout.EAST, editSubReqPanel);

		subreqPanelLayout.putConstraint(SpringLayout.NORTH, addReq, 5,
				SpringLayout.SOUTH, bottomScrollPane);
		subreqPanelLayout.putConstraint(SpringLayout.WEST, addReq, 16,
				SpringLayout.WEST, editSubReqPanel);
		subreqPanelLayout.putConstraint(SpringLayout.EAST, addReq, 16,
				SpringLayout.EAST, removeChild);

		//Set the layout of this panel and the lower panel
		this.setLayout(layout);
		editSubReqPanel.setLayout(subreqPanelLayout);

		this.add(parentLabel); //Add the swing components
		this.add(parentReq);
		this.add(childLabel);
		this.add(topScrollPane);
		this.add(removeParent);
		this.add(removeChild);

		editSubReqPanel.add(radioChild); //Add the lower swing components for adding requirements 
		editSubReqPanel.add(radioParent);
		editSubReqPanel.add(bottomScrollPane);
		editSubReqPanel.add(addReq);

		this.add(editSubReqPanel); //Add the lower panel

		// If the requirement is not deleted or complete, set actions and enabling
		if (requirement.getStatus() != Status.DELETED
				&& requirement.getStatus() != Status.COMPLETE) {
			removeChild.setAction(new RemoveChildAction(
					new RemoveChildController(this, requirement, panel)));
			addReq.setAction(new AssignChildAction(new AssignChildController(
					this, requirement, panel)));
			removeParent.setAction(new RemoveParentAction(
					new RemoveParentController(this, requirement, panel)));

			removeChild.setEnabled(false); //List starts with no selection, initialize to disabled

			radioChild.addActionListener(new ActionListener() { //Have the radio child listen for when it's clicked
				public void actionPerformed(ActionEvent e) {
					setActionToChild();
					refreshValidChildren();
					parentSelected = false;
				}
			});

			radioParent.addActionListener(new ActionListener() { //Have the radio parent listen for when it's clicked
				public void actionPerformed(ActionEvent e) {
					setActionToParent();
					refreshValidParents();
					parentSelected = true;
					updateAddButtontext();
				}
			});

			refreshAll(); //Refresh all fields

		}
	}

	/*private void initializeBottomListToValidChildren() {
		List<Requirement> requirements = RequirementDatabase.getInstance()
				.getAll();

		for (Requirement req : requirements) {
			validChildList.addElement(req.getName());
		}
	}*/

	/**
	 * Add the requirement's children to the top panel
	 * 
	 * @param requirement The requirement to iterate through
	 */
	private void initializeTopList(Requirement requirement) {
		Requirement tempReq = null;
		for (int req : requirement.getSubRequirements()) {//For all the requirement's children
			try {
				tempReq = RequirementDatabase.getInstance().get(req);//Grab the requirement
				childrenList.addElement(tempReq.getName());//Place it in the list
			} catch (RequirementNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * Find the requirements that can be added as a subrequirement
	 * to this requirement for the lower panel
	 */
	public void addValidChildren() {
		List<Requirement> requirements = RequirementDatabase.getInstance()
				.getAll();

		int rootID;
		rootID = checkParentRoot(this.requirement); //set rootID to this requirement's highest parent
		for (Requirement req : requirements) { //For all the requirements in the project
			if (req.getpUID().size() == 0) { //If the requirement has no parents
				if (req.getStatus() != Status.DELETED //Is not deleted
						&& req.getStatus() != Status.COMPLETE) { //Is not complete
					if (req.getrUID() != rootID) { //and is not this requirement's root
						validChildList.addElement(req.getName()); //add the requirement to the list
					}
				}
			}

		}
	}

	/**
	 * Find the requirements that can be added as a parent
	 * to this requirement for the lower panel
	 */
	public void addValidParents() {
		List<Requirement> requirements = RequirementDatabase.getInstance()
				.getAll();
		Requirement parentReq = null;
		if (requirement.getpUID().size() > 0) { //Store the parent requirement
			try {
				parentReq = RequirementDatabase.getInstance().get(
						requirement.getpUID().get(0));
			} catch (RequirementNotFoundException e) {
				e.printStackTrace();
			}
		}
		for (Requirement req : requirements) { //For all the requirements in the project
			if (req.getStatus() != Status.DELETED //If it is not deleted
					&& req.getStatus() != Status.COMPLETE) {//or complete
				if (!containsCurrentRequirement(requirement, req)) {//and if this requirement doesn't have it in it's tree
					if (!req.equals(parentReq))//and it is not it's parent
						validParentList.addElement(req.getName());//add to list
				}
			}
		}
	}

	/**
	 * Grab the lowest node (root) of the requirement tree from a
	 * given requirement
	 * @param aReq The requirement to iterate up and find it's root
	 * @return The root's ID
	 */
	public int checkParentRoot(Requirement aReq) {
		Requirement tempReq = null;
		if (aReq.getpUID().size() == 0) { //If it has no parent, it is the root
			return aReq.getrUID();
		} else {
			try { //Otherwise, grab the requirement's parent and recursively try again
				tempReq = RequirementDatabase.getInstance().get(
						aReq.getpUID().get(0));
			} catch (RequirementNotFoundException e) {
				e.printStackTrace();
			}
			return checkParentRoot(tempReq);
		}

	}

	/**
	 * Check if a requirement contains another requirement in it's
	 * tree of subrequirements
	 * @param req The requirement whose subrequirement tree we want to check
	 * @param current The requirement that may be contained in the tree
	 * @return
	 */
	public boolean containsCurrentRequirement(Requirement req,
			Requirement current) {
		Requirement child = null;
		Boolean check = false;
		if (req.getrUID() == current.getrUID()) { //If the requirements are equal, it was in the tree
			return true;
		} else {
			for (Integer i : req.getSubRequirements()) { //For all the subrequirements
				try {
					child = RequirementDatabase.getInstance().get(i); //Grab the child
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
				check = containsCurrentRequirement(child, current); //See if the child is equal to current, and rec check
				if (check)
					return check; //Return true if we found the child. Otherwise loop again
			}
			return false; //Return false if we did not find anything
		}
	}

	/**
	 * Refresh the top panel of subrequirements
	 */
	public void refreshTopPanel() {
		childrenList = new DefaultListModel();
		initializeTopList(requirement);
		topReqNames = new JList(childrenList); //Create list of children and put it in a JList
		topScrollPane.setViewportView(topReqNames);
		removeChild.setEnabled(false); //Set the viewport and initialize the button as disabled

		topReqNames.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) { //If children are selected, enable the button
				if (topReqNames.getSelectedValues().length == 0) //Otherwise, disable it
					removeChild.setEnabled(false);
				else
					removeChild.setEnabled(true);
			}
		});
	}

	/**
	 * Refresh lower panel for possible subrequirements
	 */
	public void refreshValidChildren() {
		validChildList = new DefaultListModel();
		addValidChildren();
		bottomReqNames = new JList(validChildList);//Create list of valid children and put it in a JList
		bottomScrollPane.setViewportView(bottomReqNames);
		addReq.setEnabled(false);//Set the viewport and initialize the button as disabled

		bottomReqNames.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) { //If children are selected, enable the button
				if (bottomReqNames.getSelectedValues().length == 0) //Otherwise, disable it
					addReq.setEnabled(false);
				else
					addReq.setEnabled(true);
			}
		});
	}

	/**
	 * Refresh lower panel for possible parents
	 */
	public void refreshValidParents() {
		validParentList = new DefaultListModel();
		addValidParents();
		bottomReqNames = new JList(validParentList);//Create list of valid parents and put it in a JList
		bottomScrollPane.setViewportView(bottomReqNames);
		addReq.setEnabled(false);//Set the viewport and initialize the button as disabled

		bottomReqNames.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (bottomReqNames.getSelectedValues().length == 0)//If parents are selected, enable the button
					addReq.setEnabled(false);	//Otherwise, disable it
				else
					addReq.setEnabled(true);
			}
		});
	}

	/**
	 * Refresh higher label to display the parent
	 */
	public void refreshParentLabel() {

		Requirement tempReq = null;
		if (requirement.getpUID().size() == 0) {//If this requirement has no parent
			parentReq.setText(""); //Display a blank, and disable the reove parent button
			removeParent.setEnabled(false);
		} else {
			removeParent.setEnabled(true);//Otherwise, enable the button, get the requirement
			for (int ID : requirement.getpUID()) {// And display it's name
				try {
					tempReq = RequirementDatabase.getInstance().get(ID);
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
			}
			parentReq.setText(tempReq.getName());
		}
		updateAddButtontext();//Update the add button (either change or assign)
	}

	/**
	 * Update the button for altering the requirement's parent
	 */
	public void updateAddButtontext() {
		if (parentSelected) {
			if (requirement.getpUID().size() > 0) {
				addReq.setText("Change Parent"); //If it has a parent, display that the action will change it
			} else {
				addReq.setText("Assign Parent"); //If it does not have a parent, display the action will add one
			}
		}
	}

	/**
	 * Get the lower list of requirements
	 * @return the lower list of requirements
	 */
	public JList getList() {
		return bottomReqNames;
	}

	/**
	 * Get the list of subrequirements
	 * @return the lower list of subrequirements
	 */
	public JList getListSubReq() {
		return topReqNames;
	}

	/**
	 * Set the requirement add button for adding a parent
	 */
	protected void setActionToParent() {
		addReq.setAction(new AssignParentAction(new AssignParentController(
				this, requirement, panel)));

	}
	
	/**
	 * Set the requirement add button for adding a child
	 */
	protected void setActionToChild() {
		addReq.setAction(new AssignChildAction(new AssignChildController(this,
				requirement, panel)));
	}

	/**
	 * Refresh all of the panels fields, buttons, lists, and labels
	 */
	public void refreshAll() {
		try {
			Requirement tempReq = RequirementDatabase.getInstance().get(
					requirement.getrUID()); //Grab the latest version of the requirement
			if (tempReq != null) {
				requirement = tempReq; //Set the requirement field to it
				removeChild.setAction(new RemoveChildAction(
						new RemoveChildController(this, requirement, panel))); //Reset the actions with the new requirement
				if (parentSelected) {
					setActionToParent();
				} else {
					setActionToChild();
				}
				removeParent.setAction(new RemoveParentAction(
						new RemoveParentController(this, requirement, panel)));
				refreshTopPanel(); //Refresh the parent label, subrequirements, and lower pane
				refreshParentLabel();
				if (parentSelected)
					refreshValidParents();
				else
					refreshValidChildren();
			}
		} catch (RequirementNotFoundException e) {
		}

	}

	/**
	 * Disable button input (for deleted and completed requirements)
	 */
	public void disableUserButtons() {
		removeParent.setEnabled(false); //Disable all buttons
		removeChild.setEnabled(false);
		addReq.setEnabled(false);
		bottomReqNames.setEnabled(false);
		if(topReqNames != null){
			topReqNames.setEnabled(false);
		}
	}
}
