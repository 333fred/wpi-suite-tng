/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Steven Kordell, Alex Chen, Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.text.AbstractDocument;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.Tab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.CancelAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.EditRequirementAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.SaveRequirementAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.atest.DetailATestView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.DetailEventPane;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.DocumentNumberAndSizeFilter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.DocumentSizeFilter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.ItemStateListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.TextUpdateListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.DetailNoteView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.SubRequirementPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.task.DetailTaskView;

/**
 * JPanel class to display the different fields of the requirement
 */
@SuppressWarnings("serial")
public class DetailPanel extends Tab implements ISaveNotifier {

	public enum Mode {
		CREATE, EDIT, UPDATE, VIEW;
	}
	
	/** VIEW: Only View
	 *  UPDATE: Change ActualEffort, Notes, Tests, Tasks
	 *  ADMIN: Everything
	 */

	// Text fields
	private JTextArea textName;
	private JTextArea textDescription;
	private JTextArea textNameValid;
	private JTextArea textDescriptionValid;
	private JTextArea textSaveError;
	private JTextField textEstimate;
	private JTextField textActual;
	private JTextField textRelease;

	// combo boxes
	private JComboBox comboBoxType;
	private JComboBox comboBoxStatus;
	private JComboBox comboBoxPriority;
	private JComboBox comboBoxIteration;

	// Event Views
	private DetailNoteView noteView;
	private DetailLogView logView;
	private AssigneePanel userView;
	private DetailTaskView taskView;
	private SubRequirementPanel subRequirementView;
	private DetailATestView aTestView;

	// requirement that is displayed
	private Requirement requirement;

	// controller for all the tabs
	private MainTabController mainTabController;

	// Buttons
	private JButton btnSave;
	private JButton btnCancel;

	// layouts
	private GridLayout mainLayout;
	private SpringLayout layout;
	private SpringLayout buttonPanelLayout;

	// OnChange Action Listeners
	private TextUpdateListener textTitleListener;
	private TextUpdateListener textDescriptionListener;
	private ItemStateListener comboBoxTypeListener;
	private ItemStateListener comboBoxStatusListener;
	private ItemStateListener comboBoxPriorityListener;
	private ItemStateListener comboBoxIterationListener;
	private TextUpdateListener textEstimateListener;
	private TextUpdateListener textActualListener;
	private TextUpdateListener textReleaseListener;

	// swing spacing constants
	private static final int VERTICAL_PADDING = 10;
	private static final int HORIZONTAL_PADDING = 20;
	private static final int CLOSE = -5;

	// Text labels
	private JLabel lblName;
	private JLabel lblDescription;
	private JLabel lblType;
	private JLabel lblStatus;
	private JLabel lblPriority;
	private JLabel lblIteration;
	private JLabel lblEstimate;
	private JLabel lblActual;
	private JLabel lblRelease;
	private JLabel lblTotalEstimate;
	private JLabel lblTotEstDisplay;

	// Sub-panels
	private JPanel mainPanel;
	private JPanel buttonPanel;
	private JPanel leftPanel;
	private JScrollPane scrollDescription;
	private JScrollPane mainScrollPane;
	private DetailEventPane eventPane;
	private JSplitPane splitPane;

	// Boolean to indicate whether the tab should be closed upon saving
	private boolean closeTab;

	private Color defaultColor;

	/** The edit mode of this requirement view */
	public Mode mode;

	public DetailPanel(Requirement requirement, Mode mode,
			MainTabController mainTabController) {
		this.requirement = requirement;
		this.mode = mode;
		this.mainTabController = mainTabController;

		createPanels();
		createComponents();
		setPanelSizes();
		createComponentListeners();
		addComponents();
		addComponentConstraints();
		loadFields();
		createEventSidePanel();
		addSplitPane();
		setDisabledTextColor();
		disableFieldsMode();
		disableSaveButton();
		
	}
	
	private void createPanels() {
		this.mainPanel = new JPanel();
		this.defaultColor = mainPanel.getBackground();
		this.mainLayout = new GridLayout(0, 1);
		this.setLayout(mainLayout);
		layout = new SpringLayout();
		mainPanel.setLayout(layout);
		mainScrollPane = new JScrollPane();
		mainScrollPane.getVerticalScrollBar().setUnitIncrement(10);
		mainScrollPane.getViewport().add(mainPanel);
		mainScrollPane.setBorder(null);

		buttonPanelLayout = new SpringLayout();
		buttonPanel = new JPanel(buttonPanelLayout);
		leftPanel = new JPanel(new BorderLayout());
	}
	
	private void createComponents() {
		createJLabels();
		createTextNameArea();
		createTextNameValidArea();
		createTextDescriptionArea();
		createTextDescriptionValidArea();
		createSaveErrorArea();
		createComboBoxes();
		createTextEstimateArea();
		createTextActualArea();
		createTextReleaseArea();
		createButtons();
	}

