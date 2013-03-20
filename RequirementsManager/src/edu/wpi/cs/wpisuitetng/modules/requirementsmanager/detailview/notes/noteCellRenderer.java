package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview.notes;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;


import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.Note;

public class noteCellRenderer implements ListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList list, Object value,	int index, boolean isSelected, boolean cellHasFocus) {		
		final JPanel panel;		
		panel = new NotePanel((Note)value);		
		return panel;
	}

}