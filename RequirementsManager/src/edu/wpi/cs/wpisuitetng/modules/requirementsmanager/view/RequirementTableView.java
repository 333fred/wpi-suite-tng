/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Steve Kordell, Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.JPlaceholderTextField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.PermissionModelController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IDatabaseListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllIterationsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllRequirementsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrievePermissionsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveRequirementByIDRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IReceivedAllRequirementNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IRetreivedAllIterationsNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.Tab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.UnclosableTabComponent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar.PermissionToolbarPane;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar.ToolbarView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.EnableEditingAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.OpenRequirementTabAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.RefreshAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.SaveEditingTableAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter.FilterUpdateListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter.FilterView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.TextSearchListener;

/**
 * RequirementListView is the basic GUI that will display a list of the current
 * Requirements, fetched from the database. It will allow the user to select a
 * given requirement and view/edit it. It also allows users to create new
 * requirements, and refresh the list of requirements
 * 
 * @author Steve, Mitchell
 * 
 */
@SuppressWarnings("serial")
public class RequirementTableView extends Tab implements IToolbarGroupProvider,
		IDatabaseListener, IReceivedAllRequirementNotifier,
		IRetreivedAllIterationsNotifier, FilterUpdateListener {

	private static RequirementTableView tv;

	public static RequirementTableView getInstance() {
		return RequirementTableView.tv;
	}

	public static RequirementTableView getInstance(
			final MainTabController tabController) {
		if (RequirementTableView.tv == null) {
			RequirementTableView.tv = new RequirementTableView(tabController);
		}
		return RequirementTableView.tv;
	}

	/** The MainTabController that this view is inside of */
	private final MainTabController tabController;

	private RequirementsController requirementsController;

	private IterationController iterationController;

	/** The list of requirements that the view is displaying */
	private Requirement[] requirements;
	/**
	 * The View that will display on the toolbar and contain buttons relating to
	 * this view
	 */
	private ToolbarGroupView toolbarGroupView;

	/** The View and Refresh buttons used on the toolbar */
	private JButton butView;

	private JButton butRefresh;

	/** Flag used to make paint only refresh the requirements once, on load */
	private boolean firstPaint;

	@SuppressWarnings("rawtypes")
	private Vector<Vector> rowData;
	private RequirementsTable table;
	private JButton btnEdit;

	private JButton btnSave;
	private JButton btnClearTreeFilter;
	private JTextArea textEditInfo;

	private JTextArea textTreeFilterInfo;

	private JTextArea textFilterInfo;

	private JPlaceholderTextField textSearchBox;

	private TextSearchListener textSearchListener;

	private boolean isEditable;

	private TableRowSorter<TableModel> sorter;
	

	/** Row filter for filtering by iteration */
	private RowFilter<Object, Object> searchFilter;
	/** Row filter for search bar*/
	private RowFilter<Object, Object> treeFilter;

	/**
	 * Constructor for a RequirementTableView
	 * 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private RequirementTableView(final MainTabController tabController) {
		this.tabController = tabController;
		FilterView.getInstance().addFilterUpdateListener(this);
		firstPaint = false;
		// register this listener to the Database

		iterationController = new IterationController();
		requirementsController = new RequirementsController();
		// init the toolbar group
		initializeToolbarGroup();

		requirements = new Requirement[0];

		setLayout(new BorderLayout(0, 0));

		final Vector<String> columnNames = new Vector<String>();
		columnNames.addElement("ID");
		columnNames.addElement("Name");
		columnNames.addElement("Type");
		columnNames.addElement("Priority");
		columnNames.addElement("Status");
		columnNames.addElement("Iteration");
		columnNames.addElement("Estimate");
		columnNames.addElement("Effort");
		columnNames.addElement("Release Number");

		rowData = new Vector<Vector>();

		// editing is disabled until the button is pressed
		isEditable = false;

		table = new RequirementsTable(rowData, columnNames, this);

		btnEdit = new JButton("Enable Editing");
		btnSave = new JButton("Save Changes");
		btnClearTreeFilter = new JButton("Clear Filter");

		// save button is diabled until editing is enabled
		btnSave.setEnabled(false);

		// set button actions
		btnSave.setAction(new SaveEditingTableAction(this, sorter));
		btnEdit.setAction(new EnableEditingAction(this, sorter));

		final AbstractAction ClearFilterAction = new AbstractAction() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				treeFilter = null;
				ApplyFilters(sorter);	
				textTreeFilterInfo.setText("");
				btnClearTreeFilter.setEnabled(false);
				if (getTable().getRowCount() == 0) {
					btnSave.setEnabled(false);
					btnEdit.setEnabled(false);
				}
				else {
					btnEdit.setEnabled(true);
				}
			}
		};

		// set clear tree filter's button action
		btnClearTreeFilter.setAction(ClearFilterAction);
		btnClearTreeFilter.setText("Restore Default View");
		// button is disabled until a tree filter is applied
		btnClearTreeFilter.setEnabled(false);

		textEditInfo = new JTextArea(1, 25);
		textEditInfo.setOpaque(false);
		textEditInfo.setEnabled(false);
		textEditInfo.setDisabledTextColor(Color.BLACK);
		textEditInfo.setLineWrap(true);
		textEditInfo.setWrapStyleWord(true);

		textSearchBox = new JPlaceholderTextField("Search Requirement by Name",
				18);
		textSearchBox.setOpaque(true);
		textSearchBox.setEnabled(true);
		textSearchBox.setBorder((new JTextField()).getBorder());
		textSearchBox.setDisabledTextColor(Color.BLACK);
		textSearchListener = new TextSearchListener(this, textSearchBox);
		textSearchBox.addKeyListener(textSearchListener);

		textTreeFilterInfo = new JTextArea(1, 20);
		textTreeFilterInfo.setOpaque(false);
		textTreeFilterInfo.setEnabled(false);
		textTreeFilterInfo.setDisabledTextColor(Color.BLACK);
		textTreeFilterInfo.setLineWrap(true);
		textTreeFilterInfo.setWrapStyleWord(true);

		textFilterInfo = new JTextArea(1, 15);
		textFilterInfo.setOpaque(false);
		textFilterInfo.setEnabled(false);
		textFilterInfo.setDisabledTextColor(Color.BLACK);
		textFilterInfo.setLineWrap(true);
		textFilterInfo.setWrapStyleWord(true);

		final SpringLayout editPanelLayout = new SpringLayout();
		final JPanel editPanel = new JPanel(editPanelLayout);
		editPanel.add(btnEdit);
		editPanel.add(btnSave);
		editPanel.add(textFilterInfo);
		editPanel.add(textEditInfo);
		editPanel.add(btnClearTreeFilter);
		editPanel.add(textTreeFilterInfo);
		editPanel.add(textSearchBox);
		editPanel
				.setPreferredSize(new Dimension(
						btnEdit.getPreferredSize().width,
						btnEdit.getPreferredSize().height
								+ (btnEdit.getPreferredSize().height / 2)
								+ textEditInfo
										.getPreferredScrollableViewportSize().height
								+ 10));

		editPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnEdit, 0,
				SpringLayout.VERTICAL_CENTER, editPanel);
		editPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnSave, 0,
				SpringLayout.VERTICAL_CENTER, editPanel);

		editPanelLayout.putConstraint(SpringLayout.WEST, btnEdit, 5,
				SpringLayout.HORIZONTAL_CENTER, editPanel);
		editPanelLayout.putConstraint(SpringLayout.EAST, btnSave, -5,
				SpringLayout.HORIZONTAL_CENTER, editPanel);

		editPanelLayout.putConstraint(SpringLayout.WEST, textEditInfo, 0,
				SpringLayout.WEST, btnSave);
		editPanelLayout.putConstraint(SpringLayout.NORTH, textEditInfo, 0,
				SpringLayout.SOUTH, btnSave);

		editPanelLayout.putConstraint(SpringLayout.WEST, btnClearTreeFilter, 5,
				SpringLayout.WEST, editPanel);
		editPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER,
				btnClearTreeFilter, 0, SpringLayout.VERTICAL_CENTER, editPanel);
		editPanelLayout.putConstraint(SpringLayout.WEST, textTreeFilterInfo, 5,
				SpringLayout.WEST, editPanel);
		editPanelLayout.putConstraint(SpringLayout.NORTH, textTreeFilterInfo,
				0, SpringLayout.SOUTH, btnClearTreeFilter);
		editPanelLayout.putConstraint(SpringLayout.EAST, textSearchBox, -5,
				SpringLayout.EAST, editPanel);
		editPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER,
				textSearchBox, 0, SpringLayout.VERTICAL_CENTER, editPanel);
		editPanelLayout.putConstraint(SpringLayout.EAST, textFilterInfo, 0,
				SpringLayout.EAST, editPanel);
		editPanelLayout.putConstraint(SpringLayout.NORTH, textFilterInfo, 0,
				SpringLayout.SOUTH, textSearchBox);
		editPanelLayout.putConstraint(SpringLayout.WEST, textFilterInfo, 0,
				SpringLayout.WEST, textSearchBox);

		final JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.getColumnModel()
				.removeColumn(table.getColumnModel().getColumn(0));

		// Create comparators for table sorting
		final Comparator<String> PriorityComparator = new Comparator<String>() {

			@Override
			public int compare(String s1, String s2) {
				if (s1.trim().equals("")) {
					s1 = "BLANK";
				}
				if (s2.trim().equals("")) {
					s2 = "BLANK";
				}
				final String upper1 = s1.toUpperCase();
				final String upper2 = s2.toUpperCase();
				final Priority p1 = Priority.valueOf(upper1);
				final Priority p2 = Priority.valueOf(upper2);
				return p1.compareTo(p2);
			}
		};

		final Comparator<String> IterationStringComparator = new Comparator<String>() {

			@Override
			public int compare(final String s1, final String s2) {
				final IterationDatabase Idb = IterationDatabase.getInstance();
				final Iteration Iteration1 = Idb.getIteration(s1);
				final Iteration Iteration2 = Idb.getIteration(s2);

				if (Iteration1.getStartDate().before(Iteration2.getStartDate())) {
					return -1; // first argument is less, or before second
				} else if (Iteration1.getStartDate().after(
						Iteration2.getStartDate())) {
					return 1; // first iteration is more, or after second
				}
				return 0; // dates are equal
			}
		};

		final Comparator<String> numberComparator = new Comparator<String>() {

			@Override
			public int compare(final String s1, final String s2) {
				final int Estimate1 = Integer.parseInt(s1);
				final int Estimate2 = Integer.parseInt(s2);

				if (Estimate1 < Estimate2) {
					return -1;
				} else if (Estimate1 > Estimate2) {
					return 1;
				} else {
					return 0;
				}
			}
		};

		sorter = new TableRowSorter<TableModel>(table.getModel());

		// column 3 sorts by priority
		sorter.setComparator(3, PriorityComparator);
		// column 5 sorts by iteration date
		sorter.setComparator(5, IterationStringComparator);
		// column 6 and 7 sorts as numbers rather than strings
		sorter.setComparator(6, numberComparator);
		sorter.setComparator(7, numberComparator);

		table.setRowSorter(sorter);

		// Add to this list of the column does not need equal size
		final String shortCols = "Estimate|Effort";
		for (int i = 0; i < table.getColumnCount(); i++) {
			if (table.getColumnName(i).matches(shortCols)) {
				// TODO: set width differently?
				table.getColumnModel().getColumn(i).setPreferredWidth(10);
			}
		}

		add(scrollPane, BorderLayout.CENTER);
		add(editPanel, BorderLayout.SOUTH);

		// Add double click event listener
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				if ((e.getClickCount() == 2) && !isEditable) {
					final JTable target = (JTable) e.getSource();
					final int row = target.getSelectedRow();
					onDoubleClick(row);
				}
			}
		});

		table.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent event) {
				if ((event.getKeyCode() == KeyEvent.VK_ENTER) && !isEditable) {
					viewRequirement();
				}
				event.consume();
			}
		});

		FilterDatabase.getInstance().registerListener(this);
		IterationDatabase.getInstance().registerListener(this);
		RequirementDatabase.getInstance().registerListener(this);

	}

	// sets the buttons enabled/disabled depending on the isEditable state
	public void changeButtonStatus() {
		if (isEditable) {
			btnEdit.setText("Discard Changes");
			btnSave.setEnabled(true);
		} else {
			btnEdit.setText("Enable Editing");if(rowData.size() == 0) {
				btnEdit.setEnabled(false);
			}
			btnSave.setEnabled(false);
		}
	}

	// writes to hidden panel to inform the user of editing, etc..
	public void displayEditInformation(final String text) {
		textEditInfo.setText(text);
	}

	// writes to hidden panel to inform the user of editing, etc..
	public void displayFilterInformation(final String text) {
		textTreeFilterInfo.setText(text);
	}

	@Override
	public void errorReceivingData(
			final String RetrieveAllRequirementsRequestObserver) {

	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public void filtersUpdated() {
		refresh();
	}

	/**
	 * Inherited from IToolBarGroup Provider, Provides the toolbar buttons used
	 * by this view
	 * 
	 */

	@Override
	public ToolbarGroupView getGroup() {
		return toolbarGroupView;
	}

	private void getIterationsFromServer() {
		final RetrieveAllIterationsRequestObserver observer = new RetrieveAllIterationsRequestObserver(
				this);
		iterationController.getAll(observer);
	}

	/**
	 * Updates the permission of the current user
	 */
	private void getPermissionFromServer() {
		final PermissionModelController controller = new PermissionModelController();
		final RetrievePermissionsRequestObserver observer = new RetrievePermissionsRequestObserver();

		controller.get(0, observer);
	}

	/**
	 * Function to retreive the requirements from the server
	 * 
	 * Mostly a place holder function until the backend is implemented The list
	 * view will automaticaly be updated once the request comes back
	 * 
	 */

	private void getRequirementsFromServer() {
		final RetrieveAllRequirementsRequestObserver observer = new RetrieveAllRequirementsRequestObserver(
				this);
		requirementsController.getAll(observer);
	}

	@Override
	public Component getTabComponent(final MainTabController tabController) {
		return new UnclosableTabComponent(tabController, "Requirements");
	}

	/**
	 * @return the table
	 */
	public RequirementsTable getTable() {
		return table;
	}

	/**
	 * Initializes the toolbar group, and adds the buttons that will be
	 * displayed to it.
	 * 
	 * 
	 */
	private void initializeToolbarGroup() {

		final JPanel content = new JPanel();
		final SpringLayout layout = new SpringLayout();
		content.setLayout(layout);
		content.setOpaque(false);

		butView = new JButton("Edit Requirement");
		butRefresh = new JButton("Refresh");

		butRefresh.setAction(new RefreshAction(this));

		layout.putConstraint(SpringLayout.NORTH, butRefresh, 5,
				SpringLayout.NORTH, content);
		layout.putConstraint(SpringLayout.WEST, butRefresh, 16,
				SpringLayout.WEST, content);
		layout.putConstraint(SpringLayout.EAST, butRefresh, -16,
				SpringLayout.EAST, content);

		layout.putConstraint(SpringLayout.NORTH, butView, 5,
				SpringLayout.SOUTH, butRefresh);
		layout.putConstraint(SpringLayout.WEST, butView, 16, SpringLayout.WEST,
				content);
		layout.putConstraint(SpringLayout.EAST, butView, 0, SpringLayout.EAST,
				butRefresh);

		// create and add the buttons that will be displayed
		content.add(butRefresh);

		toolbarGroupView = new ToolbarGroupView("Refresh", content);
		// set the width of the group so it is not too long
		toolbarGroupView.setPreferredWidth((int) (butView.getPreferredSize()
				.getWidth() + 40));

	}

	/**
	 * @return the isEditable
	 */
	public boolean isEditable() {
		return isEditable;
	}

	public void IterationFilter(final String IterationName) {
		// If current expression doesn't parse, don't update.
		try {
			treeFilter = RowFilter.regexFilter("^" + IterationName + "$", 5);
		} catch (final java.util.regex.PatternSyntaxException e) {
			return;
		}
		ApplyFilters(sorter);
		btnClearTreeFilter.setEnabled(true);
	}

	/**
	 * @return the textSearchBox
	 */
	public JPlaceholderTextField getTextSearchBox() {
		return textSearchBox;
	}

	/**
	 * @return the textFilterInfo
	 */
	public JTextArea getTextFilterInfo() {
		return textFilterInfo;
	}

	/**
	 * @return the sorter
	 */
	public TableRowSorter<TableModel> getSorter() {
		return sorter;
	}
	
	/**
	 * @param sorter the sorter to set
	 */
	public void setSorter(TableRowSorter<TableModel> sorter) {
		this.sorter = sorter;
	}

	public void nameFilter(String filterText) {
		// If current expression doesn't parse, don't update.
		try {
			searchFilter = RowFilter.regexFilter("(?i)" + filterText, 1);
		} catch (final java.util.regex.PatternSyntaxException e) {
			return;
		}
		ApplyFilters(sorter);
	}
	
	/**
	 * Removes the search filter from the table view
	 */
	public void clearSearchFilter() {
		searchFilter = null;
		ApplyFilters(sorter);
	}

	/**
	 * @author Steve Kordell
	 */
	void onDoubleClick(final int index) {
		// update to use this function instead
		viewRequirement(index);
	}

	/**
	 * Method that updates the content of the list view when this tab gains
	 * focus
	 * 
	 */

	@Override
	public void onGainedFocus() {
		refresh();
	}

	@Override
	public boolean onLostFocus() {
		if (isEditable) {
			final Object[] options = { "Save Changes", "Discard Changes",
					"Cancel" };
			final int res = JOptionPane
					.showOptionDialog(
							this,
							"There are unsaved changes, either discard them, or save them before continuing",
							"Requirements: Confirm Changes",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[2]);

			if (res == 0) {
				btnSave.getAction().actionPerformed(null);
			} else if (res == 1) {
				btnEdit.getAction().actionPerformed(null);
			} else {
				return false;
			}

		}
		return true;

	}

	/**
	 * Retrieve the requirements from the server when the GUI is first painted
	 * 
	 * @param g
	 *            The Graphics object
	 * 
	 */

	@Override
	public void paint(final Graphics g) {
		if (getTable().getRowCount() == 0) {
			btnSave.setEnabled(false);
			btnEdit.setEnabled(false);
		}
		else {
			btnEdit.setEnabled(true);
		}
		// call super so there is no change to functionality
		super.paint(g);
		// refresh the requirements, the first time this is called
		if (!firstPaint) {
			refresh();
			firstPaint = true;
		}
	}

	/**
	 * Takes a list of requirements, and turns them into a list of Strings that
	 * the View will display
	 * 
	 * @param requirements
	 *            The list of requirements
	 * @return a list of requirements in string form, that can be shown on the
	 *         JList
	 */

	private void parseRequirements() {

		rowData.clear();
		for (final Requirement requirement : requirements) {
			final Vector<String> row = new Vector<String>();
			row.addElement(String.valueOf(requirement.getrUID()));
			row.addElement(requirement.getName());
			row.addElement(requirement.getType().equals(Type.BLANK) ? ""
					: requirement
							.getType()
							.toString()
							.substring(0, 1)
							.concat(requirement.getType().toString()
									.substring(1).toLowerCase())
							.replaceAll(" s", " S").replaceAll(" f", "-F"));
			row.addElement(requirement.getPriority().equals(Priority.BLANK) ? ""
					: requirement
							.getPriority()
							.toString()
							.substring(0, 1)
							.concat(requirement.getPriority().toString()
									.substring(1).toLowerCase()));
			row.addElement(requirement.getStatus().equals(Status.BLANK) ? ""
					: requirement
							.getStatus()
							.toString()
							.substring(0, 1)
							.concat(requirement.getStatus().toString()
									.substring(1).toLowerCase())
							.replaceAll(" p", " P"));
			try {
				row.addElement(IterationDatabase.getInstance()
						.get(requirement.getIteration()).getName());
			} catch (final IterationNotFoundException e) {
				row.addElement("Iteration Not Found");
			}
			row.addElement(String.valueOf(requirement.getEstimate()));
			row.addElement(String.valueOf(requirement.getEffort()));
			row.addElement((requirement.getReleaseNum()));
			rowData.add(row);
		}
	}

	@Override
	public void receivedData(final Iteration[] iterations) {
		updateListView();
	}

	@Override
	public void receivedData(final Requirement[] requirements) {

		sorter.setRowFilter(null);

		this.requirements = RequirementDatabase.getInstance()
				.getFilteredRequirements().toArray(new Requirement[0]);
		if (this.requirements.length == 0) {
			textFilterInfo.setText("No Requirements Found");
		} else {
			textFilterInfo.setText("");
		}
		updateListView();
		ApplyFilters(sorter);
		if (getTable().getRowCount() == 0) {
			textFilterInfo.setText("No Requirements Found");
			btnEdit.setEnabled(false);
		}
		else {
			btnEdit.setEnabled(true);
		}
	}

	/**
	 * Retreives the list of requirements from the server, then updates the List
	 * View acordingly
	 * 
	 */

	@Override
	public void refresh() {
		// retrieve a new copy of requirements, and update the list view
		if (isEditable) {
			final Object[] options = { "Save Changes", "Discard Changes",
					"Cancel" };
			final int res = JOptionPane.showOptionDialog(this,
					"There are unsaved changes, refreshing will discard them.",
					"Requirements: Confirm Refresh",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

			if (res == 0) {
				btnSave.getAction().actionPerformed(null);
			} else if (res == 1) {
				btnEdit.getAction().actionPerformed(null);
			} else {

			}
		} else {
			getRequirementsFromServer();
			getIterationsFromServer();
			getPermissionFromServer();
		}

		// check if we can edit based upon current user permissions
		if (!(PermissionModel.getInstance().getPermLevel() == UserPermissionLevel.ADMIN)) {
			btnEdit.setEnabled(false);
		}

		tabController.refreshIterationTree();
		PermissionToolbarPane.getInstance().refreshPermission();
		ToolbarView.getInstance().refreshPermissions();
		tabController.refreshFilterView();
		tabController.refreshSubReqView();
	}

	/**
	 * @param editable
	 *            the isEditable to set
	 */
	public void setEditable(final boolean editable) {
		isEditable = editable;
		tabController.setSidePaneEnabled(!editable);
	}

	@Override
	public boolean shouldRemove() {
		// this listener should persist
		return false;
	}

	@Override
	public void update() {
		requirements = RequirementDatabase.getInstance()
				.getFilteredRequirements().toArray(new Requirement[0]);
		updateListView();

		// check if we can edit based upon current user permissions
		if (!(PermissionModel.getInstance().getPermLevel() == UserPermissionLevel.ADMIN)) {
			btnEdit.setEnabled(false);
		}
	}

	/**
	 * Updates the list view according to the values in the Requirements Array
	 * 
	 * @throws RequirementNotFoundException
	 * 
	 * */

	private void updateListView() {

		parseRequirements();

		table.repaint();
	}

	/**
	 * Open a new tab containing a view of the selected requirement in the list
	 * view
	 */

	public void viewRequirement() {
		for (final int i : table.getSelectedRows()) {
			viewRequirement(i);
			// TODO: Slight problem: when opening multiple requirements at the
			// same time,because of the server respond speed, the iterations
			// won't open the tabs necessarily in the order they appear in the
			// table
		}
	}

	/**
	 * Views a requirement with the given index
	 * 
	 * @param index
	 *            Index of the requirement to view
	 */

	public void viewRequirement(final int index) {
		boolean requirementIsOpen = false;

		if ((index < 0) || (index >= requirements.length)) {
			// invalid index
			System.out.println("Invalid index " + index);
		}

		Requirement requirementToFetch = null;

		// convert index to new view index (incase of sorting)
		final int newIndex = table.convertRowIndexToModel(index);
		// get the rUID of the requirement in the hidden column
		final String reqId = (String) table.getModel().getValueAt(newIndex, 0);

		// iterate through the requirements
		for (final Requirement requirement : requirements) {
			// if the rUIDs match
			if (reqId.equals(Integer.toString(requirement.getrUID()))) {
				// get this requirement
				requirementToFetch = requirement;
				// stop loop
				break;
			}
		}

		// Check to make sure the requirement is not already being
		// displayed. This is assuming that the list view is displayed in
		// the left most tab, index 0
		for (int i = 0; i < tabController.getTabView().getTabCount(); i++) {
			if (tabController.getTabView().getComponentAt(i) instanceof DetailPanel) {
				if (((((DetailPanel) tabController.getTabView().getComponentAt(
						i))).getModel().getrUID()) == (requirementToFetch
						.getrUID())) {
					tabController.switchToTab(i);
					requirementIsOpen = true;
				}
			}
		}

		if (!requirementIsOpen) {
			// create the controller for fetching the new requirement
			final RequirementsController controller = new RequirementsController();
			final RetrieveRequirementByIDRequestObserver observer = new RetrieveRequirementByIDRequestObserver(
					new OpenRequirementTabAction(tabController,
							requirementToFetch));

			// get the requirement from the server
			controller.get(requirementToFetch.getrUID(), observer);
		}
	}

	/**
	 * @return the btnEdit
	 */
	public JButton getBtnEdit() {
		return btnEdit;
	}

	/**
	 * @return the btnSave
	 */
	public JButton getBtnSave() {
		return btnSave;
	}

	public MainTabController getController() {
		return tabController;
	}
	
	public void ApplyFilters (TableRowSorter rs){
		ArrayList<RowFilter<Object, Object>> combinedFilters = new ArrayList<RowFilter<Object, Object>>();

		if(searchFilter != null){
			combinedFilters.add(searchFilter);
		}
		if(treeFilter != null){
			combinedFilters.add(treeFilter);
		}
		sorter.setRowFilter(RowFilter.andFilter(combinedFilters));	
	}

}