	private void setPanelSizes() {
		buttonPanel.setPreferredSize(new Dimension(textSaveError
				.getPreferredSize().width
				+ textSaveError.getPreferredSize().width, btnSave
				.getPreferredSize().height + 10));
		int preferredHeight = 515;
		int preferredWidth = (int) (textDescription.getPreferredSize()
				.getWidth() + HORIZONTAL_PADDING * 2);
		mainPanel.setPreferredSize(new Dimension(preferredWidth,
				preferredHeight));
	}
	
	private void createComponentListeners() {
		if (mode == Mode.VIEW) {
		
		}
		else if (mode == Mode.UPDATE) {
			addTextActualListener();	
		}
		else {
			addTextNameAreaListener();
			addTextDescriptionAreaListener();
			addComboBoxListeners();
			addTextEstimateListener();
			addTextActualListener();
			addTextReleaseListener();
		}
	}
	
	private void addComponents() {
		addJLabels();
		mainPanel.add(textName);
		mainPanel.add(textNameValid);
		mainPanel.add(scrollDescription);
		mainPanel.add(textDescriptionValid);
		mainPanel.add(comboBoxStatus);
		mainPanel.add(comboBoxType);
		mainPanel.add(comboBoxPriority);
		mainPanel.add(comboBoxIteration);
		mainPanel.add(textEstimate);
		mainPanel.add(textActual);
		mainPanel.add(textRelease);

		buttonPanel.add(btnSave);
		buttonPanel.add(btnCancel);
		buttonPanel.add(textSaveError);

		leftPanel.add(mainScrollPane, BorderLayout.CENTER);
		leftPanel.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private void addSplitPane() {
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel,
				eventPane);
		add(splitPane);
		splitPane.setResizeWeight(0.75);
	}

	/**
	 * Disables the editing fields based upon the edit mode
	 * 
	 */

	private void disableFieldsMode() {
		if (mode == Mode.VIEW) {
			textName.setEnabled(false);
			textName.setBackground(defaultColor);

			textDescription.setEnabled(false);
			textDescription.setBackground(defaultColor);

			getComboBoxIteration().setEnabled(false);
			getComboBoxIteration().setBackground(defaultColor);
			getComboBoxIteration().setForeground(Color.BLACK);

			getComboBoxStatus().setEnabled(false);
			getComboBoxStatus().setBackground(defaultColor);
			getComboBoxStatus().setForeground(Color.BLACK);

			textRelease.setEnabled(false);
			textRelease.setBackground(defaultColor);

			textEstimate.setEnabled(false);
			textEstimate.setBackground(defaultColor);

			textActual.setEnabled(false);
			textActual.setBackground(defaultColor);

			comboBoxType.setEnabled(false);
			comboBoxType.setBackground(defaultColor);
			comboBoxType.setForeground(Color.BLACK);

			comboBoxPriority.setEnabled(false);
			comboBoxPriority.setBackground(defaultColor);
			comboBoxPriority.setForeground(Color.BLACK);

		} else if (mode == Mode.UPDATE) {
			textName.setEnabled(false);
			textName.setBackground(defaultColor);

			textDescription.setEnabled(false);
			textDescription.setBackground(defaultColor);

			getComboBoxIteration().setEnabled(false);
			getComboBoxIteration().setBackground(defaultColor);
			getComboBoxIteration().setForeground(Color.BLACK);

			getComboBoxStatus().setEnabled(false);
			getComboBoxStatus().setBackground(defaultColor);
			getComboBoxStatus().setForeground(Color.BLACK);

			textRelease.setEnabled(false);
			textRelease.setBackground(defaultColor);

			textEstimate.setEnabled(false);
			textEstimate.setBackground(defaultColor);
			
			comboBoxType.setEnabled(false);
			comboBoxType.setBackground(defaultColor);
			comboBoxType.setForeground(Color.BLACK);

			comboBoxPriority.setEnabled(false);
			comboBoxPriority.setBackground(defaultColor);
			comboBoxPriority.setForeground(Color.BLACK);
			
			

		} else if (mode == Mode.EDIT) {
			// edit, everything can be edited
		}
	}

	/** Sets the disabled text color of all the fields */

	private void setDisabledTextColor() {
		textName.setDisabledTextColor(Color.BLACK);
		textDescription.setDisabledTextColor(Color.BLACK);
		textRelease.setDisabledTextColor(Color.BLACK);
		textEstimate.setDisabledTextColor(Color.BLACK);
		textActual.setDisabledTextColor(Color.BLACK);
	}

