/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Gorowara
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import abbot.finder.Matcher;
import abbot.finder.matchers.ClassMatcher;
import abbot.finder.BasicFinder;
import abbot.finder.MultipleComponentsFoundException;
import abbot.finder.ComponentNotFoundException;

import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

public class BasicGUITest {

	MainTabController mainTabController;
	RequirementTableView requirementTableView;
	
	@Before
	public void setup(){
		this.mainTabController = new MainTabController();
		this.requirementTableView = RequirementTableView.getInstance(this.mainTabController);
	}
	
	@Test
	public void testProperMatcherUse(){
		Matcher m = new ClassMatcher(JButton.class);
		BasicFinder bf = new BasicFinder();
		try{
			bf.find(requirementTableView, m);
		}
		catch(MultipleComponentsFoundException e){
			
		}
		catch(ComponentNotFoundException e){
			fail();
		}
	}
	
}
