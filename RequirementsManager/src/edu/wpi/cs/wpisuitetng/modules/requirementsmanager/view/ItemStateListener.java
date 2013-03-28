/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

/**
 * @author Alex C
 *
 */
public class ItemStateListener implements ItemListener {
	protected final DetailPanel panel;
	protected final JComboBox component;
	
	public ItemStateListener(DetailPanel panel, JComboBox comboBoxType) {
		this.panel = panel;
		this.component = comboBoxType;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		checkIfUpdated(e);
	}

	public void checkIfUpdated(ItemEvent e) {
		if (panel.getTextName().getText().trim().length() > 0 && panel.getTextDescription().getText().trim().length() > 0) {
			panel.enableSaveButton();
		}
	}
}
