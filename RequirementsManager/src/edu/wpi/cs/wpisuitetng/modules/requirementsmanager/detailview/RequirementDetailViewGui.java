package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.Component;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import javax.swing.JTabbedPane;
import javax.swing.JInternalFrame;
import java.awt.BorderLayout;
import java.awt.TextArea;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;


//TODO: Add comments, integrate into Janeway, and perform tests
public class RequirementDetailViewGui extends JPanel {
	private JTextField textField;
	private JTextArea txtNotes;

	/**
	 * Create the panel.
	 */
	public RequirementDetailViewGui() {
		

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

		JLabel lblNotes = new JLabel("Notes:");
		lblNotes.setBounds(400, 50, 55, 16);
		add(lblNotes);

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

		txtNotes = new JTextArea();
		txtNotes.setBounds(350, 80, 150, 130);
		add(txtNotes);

		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(375, 220, 100, 25);
		add(btnAdd);

		JButton btnNew = new JButton("New");
		btnNew.setBounds(504, 132, 100, 25);
		add(btnNew);

		JButton btnOk = new JButton("OK");
		btnOk.setBounds(375, 305, 100, 25);
		add(btnOk);

	}
}
