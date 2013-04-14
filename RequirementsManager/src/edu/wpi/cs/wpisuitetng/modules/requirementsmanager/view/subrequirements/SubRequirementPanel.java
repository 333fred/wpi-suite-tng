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
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * @author Kyle, Matt, Chris
 *
 */
public class SubRequirementPanel extends JPanel {
	
	/** The list of requirements that the view is displaying */
	
	
	private JList requirementTable;
	private DefaultListModel reqNameString;
	
	private JButton assignChildren;
	private JButton assignParent;
	
	private JScrollPane scrollPane;
	
	
	public SubRequirementPanel(Requirement requirement, DetailPanel panel) {
		
		
		reqNameString = new DefaultListModel();
		initializeList();
		
		requirementTable = new JList(reqNameString);
		
		JPanel subreqPane = new JPanel();
		subreqPane.setLayout(new BorderLayout());
		
		SpringLayout layout = new SpringLayout();
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		
		assignChildren = new JButton("Assign Children");
		assignParent = new JButton("Assign Parent");
		
		buttonsPanel.add(assignChildren);
		buttonsPanel.add(assignParent);
		
		scrollPane = new JScrollPane(requirementTable);
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
		
		
	}
	
	private void initializeList() {
		List<Requirement> requirements = RequirementDatabase.getInstance().getAllRequirements();
		
		for(Requirement req :  requirements) {
				reqNameString.addElement(req.getName());
		}
	}
}
