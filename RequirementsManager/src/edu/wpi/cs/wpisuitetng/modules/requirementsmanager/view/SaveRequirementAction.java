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

		switch (parentView.comboBoxPriority.getSelectedIndex()) {
		case 0:
			requirement.setPriority(Priority.BLANK);
			break;
		case 1:
			requirement.setPriority(Priority.HIGH);
			break;
		case 2:
			requirement.setPriority(Priority.MEDIUM);
			break;
		case 3:
			requirement.setPriority(Priority.LOW);
			break;
		default:
			requirement.setPriority(Priority.BLANK);
		}

		switch (parentView.comboBoxType.getSelectedIndex()) {
		case 0:
			requirement.setType(Type.BLANK);
			break;
		case 1:
			requirement.setType(Type.EPIC);
			break;
		case 2:
			requirement.setType(Type.THEME);
			break;
		case 3:
			requirement.setType(Type.USER_STORY);
			break;
		case 4:
			requirement.setType(Type.NON_FUNCTIONAL);
			break;
		case 5:
			requirement.setType(Type.SCENARIO);
			break;
		default:
			requirement.setType(Type.BLANK);
		}

		switch (parentView.comboBoxStatus.getSelectedIndex()) {
		case 0:
			requirement.setStatus(Status.BLANK);
			break;
		case 1:
			requirement.setStatus(Status.NEW);
			break;
		case 2:
			requirement.setStatus(Status.IN_PROGRESS);
			break;
		case 3:
			requirement.setStatus(Status.COMPLETE);
			break;
		case 4:
			requirement.setStatus(Status.DELETED);
			break;
		default:
			requirement.setStatus(Status.BLANK);
		}

		controller.AddRequirement(requirement);
	}
}