	private void createEventSidePanel() {
		logView = new DetailLogView(this.getRequirement(), this);
		noteView = new DetailNoteView(this.getRequirement(), this);
		userView = new AssigneePanel(requirement, this);
		taskView = new DetailTaskView(this.getRequirement(), this);
		aTestView = new DetailATestView(this.getRequirement(), this);
		subRequirementView = new SubRequirementPanel(this.getRequirement(),
				this);
		eventPane = new DetailEventPane(noteView, logView, userView, taskView,
				aTestView, subRequirementView);
		if (requirement.getStatus() == Status.DELETED
				|| requirement.getStatus() == Status.COMPLETE || mode == Mode.VIEW ) {//|| mode == Mode.CREATE
			eventPane.disableUserButtons();
		}
		if (mode == Mode.UPDATE) {
			eventPane.disableUsersAndSubReqs();
		}
		else if (mode ==Mode.CREATE){
			eventPane.disableSubReqs();
		}
		
	}

	private void createButtons() {
		btnSave = new JButton("Save Requirement");
		btnCancel = new JButton("Cancel");
		btnCancel.setAction(new CancelAction(this));

		switch (mode) {
		case CREATE:
			btnSave.setText("Create Requirement");
			break;
		case EDIT:
		case UPDATE:
		case VIEW:
			btnSave.setText("Save Requirement");
			break;
		}
	}

