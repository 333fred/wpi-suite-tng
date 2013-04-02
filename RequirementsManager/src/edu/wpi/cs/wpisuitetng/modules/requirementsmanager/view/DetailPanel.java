/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.text.AbstractDocument;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveRequirementByIDController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationIsNegativeException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
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
 */
public class DetailPanel extends FocusableTab {

	// Textfields
	private JTextArea textName;
	private JTextArea textDescription;
	private JTextArea textNameValid;
	private JTextArea textDescriptionValid;
	private JTextField textEstimate;

	private JTextField textActual;
	private JTextArea textRelease;
	JTextArea saveError;

	// combo boxes
	private JComboBox comboBoxType;
	private JComboBox comboBoxStatus;
	private JComboBox comboBoxPriority;
	private JComboBox comboBoxIteration;

	// requirement that is displayed
	private Requirement requirement;
	// controller for all the tabs
	private MainTabController mainTabController;
	// the view that shows the notes
	private DetailNoteView noteView;

	//the view that shows the notes
	public DetailLogView logView;
	//the view that shows the users assigned to the requirement
	private AssigneePanel userView;
	
	JButton btnSave;
	
	protected final TextUpdateListener textTitleListener;
	protected final TextUpdateListener textDescriptionListener;
	
	protected final ItemStateListener comboBoxTypeListener;
	protected final ItemStateListener comboBoxStatusListener;
	protected final ItemStateListener comboBoxPriorityListener;
	protected final ItemStateListener comboBoxIterationListener;
	
	protected final TextUpdateListener textEstimateListener;

	// swing constants
	private static final int VERTICAL_PADDING = 10;
	private static final int VERTICAL_CLOSE = -5;
	private static final int VERTICAL_CLOSE2 = -10;
	private static final int HORIZONTAL_PADDING = 20;
	
	/** The controller for retrieving the requirement from the sever */
	private RetrieveRequirementByIDController retreiveRequirementController;

	private SpringLayout layout;
	
	// add labels to the overall panel
	private JLabel lblName;
	private JLabel lblDescription;
	private JLabel lblType;
	private JLabel lblStatus;
	private JLabel lblPriority;
	private JLabel lblIteration;
	private JLabel lblEstimate;
	private JLabel lblActual;
	private JLabel lblRelease;

	private JScrollPane scroll;
	private JButton btnCancel;
	
	private JPanel mainPanel;
	
