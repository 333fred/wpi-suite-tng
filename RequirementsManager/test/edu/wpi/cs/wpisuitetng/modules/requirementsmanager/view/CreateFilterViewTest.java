/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Jason Whitehouse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterOperation;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter.CreateFilterView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter.FilterView;

public class CreateFilterViewTest {
	
	User u1 = new User("user", "user1", "password", 0);
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		FilterDatabase.getInstance().set(new ArrayList<Filter>());
		final Iteration theIteration = new Iteration("Yo", new Date(),
				new Date());
		IterationDatabase.getInstance().set(new ArrayList<Iteration>());
		IterationDatabase.getInstance().add(theIteration);
	}
	
	@Test
	public void testInitializeWithFilter() {
		final Filter theFilter = new Filter(u1.getUsername(), FilterField.NAME,
				FilterOperation.EQUAL, "name1");
		final CreateFilterView view = new CreateFilterView(
				FilterView.getInstance(), theFilter);
		view.editFilter(theFilter);
		view.onSavePressed();
		Assert.assertNotNull(view);
	}
	
	@Test
	public void testInitializeWithFilterEstimate() {
		final Filter theFilter = new Filter(u1.getUsername(),
				FilterField.ESTIMATE, FilterOperation.EQUAL, "6");
		final CreateFilterView view = new CreateFilterView(
				FilterView.getInstance(), theFilter);
		view.editFilter(theFilter);
		view.onSavePressed();
		Assert.assertNotNull(view);
	}
	
	@Test
	public void testInitializeWithFilterIterationEqual() {
		final Filter theFilter = new Filter(u1.getUsername(),
				FilterField.ITERATION, FilterOperation.EQUAL, 2);
		final CreateFilterView view = new CreateFilterView(
				FilterView.getInstance(), theFilter);
		view.editFilter(theFilter);
		view.onSavePressed();
		Assert.assertNotNull(view);
	}
	
	@Test
	public void testInitializeWithFilterPriority() {
		final Filter theFilter = new Filter(u1.getUsername(),
				FilterField.PRIORITY, FilterOperation.EQUAL, "Complete");
		final CreateFilterView view = new CreateFilterView(
				FilterView.getInstance(), theFilter);
		view.editFilter(theFilter);
		view.onSavePressed();
		Assert.assertNotNull(view);
	}
	
	@Test
	public void testInitializeWithFilterStatus() {
		final Filter theFilter = new Filter(u1.getUsername(),
				FilterField.STATUS, FilterOperation.EQUAL, "Complete");
		final CreateFilterView view = new CreateFilterView(
				FilterView.getInstance(), theFilter);
		view.editFilter(theFilter);
		view.onSavePressed();
		Assert.assertNotNull(view);
	}
	
	@Test
	public void testInitializeWithFilterType() {
		final Filter theFilter = new Filter(u1.getUsername(), FilterField.TYPE,
				FilterOperation.EQUAL, "Complete");
		final CreateFilterView view = new CreateFilterView(
				FilterView.getInstance(), theFilter);
		view.editFilter(theFilter);
		view.onSavePressed();
		Assert.assertNotNull(view);
	}	
}
