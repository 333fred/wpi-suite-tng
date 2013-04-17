/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.KeyboardShortcut;
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
	/*KeyboardShortcut k;
	KeyStroke j;
	List<KeyboardShortcut> l;*/
	
	@Before
	public void setUp() {
		module = new JanewayTabModel(name, icon, toolbar, mainComponent);
		//k = new KeyboardShortcut(j);
		//module.addKeyboardShortcut(k);
	}
	
	@Test
	public void testGetName(){
		assertEquals(name,module.getName());		
	}
	
	@Test
	public void testGetIcon(){
		assertEquals(icon,module.getIcon());		
	}
	
	@Test
	public void testGetToolbar(){
		assertEquals(toolbar,module.getToolbar());		
	}
	@Test
	public void testGetComponent(){
		assertEquals(mainComponent,module.getMainComponent());		
	}
	//TODO:need to test getKeyboardShortcuts. tests apparently don't like lists...
	/*@Test
	public void testShortcut(){
		l = module.getKeyboardShortcuts();
		assertEquals(j,l);
	}*/
}
