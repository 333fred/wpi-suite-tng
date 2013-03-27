package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;


public class EventCellRenderer implements ListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList list, Object value,	int index, boolean isSelected, boolean cellHasFocus) {		
		final JPanel panel;		
		panel = new EventPanel((Event)value);		
		
		/*
		if (isSelected) {
			panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0), BorderFactory.createLineBorder(Color.black, 3)), BorderFactory.createEmptyBorder(8, 8, 8, 8)));
		} else {
			panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0), BorderFactory.createLineBorder(Color.black, 1)), BorderFactory.createEmptyBorder(8, 8, 8, 8)));
		}
		*/
		return panel;
	}

}
