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
	
	//fields for the top half of sub requirement panel
	private JLabel parentReq;
	private DefaultListModel childrenList; //List of subrequirements
	private JList topReqNames;
	private JScrollPane topScrollPane; //ScrollPane for subreqs/parents
	
	//fields for the bottom half of sub requirement panel
	private DefaultListModel validChildList; //List of requirements available to add as children
	private DefaultListModel validParentList; //List of requirements available to add as parents
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
		//initializeList();
		addValidChildren();
		bottomReqNames = new JList();
		
		childrenList = new DefaultListModel();
		initializeTopList(requirement);
		topReqNames = new JList(childrenList);
		
		validParentList = new DefaultListModel();
		addValidParents();
		
		JPanel subreqPane = new JPanel();
		subreqPane.setLayout(new BorderLayout());
		
		SpringLayout layout = new SpringLayout();
		
		addReq = new JButton("Add");
		removeChild = new JButton("Remove Children");
		
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
		
	    btnGroup = new ButtonGroup();
	    btnGroup.add(radioChild);
	    btnGroup.add(radioParent);
		
		topScrollPane = new JScrollPane();
		topScrollPane.setViewportView(topReqNames);
		topScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		topScrollPane.setBorder(BorderFactory.createEtchedBorder());
		
		bottomScrollPane = new JScrollPane();
		bottomScrollPane.setViewportView(bottomReqNames);
		bottomScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		bottomScrollPane.setBorder(BorderFactory.createEtchedBorder());
		
		layout.putConstraint(SpringLayout.NORTH, parentLabel, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, parentLabel, 16, SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.NORTH, childLabel, 5, SpringLayout.SOUTH, parentLabel);
		layout.putConstraint(SpringLayout.WEST, childLabel, 16, SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.NORTH, parentReq, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, parentReq, 14, SpringLayout.EAST, parentLabel);
		
		layout.putConstraint(SpringLayout.NORTH, topScrollPane, 5, SpringLayout.SOUTH, parentLabel);
		layout.putConstraint(SpringLayout.WEST, topScrollPane, 5, SpringLayout.EAST, childLabel);	
		layout.putConstraint(SpringLayout.EAST, topScrollPane, -64, SpringLayout.EAST, this);
		
		layout.putConstraint(SpringLayout.NORTH, removeChild, 5, SpringLayout.SOUTH, topScrollPane);
		layout.putConstraint(SpringLayout.WEST, removeChild, 16, SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.NORTH, removeParent, 5, SpringLayout.SOUTH, topScrollPane);
		layout.putConstraint(SpringLayout.WEST, removeParent, 5, SpringLayout.EAST, removeChild);
		
		layout.putConstraint(SpringLayout.NORTH, radioChild, 5, SpringLayout.SOUTH, removeChild);
		layout.putConstraint(SpringLayout.WEST, radioChild, 16, SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.NORTH, radioParent, 5, SpringLayout.SOUTH, removeChild);
		layout.putConstraint(SpringLayout.WEST, radioParent, 5, SpringLayout.EAST, radioChild);
		
		layout.putConstraint(SpringLayout.NORTH, bottomScrollPane, 5, SpringLayout.SOUTH, radioParent);
		layout.putConstraint(SpringLayout.WEST, bottomScrollPane, 5, SpringLayout.EAST, childLabel);
		layout.putConstraint(SpringLayout.EAST, bottomScrollPane, -64, SpringLayout.EAST, this);
		
		layout.putConstraint(SpringLayout.NORTH, addReq, 5, SpringLayout.SOUTH, bottomScrollPane);
		layout.putConstraint(SpringLayout.WEST, addReq, 16, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, addReq, 0, SpringLayout.EAST, removeChild);
		
		//layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.SOUTH, this);
		//layout.putConstraint(SpringLayout.WEST, scrollPane, 5, SpringLayout.EAST, parentLabel);

		layout.putConstraint(SpringLayout.WIDTH, subreqPane, 5, SpringLayout.WIDTH, this);

		this.setLayout(layout);
		this.add(topScrollPane);
		this.add(bottomScrollPane);
		this.add(parentLabel);
		this.add(removeParent);
		this.add(removeChild);
		this.add(childLabel);
		this.add(parentReq);
		this.add(radioChild);
		this.add(radioParent);
		this.add(subreqPane);
		this.add(addReq);
		
		//Do other things here
		removeChild.setAction(new RemoveChildAction(new RemoveChildController(this, requirement, panel)));
		addReq.setAction(new AssignChildAction(new AssignChildController(this, requirement, panel)));
		removeParent.setAction(new RemoveParentAction(new RemoveParentController(this, requirement, panel)));
		refreshParentLabel();
		
		radioChild.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setActionToChild();
				refreshValidChildren();
				parentSelected = false;
			}
		});
		
		radioParent.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setActionToParent();
				refreshValidParents();
				parentSelected = true;
				updateAddButtontext();
			}
		});
		
		refreshValidChildren();
	}

	private void initializeBottomListToValidChildren() {
		List<Requirement> requirements = RequirementDatabase.getInstance().getAllRequirements();
		
		for(Requirement req :  requirements) {
				validChildList.addElement(req.getName());
		}
	}
	
	private void initializeTopList(Requirement requirement) {
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
	
	public void addValidParents(){
		List<Requirement> requirements = RequirementDatabase.getInstance()
				.getAllRequirements();
		Requirement parentReq = null;
		if(requirement.getpUID().size()>0){
			try {
				parentReq = RequirementDatabase.getInstance()
						.getRequirement(requirement.getpUID().get(0));
			} catch (RequirementNotFoundException e) {
				e.printStackTrace();
			}
		}
		for(Requirement req : requirements){
				if(!containsCurrentRequirement(requirement,req)){
					if(!req.equals(parentReq))
						validParentList.addElement(req.getName());
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
	
	public boolean containsCurrentRequirement(Requirement req, Requirement current) {
		System.out.println(req.getName());
		Requirement child = null;
		Boolean check = false;
		if (req.getrUID()==current.getrUID()) {
			return true;
		} else {
			for (Integer i : req.getSubRequirements()) {				
				try {
					child = RequirementDatabase.getInstance()
							.getRequirement(i);
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
				check = containsCurrentRequirement(child, current);
				if(check) return check;
			}
			return false;
		}
	}
	
	

	public void refreshTopPanel() {
		childrenList = new DefaultListModel();
		initializeTopList(requirement);
		topReqNames = new JList(childrenList);
		topScrollPane.setViewportView(topReqNames);		
	}

	public void refreshValidChildren() {
		validChildList = new DefaultListModel();
		addValidChildren();
		bottomReqNames = new JList(validChildList);
		bottomScrollPane.setViewportView(bottomReqNames);
	}
	
	public void refreshValidParents() {
		validParentList = new DefaultListModel();
		addValidParents();
		bottomReqNames = new JList(validParentList);
		bottomScrollPane.setViewportView(bottomReqNames);
	}
	
	public void refreshParentLabel() {
		
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
		updateAddButtontext();
	}
	
	public void updateAddButtontext(){
		if (requirement.getpUID().size() > 0){
			addReq.setText("Change Parent");
		}
		else{
			addReq.setText("Assign parent");
		}
	}
	
	public JList getList(){
		return bottomReqNames;
	}
	
	public JList getListSubReq(){
		return topReqNames;
	}
	
	protected void setActionToParent() {
		addReq.setAction(new AssignParentAction(new AssignParentController(this, requirement, panel)));
		
	}

	protected void setActionToChild() {
		addReq.setAction(new AssignChildAction(new AssignChildController(this, requirement, panel)));		
	}
	
}
