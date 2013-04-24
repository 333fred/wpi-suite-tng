/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Alex Woodyard
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.sql.Date;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationView.Status;

public class IterationViewTest {

	MainTabController controller;
	Iteration iteration;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setup(){
		controller = new MainTabController();
		iteration = new Iteration("Test Iteration", new Date(2013, 20, 04), new Date(2013, 04, 30));
			}
	
	@Test
	public void constructTest(){
		IterationView view = new IterationView(iteration, Status.CREATE, controller);
		assertNotNull(view);
		assertEquals(Status.CREATE, view.getStatus());
	}
	
	@Test
	public void editIteration(){
		IterationView edit = new IterationView(iteration, Status.EDIT, controller);
		assertNotNull(edit);
		assertEquals(Status.EDIT, edit.getStatus());
	}
}
