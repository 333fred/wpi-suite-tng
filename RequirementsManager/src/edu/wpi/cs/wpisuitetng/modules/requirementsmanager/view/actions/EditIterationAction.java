/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		@author Team Swagasaurus
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

/**
 * Action which concerns editing of an iteration
 */
public class EditIterationAction extends AbstractAction {
	
	/** The iteration view to receive the iteration from */
	private final IterationTreeView iterationTreeView;
	
	/** The tab controller to open the new tab in */
	private final MainTabController tabController;
	
	public EditIterationAction(final IterationTreeView iterationTreeView,
			final MainTabController tabController) {
		this.iterationTreeView = iterationTreeView;
		this.tabController = tabController;
	}
	
	/** Opens the currently selected iterations in the IterationTreeView */
	@Override
	public void actionPerformed(final ActionEvent e) {
		
		final List<Iteration> iterationsToOpen = iterationTreeView
				.getSelectedIterations();
		final int numIterations = iterationsToOpen.size();
		
		for (final Iteration i : iterationsToOpen) {
			if ((i == null) || i.getName().equals("Backlog")
					|| i.getName().equals("Deleted")) {
				continue;
			}
			boolean IterationIsOpen = false;
			for (int j = 0; j < tabController.getTabView().getTabCount(); j++) {
				for (int k = 0; k < numIterations; k++) {
					final Component tabComponent = tabController.getTabView()
							.getComponentAt(j);
					if ((tabComponent instanceof IterationView)
							&& tabController.getTabView().getTitleAt(j)
									.equals(iterationsToOpen.get(k).getName())) {
						
						tabController.switchToTab(j);
						IterationIsOpen = true;
					}
				}
			}
			if (!IterationIsOpen) {
				tabController.addIterationTab(i);
			}
		}
		
	}	
}
