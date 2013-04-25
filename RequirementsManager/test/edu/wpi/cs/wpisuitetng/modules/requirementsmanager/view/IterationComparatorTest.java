/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *				Alex Gorowara
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;

public class IterationComparatorTest {
	
	IterationComparator iterationComparator;
	Iteration middle;
	Iteration middleDuplicate;
	Iteration last;
	Iteration first;
	
	@Test
	public void testEarlierIteration() {
		Assert.assertEquals(-1, iterationComparator.compare(first, middle));
	}
	
	@Test
	public void testEqualDates() {
		Assert.assertEquals(0,
				iterationComparator.compare(middle, middleDuplicate));
	}
	
	@Test
	public void testLaterIteration() {
		Assert.assertEquals(1, iterationComparator.compare(last, middle));
	}
	
	@Before
	public void testSameIteration() {
		
		iterationComparator = new IterationComparator();
		middle = new Iteration("middle", new Date(1000), new Date(2000));
		middleDuplicate = new Iteration("middleDuplicate", new Date(1000),
				new Date(2000));
		last = new Iteration("last", new Date(2000), new Date(3000));
		first = new Iteration("first", new Date(0), new Date(1000));
		
	}
	
	@Test
	public void testSelf() {
		Assert.assertEquals(0, iterationComparator.compare(middle, middle));
	}
	
}
