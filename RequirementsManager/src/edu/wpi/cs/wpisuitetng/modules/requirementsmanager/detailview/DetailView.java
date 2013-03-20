/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview;

import java.awt.*;

import javax.swing.*;

//import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.defectevents.model.DefectEventListModel;
//import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview.notes.NotePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.Requirement;
/**
 * @author Alex Woodyard
 *
 */
public class DetailView extends JPanel{

	private JPanel top, bottom, left, right;
	private JButton ok;
	private JTextField name;
	private JLabel nameLabel;
	
	/** For Notes */
	protected DefaultListModel noteList;
	protected JList notes;
	protected NotePanel notePanel;
	
	Requirement requirement;
	
	public DetailView(Requirement requirement){
		
		this.requirement = requirement;
		
		//creates notepanel
		notePanel = new NotePanel(requirement, this); 
		
		setLayout(new BorderLayout()); //set to use outside edges as basis for sub panels.
		
		//Top Panel for displaying name and name text box
		top = new JPanel();
		top.setPreferredSize(new Dimension(100,25));
		nameLabel = new JLabel("Name: ");
		name = new JTextField();
		//add the name label and text box to the top panel
		top.setLayout(new GridLayout(1,2));
		//Grid layout: use a grid for the base layout for the panel.
		top.add(nameLabel);
		top.add(name);
		
		//temporary left panel where requirement fields will be displayed
		left = new JPanel();
		left.setPreferredSize(new Dimension(300,300));
		
		//Note panel will be on the right
		right = notePanel;
		right.setPreferredSize(new Dimension(300,200));
		noteList = new DefaultListModel();
		notes = new JList(noteList);

		//JScrollPane notePanel = new JScrollPane(notes);
		
		//An ok button at the bottom
		bottom = new JPanel();
		bottom.setPreferredSize(new Dimension(100,300));
		bottom.setLayout(null);
		
		
		//Iterate over the list of existing notes in the requirement, effectively displaying the,
		
		//TODO: fix Null Pointer/ figure out null pointer
//		for(Note aNote : requirement.getNotes()) {
//			noteList.addElement(aNote);
//		}
				
		
	
		// uncomment these lines when ready to add the notes module and the module
		// that has the description, status, etc
		//left = new DescriptionPanel();
	
		//add all the sub panels to the main panel.
		add(top, BorderLayout.NORTH);
		add(bottom,BorderLayout.SOUTH);
		ok = new JButton("OK");
		ok.setBounds(0, 64, 150, 25);
		bottom.add(ok);
		add(left, BorderLayout.WEST);
		add(right, BorderLayout.EAST);
		add(notes); //This is for displaying the notes
		
		
	}
	
	public DefaultListModel getNoteList() {
		return noteList;
	}
	
	//public Requirement getModel() {
	//	return this.model; //TODO: Make sure the Requirement member is in here
	//}
}
