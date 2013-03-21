package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.event.ActionEvent;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;


import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.AddRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;


public class SaveRequirementAction extends AbstractAction {

	private Requirement requirement;
	private DetailPanel parentView;

	SaveRequirementAction(Requirement requirement, DetailPanel parentView) {
		super("Save Requirement");
		this.requirement = requirement;
		this.parentView = parentView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AddRequirementController controller = new AddRequirementController(this.parentView);

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
			requirement.setType(Type.SCENARIO);
		}

		switch (parentView.comboBoxStatus.getSelectedIndex()) {
		case 0:
			requirement.setStatus(Status.NEW);
			break;
		case 1:
			requirement.setStatus(Status.IN_PROGRESS);
			break;
		case 2:
			requirement.setStatus(Status.OPEN);
			break;			
		case 3:
			requirement.setStatus(Status.COMPLETE);
			break;
		case 4:
			requirement.setStatus(Status.DELETED);
			break;
		case 5:
			requirement.setStatus(Status.BLANK);
			break;
		}
		
		System.out.println("HIT DA BUTTON");
		controller.AddRequirement(requirement);
		JOptionPane.showMessageDialog(parentView, "MAGIC", "YAY",
				JOptionPane.OK_OPTION);
	}
}
