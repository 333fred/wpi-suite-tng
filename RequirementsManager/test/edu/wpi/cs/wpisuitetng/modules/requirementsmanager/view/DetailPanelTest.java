/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import junit.extensions.abbot.ComponentTestFixture;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import abbot.tester.JTextComponentTester;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel.Mode;

public class DetailPanelTest extends ComponentTestFixture {
	
	MainTabController mainTabController = null;
	
	@Before
	public void setup() {
		mainTabController = new MainTabController();
	}
	
	@Test
	public void testCreationForComplete() {
		final Requirement r = new Requirement();
		r.setStatus(Status.COMPLETE);
		DetailPanel detailPanel = null;
		detailPanel = new DetailPanel(r, Mode.EDIT, mainTabController);
		Assert.assertNotNull(detailPanel);
	}
	
	@Test
	public void testCreationForDeleted() {
		final Requirement r = new Requirement();
		r.setStatus(Status.DELETED);
		DetailPanel detailPanel = null;
		detailPanel = new DetailPanel(r, Mode.EDIT, mainTabController);
		Assert.assertNotNull(detailPanel);
	}
	
	@Test
	public void testCreationForEachModeNew() {
		final Requirement r = new Requirement();
		r.setStatus(Status.NEW);
		DetailPanel detailPanel = null;
		for (final Mode mode : Mode.values()) {
			detailPanel = new DetailPanel(r, mode, mainTabController);
			Assert.assertNotNull(detailPanel);
		}
	}
	
	@Test
	public void testCreationForInProgress() {
		final Requirement r = new Requirement();
		r.setStatus(Status.IN_PROGRESS);
		DetailPanel detailPanel = null;
		detailPanel = new DetailPanel(r, Mode.EDIT, mainTabController);
		Assert.assertNotNull(detailPanel);
	}
	
	@Test
	public void testCreationForOpen() {
		final Requirement r = new Requirement();
		r.setStatus(Status.OPEN);
		DetailPanel detailPanel = null;
		detailPanel = new DetailPanel(r, Mode.EDIT, mainTabController);
		Assert.assertNotNull(detailPanel);
	}
	
	@Test
	public void testGetMainTabController() {
		final Requirement r = new Requirement();
		// TODO: Why the heck isn't before being called to set this up?
		if (mainTabController == null) {
			mainTabController = new MainTabController();
		}
		final DetailPanel detailPanel = new DetailPanel(r, Mode.EDIT,
				mainTabController);
		
		Assert.assertTrue(detailPanel.getMainTabController().equals(
				mainTabController));
	}
	
	@Test
	public void testGetModel() {
		final Requirement r = new Requirement();
		final DetailPanel detailPanel = new DetailPanel(r, Mode.EDIT,
				mainTabController);
		
		Assert.assertTrue(detailPanel.getModel().equals(r));
	}
	
	@Test
	public void testNoteSetAndGet() {
		final Requirement r = new Requirement();
		final Note n = new Note("note", "creator");
		r.addNote(n);
		final List<Note> noteList = new ArrayList<Note>();
		noteList.add(n);
		final DetailPanel detailPanel = new DetailPanel(r, Mode.EDIT,
				mainTabController);
		
		Assert.assertTrue(detailPanel.getNoteList().equals(noteList));
		
		final Note n2 = new Note("note2", "Creator");
		noteList.add(n2);
		detailPanel.setNoteList(noteList);
		
		Assert.assertTrue(detailPanel.getNoteList().equals(noteList));
		
	}
	
	@Test
	public void testSaveButtonEnables() {
		final Requirement r = new Requirement();
		final DetailPanel panel = new DetailPanel(r, DetailPanel.Mode.CREATE,
				mainTabController);
		
		final JTextComponentTester tester = new JTextComponentTester();
		
		showFrame(panel);
		
		Assert.assertFalse(panel.getBtnSave().isEnabled());
		
		tester.actionEnterText(panel.getTextName(), "Req Name");
		Assert.assertFalse(panel.getBtnSave().isEnabled());
		
		tester.actionEnterText(panel.getTextDescription(), "Req Desc");
		Assert.assertTrue(panel.getBtnSave().isEnabled());
		
	}
	
