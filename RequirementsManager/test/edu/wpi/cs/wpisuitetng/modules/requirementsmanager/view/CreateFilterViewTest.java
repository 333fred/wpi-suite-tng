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

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterOperation;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
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
	}

	@Test
	public void testInitializeWithFilter() {
		Filter theFilter = new Filter(u1.getUsername(), FilterField.NAME,
				FilterOperation.EQUAL, "name1");
		CreateFilterView view = new CreateFilterView(FilterView.getInstance(), theFilter);
		view.populateFieldsFromFilter();
		view.onSavePressed();
		assertNotNull(view);
	}
	
	@Test
	public void testInitializeWithFilterEstimate() {
		Filter theFilter = new Filter(u1.getUsername(), FilterField.ESTIMATE,
				FilterOperation.EQUAL, "6");
		CreateFilterView view = new CreateFilterView(FilterView.getInstance(), theFilter);
		view.populateFieldsFromFilter();
		view.onSavePressed();
		assertNotNull(view);
	}
	
	@Test
	public void testInitializeWithFilterStatus() {
		Filter theFilter = new Filter(u1.getUsername(), FilterField.STATUS,
				FilterOperation.EQUAL, "Complete");
		CreateFilterView view = new CreateFilterView(FilterView.getInstance(), theFilter);
		view.populateFieldsFromFilter();
		view.onSavePressed();
		assertNotNull(view);
	}
	
	@Test
	public void testInitializeWithFilterPriority() {
		Filter theFilter = new Filter(u1.getUsername(), FilterField.PRIORITY,
				FilterOperation.EQUAL, "Complete");
		CreateFilterView view = new CreateFilterView(FilterView.getInstance(), theFilter);
		view.populateFieldsFromFilter();
		view.onSavePressed();
		assertNotNull(view);
	}
	
	@Test
	public void testInitializeWithFilterType() {
		Filter theFilter = new Filter(u1.getUsername(), FilterField.TYPE,
				FilterOperation.EQUAL, "Complete");
		CreateFilterView view = new CreateFilterView(FilterView.getInstance(), theFilter);
		view.populateFieldsFromFilter();
		assertNotNull(view);
	}
	
	@Test
	public void testInitializeWithFilterIteration() {
		Filter theFilter = new Filter(u1.getUsername(), FilterField.ITERATION,
				FilterOperation.EQUAL, "Complete");
		CreateFilterView view = new CreateFilterView(FilterView.getInstance(), theFilter);
		view.populateFieldsFromFilter();
		assertNotNull(view);
	}

}
