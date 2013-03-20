package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note;

import java.awt.Color;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
/**
 * The notePanel class is what displays the note panel in the larger requirements creator pannel 
 * @author spkordell
 */
@SuppressWarnings("serial")
public class NotePanel extends JPanel {
	
	protected JLabel title;
	protected JLabel content;
	
	/**
	 * The note panel is the panel that is used to create and display notes
	 * @param note the note that is displayed
	 */
	public NotePanel(Note note) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		title = new JLabel("<html><font size=4><b>" + note.getCreator() + "<font size=.25></b> added on " + new SimpleDateFormat("MM/dd/yy hh:mm a").format(note.getDate()) + "</html>");
		title.setFont(title.getFont().deriveFont(9));
		title.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		content = new JLabel("<html><i>" + note.getNote() + "</i></html>");
		content.setFont(content.getFont().deriveFont(9));
		content.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		this.add(title);
		this.add(content);
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0), BorderFactory.createLineBorder(Color.black, 1)), BorderFactory.createEmptyBorder(8, 8, 8, 8)));
	}

	public JLabel getnoteField() {
		// TODO Auto-generated method stub
		return null;
	}

	public JLabel getNoteList() {
		// TODO Auto-generated method stub
		return null;
	}
}
