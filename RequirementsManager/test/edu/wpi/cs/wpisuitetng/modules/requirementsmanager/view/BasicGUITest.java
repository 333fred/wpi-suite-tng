package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.swing.JButton;

import org.junit.Before;
import org.junit.Test;

import abbot.finder.matchers.ClassMatcher;
import abbot.finder.Matcher;
import abbot.finder.BasicFinder;
import abbot.finder.MultipleComponentsFoundException;
import abbot.finder.ComponentNotFoundException;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

public class BasicGUITest {
	
	MainTabController mainTabController;
	RequirementTableView requirementTableView;
	
	@Before
	public void setup(){
		
		this.mainTabController = new MainTabController();
		this.requirementTableView = RequirementTableView.getInstance(mainTabController);
		
	}
	
	@Test
	public void findButView(){
		Matcher m = new ClassMatcher(JButton.class);
		BasicFinder bf = new BasicFinder();
		try{
			assertNotNull(bf.find(this.requirementTableView, m));
		}
		catch(MultipleComponentsFoundException e){
		}
		catch(ComponentNotFoundException e){
			fail();
		}
	}

}
