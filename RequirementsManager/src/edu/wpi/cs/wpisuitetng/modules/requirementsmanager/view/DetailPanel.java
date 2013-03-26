/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.text.AbstractDocument;


import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveIterationsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.FocusableTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailNoteView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailLogView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.CancelAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.EditRequirementAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.SaveRequirementAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.DetailEventPane;

/**
 * JPanel class to display the different fields of the requirement
 * 
 * @author Swagasaurus
 * 
 */
public class DetailPanel extends FocusableTab {



	// Textfields
	private JTextArea textName;
	private JTextArea textDescription;
	private JTextArea textNameValid;
	private JTextArea textDescriptionValid;
	JTextArea saveError;

	// combo boxes

	JComboBox comboBoxType;
	JComboBox comboBoxStatus;
	JComboBox comboBoxPriority;
	JComboBox comboBoxIteration;

	//requirement that is displayed
	private Requirement requirement;
	//controller for all the tabs
	private MainTabController mainTabController;
	//the view that shows the notes
	private DetailNoteView noteView;
	//the view that shows the notes
	private DetailLogView logView;
	//the view that shows the users assigned to the requirement
	private AssigneePanel userView;
	
	protected final TextUpdateListener txtTitleListener;
	protected final TextUpdateListener txtDescriptionListener;

	//swing constants
	private static final int VERTICAL_PADDING = 10;
	private static final int VERTICAL_CLOSE = -5;
	private static final int VERTICAL_CLOSE2 = -10;
	private static final int VERTICAL_FAR = 20;
	private static final int HORIZONTAL_PADDING = 20;
	private static final int HORIZONTAL_PADDING2 = 100;
	private static final int HORIZONTAL_CLOSE = -7;

