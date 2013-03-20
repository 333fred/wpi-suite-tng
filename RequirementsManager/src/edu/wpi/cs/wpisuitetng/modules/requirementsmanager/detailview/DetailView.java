/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.net.ssl.SSLEngineResult.Status;
import javax.swing.*;


import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview.notes.NotePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.Requirement;
/**
 * @author Swagasaurus
 *
 */
public class DetailView extends JPanel{
	private JTextField nameField;
	
	/** For Notes */
//	public DefaultListModel noteList;
	//protected JList notes;
	protected NotePanel notePanel;
	
	Requirement requirement;
	
	public DetailView(Requirement requirement){
		this.requirement = requirement;
				
		setLayout(null);
				
		// Set up name label
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(12, 50, 55, 16);
		add(lblName);
		
		// Set up description label
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(12, 80, 77, 16);
		add(lblDescription);
		
		// Set up type label
		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(12, 250, 55, 16);
		add(lblType);
		
		// Set up status label
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(12, 300, 55, 16);
		add(lblStatus);
		
		// Set up priority label
		JLabel lblPriority = new JLabel("Priority:");
		lblPriority.setBounds(12, 350, 55, 16);
		add(lblPriority);
		
		// This is the name field
		nameField = new JTextField();
		nameField.setBounds(110, 50, 200, 20);
		add(nameField);
		nameField.setColumns(10);
		
		// This is the description box
		JTextArea textDescription = new JTextArea();
		textDescription.setBounds(110, 80, 200, 130);
		textDescription.setLineWrap(true);
		add(textDescription);
				
		// This is the Type dropdown box
		String[] typeValues = {"", "Epic", "Theme", "User Story", "Non Functional", "Scenario"};
		JComboBox comboBoxType = new JComboBox(typeValues);
		comboBoxType.setBounds(110, 250, 100, 20);
		add(comboBoxType);
		
		// This is the status dropdown box
		String[] statusValues = {"", "New", "In Progress", "Open", "Complete", "Deleted"};
		JComboBox comboBoxStatus = new JComboBox(statusValues);
		comboBoxStatus.setBounds(110, 300, 100, 20);
		add(comboBoxStatus);
		
		// This is the priority value dropbox box
		String[] priorityValues = {"", "High", "Medium", "Low"};
		JComboBox comboBoxPriority = new JComboBox(priorityValues);
		comboBoxPriority.setBounds(110, 350, 100, 20);
		add(comboBoxPriority);
		
		// This is the ok button
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(410, 296, 200, 25);
		add(btnOk);
		
		setLayout(new BorderLayout());
		
		//create note panel
		//noteList = new DefaultListModel();
		//notes = new JList(noteList);
		notePanel = new NotePanel(this.requirement, this); 
		//notePanel.setBounds(110, 50, 200, 20);
		//JScrollPane noteScrollPanel = new JScrollPane(notes);
		
		//add(this.notes); //This is for displaying the notes
		add(this.notePanel,BorderLayout.SOUTH);
	}
}