	@Test
	public void testSaveError() {
		final Requirement r = new Requirement();
		final DetailPanel detailPanel = new DetailPanel(r, Mode.EDIT,
				mainTabController);
		
		detailPanel.displaySaveError("Save Error");
		
		Assert.assertEquals(detailPanel.getTextSaveError().getText(),
				"Save Error");
	}
	
	@Test
	public void testTabClose() {
		final Requirement r = new Requirement();
		final DetailPanel detailPanel = new DetailPanel(r, Mode.EDIT,
				mainTabController);
		
		Assert.assertTrue(detailPanel.onTabClosed());
	}
	
	@Test
	public void testTextActual() {
		final Requirement r = new Requirement();
		final DetailPanel detailPanel = new DetailPanel(r, Mode.EDIT,
				mainTabController);
		final JTextField textActual = new JTextField("123");
		detailPanel.setTextActual(textActual);
		
		Assert.assertTrue(detailPanel.getTextActual().equals(textActual));
		Assert.assertEquals(detailPanel.getTextActual().getText(), "123");
	}
	
	@Test
	public void testTextDescription() {
		final Requirement r = new Requirement();
		final DetailPanel detailPanel = new DetailPanel(r, Mode.EDIT,
				mainTabController);
		final JTextArea textDesc = new JTextArea("Desc");
		detailPanel.setTextDescription(textDesc);
		
		Assert.assertTrue(detailPanel.getTextDescription().equals(textDesc));
		Assert.assertEquals(detailPanel.getTextDescription().getText(), "Desc");
	}
	
	@Test
	public void testTextDescriptionValid() {
		final Requirement r = new Requirement();
		final DetailPanel detailPanel = new DetailPanel(r, Mode.EDIT,
				mainTabController);
		final JTextArea textDescValid = new JTextArea("valid");
		detailPanel.setTextDescriptionValid(textDescValid);
		
		Assert.assertTrue(detailPanel.getTextDescriptionValid().equals(
				textDescValid));
		Assert.assertEquals(detailPanel.getTextDescriptionValid().getText(),
				"valid");
	}
	
	@Test
	public void testTextEstimate() {
		final Requirement r = new Requirement();
		final DetailPanel detailPanel = new DetailPanel(r, Mode.EDIT,
				mainTabController);
		final JTextField textEst = new JTextField("123");
		detailPanel.setTextEstimate(textEst);
		
		Assert.assertTrue(detailPanel.getTextEstimate().equals(textEst));
		Assert.assertEquals(detailPanel.getTextEstimate().getText(), "123");
	}
	
	@Test
	public void testTextName() {
		final Requirement r = new Requirement();
		final DetailPanel detailPanel = new DetailPanel(r, Mode.EDIT,
				mainTabController);
		final JTextArea textName = new JTextArea("name");
		detailPanel.setTextName(textName);
		
		Assert.assertTrue(detailPanel.getTextName().equals(textName));
		Assert.assertEquals(detailPanel.getTextName().getText(), "name");
	}
	
	@Test
	public void testTextNameValid() {
		final Requirement r = new Requirement();
		final DetailPanel detailPanel = new DetailPanel(r, Mode.EDIT,
				mainTabController);
		final JTextArea textNameValid = new JTextArea("valid");
		detailPanel.setTextNameValid(textNameValid);
		
		Assert.assertTrue(detailPanel.getTextNameValid().equals(textNameValid));
		Assert.assertEquals(detailPanel.getTextNameValid().getText(), "valid");
	}
	
	@Test
	public void testTextRelease() {
		final Requirement r = new Requirement();
		final DetailPanel detailPanel = new DetailPanel(r, Mode.EDIT,
				mainTabController);
		final JTextField textRelease = new JTextField("v1.1");
		detailPanel.setTextRelease(textRelease);
		
		Assert.assertTrue(detailPanel.getTextRelease().equals(textRelease));
		Assert.assertEquals(detailPanel.getTextRelease().getText(), "v1.1");
	}
}
