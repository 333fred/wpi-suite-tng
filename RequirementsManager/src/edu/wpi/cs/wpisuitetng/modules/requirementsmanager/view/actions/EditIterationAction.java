/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		
 ********************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationTreeView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationView;

public class EditIterationAction extends AbstractAction {

	/** The iteration view to receive the iteration from */
	private IterationTreeView iterationTreeView;

	/** The tab controller to open the new tab in */
	private MainTabController tabController;

	public EditIterationAction(IterationTreeView iterationTreeView,
			MainTabController tabController) {
		this.iterationTreeView = iterationTreeView;
		this.tabController = tabController;
	}

	/** Opens the currently selected iterations in the IterationTreeView */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		List<Iteration> iterationsToOpen = iterationTreeView
				.getSelectedIterations();
		int numIterations = iterationsToOpen.size();

		for (Iteration i : iterationsToOpen) {
			if (i == null || i.getName().equals("Backlog")
					|| i.getName().equals("Deleted"))
				continue;
			boolean IterationIsOpen = false;
			for (int j = 0; j < this.tabController.getTabView().getTabCount(); j++) {
				for (int k = 0; k < numIterations; k++) {
					Component tabComponent = this.tabController.getTabView()
							.getComponentAt(j);
					if (tabComponent instanceof IterationView
							&& this.tabController.getTabView().getTitleAt(j)
									.equals(iterationsToOpen.get(k).getName())) {

						this.tabController.switchToTab(j);
						IterationIsOpen = true;
					}
				}
			}
			if (!IterationIsOpen) {
				tabController.addIterationTab(i);
			}
		}

	}
	/*
	 * @Override public void actionPerformed(ActionEvent e) { Iteration[]
	 * iterationsToOpen = iterationTreeView.getSelectedIterations(); + int
	 * numIterations = iterationsToOpen.length; for (Iteration i :
	 * iterationsToOpen) { if (i == null || i.getName().equals("Backlog"))
	 * continue; - tabController.addEditIterationTab(i); + boolean
	 * IterationIsOpen = false; + for (int j = 0; j <
	 * this.tabController.getTabView().getTabCount(); j++) { + for (int k = 0; k
	 * < numIterations; k++) { + if
	 * (this.tabController.getTabView().getTitleAt(j
	 * ).equals(iterationsToOpen[k].getName())) { +
	 * this.tabController.switchToTab(j); + IterationIsOpen = true; + } + } + }
	 * + if (!IterationIsOpen) { + tabController.addEditIterationTab(i); + } }
	 */

}
