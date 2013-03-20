package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.event.ActionEvent;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;

import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.AddRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.SaveNoteController;

public class SaveRequirementAction extends AbstractAction {
	
	private Requirement requirement;
	private DetailView parentView;
	
	SaveRequirementAction(Requirement requirement, DetailView parentView) {
		super("Add Requirement");
		this.requirement = requirement;
		this.parentView = parentView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AddRequirementController controller = new AddRequirementController();
		
		requirement.setName(parentView.textName.getText());
		requirement.setDescription(parentView.textDescription.getText());
		
		switch((String)parentView.comboBoxPriority.getSelectedItem()) {
		case "":
			requirement.setPriority(Priority.BLANK);
			break;
		case "High":
			requirement.setPriority(Priority.HIGH);
			break;
		case "Medium":
			requirement.setPriority(Priority.MEDIUM);
			break;
		case "Low":
			requirement.setPriority(Priority.LOW);
			break;
		}

		switch((String)parentView.comboBoxType.getSelectedItem()) {
		case "":
			requirement.setType(Type.BLANK);
			break;
		case "Epic":
			requirement.setType(Type.EPIC);
			break;
		case "Theme":
			requirement.setType(Type.THEME);
			break;
		case "User Story":
			requirement.setType(Type.USER_STORY);
			break;
		case "Non-functional":
			requirement.setType(Type.NON_FUNCTIONAL);
			break;
		case "Scenario":
			requirement.setType(Type.SCENARIO);
			break;
		}
				
		switch((String)parentView.comboBoxStatus.getSelectedItem()) {
		case "":
			requirement.setStatus(Status.BLANK);
			break;
		case "New":
			requirement.setStatus(Status.NEW);
			break;
		case "In Progress":
			requirement.setStatus(Status.IN_PROGRESS);
			break;
		case "Complete":
			requirement.setStatus(Status.COMPLETE);
			break;
		case "Deleted":
			requirement.setStatus(Status.DELETED);
			break;
		}
		
		controller.AddRequirement(requirement);
	}
}
