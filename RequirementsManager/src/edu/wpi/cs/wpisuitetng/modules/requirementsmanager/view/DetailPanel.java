/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.text.AbstractDocument;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.FocusableTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventCellRenderer;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.MakeNotePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.noteCellRenderer;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailNoteView;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.MakeNotePanel;

/**
 * @author Swagasaurus
 * 
 */
public class DetailPanel extends FocusableTab {



	// Textfields
	JTextArea textName;
	JTextArea textDescription;

	// combo boxes
	JComboBox comboBoxType;
	JComboBox comboBoxStatus;
	JComboBox comboBoxPriority;

	private Requirement requirement;
	private MainTabController mainTabController;
	private DetailNoteView noteView;

	private static final int VERTICAL_PADDING = 10;
	private static final int VERTICAL_CLOSE = -5;
	private static final int VERTICAL_FAR = 20;
	private static final int HORIZONTAL_PADDING = 20;

	public DetailPanel(Requirement requirement, MainTabController mainTabController) {
		this.requirement = requirement;
		this.mainTabController = mainTabController;
		
		JPanel mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(0, 2);
		setLayout(mainLayout);

		SpringLayout layout = new SpringLayout();
		mainPanel.setLayout(layout);

		JLabel lblName = new JLabel("Name:");
		mainPanel.add(lblName);

		JLabel lblDescription = new JLabel("Description:");
		mainPanel.add(lblDescription);

		JLabel lblType = new JLabel("Type:");
		mainPanel.add(lblType);

		JLabel lblStatus = new JLabel("Status:");
		mainPanel.add(lblStatus);

		JLabel lblPriority = new JLabel("Priority:");
		mainPanel.add(lblPriority);

		textName = new JTextArea(1, 40);
		textName.setLineWrap(true);
		textName.setWrapStyleWord(true);
		AbstractDocument pDoc = (AbstractDocument) textName.getDocument();
		pDoc.setDocumentFilter(new DocumentSizeFilter(100));
		textName.setBorder((new JTextField()).getBorder());
		mainPanel.add(textName);
		
		textName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						textName.transferFocus();
					}
					else {
						textName.transferFocusBackward();
					}
					event.consume();
				}
			}
		});

		textDescription = new JTextArea(8, 40);
		textDescription.setLineWrap(true);
		textDescription.setWrapStyleWord(true);
		textDescription.setBorder((new JTextField()).getBorder());
		JScrollPane scroll = new JScrollPane(textDescription);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setSize(400, 450);
		scroll.setBorder(null);
		mainPanel.add(scroll);

		String[] availableTypes = { "", "Epic", "Theme", "User Story",
				"Non-functional", "Scenario" };
		comboBoxType = new JComboBox(availableTypes);
		comboBoxType.setPrototypeDisplayValue("Non-functional");
		mainPanel.add(comboBoxType);

		String[] availableStatuses = { "New", "In Progress", "Open",
				"Complete", "Deleted", "" };
		comboBoxStatus = new JComboBox(availableStatuses);
		comboBoxStatus.setPrototypeDisplayValue("Non-functional");
		mainPanel.add(comboBoxStatus);

		String[] availablePriorities = { "", "High", "Medium", "Low" };
		comboBoxPriority = new JComboBox(availablePriorities);
		comboBoxPriority.setPrototypeDisplayValue("Non-functional");
		mainPanel.add(comboBoxPriority);
		
		if(requirement.getName().equals(""))
		{
		JButton btnSave = new JButton("Save Requirement");
		btnSave.setAction(new SaveRequirementAction(requirement, this));
		mainPanel.add(btnSave);
		
		layout.putConstraint(SpringLayout.WEST, btnSave, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, btnSave, VERTICAL_PADDING,
				SpringLayout.SOUTH, comboBoxPriority);
		
		comboBoxStatus.setEnabled(false);
		comboBoxStatus.setSelectedItem("NEW");
		}
		else
		{
			switch (requirement.getStatus()) {
			case NEW:
				comboBoxStatus.setSelectedIndex(0);
				break;
			case IN_PROGRESS:
				comboBoxStatus.setSelectedIndex(1);
				break;
			case OPEN:
				comboBoxStatus.setSelectedIndex(2);
				break;
			case COMPLETE:
				comboBoxStatus.setSelectedIndex(3);
				break;
			case DELETED:
				comboBoxStatus.setSelectedIndex(4);
				break;
			case BLANK:
				comboBoxStatus.setSelectedIndex(5);
				break;
			}
		}
		
		
		// Align left edges of objects
		layout.putConstraint(SpringLayout.WEST, lblName, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblDescription,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblType, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblStatus, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblPriority,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, textName, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, scroll,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, comboBoxType,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, comboBoxStatus,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, comboBoxPriority,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);

		// Align North Edges of Objects
		layout.putConstraint(SpringLayout.NORTH, lblName, VERTICAL_PADDING,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, textName, VERTICAL_PADDING
				+ VERTICAL_CLOSE, SpringLayout.SOUTH, lblName);
		layout.putConstraint(SpringLayout.NORTH, lblDescription,
				VERTICAL_PADDING, SpringLayout.SOUTH, textName);
		layout.putConstraint(SpringLayout.NORTH, scroll,
				VERTICAL_PADDING + VERTICAL_CLOSE, SpringLayout.SOUTH,
				lblDescription);
		layout.putConstraint(SpringLayout.NORTH, lblType, VERTICAL_PADDING,
				SpringLayout.SOUTH, scroll);
		layout.putConstraint(SpringLayout.NORTH, comboBoxType, VERTICAL_PADDING
				+ VERTICAL_CLOSE, SpringLayout.SOUTH, lblType);
		layout.putConstraint(SpringLayout.NORTH, lblStatus, VERTICAL_PADDING,
				SpringLayout.SOUTH, comboBoxType);
		layout.putConstraint(SpringLayout.NORTH, comboBoxStatus,
				VERTICAL_PADDING + VERTICAL_CLOSE, SpringLayout.SOUTH,
				lblStatus);
		layout.putConstraint(SpringLayout.NORTH, lblPriority, VERTICAL_PADDING,
				SpringLayout.SOUTH, comboBoxStatus);
		layout.putConstraint(SpringLayout.NORTH, comboBoxPriority,
				VERTICAL_PADDING + VERTICAL_CLOSE, SpringLayout.SOUTH,
				lblPriority);

		textName.setText(requirement.getName());
		textDescription.setText(requirement.getDescription());
		switch (requirement.getType()) {
		case BLANK:
			comboBoxType.setSelectedIndex(0);
			break;
		case EPIC:
			comboBoxType.setSelectedIndex(1);
			break;
		case THEME:
			comboBoxType.setSelectedIndex(2);
			break;
		case USER_STORY:
			comboBoxType.setSelectedIndex(3);
			break;
		case NON_FUNCTIONAL:
			comboBoxType.setSelectedIndex(4);
			break;
		case SCENARIO:
			comboBoxType.setSelectedIndex(5);
		}
		switch (requirement.getPriority()) {
		case BLANK:
			comboBoxPriority.setSelectedIndex(0);
			break;
		case HIGH:
			comboBoxPriority.setSelectedIndex(1);
			break;
		case MEDIUM:
			comboBoxPriority.setSelectedIndex(2);
			break;
		case LOW:
			comboBoxPriority.setSelectedIndex(3);
			break;
		}

		noteView = new DetailNoteView(this.requirement, this);
	
		//create the new eventPane
		DetailEventPane eventPane = new DetailEventPane(noteView, new JPanel());
		
		// Add everything to this
		add(mainPanel);
		add(eventPane);
	}

	DefaultListModel listModel = new DefaultListModel();
	public DefaultListModel getNoteList() {
		return noteView.getNoteList();
	}
	
	public MainTabController getMainTabController() {
		return mainTabController;
	}
}