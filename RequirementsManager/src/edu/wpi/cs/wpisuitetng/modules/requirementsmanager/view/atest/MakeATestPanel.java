/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Nick M
 *    Matt C
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.atest;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.AbstractDocument;
import javax.swing.text.JTextComponent;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.DocumentSizeFilter;

/**
 * A panel containing a a creation and edit form for the aTests in a requirement
 * 
 * @author Nick M, Matt C, Steve Kordell
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class MakeATestPanel extends JPanel {

	// The fields to make a aTest
	private final JTextArea txtName;
	private final JTextArea txtDescription;
	private final JButton butAdd;
	private final JButton butCancel;
	private final JLabel addaTestLabel;
	private final JComboBox cbxStatus;

	// Jlabels for aTest fields
	private final JLabel aTestStatus;
	private final JLabel nameaTestLabel;
	private final JLabel descaTestLabel;

	private final JLabel labSaveError;

	private static final int VERTICAL_PADDING = 5;
	private static final int note_FIELD_HEIGHT = 50;
	private final JScrollPane aTestFieldPane;

	/** The listner for txt fields for real time validation */
	private MakeATestListener makeATestListener;

	/** The detail a test view */
	private DetailATestView aTestView;

	/**
	 * Construct the panel, add and layout components.
	 * 
	 * @param model
	 *            the requirement to which a notes made with this class will be
	 *            saved
	 */
	@SuppressWarnings("unchecked")
	public MakeATestPanel(final Requirement model, DetailATestView aTestView) {
		this.aTestView = aTestView;

		// setup the aTest name field
		txtName = new JTextArea(1, 40);
		txtName.setLineWrap(true);
		txtName.setWrapStyleWord(true);
		txtName.setMaximumSize(new Dimension(40, 2));
		final AbstractDocument textNameDoc = (AbstractDocument) txtName
				.getDocument();
		textNameDoc.setDocumentFilter(new DocumentSizeFilter(100));
		txtName.setBorder((new JTextField()).getBorder());
		txtName.setName("Name");
		txtName.setDisabledTextColor(Color.GRAY);

		txtName.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						txtName.transferFocus();
					} else {
						txtName.transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					event.consume();
				}
			}
		});

		// setup the aTest description field
		txtDescription = new JTextArea();
		txtDescription.setLineWrap(true);
		txtDescription.setWrapStyleWord(true);
		txtDescription.setDisabledTextColor(Color.GRAY);

		txtDescription.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						txtDescription.transferFocus();
					} else {
						txtDescription.transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					txtDescription.append("\n");
					event.consume();
				}
			}
		});

		// setup all the buttons and label text
		butAdd = new JButton("Save");

		butCancel = new JButton("Cancel");

		butCancel.setAction(new AbstractAction("Cancel") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onCancel();
				txtName.setBackground(Color.white);
				txtDescription.setBackground(Color.white);
			}

		});

		labSaveError = new JLabel("");

		makeATestListener = new MakeATestListener(this);
		txtName.addKeyListener(makeATestListener);
		txtDescription.addKeyListener(makeATestListener);

		aTestStatus = new JLabel(
				"No Acceptance Test selected. Fill name and description to create a new one.");
		addaTestLabel = new JLabel("Acceptance Test:");
		nameaTestLabel = new JLabel("*Name:");
		descaTestLabel = new JLabel("*Description:");
		final String[] availableStatuses = { "", "PASSED", "FAILED" };
		cbxStatus = new JComboBox(availableStatuses);
		cbxStatus.setBackground(Color.WHITE);

		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.black, 1),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		final SpringLayout layout = new SpringLayout();
		setLayout(layout);

		aTestFieldPane = new JScrollPane(txtDescription);

		// Setup the layout of the aTest Panel
		layout.putConstraint(SpringLayout.NORTH, addaTestLabel, 0,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, addaTestLabel, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, aTestStatus, 0,
				SpringLayout.SOUTH, addaTestLabel);
		layout.putConstraint(SpringLayout.NORTH, nameaTestLabel,
				MakeATestPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				aTestStatus);
		layout.putConstraint(SpringLayout.NORTH, txtName,
				MakeATestPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				nameaTestLabel);
		layout.putConstraint(SpringLayout.WEST, txtName, 0, SpringLayout.WEST,
				nameaTestLabel);
		layout.putConstraint(SpringLayout.EAST, txtName, 0, SpringLayout.EAST,
				this);
		layout.putConstraint(SpringLayout.NORTH, descaTestLabel,
				MakeATestPanel.VERTICAL_PADDING, SpringLayout.SOUTH, txtName);
		layout.putConstraint(SpringLayout.NORTH, aTestFieldPane,
				MakeATestPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				descaTestLabel);
		layout.putConstraint(SpringLayout.WEST, aTestFieldPane, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, aTestFieldPane, 0,
				SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, aTestFieldPane,
				MakeATestPanel.note_FIELD_HEIGHT, SpringLayout.NORTH,
				aTestFieldPane);

		layout.putConstraint(SpringLayout.NORTH, cbxStatus,
				MakeATestPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				aTestFieldPane);
		layout.putConstraint(SpringLayout.WEST, cbxStatus, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, this,
				MakeATestPanel.VERTICAL_PADDING, SpringLayout.SOUTH, cbxStatus);

		layout.putConstraint(SpringLayout.NORTH, butCancel,
				MakeATestPanel.VERTICAL_PADDING, SpringLayout.SOUTH, cbxStatus);
		layout.putConstraint(SpringLayout.EAST, butCancel, 0,
				SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, this,
				MakeATestPanel.VERTICAL_PADDING, SpringLayout.SOUTH, butCancel);

		layout.putConstraint(SpringLayout.NORTH, butAdd, 0, SpringLayout.NORTH,
				butCancel);
		layout.putConstraint(SpringLayout.EAST, butAdd, -4, SpringLayout.WEST,
				butCancel);

		layout.putConstraint(SpringLayout.NORTH, labSaveError, 0,
				SpringLayout.NORTH, butCancel);
		layout.putConstraint(SpringLayout.WEST, labSaveError, 4,
				SpringLayout.WEST, this);

		// add all of the swing components to the this aTest panel
		this.add(aTestStatus);
		this.add(addaTestLabel);
		this.add(butCancel);
		this.add(butAdd);
		this.add(txtName);
		this.add(nameaTestLabel);
		this.add(descaTestLabel);
		this.add(cbxStatus);
		this.add(aTestFieldPane);
		this.add(labSaveError);

		// default the add button and complete checkbox
		// to un-enabled
		butAdd.setEnabled(false);
		cbxStatus.setEnabled(false);

	}

	/**
	 * Returns the add ATest button
	 * 
	 * @return the button
	 */
	public JButton getAddATest() {
		return butAdd;
	}

	/**
	 * A function to the get the text area
	 * 
	 * @return the note JTextArea
	 */
	public JTextArea getaTestField() {
		return txtDescription;
	}

	/**
	 * @return the ATest field scroll pane
	 */
	public JScrollPane getaTestFieldPane() {
		return aTestFieldPane;
	}

	/**
	 * @return the ATest name text component
	 */
	public JTextComponent getaTestName() {
		return txtName;
	}

	/**
	 * @return the status label
	 */
	public JLabel getaTestStatus() {
		return aTestStatus;
	}

	/**
	 * @return the status combobox
	 */
	public JComboBox getaTestStatusBox() {
		return cbxStatus;
	}

	/**
	 * Enables and disables input on this panel.
	 * 
	 * @param value
	 *            if value is true, input is enabled, otherwise input is
	 *            disabled.
	 */
	public void setInputEnabled(final boolean value) {
		txtDescription.setEnabled(value);
		butAdd.setEnabled(value);
		txtName.setEnabled(value);
		if (value) {
			addaTestLabel.setForeground(Color.black);
		} else {
			addaTestLabel.setForeground(Color.gray);
		}
	}

	/** Validates the input */

	public boolean validateInput() {

		boolean error = false;
		String errorText = "";

		txtDescription.setBackground(Color.white);
		txtName.setBackground(Color.white);

		if (txtDescription.getText().trim().isEmpty()) {
			error = true;
			errorText = "Description must not be blank";
			txtDescription.setBackground(new Color(243, 243, 209));
		}

		if (txtName.getText().trim().isEmpty()) {
			error = true;
			txtName.setBackground(new Color(243, 243, 209));
			errorText = "Name must not be blank";
		}

		if (error) {
			butAdd.setEnabled(false);
			labSaveError.setText(errorText);
			return false;
		} else {
			butAdd.setEnabled(true);
			labSaveError.setText("");
			return true;
		}

	}

	/**
	 * Called when the cancel button is pressed
	 * 
	 */

	public void onCancel() {
		txtName.setText("");
		txtDescription.setText("");
		butAdd.setEnabled(false);
		aTestView.clearSelection();
	}

}
