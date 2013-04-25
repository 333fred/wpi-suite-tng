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

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationView.Status;

public class IterationViewTest {

	MainTabController mainTabController = null;
	Calendar startCalendar;
	Calendar endCalendar;

	
	@Before
	public void setup(){
		startCalendar = Calendar.getInstance();
		endCalendar = Calendar.getInstance();
		startCalendar.set(0, 0, 10);
		endCalendar.set(0, 0, 12);
		if(this.mainTabController == null){
			this.mainTabController = new MainTabController();
		}
	}
	

	@Test
	public void testInitializePanelCreate() {
		IterationView it = new IterationView(mainTabController);
		assertNotNull(it);
	}
	
	@Test
	public void testInitializePanelView() {
		Iteration iteration = new Iteration("Good Iteration",
				startCalendar.getTime(), endCalendar.getTime(), 1);
		IterationView it = new IterationView(iteration, Status.VIEW, mainTabController);
		assertNotNull(it);
	}
	
	@Test
	public void testInitializePanelEdit() {
		Iteration iteration = new Iteration("Good Iteration",
				startCalendar.getTime(), endCalendar.getTime(), 1);
		IterationView it = new IterationView(iteration, Status.EDIT, mainTabController);
		assertNotNull(it);
	}
	
	@Test
	public void testInitializePanelEditEmptyName() {
		Iteration iteration = new Iteration("",
				startCalendar.getTime(), endCalendar.getTime(), 1);
		IterationView it = new IterationView(iteration, Status.EDIT, mainTabController);
		it.updateSave(null);
		assertFalse(it.getButSave().isEnabled());
	}
	
	@Test
	public void testInitializePanelEditOutOfOrderDate() {
		Iteration iteration = new Iteration("Good Iteration",
				endCalendar.getTime(), startCalendar.getTime(), 1);
		IterationView it = new IterationView(iteration, Status.EDIT, mainTabController);
		it.updateSave(null);
		assertFalse(it.getButSave().isEnabled());
	}
	
	@Test
	public void testInitializePanelEditStartEndSameDate() {
		Iteration iteration = new Iteration("Good Iteration",
				startCalendar.getTime(), startCalendar.getTime(), 1);
		IterationView it = new IterationView(iteration, Status.EDIT, mainTabController);
		it.updateSave(null);
		assertFalse(it.getButSave().isEnabled());
	}
}
