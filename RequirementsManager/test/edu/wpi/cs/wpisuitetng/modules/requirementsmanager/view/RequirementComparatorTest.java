/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *				Alex Gorowara, Nick Massa
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subrequirementsTree.RequirementComparator;

public class RequirementComparatorTest {
	
	RequirementComparator RequirementComparator;
	Requirement middle;
	Requirement middleDuplicate;
	Requirement last;
	Requirement first;
	
	@Test
	public void testEarlierRequirement() {
		Assert.assertEquals(-1, RequirementComparator.compare(first, middle));
	}
	
	@Test
	public void testLaterRequirement() {
		Assert.assertEquals(1, RequirementComparator.compare(last, middle));
	}
	
	@Before
	public void testSameRequirement() {
		
		RequirementComparator = new RequirementComparator();
		first = new Requirement();
		first.setrUID(0);
		middle = new Requirement();
		middle.setrUID(1);
		last = new Requirement();
		last.setrUID(2);
		
	}
	
	@Test
	public void testSelf() {
		Assert.assertEquals(0, RequirementComparator.compare(middle, middle));
	}
	
}