	public DetailPanel(Requirement requirement, MainTabController mainTabController) {
		this.requirement = requirement;
		this.mainTabController = mainTabController;
		
		mainPanel = new JPanel();
		Color defaultColor = mainPanel.getBackground();	
		GridLayout mainLayout = new GridLayout(0, 1);
		setLayout(mainLayout);
		layout = new SpringLayout();
		mainPanel.setLayout(layout);

		addJLabels();

		// formatting for textName area
		textName = new JTextArea(1, 40);
		textName.setLineWrap(true);
		textName.setWrapStyleWord(true);
		textName.setMaximumSize(new Dimension(40, 2));
		AbstractDocument textNameDoc = (AbstractDocument) textName.getDocument();
		textNameDoc.setDocumentFilter(new DocumentSizeFilter(100));
		textName.setBorder((new JTextField()).getBorder());
		textName.setName("Name");
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
		
		// Add TextUpdateListeners
		textTitleListener = new TextUpdateListener(this, textName, textNameValid);
		textName.addKeyListener(textTitleListener);
		
		// textDescription formatting
		textDescription = new JTextArea(8, 40);
		textDescription.setLineWrap(true);
		textDescription.setWrapStyleWord(true);
		textDescription.setBorder((new JTextField()).getBorder());
		textDescription.setName("Description");
		scroll = new JScrollPane(textDescription);
		
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
		
		// Add TextUpdateListeners
		textDescriptionListener = new TextUpdateListener(this, textDescription, textDescriptionValid);
		textDescription.addKeyListener(textDescriptionListener);
		
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
		comboBoxType.setBackground(Color.WHITE);
		mainPanel.add(comboBoxType);
		
		comboBoxTypeListener = new ItemStateListener(this, comboBoxType);
		comboBoxType.addItemListener(comboBoxTypeListener);
		
		// set up and add status combobox
		String[] availableStatuses = { "New", "In Progress", "Open",
				"Complete", "Deleted"};

		comboBoxStatus = new JComboBox(availableStatuses);
		comboBoxStatus.setPrototypeDisplayValue("Non-functional");
		comboBoxStatus.setBackground(Color.WHITE);
		mainPanel.add(comboBoxStatus);
		
		comboBoxStatusListener = new ItemStateListener(this, comboBoxStatus);
		comboBoxStatus.addItemListener(comboBoxStatusListener);
				
		// setup and add priorities combobox
		String[] availablePriorities = { "", "High", "Medium", "Low" };
		comboBoxPriority = new JComboBox(availablePriorities);
		comboBoxPriority.setPrototypeDisplayValue("Non-functional");
		comboBoxPriority.setBackground(Color.WHITE);
		mainPanel.add(comboBoxPriority);
		
		comboBoxPriorityListener = new ItemStateListener(this, comboBoxPriority);
		comboBoxPriority.addItemListener(comboBoxPriorityListener);
				
		List<Iteration> iterationList = IterationDatabase.getInstance().getAllIterations();
		iterationList = sortIterations(iterationList);
		
		String[] availableIterations = new String[iterationList.size()];
		for (int i = 0; i < iterationList.size(); i++) {
			availableIterations[i] = iterationList.get(i).getName();
		}
		
		comboBoxIteration = new JComboBox(availableIterations);
		comboBoxIteration.setName("Iteration");
		comboBoxIteration.setPrototypeDisplayValue("Non-functional");
		comboBoxIteration.setBackground(Color.WHITE);
		mainPanel.add(comboBoxIteration);
		
		comboBoxIterationListener = new ItemStateListener(this, comboBoxIteration);
		comboBoxIteration.addItemListener(comboBoxIterationListener);
		
		textEstimate = new JTextField(9);
		textEstimate.setBorder((new JTextField()).getBorder());
		textEstimate.setMaximumSize(textEstimate.getPreferredSize());
		textEstimate.setName("Estimate");
		textEstimate.setDisabledTextColor(Color.GRAY);
		AbstractDocument textEstimateDoc = (AbstractDocument) textEstimate.getDocument();
		textEstimateDoc.setDocumentFilter(new DocumentNumberAndSizeFilter(14)); // box allows 14 characters before expanding
		mainPanel.add(textEstimate);
		
		textEstimate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						textEstimate.transferFocus();
					}
					else {
						textEstimate.transferFocusBackward();
					}
					event.consume();
				}
			}
		});
		
		// Add TextUpdateListeners,
		textEstimateListener = new TextUpdateListener(this, textEstimate, null);
		textEstimate.addKeyListener(textEstimateListener);
		
		textActual = new JTextField(9);
		textActual.setBorder((new JTextField()).getBorder());
		textActual.setEnabled(false); // DISABLE THIS ITERATION
		textActual.setBackground(defaultColor);
		textActual.setMaximumSize(textActual.getPreferredSize());
		textActual.setName("Actual");
		textActual.setDisabledTextColor(Color.GRAY);
		AbstractDocument textActualDoc = (AbstractDocument) textActual.getDocument();
		textActualDoc.setDocumentFilter(new DocumentNumberAndSizeFilter(14)); // box allows 14 characters before expanding
		mainPanel.add(textActual);
		
		textActual.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						textActual.transferFocus();
					}
					else {
						textActual.transferFocusBackward();
					}
					event.consume();
				}
				if(event.getKeyCode() == KeyEvent.VK_ENTER) {
					// don't allow enter, consume the event, do nothing
					event.consume();
				}
			}
		});
		
		textRelease = new JTextArea(1,9);
		textRelease.setBorder((new JTextField()).getBorder());
		textRelease.setEnabled(false); // DISABLE THIS ITERATION
		textRelease.setBackground(defaultColor);
		textRelease.setName("Release");
		textRelease.setDisabledTextColor(Color.GRAY);
		mainPanel.add(textRelease);
		
		textRelease.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						textRelease.transferFocus();
					}
					else {
						textRelease.transferFocusBackward();
					}
					event.consume();
				}
				if(event.getKeyCode() == KeyEvent.VK_ENTER) {
					// don't allow enter, consume the event, do nothing
					event.consume();
				}
			}
		});
		
		btnSave = new JButton("Save Requirement");
		mainPanel.add(btnSave);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setAction(new CancelAction(this));
		mainPanel.add(btnCancel);

		// check if name field is blank
		if (requirement.getName().trim().equals("")) {
			btnSave.setAction(new SaveRequirementAction(requirement, this));
			comboBoxStatus.setEnabled(false);
			comboBoxStatus.setSelectedItem("NEW");
			textEstimate.setEnabled(false);
			textEstimate.setBackground(defaultColor);
			comboBoxIteration.setEnabled(false);
			comboBoxIteration.setBackground(defaultColor);
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
		
		
		addComponentConstraints();
		loadFields();
		
		logView = new DetailLogView(this.requirement, this);
		noteView = new DetailNoteView(this.requirement, this);
		userView = new AssigneePanel(requirement,this);
		
	
		// create the new eventPane
		DetailEventPane eventPane = new DetailEventPane(noteView, logView, userView);
		
		if(requirement.getStatus() == Status.DELETED){
			eventPane.disableUserButtons();
		}		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.getViewport().add(mainPanel);		
		
		//TODO: Implement a proper preferredHeight, but Width works
		//set the preffered size of mainPanel
		//int preferredHeight = (int) (btnCancel.getLocation().getY() + btnCancel.getPreferredSize().getHeight() + VERTICAL_PADDING * 2);
		int preferredHeight = 515;
		int preferredWidth = (int) (textDescription.getPreferredSize().getWidth() + HORIZONTAL_PADDING * 2);
		
		//set the preferred size
		mainPanel.setPreferredSize(new Dimension(preferredWidth, preferredHeight));		
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scrollPane, eventPane);
		//mainLayout = new SpringLayout();
		add(splitPane);

		splitPane.setResizeWeight(0.5);
		
		this.determineAvailableStatusOptions();
		this.disableSaveButton();
		this.disableAllFieldsIfDeleted();
		
		// prevent in-progress or complete requirements from having their estimates changed
		if (requirement.getStatus() == Status.IN_PROGRESS ||  requirement.getStatus() == Status.COMPLETE) {
			textEstimate.setEnabled(false);
			textEstimate.setBackground(defaultColor);
		}
		
		// prevent requirements with subrequirements from having their estimates changed
		if (requirement.getSubRequirements() != null && requirement.getSubRequirements().size() > 0){
			// TODO: ensure that the estimate of any requirement with subrequirements
			// is the sum of the estimates of its subrequirements
			textEstimate.setEnabled(false);
			textEstimate.setBackground(defaultColor);
		}
		
	}
	/**
	 * 
	 */
	private void addJLabels() {
		// add labels to the overall panel
		lblName = new JLabel("Name:");
		lblDescription = new JLabel("Description:");
		lblType = new JLabel("Type:");		
		lblStatus = new JLabel("Status:");
		lblPriority = new JLabel("Priority:");
		lblIteration = new JLabel("Iteration:");	
		lblEstimate = new JLabel("Estimate:");
		lblActual = new JLabel("Actual:");
		lblRelease = new JLabel("Release Number:");
		
		mainPanel.add(lblName);
		mainPanel.add(lblDescription);
		mainPanel.add(lblType);
		mainPanel.add(lblStatus);
		mainPanel.add(lblPriority);
		mainPanel.add(lblIteration);
		mainPanel.add(lblEstimate);	
		mainPanel.add(lblActual);
		mainPanel.add(lblRelease);
	}
	/**
	 * @param requirement
	 */
	private void loadFields() {
		textName.setText(requirement.getName());
		textDescription.setText(requirement.getDescription());
		textEstimate.setText(Integer.toString(requirement.getEstimate()));

		try {
			comboBoxIteration.setSelectedItem(IterationDatabase.getInstance().getIteration(requirement.getIteration()).getName());
			System.out.println(IterationDatabase.getInstance().getIteration(requirement.getIteration()).getName());
		} catch (IterationNotFoundException e) {
			System.out.println("Exception Caught: Iteration Not Found.");
		}
		
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
	}
	/**
	 * @param lblName
	 * @param lblDescription
	 * @param lblType
	 * @param lblStatus
	 * @param lblPriority
	 * @param lblIteration
	 * @param lblEstimate
	 * @param lblActual
	 * @param lblRelease
	 * @param scroll
	 * @param btnCancel
	 */
	private void addComponentConstraints() {
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
		layout.putConstraint(SpringLayout.WEST, comboBoxIteration, HORIZONTAL_PADDING, 
				SpringLayout.EAST, comboBoxStatus);
		layout.putConstraint(SpringLayout.WEST, lblEstimate, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblActual, HORIZONTAL_PADDING, 
				SpringLayout.EAST, comboBoxStatus);
		layout.putConstraint(SpringLayout.WEST, textEstimate, HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, textActual, HORIZONTAL_PADDING, 
				SpringLayout.EAST, comboBoxStatus);
		layout.putConstraint(SpringLayout.WEST, lblRelease, HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, textRelease, HORIZONTAL_PADDING, 
				SpringLayout.WEST, this);
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
		layout.putConstraint(SpringLayout.NORTH, comboBoxIteration,
				VERTICAL_PADDING + VERTICAL_CLOSE, SpringLayout.SOUTH,
				lblIteration);
		layout.putConstraint(SpringLayout.NORTH, lblEstimate, VERTICAL_PADDING,
				SpringLayout.SOUTH, comboBoxStatus);
		layout.putConstraint(SpringLayout.NORTH, lblActual,
				VERTICAL_PADDING - VERTICAL_CLOSE, SpringLayout.SOUTH,
				comboBoxIteration);
		layout.putConstraint(SpringLayout.NORTH, textEstimate,
				VERTICAL_PADDING + VERTICAL_CLOSE, SpringLayout.SOUTH,
				lblEstimate);
		layout.putConstraint(SpringLayout.NORTH, textActual,
				VERTICAL_PADDING + VERTICAL_CLOSE, SpringLayout.SOUTH,
				lblActual);
		layout.putConstraint(SpringLayout.NORTH, lblRelease,
				VERTICAL_PADDING , SpringLayout.SOUTH,
				textEstimate);
		layout.putConstraint(SpringLayout.NORTH, textRelease,
				VERTICAL_PADDING + VERTICAL_CLOSE, SpringLayout.SOUTH,
				lblRelease);
		layout.putConstraint(SpringLayout.NORTH, btnSave, VERTICAL_PADDING,
				SpringLayout.SOUTH, textRelease);
		layout.putConstraint(SpringLayout.NORTH, btnCancel, VERTICAL_PADDING,
				SpringLayout.SOUTH, textRelease);
		layout.putConstraint(SpringLayout.NORTH, saveError, VERTICAL_PADDING
				+ VERTICAL_CLOSE2, SpringLayout.SOUTH, btnSave);
	}
	/**
	 * Method to determine to which statuses the currently viewed requirement 
	 * can manually be set based on its current status as governed by the stakeholders' specs;
	 * also sets the combo box to the appropriate set of statuses
	 */
	private void determineAvailableStatusOptions() {
		// String[] availableStatuses = { "New", "In Progress", "Open","Complete", "Deleted"};
		if (requirement.getStatus() == Status.IN_PROGRESS) {
			//In Progress: In Progress, Complete, Deleted	
			this.comboBoxStatus.removeItem("New");
			this.comboBoxStatus.removeItem("Open");
			this.comboBoxStatus.removeItem("Deleted");
		}
		else if (requirement.getSubRequirements().size() > 0 || !requirement.tasksCompleted())
		{
			this.comboBoxStatus.removeItem("Deleted");
		}
		if (requirement.getStatus() == Status.NEW) {
			//New: New, Deleted
			this.comboBoxStatus.removeItem("In Progress");
			this.comboBoxStatus.removeItem("Open");
			this.comboBoxStatus.removeItem("Complete");
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
			this.comboBoxStatus.removeItem("Complete");		
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
	
	/**
	 * @return the textEstimate
	 */
	public JTextField getTextEstimate() {
		return textEstimate;
	}
	
	/**
	 * @param textEstimate the textEstimate to set
	 */
	public void setTextEstimate(JTextField textEstimate) {
		this.textEstimate = textEstimate;
	}
	
	public Requirement getModel(){
		return requirement;
	}
	
	public JComboBox getTextIteration() {
		return this.comboBoxIteration;
	}
	
	public void disableSaveButton() {
		this.btnSave.setEnabled(false);
	}
	
	public void enableSaveButton() {
		this.btnSave.setEnabled(true);
	}
	public List<String> getAssignedUsers() {
		return this.userView.getAssignedUsersList();
	}
	
	
	/**
	 * Checks requirement status:
	 * 	If deleted, disables all editable fields except status
	 * 	Else do nothing
	 */
	public void disableAllFieldsIfDeleted(){
		JPanel panel = new JPanel();
		Color defaultColor = panel.getBackground();
		
		if(requirement.getStatus() != Status.DELETED)
			return;
		textName.setEnabled(false);
		textName.setBackground(defaultColor);
		textName.setDisabledTextColor(Color.GRAY);
		textDescription.setEnabled(false);
		textDescription.setBackground(defaultColor);
		textDescription.setDisabledTextColor(Color.GRAY);
		comboBoxIteration.setEnabled(false);
		comboBoxIteration.setBackground(defaultColor);
		textRelease.setEnabled(false);
		textEstimate.setEnabled(false);
		textEstimate.setBackground(defaultColor);
		textActual.setEnabled(false);
		
		comboBoxType.setEnabled(false);
		comboBoxPriority.setEnabled(false);
		
		
	}
	
	public static List<Iteration> sortIterations(List<Iteration> iterations) {	
		Collections.sort(iterations, new IterationComparator());
		return iterations;
	}

}