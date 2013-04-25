/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jason Whitehouse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationView.Status;

public class IterationViewTest {
	
	MainTabController mainTabController = null;
	Calendar startCalendar;
	Calendar endCalendar;
	
	@Before
	public void setup() {
		startCalendar = Calendar.getInstance();
		endCalendar = Calendar.getInstance();
		startCalendar.set(0, 0, 13);
		endCalendar.set(0, 0, 14);
		final Iteration existingIteration = new Iteration("Existing Iteration",
				startCalendar.getTime(), endCalendar.getTime());
		IterationDatabase.getInstance().set(new ArrayList<Iteration>());
		IterationDatabase.getInstance().add(existingIteration);
		startCalendar.set(0, 0, 10);
		endCalendar.set(0, 0, 12);
		if (mainTabController == null) {
			mainTabController = new MainTabController();
		}
	}
	
	@Test
	public void testInitializePanelCreate() {
		final IterationView it = new IterationView(mainTabController);
		Assert.assertNotNull(it);
	}
	
	@Test
	public void testInitializePanelEdit() {
		final Iteration iteration = new Iteration("Good Iteration",
				startCalendar.getTime(), endCalendar.getTime(), 1);
		final IterationView it = new IterationView(iteration, Status.EDIT,
				mainTabController);
		Assert.assertNotNull(it);
	}
	
	@Test
	public void testInitializePanelEditEmptyName() {
		final Iteration iteration = new Iteration("", startCalendar.getTime(),
				endCalendar.getTime(), 1);
		final IterationView it = new IterationView(iteration, Status.EDIT,
				mainTabController);
		it.updateSave(null);
		Assert.assertFalse(it.getButSave().isEnabled());
	}
	
	@Test
	public void testInitializePanelEditOutOfOrderDate() {
		final Iteration iteration = new Iteration("Good Iteration",
				endCalendar.getTime(), startCalendar.getTime(), 1);
		final IterationView it = new IterationView(iteration, Status.EDIT,
				mainTabController);
		it.updateSave(null);
		Assert.assertFalse(it.getButSave().isEnabled());
	}
	
	@Test
	public void testInitializePanelEditStartEndSameDate() {
		final Iteration iteration = new Iteration("Good Iteration",
				startCalendar.getTime(), startCalendar.getTime(), 1);
		final IterationView it = new IterationView(iteration, Status.EDIT,
				mainTabController);
		it.updateSave(null);
		Assert.assertFalse(it.getButSave().isEnabled());
	}
	
	@Test
	public void testInitializePanelView() {
		final Iteration iteration = new Iteration("Good Iteration",
				startCalendar.getTime(), endCalendar.getTime(), 1);
		final IterationView it = new IterationView(iteration, Status.VIEW,
				mainTabController);
		Assert.assertNotNull(it);
	}
}
