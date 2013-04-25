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
 */

@SuppressWarnings ("serial")
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
		
		// If we can't edit a requirement, say we view it
		if (!PermissionModel.getInstance().getUserPermissions()
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
			
			// If the requirement is editable
			if ((tempReq.getStatus() != Status.DELETED)
					&& (tempReq.getStatus() != Status.COMPLETE)) {
				// If it has a parent, suggest a remove parent option
				if (tempReq.getpUID().size() > 0) {
					menuRemoveParent = new JMenuItem("Remove Parent");
					menuRemoveParent.addActionListener(this);
					add(menuRemoveParent);
				}
				
				// If it has children, create a remove child menu
				if (tempReq.getSubRequirements().size() > 0) {
					menuRemoveChildren = new JMenu("Remove Children");
					JMenuItem menuChild = null;
					
					for (final int reqID : tempReq.getSubRequirements()) {
						// For all the subrequirements, try to add it to the
						// remove child menu
						try {
							tempSubReq = RequirementDatabase.getInstance().get(
									reqID);
							// Send in name and ID
							menuChild = new JMenuItem(tempSubReq.getName());
							menuChild.putClientProperty(0, reqID);
							menuChild.addActionListener(this);
							menuRemoveChildren.add(menuChild);
						} catch (final RequirementNotFoundException e) {
							e.printStackTrace();
						}
					}
					menuRemoveChildren.addActionListener(this);
					// Add a separator before the menu
					addSeparator();
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
			// View the requirements that have been selected
			for (final Requirement r : selectedRequirements) {
				tabController.addViewRequirementTab(r);
			}
		} else if (e.getSource().equals(menuRemoveParent)) {
			try {
				// Grab the parent req
				otherReq = RequirementDatabase.getInstance().get(
						tempReq.getpUID().get(0));
			} catch (final RequirementNotFoundException e1) {
				e1.printStackTrace();
			}
			// Remove parent's child
			otherReq.removeSubRequirement(tempReq.getrUID());
			// Remove child's parent
			tempReq.removePUID(otherReq.getrUID());
			// Save the requirements
			controller.save(otherReq, reqObserver);
			controller.save(tempReq, reqObserver);
			
		} else if (!e.getSource().equals(menuRemoveChildren)) {
			try {
				// Grab the parent req
				otherReq = RequirementDatabase.getInstance().get(
						((Integer) ((JMenuItem) e.getSource())
								.getClientProperty(0)));
			} catch (final RequirementNotFoundException e1) {
				e1.printStackTrace();
			}
			// Remove the child's parent
			otherReq.removePUID(tempReq.getrUID());
			// Remove the parent's child
			tempReq.removeSubRequirement(otherReq.getrUID());
			// Save the requirements
			controller.save(otherReq, reqObserver);
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
