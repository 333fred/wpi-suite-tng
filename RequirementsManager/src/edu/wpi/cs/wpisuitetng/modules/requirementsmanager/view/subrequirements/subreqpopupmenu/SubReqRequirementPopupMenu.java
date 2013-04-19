/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		Nick
 ********************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subreqpopupmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;

/**
 * Class for creating a right click menu in IterationTreeView, when a user right
 * clicks on a requirement node
 * 
 * @author Nick
 * 
 */

public class SubReqRequirementPopupMenu extends JPopupMenu implements ActionListener, ISaveNotifier {

	/** Menu options for the PopupMenu */
	private JMenuItem menuViewRequirement;
	
	private JMenuItem menuRemoveParent;
	private JMenu menuRemoveChildren;

	/** The tab controller to open tabs in */
	private MainTabController tabController;

	/** List of the selected requirements to possibly open */
	private List<Requirement> selectedRequirements;

	public SubReqRequirementPopupMenu(MainTabController tabController,
			List<Requirement> selectedRequirements) {
		this.tabController = tabController;
		this.selectedRequirements = selectedRequirements;

		if (selectedRequirements.size() == 1) {
			menuViewRequirement = new JMenuItem("Edit Requirement");
		} else {
			menuViewRequirement = new JMenuItem("Edit Requirements");
		}		

		menuViewRequirement.addActionListener(this);
		add(menuViewRequirement);
		
		Requirement tempReq = selectedRequirements.get(0);
		Requirement tempSubReq = null;
		
		if(tempReq.getpUID().size()>0){
			menuRemoveParent = new JMenuItem("Remove Parent");
			menuRemoveParent.addActionListener(this);
			add(menuRemoveParent);			
		}
		
		if(tempReq.getSubRequirements().size()>0){
			menuRemoveChildren = new JMenu("Remove Children");
			JMenuItem menuChild = null;
			
			for(int reqID : tempReq.getSubRequirements()){
				try {
					tempSubReq = RequirementDatabase.getInstance().get(reqID);
					menuChild = new JMenuItem(tempSubReq.getName());
					menuChild.addActionListener(this);
					menuRemoveChildren.add(menuChild);
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
			}
			menuRemoveChildren.addActionListener(this);
			addSeparator();
			add(menuRemoveChildren);	
		}
			

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Requirement tempReq = selectedRequirements.get(0);
		Requirement otherReq = null;
		UpdateRequirementRequestObserver reqObserver = new UpdateRequirementRequestObserver(this);
		RequirementsController controller = new RequirementsController();
		
		if(e.getSource().equals(menuViewRequirement)){
			for (Requirement r : selectedRequirements) {
				tabController.addViewRequirementTab(r);
			}
		}else if(e.getSource().equals(menuRemoveParent)){
			try {
				otherReq = RequirementDatabase.getInstance().get(tempReq.getpUID().get(0));
			} catch (RequirementNotFoundException e1) {
				e1.printStackTrace();
			}
			otherReq.removeSubRequirement(tempReq.getrUID());
			tempReq.removePUID(otherReq.getrUID());
			controller.save(otherReq, reqObserver);
			controller.save(tempReq, reqObserver);
				
		}else if(!e.getSource().equals(menuRemoveChildren)){
			otherReq = RequirementDatabase.getInstance().getRequirement(((JMenuItem) e.getSource()).getText());
			otherReq.removePUID(tempReq.getrUID());
			tempReq.removeSubRequirement(otherReq.getrUID());
			controller.save(otherReq, reqObserver);
			controller.save(tempReq, reqObserver);
			}
		
	}

	@Override
	public void responseSuccess() {
	}

	@Override
	public void responseError(int statusCode, String statusMessage) {
	}

	@Override
	public void fail(Exception exception) {
	}

}
