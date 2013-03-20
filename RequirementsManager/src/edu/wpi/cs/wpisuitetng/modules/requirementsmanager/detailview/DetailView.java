/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;


import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview.notes.NotePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.Requirement;
/**
 * @author Swagasaurus
 *
 */
public class DetailView extends JPanel{
	private JTextField textField;
	
	/** For Notes */
//	public DefaultListModel noteList;
	//protected JList notes;
	protected NotePanel notePanel;
	
	Requirement requirement;
	
	public DetailView(Requirement requirement){
		this.requirement = requirement;
				
		setLayout(null);
				
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(12, 50, 55, 16);
		add(lblName);
		
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(12, 80, 77, 16);
		add(lblDescription);
		
		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(12, 250, 55, 16);
		add(lblType);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(12, 300, 55, 16);
		add(lblStatus);
		
		JLabel lblPriority = new JLabel("Priority:");
		lblPriority.setBounds(12, 350, 55, 16);
		add(lblPriority);
		
		textField = new JTextField();
		textField.setBounds(110, 50, 200, 20);
		add(textField);
		textField.setColumns(10);
		
		JTextArea textDescription = new JTextArea();
		textDescription.setBounds(110, 80, 200, 130);
		add(textDescription);
				
		JComboBox comboBoxType = new JComboBox();
		comboBoxType.setBounds(110, 250, 100, 20);
		add(comboBoxType);
		
		JComboBox comboBoxStatus = new JComboBox();
		comboBoxStatus.setBounds(110, 300, 100, 20);
		add(comboBoxStatus);
		
		JComboBox comboBoxPriority = new JComboBox();
		comboBoxPriority.setBounds(110, 350, 100, 20);
		add(comboBoxPriority);
		
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
