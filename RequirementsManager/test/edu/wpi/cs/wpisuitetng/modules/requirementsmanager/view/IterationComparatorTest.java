/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
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

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;

import java.util.Date;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationComparator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;

public class IterationComparatorTest {

	IterationComparator iterationComparator;
	Iteration middle;
	Iteration middleDuplicate;
	Iteration last;
	Iteration first;
	
	@Before
	public void testSameIteration(){
		
		this.iterationComparator = new IterationComparator();
		this.middle = new Iteration("middle", new Date(1000), new Date(2000));
		this.middleDuplicate = new Iteration("middleDuplicate", new Date(1000), new Date(2000));
		this.last = new Iteration("last", new Date(2000), new Date(3000));
		this.first = new Iteration("first", new Date(0), new Date(1000));
		
	}
	
	@Test
	public void testSelf(){
		assertEquals(0, this.iterationComparator.compare(this.middle, this.middle));
	}
	
	@Test
	public void testEqualDates(){
		assertEquals(0, this.iterationComparator.compare(this.middle, this.middleDuplicate));
	}
	
	@Test
	public void testEarlierIteration(){
		assertEquals(-1, this.iterationComparator.compare(this.first, this.middle));
	}
	
	@Test
	public void testLaterIteration(){
		assertEquals(1, this.iterationComparator.compare(this.last, this.middle));
	}
	
}
