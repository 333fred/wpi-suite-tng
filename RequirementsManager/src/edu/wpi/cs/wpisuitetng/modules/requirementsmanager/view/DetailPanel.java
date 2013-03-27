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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IRetreiveRequirementByIDControllerNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveRequirementByIDController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.FocusableTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.CancelAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.EditRequirementAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.SaveRequirementAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.DetailEventPane;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.DetailNoteView;

/**
 * JPanel class to display the different fields of the requirement
 * 
 * @author Swagasaurus
 * 
 */
public class DetailPanel extends FocusableTab implements IRetreiveRequirementByIDControllerNotifier {

	// Textfields
	private JTextArea textName;
	private JTextArea textDescription;
	private JTextArea textNameValid;
	private JTextArea textDescriptionValid;
	private JTextArea textIteration;
	JTextArea saveError;

	// combo boxes
	JComboBox comboBoxType;
	JComboBox comboBoxStatus;
	JComboBox comboBoxPriority;

	// requirement that is displayed
	Requirement requirement;
	// controller for all the tabs
	private MainTabController mainTabController;
	// the view that shows the notes
	private DetailNoteView noteView;

	//the view that shows the notes
	public DetailLogView logView;
	//the view that shows the users assigned to the requirement
	private AssigneePanel userView;
	
	JButton btnSave;
	
	protected final TextUpdateListener txtTitleListener;
	protected final TextUpdateListener txtDescriptionListener;
	protected final TextUpdateListener txtIterationListener;

	// swing constants
	private static final int VERTICAL_PADDING = 10;
	private static final int VERTICAL_CLOSE = -5;
	private static final int VERTICAL_CLOSE2 = -10;
	private static final int HORIZONTAL_PADDING = 20;
	
	/** The controller for retreiving the requirement from the sever */
	private RetrieveRequirementByIDController retreiveRequirementController;

