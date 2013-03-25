package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The EventPanel class displays the individual event panels
 * @author spkordell
 */
@SuppressWarnings("serial")
public class EventPanel extends JPanel {
	
	protected JLabel title;
	protected JLabel content;
	
	/**
	 * The note panel is the panel that is used to create and display notes
	 * @param note the note that is displayed
	 */
	public EventPanel(Event event) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		//title = new JLabel("<html><font size=4><b>" + note.getCreator() + "<font size=.25></b> added on " + new SimpleDateFormat("MM/dd/yy hh:mm a").format(note.getDate()) + "</html>");
		title = new JLabel( event.getTitle());
		title.setFont(title.getFont().deriveFont(9));
		title.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		//content = new JLabel("<html><i>" + note.getNote() + "</i></html>");
		content = new JLabel(event.getContent());
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
