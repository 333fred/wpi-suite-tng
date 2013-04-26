/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kyle Burns
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.ATest;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.task.DetailTaskView;

public class DetailTaskViewTest {
	
	@Before
	public void setUp() {
		final User u1 = new User("user1", "user1", "password", 1);
		final List<Integer> subReqs = new ArrayList<Integer>();
		final List<Note> notes = new ArrayList<Note>();
		final List<String> assignees = new ArrayList<String>();
		final List<Integer> pUID = new ArrayList<Integer>();
		final List<Task> tasks = new ArrayList<Task>();
		final List<ATest> tests = new ArrayList<ATest>();
		final Requirement r1 = new Requirement("name", "desc", "v1.0",
				Type.USER_STORY, subReqs, notes, 1, 5, 5, assignees, pUID,
				tasks);
		final Requirement r2 = new Requirement("name", "desc", "v1.0",
				Type.EPIC, subReqs, notes, 1, 5, 5, assignees, pUID, tasks);
		final Requirement r3 = new Requirement("name", "desc", "v1.0",
				Type.THEME, subReqs, notes, 1, 5, 5, assignees, pUID, tasks);
		
		r1.setPriority(Priority.HIGH);
		r1.setTests(tests);
		r1.addUser("user1");
		
		r2.setPriority(Priority.MEDIUM);
		r2.setTests(tests);
		r2.setStatus(Status.DELETED);
		
		r3.setPriority(Priority.LOW);
		r3.setTests(tests);
		r3.setStatus(Status.COMPLETE);
		r3.addUser("user1");
		
		final Iteration i1 = new Iteration();
		final MainTabController m1 = new MainTabController();
		final DetailPanel viewPanel = new DetailPanel(r1, Mode.VIEW, m1);
		final DetailPanel updatePanel = new DetailPanel(r2, Mode.UPDATE, m1);
		final DetailPanel createPanel = new DetailPanel(r1, Mode.CREATE, m1);
		final DetailPanel editPanel = new DetailPanel(r3, Mode.EDIT, m1);
		final DetailTaskView d1 = new DetailTaskView(r1, viewPanel);
	}
	
	@Test
	public void test() {
		Assert.assertTrue(true);
	}
	
}