	private void addTextReleaseListener() {
		textRelease.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						textRelease.transferFocus();
					} else {
						textRelease.transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					// don't allow enter, consume the event, do nothing
					event.consume();
				}
			}
		});
		textReleaseListener = new TextUpdateListener(this, textRelease, null);
		textRelease.addKeyListener(textReleaseListener);
	}

	private void createTextReleaseArea() {
		textRelease = new JTextField(9);
		textRelease.setBorder((new JTextField()).getBorder());
		textRelease.setName("ReleaseNum");
		textRelease.setMaximumSize(textRelease.getPreferredSize());
		textRelease.setDisabledTextColor(Color.GRAY);
	}

	private void addTextActualListener() {
		textActual.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						textActual.transferFocus();
					} else {
						textActual.transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					// don't allow enter, consume the event, do nothing
					event.consume();
				}
			}
		});
		textActualListener = new TextUpdateListener(this, textActual, null);
		textActual.addKeyListener(textActualListener);
	}

	private void createTextActualArea() {
		textActual = new JTextField(9);
		textActual.setBorder((new JTextField()).getBorder());
		textActual.setEnabled(false); // disabled until complete
		textActual.setBackground(defaultColor);
		textActual.setMaximumSize(textActual.getPreferredSize());
		textActual.setName("Effort");
		textActual.setDisabledTextColor(Color.GRAY);
		AbstractDocument textActualDoc = (AbstractDocument) textActual
				.getDocument();
		// box allows 12 numbers (around max int)
		textActualDoc.setDocumentFilter(new DocumentNumberAndSizeFilter(12));

		// actual field is editable when requirement is complete
		if (requirement.getStatus() == Status.COMPLETE) {
			textActual.setEnabled(true);
			textActual.setBackground(Color.WHITE);
		}
	}

	private void addTextEstimateListener() {
		textEstimate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						textEstimate.transferFocus();
					} else {
						textEstimate.transferFocusBackward();
					}
					event.consume();
				}
			}
		});
		textEstimateListener = new TextUpdateListener(this, textEstimate, null);
		textEstimate.addKeyListener(textEstimateListener);
	}

	private void createTextEstimateArea() {
		textEstimate = new JTextField(9);
		textEstimate.setBorder((new JTextField()).getBorder());
		textEstimate.setMaximumSize(textEstimate.getPreferredSize());
		textEstimate.setName("Estimate");
		textEstimate.setDisabledTextColor(Color.GRAY);
		AbstractDocument textEstimateDoc = (AbstractDocument) textEstimate
				.getDocument();
		textEstimateDoc.setDocumentFilter(new DocumentNumberAndSizeFilter(12));

		// prevent in-progress or complete requirements from having their
		// estimates changed
		if (requirement.getStatus() == Status.IN_PROGRESS
				|| requirement.getStatus() == Status.COMPLETE) {
			textEstimate.setEnabled(false);
			textEstimate.setBackground(defaultColor);
		}
	}

	private void addComboBoxListeners() {
		comboBoxStatusListener = new ItemStateListener(this, comboBoxStatus);
		comboBoxStatus.addItemListener(comboBoxStatusListener);
		comboBoxTypeListener = new ItemStateListener(this, comboBoxType);
		comboBoxType.addItemListener(comboBoxTypeListener);
		comboBoxPriorityListener = new ItemStateListener(this, comboBoxPriority);
		comboBoxPriority.addItemListener(comboBoxPriorityListener);
		comboBoxIterationListener = new ItemStateListener(this,
				getComboBoxIteration());
		getComboBoxIteration().addItemListener(comboBoxIterationListener);
	}

	private void createComboBoxes() {
		// Type ComboBox

		comboBoxType = new JComboBox();
		comboBoxType.setBackground(Color.WHITE);

		for (Type t : Type.values()) {
			comboBoxType.addItem(t.toString());
		}
		comboBoxType.setPrototypeDisplayValue(Type.NON_FUNCTIONAL.toString());
		comboBoxType.setSelectedIndex(0);

		comboBoxStatus = new JComboBox();
		comboBoxStatus.setBackground(Color.WHITE);

		for (Status t : Status.values()) {
			comboBoxStatus.addItem(t.toString());
		}
		comboBoxStatus.setPrototypeDisplayValue(Type.NON_FUNCTIONAL.toString());
		comboBoxStatus.setSelectedIndex(0);

		this.determineAvailableStatusOptions();

		comboBoxPriority = new JComboBox();

		comboBoxPriority.setBackground(Color.WHITE);

		for (Priority t : Priority.values()) {
			comboBoxPriority.addItem(t.toString());
		}
		comboBoxPriority.setSelectedIndex(0);
		comboBoxPriority.setPrototypeDisplayValue(Type.NON_FUNCTIONAL
				.toString());

		createIterationComboBox();
	}

	private void createIterationComboBox() {
		List<Iteration> iterationList = IterationDatabase.getInstance()
				.getAll();
		iterationList = Iteration.sortIterations(iterationList);
		int availableIterationNum = 0;
		int currentAvailableIterationIndex = 0;
		Date currentDate = new Date();
		for (Iteration iteration : iterationList) {
			// if the current date is before the end date of the iteration, or
			// the iteration is this requirement's current iteration or is the
			// backlog
			if ((currentDate.compareTo(iteration.getEndDate()) <= 0
					|| requirement.getIteration() == iteration.getId() || iteration
					.getId() == -1) && iteration.getId() != -2) {
				// increment the number of available iterations
				availableIterationNum++;
			}

		}
		String[] availableIterations = new String[availableIterationNum];
		for (Iteration iteration : iterationList) {
			// if the current date is before the end date of the iteration,
			// or the iteration is this requirement's current iteration,
			// or it is the backlog, add it to the list
			if ((currentDate.compareTo(iteration.getEndDate()) <= 0
					|| requirement.getIteration() == iteration.getId() || iteration
					.getId() == -1) && iteration.getId() != -2) {
				availableIterations[currentAvailableIterationIndex] = iteration
						.getName();
				currentAvailableIterationIndex++;
			}
		}
		comboBoxIteration = new JComboBox(availableIterations);
		comboBoxIteration.setName("Iteration");
		comboBoxIteration.setPrototypeDisplayValue("Non-Functional");
		comboBoxIteration.setBackground(Color.WHITE);
	}

	private void createSaveErrorArea() {
		textSaveError = new JTextArea(1, 40);
		textSaveError.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		textSaveError.setOpaque(false);
		textSaveError.setEnabled(false);
		textSaveError.setDisabledTextColor(Color.BLACK);
		textSaveError.setLineWrap(true);
		textSaveError.setWrapStyleWord(true);
	}

	private void createTextDescriptionValidArea() {
		textDescriptionValid = new JTextArea(1, 40);
		textDescriptionValid.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		textDescriptionValid.setOpaque(false);
		textDescriptionValid.setEnabled(false);
		textDescriptionValid.setDisabledTextColor(Color.BLACK);
		textDescriptionValid.setLineWrap(true);
		textDescriptionValid.setWrapStyleWord(true);
	}

	private void addTextDescriptionAreaListener() {
		textDescription.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						textDescription.transferFocus();
					} else {
						textDescription.transferFocusBackward();
					}
					event.consume();
				}
			}
		});
		textDescriptionListener = new TextUpdateListener(this, textDescription,
				textDescriptionValid);
		textDescription.addKeyListener(textDescriptionListener);
	}

	private void createTextDescriptionArea() {
		textDescription = new JTextArea(8, 40);
		textDescription.setLineWrap(true);
		textDescription.setWrapStyleWord(true);
		textDescription.setBorder((new JTextField()).getBorder());
		textDescription.setName("Description");
		textDescription.setDisabledTextColor(Color.GRAY);
		scrollDescription = new JScrollPane(textDescription);
		scrollDescription
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollDescription.setSize(400, 450);
		scrollDescription.setBorder(null);
	}

	private void createTextNameValidArea() {
		textNameValid = new JTextArea(1, 40);
		textNameValid.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		textNameValid.setOpaque(false);
		textNameValid.setEnabled(false);
		textNameValid.setDisabledTextColor(Color.BLACK);
		textNameValid.setLineWrap(true);
		textNameValid.setWrapStyleWord(true);
	}

	private void addTextNameAreaListener() {
		textName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						textName.transferFocus();
					} else {
						textName.transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					event.consume(); // consume the event and do nothing
				}
			}
		});
		textTitleListener = new TextUpdateListener(this, textName,
				textNameValid);
		textName.addKeyListener(textTitleListener);
	}

	private void createTextNameArea() {
		textName = new JTextArea(1, 40);
		textName.setLineWrap(true);
		textName.setWrapStyleWord(true);
		textName.setMaximumSize(new Dimension(40, 2));
		AbstractDocument textNameDoc = (AbstractDocument) textName
				.getDocument();
		textNameDoc.setDocumentFilter(new DocumentSizeFilter(100));
		textName.setBorder((new JTextField()).getBorder());
		textName.setName("Name");
		textName.setDisabledTextColor(Color.GRAY);
	}

	public void createJLabels() {
		lblName = new JLabel("*Name:");
		lblDescription = new JLabel("*Description:");
		lblType = new JLabel("Type:");
		lblStatus = new JLabel("Status:");
		lblPriority = new JLabel("Priority:");
		lblIteration = new JLabel("Iteration:");
		lblEstimate = new JLabel("Estimate:");
		lblActual = new JLabel("Actual:");
		lblRelease = new JLabel("Release Number:");
		lblTotalEstimate = new JLabel("Total Estimate:");
		lblTotEstDisplay = new JLabel("");
	}

	/**
	 * 
	 */
	private void addJLabels() {
		mainPanel.add(lblName);
		mainPanel.add(lblDescription);
		mainPanel.add(lblType);
		mainPanel.add(lblStatus);
		mainPanel.add(lblPriority);
		mainPanel.add(lblIteration);
		mainPanel.add(lblEstimate);
		mainPanel.add(lblActual);
		mainPanel.add(lblRelease);
		mainPanel.add(lblTotalEstimate);
		mainPanel.add(lblTotEstDisplay);
	}

	/**
	 *
	 */
	private void loadFields() {
		if (mode == Mode.CREATE) {
			setSaveButtonAction();
			comboBoxStatus.setEnabled(false);
			comboBoxStatus.setSelectedItem("NEW");
			textEstimate.setEnabled(false);
			textEstimate.setBackground(defaultColor);
			getComboBoxIteration().setEnabled(false);
			getComboBoxIteration().setBackground(defaultColor);
			comboBoxStatus.setSelectedItem(Status.NEW.toString());

		} else {
			textName.setText(getRequirement().getName());
			textDescription.setText(getRequirement().getDescription());
			textEstimate.setText(Integer.toString(getRequirement()
					.getEstimate()));
			textActual.setText(Integer.toString(getRequirement().getEffort()));
			textRelease.setText(getRequirement().getReleaseNum());
			lblTotEstDisplay.setText(getTotalEstimate().toString());

			try {
				getComboBoxIteration()
						.setSelectedItem(
								IterationDatabase.getInstance()
										.get(getRequirement().getIteration())
										.getName());
			} catch (IterationNotFoundException e) {
				System.out.println("Exception Caught: Iteration Not Found.");
			}

			comboBoxType.setSelectedItem(getRequirement().getType().toString());

			comboBoxPriority.setSelectedItem(getRequirement().getPriority()
					.toString());

			setSaveButtonAction();
			comboBoxStatus.setSelectedItem(requirement.getStatus().toString());

			this.disableAllFieldsIfDeleted();
			comboBoxStatus.removeItem("None");
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
	 * @param scrollDescription
	 * @param btnCancel
	 */
	private void addComponentConstraints() {
		// Align left edges of objects
		layout.putConstraint(SpringLayout.WEST, lblName, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblDescription,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblType, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblStatus, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, textName, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, textNameValid,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, scrollDescription,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, textDescriptionValid,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, comboBoxType,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, comboBoxStatus,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblPriority,
				HORIZONTAL_PADDING, SpringLayout.EAST, comboBoxType);
		layout.putConstraint(SpringLayout.WEST, lblIteration,
				HORIZONTAL_PADDING, SpringLayout.EAST, comboBoxStatus);
		layout.putConstraint(SpringLayout.WEST, comboBoxPriority,
				HORIZONTAL_PADDING, SpringLayout.EAST, comboBoxType);
		layout.putConstraint(SpringLayout.WEST, getComboBoxIteration(),
				HORIZONTAL_PADDING, SpringLayout.EAST, comboBoxStatus);
		layout.putConstraint(SpringLayout.WEST, lblEstimate,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblActual, HORIZONTAL_PADDING,
				SpringLayout.EAST, comboBoxStatus);
		layout.putConstraint(SpringLayout.WEST, textEstimate,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, textActual, HORIZONTAL_PADDING,
				SpringLayout.EAST, comboBoxStatus);
		layout.putConstraint(SpringLayout.WEST, lblRelease, 0,
				SpringLayout.WEST, lblActual);
		layout.putConstraint(SpringLayout.WEST, textRelease, 0,
				SpringLayout.WEST, lblActual);
		layout.putConstraint(SpringLayout.WEST, lblTotalEstimate,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lblTotEstDisplay,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);

		// Align North Edges of Objects
		layout.putConstraint(SpringLayout.NORTH, lblName, VERTICAL_PADDING,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, textName, VERTICAL_PADDING
				+ CLOSE, SpringLayout.SOUTH, lblName);
		layout.putConstraint(SpringLayout.NORTH, textNameValid,
				VERTICAL_PADDING + CLOSE, SpringLayout.SOUTH, textName);
		layout.putConstraint(SpringLayout.NORTH, lblDescription,
				VERTICAL_PADDING, SpringLayout.SOUTH, textNameValid);
		layout.putConstraint(SpringLayout.NORTH, scrollDescription,
				VERTICAL_PADDING + CLOSE, SpringLayout.SOUTH, lblDescription);
		layout.putConstraint(SpringLayout.NORTH, textDescriptionValid,
				VERTICAL_PADDING + CLOSE, SpringLayout.SOUTH, scrollDescription);
		layout.putConstraint(SpringLayout.NORTH, lblType, VERTICAL_PADDING,
				SpringLayout.SOUTH, textDescriptionValid);
		layout.putConstraint(SpringLayout.NORTH, comboBoxType, VERTICAL_PADDING
				+ CLOSE, SpringLayout.SOUTH, lblType);
		layout.putConstraint(SpringLayout.NORTH, lblStatus, VERTICAL_PADDING,
				SpringLayout.SOUTH, comboBoxType);
		layout.putConstraint(SpringLayout.NORTH, comboBoxStatus,
				VERTICAL_PADDING + CLOSE, SpringLayout.SOUTH, lblStatus);
		layout.putConstraint(SpringLayout.NORTH, lblPriority, VERTICAL_PADDING,
				SpringLayout.SOUTH, textDescriptionValid);
		layout.putConstraint(SpringLayout.NORTH, comboBoxPriority,
				VERTICAL_PADDING + CLOSE, SpringLayout.SOUTH, lblPriority);
		layout.putConstraint(SpringLayout.NORTH, lblIteration,
				VERTICAL_PADDING, SpringLayout.SOUTH, comboBoxPriority);
		layout.putConstraint(SpringLayout.NORTH, getComboBoxIteration(),
				VERTICAL_PADDING + CLOSE, SpringLayout.SOUTH, lblIteration);
		layout.putConstraint(SpringLayout.NORTH, lblEstimate, VERTICAL_PADDING,
				SpringLayout.SOUTH, comboBoxStatus);
		layout.putConstraint(SpringLayout.NORTH, lblActual, VERTICAL_PADDING,
				SpringLayout.SOUTH, getComboBoxIteration());
		layout.putConstraint(SpringLayout.NORTH, textEstimate, VERTICAL_PADDING
				+ CLOSE, SpringLayout.SOUTH, lblEstimate);
		layout.putConstraint(SpringLayout.NORTH, textActual, VERTICAL_PADDING
				+ CLOSE, SpringLayout.SOUTH, lblActual);
		layout.putConstraint(SpringLayout.NORTH, lblRelease, VERTICAL_PADDING,
				SpringLayout.SOUTH, textEstimate);
		layout.putConstraint(SpringLayout.NORTH, textRelease, VERTICAL_PADDING
				+ CLOSE, SpringLayout.SOUTH, lblRelease);
		layout.putConstraint(SpringLayout.NORTH, lblTotalEstimate,
				VERTICAL_PADDING, SpringLayout.SOUTH, textEstimate);
		layout.putConstraint(SpringLayout.NORTH, lblTotEstDisplay,
				VERTICAL_PADDING + CLOSE, SpringLayout.SOUTH, lblTotalEstimate);

		// Align Buttons
		buttonPanelLayout.putConstraint(SpringLayout.NORTH, btnSave, 5,
				SpringLayout.NORTH, buttonPanel);
		buttonPanelLayout.putConstraint(SpringLayout.WEST, btnSave, 5,
				SpringLayout.WEST, buttonPanel);
		buttonPanelLayout.putConstraint(SpringLayout.NORTH, btnCancel, 5,
				SpringLayout.NORTH, buttonPanel);
		buttonPanelLayout.putConstraint(SpringLayout.WEST, btnCancel, 10,
				SpringLayout.EAST, btnSave);
		buttonPanelLayout.putConstraint(SpringLayout.WEST, textSaveError, 10,
				SpringLayout.EAST, btnCancel);
		buttonPanelLayout.putConstraint(SpringLayout.NORTH, textSaveError, 5,
				SpringLayout.SOUTH, buttonPanel);
	}

	/**
	 * Method to determine to which statuses the currently viewed requirement
	 * can manually be set based on its current status as governed by the
	 * stakeholders' specs; also sets the combo box to the appropriate set of
	 * statuses
	 */
	private void determineAvailableStatusOptions() {
		// String[] availableStatuses = { "New", "In Progress",
		// "Open","Complete", "Deleted"};

		boolean hasComplete = true;
		for (Task aTask : requirement.getTasks()) {
			if (!aTask.isCompleted())
				hasComplete = false;
		}
		if (!hasComplete)
			this.comboBoxStatus.removeItem(Status.COMPLETE.toString());

		if (getRequirement().getStatus() == Status.IN_PROGRESS) {
			// In Progress: In Progress, Complete, Deleted
			this.comboBoxStatus.removeItem(Status.NEW.toString());
			if (!getRequirement().subReqsCompleted()) {
				this.comboBoxStatus.removeItem(Status.COMPLETE.toString());
			}

			if (getRequirement().getSubRequirements().size() > 0
					|| !getRequirement().tasksCompleted()) {
				this.comboBoxStatus.removeItem(Status.DELETED.toString());
			}
		} else if (getRequirement().getSubRequirements().size() > 0
				|| !getRequirement().tasksCompleted()) {
			this.comboBoxStatus.removeItem(Status.DELETED.toString());
		}
		if (getRequirement().getStatus() == Status.NEW) {
			// New: New, Deleted
			this.comboBoxStatus.removeItem(Status.IN_PROGRESS.toString());
			this.comboBoxStatus.removeItem(Status.OPEN.toString());
			if (hasComplete)
				this.comboBoxStatus.removeItem(Status.COMPLETE.toString());
		}
		if (getRequirement().getStatus() == Status.OPEN) {
			// Open: Open, Deleted
			this.comboBoxStatus.removeItem(Status.NEW.toString());
			this.comboBoxStatus.removeItem(Status.IN_PROGRESS.toString());
			if (hasComplete)
				this.comboBoxStatus.removeItem(Status.COMPLETE.toString());
		}
		if (getRequirement().getStatus() == Status.COMPLETE) {
			// Complete: Open, Complete, Deleted
			this.comboBoxStatus.removeItem(Status.NEW.toString());
			this.comboBoxStatus.removeItem(Status.IN_PROGRESS.toString());
		}
		if (getRequirement().getStatus() == Status.DELETED) {
			// Deleted: Open, Deleted, Complete
			this.comboBoxStatus.removeItem(Status.NEW.toString());
			this.comboBoxStatus.removeItem(Status.IN_PROGRESS.toString());
			if (hasComplete)
				this.comboBoxStatus.removeItem(Status.COMPLETE.toString());
		}

	}


	public List<Note> getNoteList() {
		return noteView.getNotesList();
	}
	
	public void setNoteList(List<Note> notes) {
		noteView.setNotesList(notes);
	}

	public MainTabController getMainTabController() {
		return mainTabController;
	}

	public void displaySaveError(String error) {
		this.textSaveError.setText(error);
	}

	/**
	 * @return the textName
	 */
	public JTextArea getTextName() {
		return textName;
	}

	/**
	 * @param textName
	 *            the textName to set
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
	 * @param textNameValid
	 *            the textNameValid to set
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
	 * @param textDescription
	 *            the textDescription to set
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
	 * @param textDescriptionValid
	 *            the textDescriptionValid to set
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
	 * @param comboBoxPriority
	 *            the comboBoxPriority to set
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
	 * @param comboBoxType
	 *            the comboBoxType to set
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
	 * @param comboBoxStatus
	 *            the comboBoxStatus to set
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
	 * @param textEstimate
	 *            the textEstimate to set
	 */
	public void setTextEstimate(JTextField textEstimate) {
		this.textEstimate = textEstimate;
	}

	public Requirement getModel() {
		return getRequirement();
	}

	public JComboBox getTextIteration() {
		return this.getComboBoxIteration();
	}

	/**
	 * @return the textRelease
	 */
	public JTextField getTextRelease() {
		return textRelease;
	}

	/**
	 * @param textRelease
	 *            the textRelease to set
	 */
	public void setTextRelease(JTextField textRelease) {
		this.textRelease = textRelease;
	}

	/**
	 * @return the textActual
	 */
	public JTextField getTextActual() {
		return textActual;
	}

	/**
	 * @param textActual
	 *            the textActual to set
	 */
	public void setTextActual(JTextField textActual) {
		this.textActual = textActual;
	}

	public void disableSaveButton() {
		this.btnSave.setEnabled(false);
	}

	public void enableSaveButton() {
		if (mode == Mode.VIEW) {
			return; // we can not enable save in view mode
		}
		this.btnSave.setEnabled(true);
	}

	public List<String> getAssignedUsers() {
		return this.userView.getAssignedUsersList();
	}

	/**
	 * Checks requirement status: If deleted, disables all editable fields
	 * except status Else do nothing
	 */
	public void disableAllFieldsIfDeleted() {
		JPanel panel = new JPanel();
		Color defaultColor = panel.getBackground();

		if (getRequirement().getStatus() != Status.DELETED
				&& getRequirement().getStatus() != Status.COMPLETE)
			return;
		textName.setEnabled(false);
		textName.setBackground(defaultColor);
		textDescription.setEnabled(false);
		textDescription.setBackground(defaultColor);
		getComboBoxIteration().setEnabled(false);
		getComboBoxIteration().setBackground(defaultColor);
		textRelease.setEnabled(false);
		textRelease.setBackground(defaultColor);
		textEstimate.setEnabled(false);
		textEstimate.setBackground(defaultColor);
		if(getRequirement().getStatus() != Status.COMPLETE){
			textActual.setEnabled(false);
			textActual.setBackground(defaultColor);
		}
		comboBoxType.setEnabled(false);
		comboBoxPriority.setEnabled(false);

	}

	/**
	 * @return the requirement
	 */
	public Requirement getRequirement() {
		return requirement;
	}

	@Override
	public void responseSuccess() {

		for (int i = 0; i < mainTabController.getTabView().getTabCount(); i++) {
			if (mainTabController.getTabView().getComponentAt(i) instanceof DetailPanel) {
				(((DetailPanel) mainTabController.getTabView()
						.getComponentAt(i))).updateTotalEstimate();
				(((DetailPanel) mainTabController.getTabView()
						.getComponentAt(i))).updateSubReqTab();
			}
		}

		// if the tab should close, close it
		if (closeTab) {
			getMainTabController().closeCurrentTab();
			return;
		}
		// other wise we shall update the requriment in the log view

		Requirement updatedRequirement;
		try {
			updatedRequirement = RequirementDatabase.getInstance().get(
					this.requirement.getrUID());
			logView.refresh(updatedRequirement);
		} catch (RequirementNotFoundException e) {
			System.out.println("Unable to find requirement? Wat?");
		}

	}

	@Override
	public void responseError(int statusCode, String statusMessage) {
		displaySaveError("Received " + statusCode + " error from server: "
				+ statusMessage);

	}

	@Override
	public void fail(Exception exception) {
		displaySaveError("Unable to complete request: "
				+ exception.getMessage());
	}

	/**
	 * Method to set this tab to close when the tab closes
	 * 
	 */

	public void closeTabAfterSave() {
		closeTab = true;
	}

	/**
	 * @return the comboBoxIteration
	 */
	public JComboBox getComboBoxIteration() {
		return comboBoxIteration;
	}

	public DetailTaskView getTaskView() {
		return taskView;
	}

	public DetailNoteView getNoteView() {
		return noteView;
	}

	public AssigneePanel getUserView() {
		return userView;
	}

	@Override
	public boolean onTabClosed() {
		if (btnSave.isEnabled()) {

			mainTabController.switchToTab(this);

			Object[] options = { "Save Changes", "Discard Changes", "Cancel" };

			int res = JOptionPane
					.showOptionDialog(
							this,
							"There are unsaved changes, are you sure you want to continue?",
							requirement.getName() + ": Confirm Close",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[2]);
			System.out.println("Detail Panel RES: " + res);
			if (res == 0) {
				closeTabAfterSave();
				btnSave.getAction().actionPerformed(null);
			} else if (res == 1) {
				return true;
			} else {
				return false;
			}

		}

		if (taskView.getTaskPanel().getAddTask().isEnabled()
				|| noteView.getNotePanel().getAddnote().isEnabled()
				|| aTestView.getTestPanel().getAddATest().isEnabled()) {
			mainTabController.switchToTab(this);

			Object[] altOptions = { "Discard Changes", "Cancel" };

			int res = JOptionPane
					.showOptionDialog(
							this,
							"There are unsaved changes in subtabs, are you sure you want to continue?",
							requirement.getName() + ": Confirm Close",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, altOptions,
							altOptions[1]);

			if (res == 0) {
				return true;
			} else if (res == 1) {
				return false;
			}

		}
		return true;
	}

	public void updateSubReqTab() {
		subRequirementView.refreshAll();
	}

	public void updateTotalEstimate() {
		Integer estimate = 0;

		Requirement tempReq = null;
		try {
			tempReq = RequirementDatabase.getInstance().get(
					requirement.getrUID());
			estimate = traverseTreeEstimates(tempReq, tempReq.getEstimate());
			lblTotEstDisplay.setText(estimate.toString());
		} catch (RequirementNotFoundException e) {
			e.printStackTrace();
		}

	}

	private Integer getTotalEstimate() {
		return traverseTreeEstimates(requirement, requirement.getEstimate());
	}

	private int traverseTreeEstimates(Requirement current, int totals) {
		Requirement child = null;
		int sum = 0;

		for (Integer i : current.getSubRequirements()) {
			try {
				child = RequirementDatabase.getInstance().get(i);
				sum = sum + child.getEstimate()
						+ traverseTreeEstimates(child, 0);
			} catch (RequirementNotFoundException e) {
				e.printStackTrace();
			}
		}
		return totals + sum;
	}

	private void setSaveButtonAction() {
		if (mode == Mode.CREATE) {
			btnSave.setAction(new SaveRequirementAction(requirement, this));
		} else if (mode == Mode.EDIT) {
			btnSave.setAction(new EditRequirementAction(requirement, this));
		}
	}
}