	public DetailPanel(Requirement requirement, MainTabController mainTabController) {
		this.requirement = requirement;
		this.mainTabController = mainTabController;
		
		JPanel mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(0, 2);
		setLayout(mainLayout);

		SpringLayout layout = new SpringLayout();
		mainPanel.setLayout(layout);

		//add labels to the overal panel
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
		
		JLabel lblIteration = new JLabel("Iteration:");
		mainPanel.add(lblIteration);

		//formatting for textName area
		setTextName(new JTextArea(1, 40));
		textName.setLineWrap(true);
		textName.setWrapStyleWord(true);
		textName.setMaximumSize(new Dimension(40, 2));
		AbstractDocument pDoc = (AbstractDocument) textName.getDocument();
		pDoc.setDocumentFilter(new DocumentSizeFilter(100));
		textName.setBorder((new JTextField()).getBorder());
		mainPanel.add(textName);
		

		//add listener for textName
		textName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						getTextName().transferFocus();
					}
					else {
						getTextName().transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					event.consume(); // consume the event and do nothing
				}
			}
		});
		
		//textName validator formatting
		textNameValid = new JTextArea(1, 40);
		textNameValid.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		textNameValid.setOpaque(false);
		textNameValid.setEnabled(false);
		textNameValid.setDisabledTextColor(Color.BLACK);
		textNameValid.setLineWrap(true);
		textNameValid.setWrapStyleWord(true);
		mainPanel.add(textNameValid);
		

		//textDescription formatting
		textDescription = new JTextArea(8, 40);
		textDescription.setLineWrap(true);
		textDescription.setWrapStyleWord(true);
		textDescription.setBorder((new JTextField()).getBorder());
		JScrollPane scroll = new JScrollPane(textDescription);

		// Add TextUpdateListeners. These check if the text component's text differs from the panel's Defect 
		// model and highlights them accordingly every time a key is pressed.
		txtTitleListener = new TextUpdateListener(this, textName, textNameValid);
		textName.addKeyListener(txtTitleListener);
		
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setSize(400, 450);
		scroll.setBorder(null);
		mainPanel.add(scroll);
		
		textDescription.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						getTextDescription().transferFocus();
					}
					else {
						getTextDescription().transferFocusBackward();
					}
					event.consume();
				}
			}
		});
		
		//description validator formatting
		textDescriptionValid = new JTextArea(1, 40);
		textDescriptionValid.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		textDescriptionValid.setOpaque(false);
		textDescriptionValid.setEnabled(false);
		textDescriptionValid.setDisabledTextColor(Color.BLACK);
		textDescriptionValid.setLineWrap(true);
		textDescriptionValid.setWrapStyleWord(true);
		mainPanel.add(textDescriptionValid);
		
		// Add TextUpdateListeners. These check if the text component's text differs from the panel's Defect 
		// model and highlights them accordingly every time a key is pressed.
		txtDescriptionListener = new TextUpdateListener(this, textDescription, textDescriptionValid);
		textDescription.addKeyListener(txtDescriptionListener);
		
		saveError = new JTextArea(1, 40);
		saveError.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		saveError.setOpaque(false);
		saveError.setEnabled(false);
		saveError.setDisabledTextColor(Color.BLACK);
		saveError.setLineWrap(true);
		saveError.setWrapStyleWord(true);
		mainPanel.add(saveError);
		
		//set up and add type combobox
		String[] availableTypes = { "", "Epic", "Theme", "User Story",
				"Non-functional", "Scenario" };
		setComboBoxType(new JComboBox(availableTypes));
		getComboBoxType().setPrototypeDisplayValue("Non-functional");
		mainPanel.add(getComboBoxType());
		
		//set up and add status combobox
		String[] availableStatuses = { "New", "In Progress", "Open",
				"Complete", "Deleted", "" };
		setComboBoxStatus(new JComboBox(availableStatuses));
		getComboBoxStatus().setPrototypeDisplayValue("Non-functional");
		mainPanel.add(getComboBoxStatus());

		//setup and add priorities combobox
		String[] availablePriorities = { "", "High", "Medium", "Low" };
		setComboBoxPriority(new JComboBox(availablePriorities));
		getComboBoxPriority().setPrototypeDisplayValue("Non-functional");
		mainPanel.add(getComboBoxPriority());
		
		String[] availableIterations = { "", "Iteration 1", "Iteration 2",
				"Iteration 3", "Iteration 4"};
		comboBoxIteration = new JComboBox(availableIterations);
		comboBoxIteration.setPrototypeDisplayValue("Non-functional");
		mainPanel.add(comboBoxIteration);
		
		JButton btnSave = new JButton("Save Requirement");
		mainPanel.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setAction(new CancelAction(requirement, this));
		mainPanel.add(btnCancel);

		//check if name field is blank
		if (requirement.getName().trim().equals("")) {
			btnSave.setAction(new SaveRequirementAction(requirement, this));
			comboBoxStatus.setEnabled(false);
			comboBoxStatus.setSelectedItem("NEW");
			comboBoxIteration.setEnabled(false);
			comboBoxIteration.setSelectedItem("");
		}
		else
		{
			btnSave.setAction(new EditRequirementAction(requirement, this));
			switch (requirement.getStatus()) {
			case NEW:
				getComboBoxStatus().setSelectedIndex(0);
				break;
			case IN_PROGRESS:
				getComboBoxStatus().setSelectedIndex(1);
				break;
			case OPEN:
				getComboBoxStatus().setSelectedIndex(2);
				break;
			case COMPLETE:
				getComboBoxStatus().setSelectedIndex(3);
				break;
			case DELETED:
				getComboBoxStatus().setSelectedIndex(4);
				break;
			case BLANK:
				getComboBoxStatus().setSelectedIndex(5);
				break;
			}
		}
		
		
		// Align left edges of objects
		layout.putConstraint(SpringLayout.WEST, lblName, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblDescription,	HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblType, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblStatus, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblPriority, HORIZONTAL_PADDING2, 
				SpringLayout.EAST, lblType);
		layout.putConstraint(SpringLayout.WEST, lblIteration, HORIZONTAL_PADDING2 + HORIZONTAL_CLOSE, 
				SpringLayout.EAST, lblStatus);
		layout.putConstraint(SpringLayout.WEST, textName, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, getTextNameValid(), HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, btnSave, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, btnCancel, HORIZONTAL_PADDING, 
				SpringLayout.EAST, btnSave);
		layout.putConstraint(SpringLayout.WEST, scroll,	HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, getTextDescriptionValid(), HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, getComboBoxType(),HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, getComboBoxStatus(),	HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, comboBoxPriority, HORIZONTAL_PADDING, 
				SpringLayout.EAST, comboBoxType);
		layout.putConstraint(SpringLayout.WEST, comboBoxIteration, HORIZONTAL_PADDING, 
				SpringLayout.EAST, comboBoxStatus);
		layout.putConstraint(SpringLayout.WEST, saveError, HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);


		

		// Align North Edges of Objects
		layout.putConstraint(SpringLayout.NORTH, lblName, VERTICAL_PADDING,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, getTextName(), VERTICAL_PADDING
				+ VERTICAL_CLOSE, SpringLayout.SOUTH, lblName);
		layout.putConstraint(SpringLayout.NORTH, getTextNameValid(), VERTICAL_PADDING
				+ VERTICAL_CLOSE2, SpringLayout.SOUTH, getTextName());
		layout.putConstraint(SpringLayout.NORTH, lblDescription,
				VERTICAL_PADDING, SpringLayout.SOUTH, getTextNameValid());
		layout.putConstraint(SpringLayout.NORTH, scroll,
				VERTICAL_PADDING + VERTICAL_CLOSE, SpringLayout.SOUTH,
				lblDescription);
		layout.putConstraint(SpringLayout.NORTH, getTextDescriptionValid(),
				VERTICAL_PADDING + VERTICAL_CLOSE2, SpringLayout.SOUTH,
				scroll);
		layout.putConstraint(SpringLayout.NORTH, lblType, VERTICAL_PADDING,
				SpringLayout.SOUTH, getTextDescriptionValid());
		layout.putConstraint(SpringLayout.NORTH, getComboBoxType(), VERTICAL_PADDING
				+ VERTICAL_CLOSE, SpringLayout.SOUTH, lblType);
		layout.putConstraint(SpringLayout.NORTH, lblStatus, VERTICAL_PADDING,
				SpringLayout.SOUTH, getComboBoxType());
		layout.putConstraint(SpringLayout.NORTH, getComboBoxStatus(),
				VERTICAL_PADDING + VERTICAL_CLOSE, SpringLayout.SOUTH,
				lblStatus);
		layout.putConstraint(SpringLayout.NORTH, lblPriority, VERTICAL_PADDING,
				SpringLayout.SOUTH, textDescriptionValid);
		layout.putConstraint(SpringLayout.NORTH, comboBoxPriority,
				VERTICAL_PADDING + VERTICAL_CLOSE, SpringLayout.SOUTH,
				lblPriority);
		layout.putConstraint(SpringLayout.NORTH, lblIteration,
				VERTICAL_PADDING , SpringLayout.SOUTH,
				comboBoxPriority);
		layout.putConstraint(SpringLayout.NORTH, comboBoxIteration,
				VERTICAL_PADDING + VERTICAL_CLOSE, SpringLayout.SOUTH,
				lblIteration);
		layout.putConstraint(SpringLayout.NORTH, btnSave, VERTICAL_PADDING,
				SpringLayout.SOUTH, comboBoxStatus);
		layout.putConstraint(SpringLayout.NORTH, btnCancel, VERTICAL_PADDING,
				SpringLayout.SOUTH, comboBoxStatus);
		layout.putConstraint(SpringLayout.NORTH, saveError, VERTICAL_PADDING
				+ VERTICAL_CLOSE2, SpringLayout.SOUTH, btnSave);


		getTextName().setText(requirement.getName());
		getTextDescription().setText(requirement.getDescription());
		switch (requirement.getType()) {
		case BLANK:
			getComboBoxType().setSelectedIndex(0);
			break;
		case EPIC:
			getComboBoxType().setSelectedIndex(1);
			break;
		case THEME:
			getComboBoxType().setSelectedIndex(2);
			break;
		case USER_STORY:
			getComboBoxType().setSelectedIndex(3);
			break;
		case NON_FUNCTIONAL:
			getComboBoxType().setSelectedIndex(4);
			break;
		case SCENARIO:
			getComboBoxType().setSelectedIndex(5);
		}
		switch (requirement.getPriority()) {
		case BLANK:
			getComboBoxPriority().setSelectedIndex(0);
			break;
		case HIGH:
			getComboBoxPriority().setSelectedIndex(1);
			break;
		case MEDIUM:
			getComboBoxPriority().setSelectedIndex(2);
			break;
		case LOW:
			getComboBoxPriority().setSelectedIndex(3);
			break;
		}

		noteView = new DetailNoteView(this.requirement, this);
		logView = new DetailLogView(this.requirement, this);
		userView = new AssigneePanel();
	
		//create the new eventPane
		DetailEventPane eventPane = new DetailEventPane(noteView, logView, userView);
		
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
	
	public void displaySaveError(String error) {
		this.saveError.setText(error);
	}


	/**
	 * @return the textName
	 */
	public JTextArea getTextName() {
		return textName;
	}

	/**
	 * @param textName the textName to set
	 */
	public void setTextName(JTextArea textName) {
		this.textName = textName;
	}

	/**
	 * @return the textNameValid
	 */
	public JTextArea getTextNameValid() {
		return textNameValid;
	}

	/**
	 * @param textNameValid the textNameValid to set
	 */
	public void setTextNameValid(JTextArea textNameValid) {
		this.textNameValid = textNameValid;
	}

	/**
	 * @return the textDescription
	 */
	public JTextArea getTextDescription() {
		return textDescription;
	}

	/**
	 * @param textDescription the textDescription to set
	 */
	public void setTextDescription(JTextArea textDescription) {
		this.textDescription = textDescription;
	}

	/**
	 * @return the textDescriptionValid
	 */
	public JTextArea getTextDescriptionValid() {
		return textDescriptionValid;
	}

	/**
	 * @param textDescriptionValid the textDescriptionValid to set
	 */
	public void setTextDescriptionValid(JTextArea textDescriptionValid) {
		this.textDescriptionValid = textDescriptionValid;
	}

	/**
	 * @return the comboBoxPriority
	 */
	public JComboBox getComboBoxPriority() {
		return comboBoxPriority;
	}

	/**
	 * @param comboBoxPriority the comboBoxPriority to set
	 */
	public void setComboBoxPriority(JComboBox comboBoxPriority) {
		this.comboBoxPriority = comboBoxPriority;
	}

	/**
	 * @return the comboBoxType
	 */
	public JComboBox getComboBoxType() {
		return comboBoxType;
	}

	/**
	 * @param comboBoxType the comboBoxType to set
	 */
	public void setComboBoxType(JComboBox comboBoxType) {
		this.comboBoxType = comboBoxType;
	}

	/**
	 * @return the comboBoxStatus
	 */
	public JComboBox getComboBoxStatus() {
		return comboBoxStatus;
	}

	/**
	 * @param comboBoxStatus the comboBoxStatus to set
	 */
	public void setComboBoxStatus(JComboBox comboBoxStatus) {
		this.comboBoxStatus = comboBoxStatus;
	}
	
	public Requirement getModel(){
		return requirement;
	}
}