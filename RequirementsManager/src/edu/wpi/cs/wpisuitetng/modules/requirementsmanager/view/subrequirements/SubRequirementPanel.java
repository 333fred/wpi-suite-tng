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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	
	
	private JTree requirementTree;
	private DefaultMutableTreeNode top;
	private DefaultListModel reqNameString;
	
	private JButton assignChildren;
	private JButton assignParent;
	
	private JScrollPane scrollPane;
	public List<Requirement> visitingReqs;
	public List<Requirement> visitedReqs;
	
	
	public SubRequirementPanel(Requirement requirement, DetailPanel panel) {		
		
		reqNameString = new DefaultListModel();
		initializeList();		
		
		this.top = new DefaultMutableTreeNode("Requirements");
		requirementTree = new JTree(top);
		requirementTree.setDragEnabled(false);
		initializeTree();
		
		JPanel subreqPane = new JPanel();
		subreqPane.setLayout(new BorderLayout());
		
		SpringLayout layout = new SpringLayout();
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		
		assignChildren = new JButton("Assign Children");
		assignParent = new JButton("Assign Parent");
		
		buttonsPanel.add(assignChildren);
		buttonsPanel.add(assignParent);
		
		scrollPane = new JScrollPane(requirementTree);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEtchedBorder());
		
		subreqPane.add(scrollPane, BorderLayout.CENTER);
		
		layout.putConstraint(SpringLayout.NORTH, subreqPane, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, buttonsPanel, 0,SpringLayout.SOUTH, subreqPane);
		layout.putConstraint(SpringLayout.WIDTH, subreqPane, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.WIDTH, buttonsPanel, 0, SpringLayout.WIDTH, this);
		this.setLayout(layout);
		this.add(subreqPane);
		this.add(buttonsPanel);
		
		//Do other things here
		assignChildren.setAction(new AssignChildAction(new AssignChildController(this, requirement, panel)));
		assignParent.setAction(new AssignParentAction(new AssignParentController(this, requirement, panel)));
		
		
	}
	
	private void initializeList() {
		List<Requirement> requirements = RequirementDatabase.getInstance().getAllRequirements();
		
		for(Requirement req :  requirements) {
				reqNameString.addElement(req.getName());
		}
	}
	
	public void initializeTree() {
		List<Requirement> requirements = RequirementDatabase.getInstance().getAllRequirements();

		DefaultMutableTreeNode requirementNode = null;
		DefaultMutableTreeNode tempNode = null;
		this.top.removeAllChildren();
		
		for (Requirement anReq : requirements) {
			if(anReq.getpUID().size()==0){
				requirementNode = new DefaultMutableTreeNode(anReq.getName());

			for (Integer aReq : anReq.getSubRequirements()) {
				try {
					tempNode = new DefaultMutableTreeNode(
							RequirementDatabase.getInstance()
							.getRequirement(aReq).getName());
					requirementNode.add(tempNode);
					cycleTree(aReq, tempNode);
				} catch (RequirementNotFoundException e) {
					
				}
			}
			this.top.add(requirementNode);
			}
		}
	}
	
	private void cycleTree(int aPrevReq, DefaultMutableTreeNode node){
		DefaultMutableTreeNode tempNode = null;

		Requirement anReq = null;
		try {
			anReq = RequirementDatabase.getInstance()
					.getRequirement(aPrevReq);
		} catch (RequirementNotFoundException e1) {
			e1.printStackTrace();
		}

			for (Integer aReq : anReq.getSubRequirements()) {
				try {
					tempNode = new DefaultMutableTreeNode(
							RequirementDatabase.getInstance()
							.getRequirement(aReq).getName());
					node.add(tempNode);
					cycleTree(aReq, tempNode);
				} catch (RequirementNotFoundException e) {
					
				}
			}

		}
	
	public JTree getTree(){
		return requirementTree;
	}

	public boolean checkCycle(Requirement parent, Requirement child) {
		Requirement tempReq = child;
		
		while(tempReq!=null){
			if(parent.getrUID() == tempReq.getrUID()) {
				return true;
			}
			else{
				/*try {
					tempReq = RequirementDatabase.getInstance().getRequirement(tempReq.getpUID());
				} catch (RequirementNotFoundException e) {
					return false;
				}*/	
			}
		}
		return false;
	}
	
	public boolean checkDirectedCycle() {
		List<Requirement> requirements = RequirementDatabase.getInstance()
				.getAllRequirements();
		visitingReqs = new ArrayList<Requirement>();
		visitedReqs = new ArrayList<Requirement>();
		boolean check = false;
		Requirement tempReq = null;

		for (Requirement anReq : requirements) {
			if (!visitedReqs.contains(anReq)) { //If requirement was not visited
				visitingReqs.add(anReq); //Add to visiting
				for (int subreq : anReq.getSubRequirements()) { //Go through sub requirements
					try {
						tempReq = RequirementDatabase.getInstance().getRequirement(subreq);
					} catch (RequirementNotFoundException e) {
						e.printStackTrace();
					}
					if (!visitedReqs.contains(tempReq)) //If this subrequirement hasn't been visited, process
						check = process(subreq);
					if(check) return true;
				}
				
				if(visitingReqs.contains(anReq)) //Safety removal
					visitingReqs.remove(anReq);
				visitedReqs.add(anReq);
			}
		}
		return false;
	}
	
	public boolean process(int subreq){
		boolean check = false;
		Requirement tempReq = null;
		Requirement tempReqLoop = null;
		try {
			tempReq = RequirementDatabase.getInstance().getRequirement(subreq);
		} catch (RequirementNotFoundException e) {
			e.printStackTrace();
		}
		
		if(visitingReqs.contains(tempReq))
			return true;		
		
		System.out.println(tempReq.getName() + "HERECT\n");
		visitingReqs.add(tempReq);
			for(int anReq : tempReq.getSubRequirements()) {
				try {
					tempReqLoop = RequirementDatabase.getInstance().getRequirement(anReq);
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
				if(!visitedReqs.contains(tempReqLoop))
					check = process(tempReqLoop.getrUID());
				if(check) return true;
			}		
		visitingReqs.remove(tempReq);
		visitedReqs.add(tempReq);
		return false;		
	}
		
	
}
