package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

public class CreateFilterViewListener implements KeyListener,
		PropertyChangeListener {

	public enum Type {
		TEXT, OTHER
	}

	/** The iteration view this listener is in */
	private CreateFilterView createFilterView;


	/** set to false after the first key press, used to stop the blinking */
	private boolean first;

	/**
	 * Creates a new IterationView Listener with the given view and component
	 * 
	 * @param iterationView
	 * @param component
	 */

	public CreateFilterViewListener(CreateFilterView createFilterView) {
		this.createFilterView = createFilterView;
		first = true;
	}

	/**
	 * Calls the appropriate update function in IterationView
	 * 
	 * 
	 */

	private void update() {
		createFilterView.updateSave();
	}

	/**
	 * Triggers an update when a key is released in a field, seems to be working
	 * good
	 * 
	 */

	@Override
	public void keyReleased(KeyEvent e) {
		update();
	}

	/**
	 * Property CHange Listener method, used to trigger an update when a change
	 * is made to the JCalendar
	 * 
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		update();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// update();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// update();
	}

}
