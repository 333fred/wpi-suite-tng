/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * Class for creating a right click menu in IterationTreeView, when a user right
 * clicks on a requirement node
 * 
 * @author Nick
 * 
 */

public class SubReqRequirementPopupMenu extends JPopupMenu implements
		ActionListener, ISaveNotifier {
	
	/** Menu options for the PopupMenu */
	private JMenuItem menuViewRequirement;
	
	private JMenuItem menuRemoveParent;
	private JMenu menuRemoveChildren;
	
	/** The tab controller to open tabs in */
	private final MainTabController tabController;
	
	/** List of the selected requirements to possibly open */
	private final List<Requirement> selectedRequirements;
	
	/**
	 * Constructor for the pop-up menu, determining what should be displayed
	 * Based on permissions and the requirement's status, parent, and children
	 * 
	 * @param tabController
	 *            The Maintabcontroller we are inside of
	 * @param selectedRequirements
	 *            The indexes of the selected requirements
	 */
	public SubReqRequirementPopupMenu(final MainTabController tabController,
			final List<Requirement> selectedRequirements) {
		this.tabController = tabController;
		this.selectedRequirements = selectedRequirements;
		
		if (!PermissionModel.getInstance().getUserPermissions() // If we can't
																// edit a
																// requirement,
																// say we view
																// it
				.canEditRequirement()) {
			menuViewRequirement = new JMenuItem("View Requirement");
			menuViewRequirement.addActionListener(this);
			add(menuViewRequirement);
		} else {// Otherwise, say edit requirement
		
			menuViewRequirement = new JMenuItem("Edit Requirement");
			menuViewRequirement.addActionListener(this);
			add(menuViewRequirement);
			
			final Requirement tempReq = selectedRequirements.get(0);
			Requirement tempSubReq = null;
			
			if ((tempReq.getStatus() != Status.DELETED)
					&& (tempReq.getStatus() != Status.COMPLETE)) { // If the
																	// requirement
																	// is
																	// editable,
				if (tempReq.getpUID().size() > 0) {// If it has a parent,
													// suggest a remove parent
													// option
					menuRemoveParent = new JMenuItem("Remove Parent");
					menuRemoveParent.addActionListener(this);
					add(menuRemoveParent);
				}
				
				if (tempReq.getSubRequirements().size() > 0) { // If it has
																// children,
																// create a
																// remove child
																// menu
					menuRemoveChildren = new JMenu("Remove Children");
					JMenuItem menuChild = null;
					
					for (final int reqID : tempReq.getSubRequirements()) {
						try { // For all the subrequirements, try to add it to
								// the remove child menu
							tempSubReq = RequirementDatabase.getInstance().get(
									reqID);
							menuChild = new JMenuItem(tempSubReq.getName(),
									tempSubReq.getrUID()); // Send in name and
															// ID (ID is stored
															// as out-dated
															// mnemonic)
							menuChild.addActionListener(this);
							menuRemoveChildren.add(menuChild);
						} catch (final RequirementNotFoundException e) {
							e.printStackTrace();
						}
					}
					menuRemoveChildren.addActionListener(this);
					addSeparator(); // Add a separator before the menu
					add(menuRemoveChildren);
				}
			}
		}
		
	}
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		final Requirement tempReq = selectedRequirements.get(0);
		Requirement otherReq = null;
		final UpdateRequirementRequestObserver reqObserver = new UpdateRequirementRequestObserver(
				this);
		final RequirementsController controller = new RequirementsController();
		
		if (e.getSource().equals(menuViewRequirement)) {
			for (final Requirement r : selectedRequirements) { // View the
																// requirements
																// that have
																// been selected
				tabController.addViewRequirementTab(r);
			}
		} else if (e.getSource().equals(menuRemoveParent)) {
			try {
				otherReq = RequirementDatabase.getInstance().get(
						tempReq.getpUID().get(0));// Grab the parent req
			} catch (final RequirementNotFoundException e1) {
				e1.printStackTrace();
			}
			otherReq.removeSubRequirement(tempReq.getrUID()); // Remove parent's
																// child
			tempReq.removePUID(otherReq.getrUID()); // Remove child's parent
			controller.save(otherReq, reqObserver); // Save the requirements
			controller.save(tempReq, reqObserver);
			
		} else if (!e.getSource().equals(menuRemoveChildren)) {
			try {
				otherReq = RequirementDatabase.getInstance().get(
						((JMenuItem) e.getSource()).getMnemonic());// Grab the
																	// parent
																	// req
			} catch (final RequirementNotFoundException e1) {
				e1.printStackTrace();
			}
			otherReq.removePUID(tempReq.getrUID());// Remove the child's parent
			tempReq.removeSubRequirement(otherReq.getrUID());// Remove the
																// parent's
																// child
			controller.save(otherReq, reqObserver);// Save the requirements
			controller.save(tempReq, reqObserver);
		}
		
	}
	
	@Override
	public void fail(final Exception exception) {
	}
	
	@Override
	public void responseError(final int statusCode, final String statusMessage) {
	}
	
	@Override
	public void responseSuccess() {
		 // On success, update sub-reqs and total estimates
		for (int i = 0; i < tabController.getTabView().getTabCount(); i++) {
			if (tabController.getTabView().getComponentAt(i) instanceof DetailPanel) {
				(((DetailPanel) tabController.getTabView().getComponentAt(i)))
						.updateTotalEstimate();
				(((DetailPanel) tabController.getTabView().getComponentAt(i)))
						.updateSubReqTab();
			}
		}
	}
	
}
