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
	private JPanel editSubReqPanel;

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

	private JLabel parentLabel;
	private JLabel childLabel;

	private JRadioButton radioParent;
	private JRadioButton radioChild;
	private ButtonGroup btnGroup;

	private JButton addReq;
	private JButton removeChild;
	private JButton removeParent;

	public Boolean parentSelected;

	public SubRequirementPanel(Requirement requirement, DetailPanel panel) {

		this.requirement = requirement;		
		this.panel = panel;

		parentSelected = false;

		validChildList = new DefaultListModel();
		// initializeList();
		//addValidChildren();
		bottomReqNames = new JList();

		childrenList = new DefaultListModel();
		//initializeTopList(requirement);
		//topReqNames = new JList(childrenList);
		topReqNames = new JList();

		validParentList = new DefaultListModel();
		//addValidParents();

		editSubReqPanel = new JPanel();
		editSubReqPanel.setBorder(BorderFactory.createTitledBorder("Add to SubRequirement Hierarchy"));


		SpringLayout layout = new SpringLayout();
		SpringLayout subreqPanelLayout = new SpringLayout();


		addReq = new JButton("Add");
		removeChild = new JButton("Remove Children");

		removeParent = new JButton("Remove Parent");
		parentLabel = new JLabel("Parent:");
		childLabel = new JLabel("Children:");
		parentReq = new JLabel();

		if (this.requirement.getpUID().size() == 0) {
			parentReq.setText("");
		} else {
			try {
				parentReq.setText(RequirementDatabase.getInstance()
						.get(this.requirement.getpUID().get(0)).getName());
			} catch (RequirementNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		JRadioButton radioChild = new JRadioButton("Add Children");
		JRadioButton radioParent = new JRadioButton("Add Parent");

		radioChild.setSelected(true);

		btnGroup = new ButtonGroup();
		btnGroup.add(radioChild);
		btnGroup.add(radioParent);

		topScrollPane = new JScrollPane();
		topScrollPane.setViewportView(topReqNames);
		topScrollPane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		topScrollPane.setBorder(BorderFactory.createEtchedBorder());

		bottomScrollPane = new JScrollPane();
		bottomScrollPane.setViewportView(bottomReqNames);
		bottomScrollPane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		bottomScrollPane.setBorder(BorderFactory.createEtchedBorder());

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

		layout.putConstraint(SpringLayout.NORTH, editSubReqPanel, 10, SpringLayout.SOUTH, removeChild);
		layout.putConstraint(SpringLayout.SOUTH, editSubReqPanel, -1, SpringLayout.SOUTH, this);
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

		subreqPanelLayout.putConstraint(SpringLayout.NORTH, bottomScrollPane, 5,
				SpringLayout.SOUTH, radioParent);
		subreqPanelLayout.putConstraint(SpringLayout.WEST, bottomScrollPane, 13,
				SpringLayout.EAST, childLabel);
		subreqPanelLayout.putConstraint(SpringLayout.EAST, bottomScrollPane, -56,
				SpringLayout.EAST, editSubReqPanel);

		subreqPanelLayout.putConstraint(SpringLayout.NORTH, addReq, 5, SpringLayout.SOUTH,
				bottomScrollPane);
		subreqPanelLayout.putConstraint(SpringLayout.WEST, addReq, 16, SpringLayout.WEST,
				editSubReqPanel);
		subreqPanelLayout.putConstraint(SpringLayout.EAST, addReq, 0, SpringLayout.EAST,
				removeChild);





		this.setLayout(layout);
		editSubReqPanel.setLayout(subreqPanelLayout);

		this.add(parentLabel);
		this.add(parentReq);
		this.add(childLabel);
		this.add(topScrollPane);
		this.add(removeParent);
		this.add(removeChild);

		editSubReqPanel.add(radioChild);
		editSubReqPanel.add(radioParent);
		editSubReqPanel.add(bottomScrollPane);
		editSubReqPanel.add(addReq);

		this.add(editSubReqPanel);


		// Do other things here
		if(requirement.getStatus()!=Status.DELETED&&requirement.getStatus()!=Status.COMPLETE){
			removeChild.setAction(new RemoveChildAction(new RemoveChildController(
					this, requirement, panel)));
			addReq.setAction(new AssignChildAction(new AssignChildController(this,
					requirement, panel)));
			removeParent.setAction(new RemoveParentAction(
					new RemoveParentController(this, requirement, panel)));

			removeChild.setEnabled(false);

			radioChild.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setActionToChild();
					refreshValidChildren();
					parentSelected = false;
				}
			});

			radioParent.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setActionToParent();
					refreshValidParents();
					parentSelected = true;
					updateAddButtontext();
				}
			});

			refreshAll();

		}
	}

	private void initializeBottomListToValidChildren() {
		List<Requirement> requirements = RequirementDatabase.getInstance()
				.getAll();

		for (Requirement req : requirements) {
			validChildList.addElement(req.getName());
		}
	}

	private void initializeTopList(Requirement requirement) {
		Requirement tempReq = null;
		for (int req : requirement.getSubRequirements()) {
			try {
				tempReq = RequirementDatabase.getInstance().get(req);
				childrenList.addElement(tempReq.getName());
			} catch (RequirementNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void addValidChildren() {
		List<Requirement> requirements = RequirementDatabase.getInstance()
				.getAll();

		int rootID;
		rootID = checkParentRoot(this.requirement);
		for (Requirement req : requirements) {
			if (req.getpUID().size() == 0) {
				if (req.getStatus() != Status.DELETED && req.getStatus() != Status.COMPLETE) {
					if (req.getrUID() != rootID) {
						validChildList.addElement(req.getName());
					}
				}
			}

		}
	}

	public void addValidParents() {
		List<Requirement> requirements = RequirementDatabase.getInstance()
				.getAll();
		Requirement parentReq = null;
		if (requirement.getpUID().size() > 0) {
			try {
				parentReq = RequirementDatabase.getInstance().get(
						requirement.getpUID().get(0));
			} catch (RequirementNotFoundException e) {
				e.printStackTrace();
			}
		}
		for (Requirement req : requirements) {
			if (req.getStatus() != Status.DELETED && req.getStatus()!=Status.COMPLETE) {
				if (!containsCurrentRequirement(requirement, req)) {
					if (!req.equals(parentReq))
						validParentList.addElement(req.getName());
				}
			}
		}
	}

	public int checkParentRoot(Requirement aReq) {
		Requirement tempReq = null;
		if (aReq.getpUID().size() == 0) {
			return aReq.getrUID();
		} else {
			try {
				tempReq = RequirementDatabase.getInstance().get(
						aReq.getpUID().get(0));
			} catch (RequirementNotFoundException e) {
				e.printStackTrace();
			}
			return checkParentRoot(tempReq);
		}

	}

	public boolean containsCurrentRequirement(Requirement req,
			Requirement current) {
		Requirement child = null;
		Boolean check = false;
		if (req.getrUID() == current.getrUID()) {
			return true;
		} else {
			for (Integer i : req.getSubRequirements()) {
				try {
					child = RequirementDatabase.getInstance().get(i);
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
				check = containsCurrentRequirement(child, current);
				if (check)
					return check;
			}
			return false;
		}
	}

	public void refreshTopPanel() {
		childrenList = new DefaultListModel();
		initializeTopList(requirement);
		topReqNames = new JList(childrenList);
		topScrollPane.setViewportView(topReqNames);
		removeChild.setEnabled(false);

		topReqNames.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(topReqNames.getSelectedValues().length==0)
					removeChild.setEnabled(false);
				else
					removeChild.setEnabled(true);
			} 
		});
	}

	public void refreshValidChildren() {
		validChildList = new DefaultListModel();
		addValidChildren();
		bottomReqNames = new JList(validChildList);
		bottomScrollPane.setViewportView(bottomReqNames);
		addReq.setEnabled(false);

		bottomReqNames.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(bottomReqNames.getSelectedValues().length==0)
					addReq.setEnabled(false);
				else
					addReq.setEnabled(true);
			} 
		});
	}

	public void refreshValidParents() {
		validParentList = new DefaultListModel();
		addValidParents();
		bottomReqNames = new JList(validParentList);
		bottomScrollPane.setViewportView(bottomReqNames);
		addReq.setEnabled(false);

		bottomReqNames.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(bottomReqNames.getSelectedValues().length==0)
					addReq.setEnabled(false);
				else
					addReq.setEnabled(true);
			} 
		});
	}

	public void refreshParentLabel() {

		Requirement tempReq = null;
		if (requirement.getpUID().size() == 0) {
			parentReq.setText("");
			removeParent.setEnabled(false);
		} else {
			removeParent.setEnabled(true);
			for (int ID : requirement.getpUID()) {
				try {
					tempReq = RequirementDatabase.getInstance().get(ID);
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
			}
			parentReq.setText(tempReq.getName());
		}
		updateAddButtontext();
	}

	public void updateAddButtontext() {
		if (parentSelected) {
			if (requirement.getpUID().size() > 0) {
				addReq.setText("Change Parent");
			} else {
				addReq.setText("Assign Parent");
			}
		}
	}

	public JList getList() {
		return bottomReqNames;
	}

	public JList getListSubReq() {
		return topReqNames;
	}

	protected void setActionToParent() {
		addReq.setAction(new AssignParentAction(new AssignParentController(
				this, requirement, panel)));

	}

	protected void setActionToChild() {
		addReq.setAction(new AssignChildAction(new AssignChildController(this,
				requirement, panel)));
	}

	public void refreshAll() {
		try {
			Requirement tempReq = RequirementDatabase.getInstance().get(requirement.getrUID());
			if(tempReq!=null){
				requirement = tempReq;
				removeChild.setAction(new RemoveChildAction(new RemoveChildController(
						this, requirement, panel)));
				if (parentSelected){
					setActionToParent();
				}
				else{
					setActionToChild();
				}
				removeParent.setAction(new RemoveParentAction(
						new RemoveParentController(this, requirement, panel)));
				refreshTopPanel();
				refreshParentLabel();
				if (parentSelected)
					refreshValidParents();
				else
					refreshValidChildren();
			}
		} catch (RequirementNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void disableUserButtons() {
		removeParent.setEnabled(false);
		removeChild.setEnabled(false);
		addReq.setEnabled(false);
		bottomReqNames.setEnabled(false);
		topReqNames.setEnabled(false);		
	}
}
