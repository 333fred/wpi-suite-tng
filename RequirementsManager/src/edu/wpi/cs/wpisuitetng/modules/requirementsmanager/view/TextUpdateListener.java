package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Checks for whether or not the text in a given JTextComponent differs from the current model.
 * 
 * Whenever a key is released in the TextUpdateListener's component, checkIfUpdated() is called. This method 
 * gets the component's name and looks up the value of the relevant field in panel's Defect model. It then 
 * compares this value to the component's text to see if the text differs from the model. If the text 
 * differs, the style of the component is changed to show that it differs from the relevant field in the model.
 * Otherwise, the component's style is changed to be normal.
 * 
 * @author Alex C
 */

public class TextUpdateListener implements KeyListener {
	protected final DetailPanel panel;
	protected final JTextComponent component;
	protected final JTextComponent errorComponent;
	protected boolean firstKeyPress; 

	/**
	 * Constructs a TextUpdateListener.
	 * 
	 * @param panel			The DefectPanel which contains the JTextComponent.
	 * @param component		The JTextComponent which will have its text compared to the model. The name 
	 * 						of the JTextComponent must match the name of a getter in Defect after the 
	 * 						"get". For instance: for the method "getTitle", the name of the 
	 * 						JTextComponent must be "Title".
	 * @param errorComponent The error box to write an error message to
	 * 						 if null, then no box exists for this component
	 */
	public TextUpdateListener(DetailPanel panel, JTextComponent component, JTextComponent errorComponent) {
		this.panel = panel;
		this.component = component;
		this.errorComponent = errorComponent;
		
		String empty = "";
		// if this component was empty to begin with, 
		// and it is not the estimate field (estimate can be empty)
		if (empty.equals(component.getText().trim()) && !component.getName().equals("Estimate")) { 
			this.firstKeyPress = false;
		}
		else {
			this.firstKeyPress = true;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_TAB) {
			checkIfUpdated();
		}
		if (firstKeyPress) {
			checkIfUpdated();
		}
		else {
			String empty = "";
			// if this component was empty to begin with, 
			// and it is not the estimate field (estimate can be empty)
			if (empty.equals(component.getText().trim()) && !component.getName().equals("Estimate")) { 
				this.firstKeyPress = false;
			}
			else {
				this.firstKeyPress = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (firstKeyPress) {
			checkIfUpdated();
		}
		else {
			String empty = "";
			// if this component was empty to begin with, 
			// and it is not the estimate field (estimate can be empty)
			if (empty.equals(component.getText().trim()) && !component.getName().equals("Estimate")) { 
				this.firstKeyPress = false;
			}
			else {
				this.firstKeyPress = true;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	/**
	 * Checks if the field is empty and changes the style of the field accordingly.
	 */
	public void checkIfUpdated() {
		String base = ""; // base of empty string to compare to
		
		// Compare base to the component's text to determine whether or not to highlight the field.
		if (base.equals(component.getText().trim())) {
			if (errorComponent != null) { // if there's an error panel to write to
				component.setBackground(new Color(243, 243, 209));
				errorComponent.setText("** Field must be non-blank **");
			}
			panel.disableSaveButton();
		}
		else {
			component.setBackground(Color.WHITE);
			if (errorComponent != null) { // if there's an error panel to write to
				errorComponent.setText("");
			}
			if (base.equals(panel.getTextName().getText().trim())
					|| base.equals(panel.getTextDescription().getText().trim())
					/*TODO: || base.equals(panel.getTextIteration().getText().trim())*/) {
				panel.disableSaveButton();
			}
			else 
				panel.enableSaveButton();
		}
	}
}