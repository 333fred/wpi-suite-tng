/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

/**
 * @author Maddie Burris
 * 
 */
public class JanewayTabModuleTest {
	
	JanewayTabModel module;
	String name;
	Icon icon;
	JComponent toolbar;
	JComponent mainComponent;
	
	@Before
	public void setUp() {
		module = new JanewayTabModel(name, icon, toolbar, mainComponent);
	}
	
	@Test
	public void testGetComponent() {
		Assert.assertEquals(mainComponent, module.getMainComponent());
	}
	
	@Test
	public void testGetIcon() {
		Assert.assertEquals(icon, module.getIcon());
	}
	
	@Test
	public void testGetName() {
		Assert.assertEquals(name, module.getName());
	}
	
	@Test
	public void testGetToolbar() {
		Assert.assertEquals(toolbar, module.getToolbar());
	}
}
