/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Kyle, Matt, Chris
 * 
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;

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
	
	private DefaultListModel validChildList; //List of requirements available to add as children
	private JList reqNames;	
	private JScrollPane scrollPane;
	
	private DefaultListModel validParentList; //List of requirements available to add as parents
	
	private JLabel parentReq;
	private DefaultListModel childrenList; //List of subrequirements
	private JList subReqNames;
	private JScrollPane scrollPaneContainedReqs; //ScrollPane for subreqs/parents
	
	private JLabel parentLabel;
	private JLabel childLabel;
	
	private JRadioButton radioParent;
	private JRadioButton radioChild;
	private ButtonGroup group;
	
	private JButton addReq;
	private JButton removeReq;
	private JButton removeParent;

	
	
	public SubRequirementPanel(Requirement requirement, DetailPanel panel) {		
		
		this.requirement = requirement;
		
		validChildList = new DefaultListModel();
		//initializeList();
		addValidChildren();
		reqNames = new JList();
		
		childrenList = new DefaultListModel();
		initializeListSubReq(requirement);
		subReqNames = new JList(childrenList);
		
		validParentList = new DefaultListModel();
		addValidParents();
		
		JPanel subreqPane = new JPanel();
		subreqPane.setLayout(new BorderLayout());
		
		SpringLayout layout = new SpringLayout();
		
		addReq = new JButton("Add");
		removeReq = new JButton("Remove Children");
		removeParent = new JButton("Remove Parent");
		
		parentLabel = new JLabel("Parent:");
		childLabel = new JLabel("Children:");
		parentReq = new JLabel();
		
		if (this.requirement.getpUID().size() == 0){
			parentReq.setText("None");
		}
		else{
			try {
				parentReq.setText(RequirementDatabase.getInstance()
						.getRequirement(this.requirement.getpUID().get(0)).getName());
			} catch (RequirementNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		JRadioButton radioChild = new JRadioButton("Add Children");
		JRadioButton radioParent = new JRadioButton("Add Parent");
		
		radioChild.setSelected(true);
		
	    group = new ButtonGroup();
	    group.add(radioChild);
	    group.add(radioParent);
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(reqNames);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEtchedBorder());
		
		scrollPaneContainedReqs = new JScrollPane();
		scrollPaneContainedReqs.setViewportView(subReqNames);
		scrollPaneContainedReqs.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPaneContainedReqs.setBorder(BorderFactory.createEtchedBorder());
		
		layout.putConstraint(SpringLayout.NORTH, parentLabel, 5, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.NORTH, childLabel, 5, SpringLayout.SOUTH, parentLabel);
		
		layout.putConstraint(SpringLayout.NORTH, parentReq, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, parentReq, 14, SpringLayout.EAST, parentLabel);
		
		layout.putConstraint(SpringLayout.NORTH, scrollPaneContainedReqs, 5, SpringLayout.SOUTH, parentLabel);
		layout.putConstraint(SpringLayout.WEST, scrollPaneContainedReqs, 5, SpringLayout.EAST, childLabel);	
		layout.putConstraint(SpringLayout.EAST, scrollPaneContainedReqs, 5, SpringLayout.EAST, this);
		
		layout.putConstraint(SpringLayout.NORTH, removeReq, 5, SpringLayout.SOUTH, scrollPaneContainedReqs);
		
		layout.putConstraint(SpringLayout.NORTH, removeParent, 5, SpringLayout.SOUTH, scrollPaneContainedReqs);
		layout.putConstraint(SpringLayout.WEST, removeParent, 5, SpringLayout.EAST, removeReq);
		
		layout.putConstraint(SpringLayout.NORTH, radioParent, 5, SpringLayout.SOUTH, removeReq);
		
		layout.putConstraint(SpringLayout.NORTH, radioChild, 5, SpringLayout.SOUTH, removeReq);
		layout.putConstraint(SpringLayout.WEST, radioChild, 5, SpringLayout.EAST, radioParent);
		
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.SOUTH, radioParent);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 5, SpringLayout.EAST, childLabel);
		layout.putConstraint(SpringLayout.EAST, scrollPane, 5, SpringLayout.EAST, this);
		
		layout.putConstraint(SpringLayout.NORTH, addReq, 5, SpringLayout.SOUTH, scrollPane);		
		
		//layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.SOUTH, this);
		//layout.putConstraint(SpringLayout.WEST, scrollPane, 5, SpringLayout.EAST, parentLabel);

		layout.putConstraint(SpringLayout.WIDTH, subreqPane, 5, SpringLayout.WIDTH, this);

		this.setLayout(layout);
		this.add(scrollPaneContainedReqs);
		this.add(scrollPane);
		this.add(parentLabel);
		this.add(removeParent);
		this.add(removeReq);
		this.add(childLabel);
		this.add(parentReq);
		this.add(radioChild);
		this.add(radioParent);
		this.add(subreqPane);
		this.add(addReq);
		
		//Do other things here
		removeReq.setAction(new RemoveReqAction(new RemoveReqController(this, requirement, panel)));
		addReq.setAction(new AssignChildAction(new AssignChildController(this, requirement, panel)));
		refreshParentPanel();
		
		radioChild.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				refreshReqPanel();
			}
		});
		
		radioParent.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				refreshReqPanelForParents();
			}
		});
		
		refreshReqPanel();
	}
	
	private void initializeList() {
		List<Requirement> requirements = RequirementDatabase.getInstance().getAllRequirements();
		
		for(Requirement req :  requirements) {
				validChildList.addElement(req.getName());
		}
	}
	
	private void initializeListSubReq(Requirement requirement) {
		Requirement tempReq = null;
		for(int req : requirement.getSubRequirements()) {
			try {
				tempReq = RequirementDatabase.getInstance().getRequirement(req);
				childrenList.addElement(tempReq.getName());
			} catch (RequirementNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
	}
	
	public JList getList(){
		return reqNames;
	}
	
	public JList getListSubReq(){
		return subReqNames;
	}
	
	public void addValidChildren(){
		List<Requirement> requirements = RequirementDatabase.getInstance()
				.getAllRequirements();
		
		int rootID;
		rootID = checkParentRoot(this.requirement);
		for (Requirement req : requirements){
			if (req.getpUID().size() == 0){
				if (req.getrUID()!=rootID){
					validChildList.addElement(req.getName());
				}
			}
			
		}
	}
	
	public int checkParentRoot(Requirement aReq){
		Requirement tempReq = null;
		if(aReq.getpUID().size()==0){
			return aReq.getrUID();
		}
		else{
			try {
				tempReq = RequirementDatabase.getInstance()
				.getRequirement(aReq.getpUID().get(0));
			} catch (RequirementNotFoundException e) {
				e.printStackTrace();
			}
			return checkParentRoot(tempReq);
		}
			
	}
	
	public void addValidParents(){
		List<Requirement> requirements = RequirementDatabase.getInstance()
				.getAllRequirements();
		for(Requirement req : requirements){
			if (!containsCurrentRequirement(req)){
				validParentList.addElement(req.getName());
			}
		}
	}
	
	public boolean containsCurrentRequirement(Requirement req) {
		System.out.println(req.getName());
		Requirement child = null;
		Boolean check = false;
		if (req.equals(requirement)) {
			return true;
		} else {
			for (Integer i : req.getSubRequirements()) {				
				try {
					child = RequirementDatabase.getInstance()
							.getRequirement(i);
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
				check = containsCurrentRequirement(child);
				if(check) return check;
			}
			return false;
		}

	}

	public void refreshSubReqPanel() {
		childrenList = new DefaultListModel();
		initializeListSubReq(requirement);
		subReqNames = new JList(childrenList);
		scrollPaneContainedReqs.setViewportView(subReqNames);		
	}

	public void refreshReqPanel() {
		validChildList = new DefaultListModel();
		addValidChildren();
		reqNames = new JList(validChildList);
		scrollPane.setViewportView(reqNames);
	}
	
	public void refreshReqPanelForParents() {
		validParentList = new DefaultListModel();
		addValidParents();
		reqNames = new JList(validParentList);
		scrollPane.setViewportView(reqNames);
	}
	
	public void refreshParentPanel() {
		
		Requirement tempReq = null;
		if(requirement.getpUID().size()==0)
			parentReq.setText("");
		else {
			for(int ID : requirement.getpUID()) {
				try {
					tempReq = RequirementDatabase.getInstance()
					.getRequirement(ID);
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
			}
			parentReq.setText(tempReq.getName());
		}			
	}
}
