/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Chen
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.text.JTextComponent;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * Checks for whether or not the text in a given JTextComponent differs from the
 * current model.
 * 
 * Whenever a key is released in the TextUpdateListener's component,
 * checkIfUpdated() is called. This method gets the component's name and looks
 * up the value of the relevant field in panel's Defect model. It then compares
 * this value to the component's text to see if the text differs from the model.
 * If the text differs, the style of the component is changed to show that it
 * differs from the relevant field in the model. Otherwise, the component's
 * style is changed to be normal.
 */
public class TextUpdateListener implements KeyListener {
	protected final DetailPanel panel;
	protected final JTextComponent component;
	protected final JTextComponent errorComponent;
	protected boolean firstKeyPress;

	/**
	 * Constructs a TextUpdateListener.
	 * 
	 * @param panel
	 *            The DefectPanel which contains the JTextComponent.
	 * @param component
	 *            The JTextComponent which will have its text compared to the
	 *            model. The name of the JTextComponent must match the name of a
	 *            getter in Defect after the "get". For instance: for the method
	 *            "getTitle", the name of the JTextComponent must be "Title".
	 * @param errorComponent
	 *            The error box to write an error message to if null, then no
	 *            box exists for this component
	 */
	public TextUpdateListener(DetailPanel panel, JTextComponent component,
			JTextComponent errorComponent) {
		this.panel = panel;
		this.component = component;
		this.errorComponent = errorComponent;

		String empty = "";
		// if this component was empty to begin with,
		// and it is not the estimate field (estimate can be empty)
		if (empty.equals(component.getText().trim())) {
			this.firstKeyPress = false;
		} else {
			this.firstKeyPress = true;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		String empty = "";
		// if this component was empty to begin with,
		if (empty.equals(component.getText().trim())) {
			this.firstKeyPress = false;
		} else {
			this.firstKeyPress = true;
		}

		if (arg0.getKeyCode() == KeyEvent.VK_TAB || firstKeyPress) {
			checkIfUpdated();
		}		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		String empty = "";
		// if this component was empty to begin with,
		if (empty.equals(component.getText().trim())) {
			this.firstKeyPress = false;
		} else {
			this.firstKeyPress = true;
		}		
		
		// as long as we're not reading the key release from tabbing, we can check
		if (arg0.getKeyCode() != KeyEvent.VK_TAB || firstKeyPress) {
			checkIfUpdated();
		}		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	/**
	 * Checks if the field is empty and changes the style of the field
	 * accordingly.
	 */
	public void checkIfUpdated() {
		String empty = ""; // empty string to compare to
		String base = ""; // the base of the string in the component already

		// Get the base String to compare to the text of the JTextComponent
		try {
			// Get the field from the Requirement model that corresponds with the name of component.
			Object field = panel.getModel().getClass().getDeclaredMethod("get" + component.getName()).invoke(panel.getModel());

			// If field is null, set base to an empty String.
			if (field == null) {
				base = "";
			}
			// If field is an instance of String, set base to that String.
			else if (field instanceof String) {
				base = (String) field;
			}
			// If field is an instance of an Integer, set base to that Integer as a String
			// occurs for the estimate field
			else if (field instanceof Integer) {
				base = Integer.toString((Integer) field);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		// add to this list the names of components if the component can be
		// blank
		// backend may assign default values if saved as blank
		String canBeEmpty = "Estimate|Effort|ReleaseNum";
		
		// if the component has is emptye
		if (empty.equals(component.getText().trim())) { 
			// if there's an error panel to write to
			if (errorComponent != null) {
				component.setBackground(new Color(243, 243, 209));
				errorComponent.setText("** Field must be non-blank **");
			} 
			// if this component cannot be empty
			if (!component.getName().matches(canBeEmpty)) {
				panel.disableSaveButton();
			} else {
				panel.enableSaveButton();
			}
		} else if (base.equals(component.getText().trim())) {
			component.setBackground(Color.WHITE);
			// if there's an error panel to write to
			if (errorComponent != null) { 
				errorComponent.setText("");
			}
			panel.disableSaveButton();
		} else {
			component.setBackground(Color.WHITE);
			// if there's an error panel to write to
			if (errorComponent != null) { 
				errorComponent.setText("");
			}
			if (empty.equals(panel.getTextName().getText().trim())
					|| empty.equals(panel.getTextDescription().getText().trim())) {
				panel.disableSaveButton();
			} else
				panel.enableSaveButton();
		}
	}
}