package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

/** IterationViewListener that is used to determine when a change has been made to a component, TextField, or a JCalendar
 * 
 * @author Mitchell, Matt
 *
 */

public class IterationViewListener implements KeyListener, MouseListener {

	
	public enum Type {
		TEXT,
		OTHER
	}
	/** The iteration view this listener is in */
	private IterationView iterationView;
	
	/** The component this listener is listening on */
	private JComponent component;
	
	
	/** Creates a new IterationView Listener with the given view and component
	 * 
	 * @param iterationView
	 * @param component
	 */
	
	public IterationViewListener(IterationView iterationView, JComponent component) {
		this.iterationView = iterationView;
		this.component = component;
	}
	
	private void update() {
		iterationView.updateSave(component);
	}

	public void mouseClicked(MouseEvent arg0) {
		update();		
	}
	
	public void mousePressed(MouseEvent arg0) {
		update();		
	}

	
	public void mouseReleased(MouseEvent arg0) {
		update();		
	}

	
	public void keyPressed(KeyEvent e) {
		update();		
	}

	
	public void keyReleased(KeyEvent e) {
		update();		
	}

	
	public void keyTyped(KeyEvent e) {
		update();		
	}
	
	
	public void mouseEntered(MouseEvent arg0) {
		
		
	}

	
	public void mouseExited(MouseEvent arg0) {
		
		
	}

}