	public DetailPanel(Requirement requirement, MainTabController mainTabController) {
		this.requirement = requirement;
		this.mainTabController = mainTabController;
		
		//initize the retreive requirement controller
		retreiveRequirementController = new RetrieveRequirementByIDController(this);
		
		JPanel mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(0, 2);
		setLayout(mainLayout);

		SpringLayout layout = new SpringLayout();
		mainPanel.setLayout(layout);

		// add labels to the overall panel
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

		// formatting for textName area
		textName = new JTextArea(1, 40);
		textName.setLineWrap(true);
		textName.setWrapStyleWord(true);
		textName.setMaximumSize(new Dimension(40, 2));
		AbstractDocument pDoc = (AbstractDocument) textName.getDocument();
		pDoc.setDocumentFilter(new DocumentSizeFilter(100));
		textName.setBorder((new JTextField()).getBorder());
		mainPanel.add(textName);
		

		// add listener for textName
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
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					event.consume(); // consume the event and do nothing
				}
			}
		});
		
		// textName validator formatting
		textNameValid = new JTextArea(1, 40);
		textNameValid.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		textNameValid.setOpaque(false);
		textNameValid.setEnabled(false);
		textNameValid.setDisabledTextColor(Color.BLACK);
		textNameValid.setLineWrap(true);
		textNameValid.setWrapStyleWord(true);
		mainPanel.add(textNameValid);
		
		// Add TextUpdateListeners, which check if the text component's text differs from the panel's requirement
		// model and highlights them accordingly every time a key is pressed.
		txtTitleListener = new TextUpdateListener(this, textName, textNameValid);
		textName.addKeyListener(txtTitleListener);
		
		// textDescription formatting
		textDescription = new JTextArea(8, 40);
		textDescription.setLineWrap(true);
		textDescription.setWrapStyleWord(true);
		textDescription.setBorder((new JTextField()).getBorder());
		JScrollPane scroll = new JScrollPane(textDescription);
		
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setSize(400, 450);
		scroll.setBorder(null);
		mainPanel.add(scroll);
		
		textDescription.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						textDescription.transferFocus();
					}
					else {
						textDescription.transferFocusBackward();
					}
					event.consume();
				}
			}
		});
		
		// description validator formatting
		textDescriptionValid = new JTextArea(1, 40);
		textDescriptionValid.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		textDescriptionValid.setOpaque(false);
		textDescriptionValid.setEnabled(false);
		textDescriptionValid.setDisabledTextColor(Color.BLACK);
		textDescriptionValid.setLineWrap(true);
		textDescriptionValid.setWrapStyleWord(true);
		mainPanel.add(textDescriptionValid);
		
		// Add TextUpdateListeners, which check if the text component's text differs from the panel's requirement
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
		
		// set up and add type combobox
		String[] availableTypes = { "", "Epic", "Theme", "User Story",
				"Non-functional", "Scenario" };
		comboBoxType = new JComboBox(availableTypes);
		comboBoxType.setPrototypeDisplayValue("Non-functional");
		mainPanel.add(comboBoxType);
		
		// set up and add status combobox
		String[] availableStatuses = { "New", "In Progress", "Open",
				"Complete", "Deleted"};

		comboBoxStatus = new JComboBox(availableStatuses);
		comboBoxStatus.setPrototypeDisplayValue("Non-functional");
		mainPanel.add(comboBoxStatus);
				
		// setup and add priorities combobox
		String[] availablePriorities = { "", "High", "Medium", "Low" };
		comboBoxPriority = new JComboBox(availablePriorities);
		comboBoxPriority.setPrototypeDisplayValue("Non-functional");
		mainPanel.add(comboBoxPriority);
		
		textIteration = new JTextArea(1,9);
		textIteration.setLineWrap(true);
		textIteration.setWrapStyleWord(true);
		textIteration.setBorder((new JTextField()).getBorder());
		mainPanel.add(textIteration);
		
		txtIterationListener = new TextUpdateListener(this, textIteration, null);
		textIteration.addKeyListener(txtIterationListener);
		
		btnSave = new JButton("Save Requirement");
		mainPanel.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setAction(new CancelAction(requirement, this));
		mainPanel.add(btnCancel);

		// check if name field is blank
		if (requirement.getName().trim().equals("")) {
			btnSave.setAction(new SaveRequirementAction(requirement, this));
			comboBoxStatus.setEnabled(false);
			comboBoxStatus.setSelectedItem("NEW");
			textIteration.setEnabled(false);
		} else {
			btnSave.setAction(new EditRequirementAction(requirement, this));
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
		layout.putConstraint(SpringLayout.WEST, lblDescription,	HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblType, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblStatus, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, textName, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, textNameValid, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, btnSave, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, btnCancel, HORIZONTAL_PADDING, 
				SpringLayout.EAST, btnSave);
		layout.putConstraint(SpringLayout.WEST, scroll,	HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, textDescriptionValid, HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, comboBoxType, HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, comboBoxStatus, HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblPriority, HORIZONTAL_PADDING, 
				SpringLayout.EAST, comboBoxType);
		layout.putConstraint(SpringLayout.WEST, lblIteration, HORIZONTAL_PADDING, 
				SpringLayout.EAST, comboBoxStatus);
		layout.putConstraint(SpringLayout.WEST, comboBoxPriority, HORIZONTAL_PADDING, 
				SpringLayout.EAST, comboBoxType);
		layout.putConstraint(SpringLayout.WEST, textIteration, HORIZONTAL_PADDING, 
				SpringLayout.EAST, comboBoxStatus);
		layout.putConstraint(SpringLayout.WEST, saveError, HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);


		

		// Align North Edges of Objects
		layout.putConstraint(SpringLayout.NORTH, lblName, VERTICAL_PADDING,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, textName, VERTICAL_PADDING
				+ VERTICAL_CLOSE, SpringLayout.SOUTH, lblName);
		layout.putConstraint(SpringLayout.NORTH, textNameValid, VERTICAL_PADDING
				+ VERTICAL_CLOSE2, SpringLayout.SOUTH, textName);
		layout.putConstraint(SpringLayout.NORTH, lblDescription,
				VERTICAL_PADDING, SpringLayout.SOUTH, textNameValid);
		layout.putConstraint(SpringLayout.NORTH, scroll,
				VERTICAL_PADDING + VERTICAL_CLOSE, SpringLayout.SOUTH,
				lblDescription);
		layout.putConstraint(SpringLayout.NORTH, textDescriptionValid,
				VERTICAL_PADDING + VERTICAL_CLOSE2, SpringLayout.SOUTH,
				scroll);
		layout.putConstraint(SpringLayout.NORTH, lblType, VERTICAL_PADDING,
				SpringLayout.SOUTH, textDescriptionValid);
		layout.putConstraint(SpringLayout.NORTH, comboBoxType, VERTICAL_PADDING
				+ VERTICAL_CLOSE, SpringLayout.SOUTH, lblType);
		layout.putConstraint(SpringLayout.NORTH, lblStatus, VERTICAL_PADDING,
				SpringLayout.SOUTH, comboBoxType);
		layout.putConstraint(SpringLayout.NORTH, comboBoxStatus,
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
		layout.putConstraint(SpringLayout.NORTH, textIteration,
				VERTICAL_PADDING + VERTICAL_CLOSE, SpringLayout.SOUTH,
				lblIteration);
		layout.putConstraint(SpringLayout.NORTH, btnSave, VERTICAL_PADDING,
				SpringLayout.SOUTH, comboBoxStatus);
		layout.putConstraint(SpringLayout.NORTH, btnCancel, VERTICAL_PADDING,
				SpringLayout.SOUTH, comboBoxStatus);
		layout.putConstraint(SpringLayout.NORTH, saveError, VERTICAL_PADDING
				+ VERTICAL_CLOSE2, SpringLayout.SOUTH, btnSave);


		textName.setText(requirement.getName());
		textDescription.setText(requirement.getDescription());
		textIteration.setText(Integer.toString(requirement.getIteration()));
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
		logView = new DetailLogView(this.requirement, this);
		userView = new AssigneePanel(requirement);
	
		// create the new eventPane
		DetailEventPane eventPane = new DetailEventPane(noteView, logView, userView);
		
		// Add everything to this
		add(mainPanel);
		add(eventPane);
		
		this.determineAvailableStatusOptions();
		this.disableSaveButton();
		
		//retrive an updated copy of the requirement from the server 
		//do it after everythign is initialized.
		retreiveRequirementFromSever();
	}
	/**
	 * Method to determine to which statuses the currently viewed requirement 
	 * can manually be set based on its current status as governed by the stakeholders' specs
	 */

	private void determineAvailableStatusOptions() {
		// String[] availableStatuses = { "New", "In Progress", "Open","Complete", "Deleted"};
		if (requirement.getStatus() == Status.NEW) {
			//New: New, Deleted
			this.comboBoxStatus.removeItem("In Progress");
			this.comboBoxStatus.removeItem("Open");
			this.comboBoxStatus.removeItem("Complete");
		}
		if (requirement.getStatus() == Status.IN_PROGRESS) {
			//In Progress: In Progress, Complete, Deleted	
			this.comboBoxStatus.removeItem("New");
			this.comboBoxStatus.removeItem("Open");
		}
		if (requirement.getStatus() == Status.OPEN) {
			//Open: Open, Deleted		
			this.comboBoxStatus.removeItem("New");
			this.comboBoxStatus.removeItem("In Progress");
			this.comboBoxStatus.removeItem("Complete");
		}
		if (requirement.getStatus() == Status.COMPLETE) {
			//Complete: Open, Complete, Deleted		
			this.comboBoxStatus.removeItem("New");
			this.comboBoxStatus.removeItem("In Progress");
		}
		if (requirement.getStatus() == Status.DELETED) {
			//Deleted: Open, Deleted, Complete
			this.comboBoxStatus.removeItem("New");
			this.comboBoxStatus.removeItem("In Progress");
			//his.comboBoxStatus.removeItem("Complete");		
		}
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
	
	public JTextArea getTextIteration() {
		return this.textIteration;
	}
	
	public void disableSaveButton() {
		this.btnSave.setEnabled(false);
	}
	
	public void enableSaveButton() {
		this.btnSave.setEnabled(true);
	}
	
	/** Updates the requirement from the server 
	 * TODO: Implement this 
	 */
	private void retreiveRequirementFromSever() {
		//get the requirement from the server with the id
		retreiveRequirementController.get(requirement.getrUID());
	}
	
	/** Updates all of the fields in the pane from the new Requirement
	 * 
	 * @param requirement The new requirement
	 * TODO: Implement this
	 * 
	 * Note - The requirement edit and save actions, pull all of the requirement data from this panel,
	 * 	Should be no need to update thier copy of requirement, assuming that the requiremetn ID never changes,
	 *  which this function should not do.
	 */
	
	private void updateFromRequirement(Requirement requirement) {
		this.requirement = requirement;
		
		//updates the nameField with the new name
		textName.setText(requirement.getName());
		//updates the description with the new description
		textDescription.setText(requirement.getDescription());
		//updates the interation with the new interation
		textIteration.setText(Integer.toString(requirement.getIteration()));
		
		//update the type of requirement
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
		
		//update the requirement priority
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
		
		//update the status
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
		
		//update the status combo box options
		determineAvailableStatusOptions();
		
		//update the noteview, logview, and userview.
		
		noteView.updateRequirement(requirement);
		logView.updateRequirement(requirement);
		userView.updateRequirement(requirement);
	}
	
	/** The server returned the updated requirement
	 * 
	 * @param requirement The updated requirement
	 */
	
	public void receivedData(Requirement requirement) {
		//update the requirement
		//TODO: uncomment this
		//updateFromRequirement(requirement);		
	}
	
	/** Server returned with an error, Print it out for now
	 * 
	 * @param errorMessage The error message received
	 */
	
	public void errorReceivingData(String errorMessage) {
		//we can just print an error
		System.out.println("Received error updating requirement: " + errorMessage);		
	}
}