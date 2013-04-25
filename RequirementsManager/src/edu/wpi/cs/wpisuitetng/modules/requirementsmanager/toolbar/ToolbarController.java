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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import java.awt.Component;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarController;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabView;

/**
 * Controller for the defect tracker toolbar. Keeps track of the displayed tab
 * in a MainTabController and displays the group of controls provided by the
 * displayed components' getGroup method, if it is an instance of
 * IToolbarGroupProvider. If the current tab has no associated toolbar group, no
 * additional group is shown in the toolbar.
 */
public class ToolbarController extends DefaultToolbarController implements
		ChangeListener {
	
	private ToolbarGroupView relevantTabGroup;
	
	/**
	 * Control the given DefaultToolbarView based on the state of the tabs in
	 * tabController.
	 * 
	 * @param toolbarView
	 *            The toolbar to add/remove groups from
	 * @param tabController
	 *            The MainTabController to listen to for changes
	 */
	public ToolbarController(final DefaultToolbarView toolbarView,
			final MainTabController tabController) {
		super(toolbarView);
		tabController.addChangeListener(this);
	}
	
	private void setRelevantTabGroup(final ToolbarGroupView group) {
		// keep track of only one toolbar group for the active tab
		if (relevantTabGroup != null) {
			setRelevant(relevantTabGroup, false);
		}
		relevantTabGroup = group;
		if (relevantTabGroup != null) {
			setRelevant(relevantTabGroup, true);
		}
	}
	
	@Override
	public void stateChanged(final ChangeEvent e) {
		// TODO: there has to be a cleaner way to do this
		if (e.getSource() instanceof MainTabView) {
			final MainTabView view = (MainTabView) e.getSource();
			final Component selectedComponent = view.getSelectedComponent();
			if (selectedComponent instanceof IToolbarGroupProvider) {
				final IToolbarGroupProvider provider = (IToolbarGroupProvider) selectedComponent;
				setRelevantTabGroup(provider.getGroup());
			} else {
				setRelevantTabGroup(null);
			}
		}
	}
	
}
