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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.DocumentSizeFilter;

/**
 * A panel containing a a creation and edit form 
 * for the aTests in a requirement
 * 
 * @author Nick M, Matt C, Steve Kordell
 */
@SuppressWarnings("serial")
public class MakeATestPanel extends JPanel {

	//The fields to make a aTest
	private JTextArea aTestName;
	private final JTextArea aTestDescription;
	private final JButton addaTest;
	private final JLabel addaTestLabel;
	private final JComboBox aTestStatusBox;
	
	//Jlabels for aTest fields
	private final JLabel aTestStatus;
	private final JLabel nameaTestLabel;
	private final JLabel descaTestLabel;

	private static final int VERTICAL_PADDING = 5;
	private static final int note_FIELD_HEIGHT = 50;
	private final JScrollPane aTestFieldPane;

	/**
	 * Construct the panel, add and layout components.
	 * 
	 * @param model
	 *            the requirement to which a notes made with this class will be
	 *            saved
	 * @param parentView
	 *            the view of the requirement in question
	 */
	public MakeATestPanel(Requirement model, DetailPanel parentView) {

		//setup the aTest name field
		aTestName = new JTextArea(1, 40);
		aTestName.setLineWrap(true);
		aTestName.setWrapStyleWord(true);
		aTestName.setMaximumSize(new Dimension(40, 2));
		AbstractDocument textNameDoc = (AbstractDocument)
		aTestName.getDocument();
		textNameDoc.setDocumentFilter(new DocumentSizeFilter(100));
		aTestName.setBorder((new JTextField()).getBorder());
		aTestName.setName("Name");
		aTestName.setDisabledTextColor(Color.GRAY);
		
		aTestName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						aTestDescription.transferFocus();
					} else {
						aTestDescription.transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					event.consume();
				}
			}
		});

		//setup the aTest description field
		aTestDescription = new JTextArea();
		aTestDescription.setLineWrap(true);
		aTestDescription.setWrapStyleWord(true);
		aTestDescription.setBorder((new JTextField()).getBorder());
		aTestDescription.setDisabledTextColor(Color.GRAY);
		
		aTestDescription.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						aTestDescription.transferFocus();
					} else {
						aTestDescription.transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					aTestDescription.append("\n");
					event.consume();
				}
			}
		});

		//setup all the buttons and label text
		addaTest = new JButton("Save");
		aTestStatus = new JLabel(
				"No Acceptance Test selected. Fill name and description to create a new one.");
		addaTestLabel = new JLabel("Acceptance Test:");
		nameaTestLabel = new JLabel("Name:");
		descaTestLabel = new JLabel("Description:");		
		String[] availableStatuses = {"","PASSED","FAILED"};		
		aTestStatusBox = new JComboBox(availableStatuses);

		this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.black, 1),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);

		aTestFieldPane = new JScrollPane(aTestDescription);
		
		//Setup the layout of the aTest Panel
		layout.putConstraint(SpringLayout.NORTH, addaTestLabel, 0,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, addaTestLabel, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, aTestStatus, 0,
				SpringLayout.SOUTH, addaTestLabel);
		layout.putConstraint(SpringLayout.NORTH, nameaTestLabel,
				VERTICAL_PADDING, SpringLayout.SOUTH, aTestStatus);
		layout.putConstraint(SpringLayout.NORTH, aTestName, VERTICAL_PADDING,
				SpringLayout.SOUTH, nameaTestLabel);
		layout.putConstraint(SpringLayout.WEST, aTestName, 0, SpringLayout.WEST,
				nameaTestLabel);
		layout.putConstraint(SpringLayout.EAST, aTestName, 0, SpringLayout.EAST,
				this);
		layout.putConstraint(SpringLayout.NORTH, descaTestLabel,
				VERTICAL_PADDING, SpringLayout.SOUTH, aTestName);
		layout.putConstraint(SpringLayout.NORTH, aTestFieldPane,
				VERTICAL_PADDING, SpringLayout.SOUTH, descaTestLabel);
		layout.putConstraint(SpringLayout.WEST, aTestFieldPane, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, aTestFieldPane, 0,
				SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, aTestFieldPane,
				note_FIELD_HEIGHT, SpringLayout.NORTH, aTestFieldPane);

		layout.putConstraint(SpringLayout.NORTH, aTestStatusBox,
				VERTICAL_PADDING, SpringLayout.SOUTH, aTestFieldPane);
		layout.putConstraint(SpringLayout.WEST, aTestStatusBox, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, this, VERTICAL_PADDING,
				SpringLayout.SOUTH, aTestStatusBox);

		layout.putConstraint(SpringLayout.NORTH, addaTest, VERTICAL_PADDING,
				SpringLayout.SOUTH, aTestStatusBox);
		layout.putConstraint(SpringLayout.EAST, addaTest, 0, SpringLayout.EAST,
				this);
		layout.putConstraint(SpringLayout.SOUTH, this, VERTICAL_PADDING,
				SpringLayout.SOUTH, addaTest);

		//add all of the swing components to the this aTest panel
		this.add(aTestStatus);
		this.add(addaTestLabel);
		this.add(addaTest);
		this.add(aTestName);
		this.add(nameaTestLabel);
		this.add(descaTestLabel);
		this.add(aTestStatusBox);
		this.add(aTestFieldPane);

		//default the add button and complete checkbox 
		//to un-enabled
		addaTest.setEnabled(false);
		aTestStatusBox.setEnabled(false);

	}

	/**
	 * A function to the get the text area
	 * 
	 * @return the note JTextArea
	 */
	public JTextArea getaTestField() {
		return aTestDescription;
	}

	/**
	 * Enables and disables input on this panel.
	 * 
	 * @param value
	 *            if value is true, input is enabled, otherwise input is
	 *            disabled.
	 */
	public void setInputEnabled(boolean value) {
		aTestDescription.setEnabled(value);
		addaTest.setEnabled(value);
		if (value) {
			addaTestLabel.setForeground(Color.black);
		} else {
			addaTestLabel.setForeground(Color.gray);
		}
	}

	public JTextComponent getaTestName() {
		return aTestName;
	}

	public JButton getAddaTest() {
		return addaTest;
	}

	public JComboBox getaTestStatusBox() {
		return aTestStatusBox;
	}

	public JScrollPane getaTestFieldPane() {
		return aTestFieldPane;
	}

	public JLabel getaTestStatus() {
		return aTestStatus;
	}

	public JButton getAddATest() {
		return addaTest;
	}
}
