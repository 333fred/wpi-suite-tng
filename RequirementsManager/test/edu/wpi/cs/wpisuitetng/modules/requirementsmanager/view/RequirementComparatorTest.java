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

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;

import java.util.Date;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subrequirementsTree.RequirementComparator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

public class RequirementComparatorTest {

	RequirementComparator RequirementComparator;
	Requirement middle;
	Requirement middleDuplicate;
	Requirement last;
	Requirement first;
	
	@Before
	public void testSameRequirement(){
		
		this.RequirementComparator = new RequirementComparator();
		this.first = new Requirement();
		first.setrUID(0);
		this.middle = new Requirement();
		middle.setrUID(1);
		this.last = new Requirement();
		last.setrUID(2);
		
	}
	
	@Test
	public void testSelf(){
		assertEquals(0, this.RequirementComparator.compare(this.middle, this.middle));
	}
	
	@Test
	public void testEarlierRequirement(){
		assertEquals(-1, this.RequirementComparator.compare(this.first, this.middle));
	}
	
	@Test
	public void testLaterRequirement(){
		assertEquals(1, this.RequirementComparator.compare(this.last, this.middle));
	}
	
}
