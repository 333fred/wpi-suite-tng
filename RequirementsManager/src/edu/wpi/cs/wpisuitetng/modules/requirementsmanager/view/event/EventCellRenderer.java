package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.Event;

public class EventCellRenderer implements ListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList list, Object value,	int index, boolean isSelected, boolean cellHasFocus) {		
		final JPanel panel;		
		panel = new EventPanel((Event)value);		
		return panel;
	}

}
