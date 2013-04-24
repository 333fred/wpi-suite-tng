package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel.Mode;

public class DetailPanelTest {
	
	MainTabController mainTabController = null;
	
	@Before
	public void setup(){
		if(this.mainTabController == null){
			this.mainTabController = new MainTabController();
		}
	}
	
	@Test
	public void testCreationForEachModeNew(){
		Requirement r = new Requirement();
		r.setStatus(Status.NEW);
		DetailPanel detailPanel = null;
		for(Mode mode : Mode.values()){
			detailPanel = new DetailPanel(r, mode, this.mainTabController);
			assertNotNull(detailPanel);
		}
	}
	
	@Test
	public void testCreationForComplete(){
		Requirement r = new Requirement();
		r.setStatus(Status.COMPLETE);
		DetailPanel detailPanel = null;
		detailPanel = new DetailPanel(r, Mode.EDIT, this.mainTabController);
		assertNotNull(detailPanel);
	}
	
	@Test
	public void testCreationForDeleted(){
		Requirement r = new Requirement();
		r.setStatus(Status.DELETED);
		DetailPanel detailPanel = null;
		detailPanel = new DetailPanel(r, Mode.EDIT, this.mainTabController);
		assertNotNull(detailPanel);
	}
	
	@Test
	public void testCreationForInProgress(){
		Requirement r = new Requirement();
		r.setStatus(Status.IN_PROGRESS);
		DetailPanel detailPanel = null;
		detailPanel = new DetailPanel(r, Mode.EDIT, this.mainTabController);
		assertNotNull(detailPanel);
	}
	
	@Test
	public void testCreationForOpen(){
		Requirement r = new Requirement();
		r.setStatus(Status.OPEN);
		DetailPanel detailPanel = null;
		detailPanel = new DetailPanel(r, Mode.EDIT, this.mainTabController);
		assertNotNull(detailPanel);
	}
	
	@Test
	public void testNoteSetAndGet() {
		Requirement r = new Requirement();
		Note n = new Note("note", "creator");
		r.addNote(n);
		List<Note> noteList = new ArrayList<Note>();
		noteList.add(n);
		DetailPanel detailPanel= new DetailPanel(r, Mode.EDIT, this.mainTabController);
		
		assertTrue(detailPanel.getNoteList().equals(noteList));
		
		Note n2 = new Note("note2", "Creator");
		noteList.add(n2);
		detailPanel.setNoteList(noteList);
		
		assertTrue(detailPanel.getNoteList().equals(noteList));
		
		}
	
	@Test
	public void testSaveError() {
		Requirement r = new Requirement();
		DetailPanel detailPanel= new DetailPanel(r, Mode.EDIT, this.mainTabController);
		
		detailPanel.displaySaveError("Save Error");
		
		assertEquals(detailPanel.getTextSaveError().getText(), "Save Error");
	}
	
	@Test 
	public void testGetMainTabController() {
		Requirement r = new Requirement();
		DetailPanel detailPanel= new DetailPanel(r, Mode.EDIT, this.mainTabController);
		
		assertTrue(detailPanel.getMainTabController().equals(mainTabController));
	}
	
	@Test
	public void testTextName() {
		Requirement r = new Requirement();
		DetailPanel detailPanel= new DetailPanel(r, Mode.EDIT, this.mainTabController);
		JTextArea textName = new JTextArea("name");
		detailPanel.setTextName(textName);
		
		assertTrue(detailPanel.getTextName().equals(textName));
		assertEquals(detailPanel.getTextName().getText(), "name");
	}
	
	@Test
	public void testTextNameValid() {
		Requirement r = new Requirement();
		DetailPanel detailPanel= new DetailPanel(r, Mode.EDIT, this.mainTabController);
		JTextArea textNameValid = new JTextArea("valid");
		detailPanel.setTextNameValid(textNameValid);
		
		assertTrue(detailPanel.getTextNameValid().equals(textNameValid));
		assertEquals(detailPanel.getTextNameValid().getText(), "valid");
	}
	
	@Test
	public void testTextDescription() {
		Requirement r = new Requirement();
		DetailPanel detailPanel= new DetailPanel(r, Mode.EDIT, this.mainTabController);
		JTextArea textDesc = new JTextArea("Desc");
		detailPanel.setTextDescription(textDesc);
		
		assertTrue(detailPanel.getTextDescription().equals(textDesc));
		assertEquals(detailPanel.getTextDescription().getText(), "Desc");
	}
	
	@Test
	public void testTextDescriptionValid() {
		Requirement r = new Requirement();
		DetailPanel detailPanel= new DetailPanel(r, Mode.EDIT, this.mainTabController);
		JTextArea textDescValid = new JTextArea("valid");
		detailPanel.setTextDescriptionValid(textDescValid);
		
		assertTrue(detailPanel.getTextDescriptionValid().equals(textDescValid));
		assertEquals(detailPanel.getTextDescriptionValid().getText(), "valid");
	}
	
	@Test
	public void testTextEstimate() {
		Requirement r = new Requirement();
		DetailPanel detailPanel= new DetailPanel(r, Mode.EDIT, this.mainTabController);
		JTextField textEst = new JTextField("123");
		detailPanel.setTextEstimate(textEst);
		
		assertTrue(detailPanel.getTextEstimate().equals(textEst));
		assertEquals(detailPanel.getTextEstimate().getText(), "123");
	}
	
	@Test 
	public void testGetModel(){
		Requirement r = new Requirement();
		DetailPanel detailPanel= new DetailPanel(r, Mode.EDIT, this.mainTabController);
		
		assertTrue(detailPanel.getModel().equals(r));
	}
	
	@Test
	public void testTextActual() {
		Requirement r = new Requirement();
		DetailPanel detailPanel= new DetailPanel(r, Mode.EDIT, this.mainTabController);
		JTextField textActual = new JTextField("123");
		detailPanel.setTextActual(textActual);
		
		assertTrue(detailPanel.getTextActual().equals(textActual));
		assertEquals(detailPanel.getTextActual().getText(), "123");
	}
	
	@Test
	public void testTextRelease() {
		Requirement r = new Requirement();
		DetailPanel detailPanel= new DetailPanel(r, Mode.EDIT, this.mainTabController);
		JTextField textRelease = new JTextField("v1.1");
		detailPanel.setTextRelease(textRelease);
		
		assertTrue(detailPanel.getTextRelease().equals(textRelease));
		assertEquals(detailPanel.getTextRelease().getText(), "v1.1");
	}
	
	@Test
	public void testTabClose() {
		Requirement r = new Requirement();
		DetailPanel detailPanel= new DetailPanel(r, Mode.EDIT, this.mainTabController);
		
		assertTrue(detailPanel.onTabClosed());
	}
}
