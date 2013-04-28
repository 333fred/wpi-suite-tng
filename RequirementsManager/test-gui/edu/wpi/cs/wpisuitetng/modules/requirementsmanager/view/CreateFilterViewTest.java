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

import junit.extensions.abbot.ComponentTestFixture;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import abbot.tester.JComboBoxTester;
import abbot.tester.JTextComponentTester;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterOperation;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter.CreateFilterView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter.FilterView;

public class CreateFilterViewTest extends ComponentTestFixture {
	
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
	
	@Test 
	public void testIsDuplicateFilter() {
		/*
		 * 	public Filter(final String user, final FilterField field,
			final FilterOperation operation, final Object value) {*/
		
		Filter f = new Filter("User", FilterField.ESTIMATE, FilterOperation.EQUAL, 5);
		Filter f2 = new Filter("User", FilterField.ITERATION, FilterOperation.EQUAL, 5);
		Filter f3 = new Filter("User", FilterField.ESTIMATE, FilterOperation.LESS_THAN, 5);
		Filter f4 = new Filter("User", FilterField.ESTIMATE, FilterOperation.EQUAL, 7);
		
		FilterDatabase.getInstance().add(f);
		
		assertTrue(CreateFilterView.isFilterDuplicate(Filter.cloneFilter(f)));
		assertFalse(CreateFilterView.isFilterDuplicate(f2));
		assertFalse(CreateFilterView.isFilterDuplicate(f3));
		assertFalse(CreateFilterView.isFilterDuplicate(f4));		
	}
	
	@Test
	public void testSaveButton() {
		FilterView filterView = FilterView.getInstance();
		CreateFilterView createFilterView = new CreateFilterView(filterView);
		
		assertFalse(createFilterView.getButSave().isEnabled());		
		assertEquals(createFilterView.getLabSaveError().getText(), "Value cannot be blank");
	}
	
	/*
	 * final JTextComponentTester tester = new JTextComponentTester();
		
		showFrame(panel);
		
		Assert.assertFalse(panel.getBtnSave().isEnabled());
		
		tester.actionEnterText(panel.getTextName(), "Req Name");
	 */
	
	@Test
	public void testSaveButton2() {
		FilterView filterView = FilterView.getInstance();
		CreateFilterView createFilterView = new CreateFilterView(filterView);
		
		createFilterView.getCboxField().setSelectedItem(FilterField.NAME);
		createFilterView.getCboxOperation().setSelectedItem(FilterOperation.EQUAL);
		showFrame(createFilterView);
		
		JTextComponentTester tester = new JTextComponentTester();		
		tester.actionEnterText(createFilterView.getTxtEqualTo(), "Yeaaah");	
		
		assertTrue(createFilterView.getButSave().isEnabled());			
	}
	
	@Test
	public void testSaveButton3() {
		FilterView filterView = FilterView.getInstance();
		CreateFilterView createFilterView = new CreateFilterView(filterView);
		
		showFrame(createFilterView);
		
		JComboBoxTester cboxTester = new JComboBoxTester();
		
		cboxTester.actionSelectItem(createFilterView.getCboxField(), FilterField.ESTIMATE.toString());
		cboxTester.actionSelectItem(createFilterView.getCboxOperation(), FilterOperation.EQUAL.toString());
		
		JTextComponentTester textTester = new JTextComponentTester();		
		textTester.actionEnterText(createFilterView.getTxtEqualTo(), "Yeaaah");	
		
		assertFalse(createFilterView.getButSave().isEnabled());	
		assertEquals(createFilterView.getLabSaveError().getText(), "Value must be a number");
	}
	
	@Test
	public void testSaveButton4() {
		FilterView filterView = FilterView.getInstance();
		CreateFilterView createFilterView = new CreateFilterView(filterView);
		
		createFilterView.getCboxField().setSelectedItem(FilterField.ESTIMATE);
		createFilterView.getCboxOperation().setSelectedItem(FilterOperation.EQUAL);
		showFrame(createFilterView);
		
		JTextComponentTester tester = new JTextComponentTester();		
		tester.actionEnterText(createFilterView.getTxtEqualTo(), "5");	
		
		assertTrue(createFilterView.getButSave().isEnabled());	
	}
}
