package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.Color;
import java.awt.event.ActionEvent;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;


import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.AddRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SaveRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;


/**
 * @author Chris
 *
 */
public class EditRequirementAction extends AbstractAction {

	private Requirement requirement;
	private DetailPanel parentView;

	EditRequirementAction(Requirement requirement, DetailPanel parentView) {
		super("Save Requirement");
		this.requirement = requirement;
		this.parentView = parentView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SaveRequirementController controller = new SaveRequirementController(this.parentView);
		
		if(!parentView.textName.getText().equals(""))
		{
			parentView.textName.setBackground(Color.WHITE);
			parentView.textNameValid.setText("");
		}
		
		if(!parentView.textDescription.getText().equals("")) 
		{
			parentView.textDescription.setBackground(Color.WHITE);
			parentView.textDescriptionValid.setText("");
		}
		
		if(!parentView.textName.getText().equals("") && !parentView.textDescription.getText().equals("")) 
		{
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

			controller.SaveRequirement(requirement);
			//Done by the observer now //this.parentView.getMainTabController().closeCurrentTab();
		}
		else {
			if(parentView.textName.getText().equals(""))
			{
				parentView.textName.setBackground(new Color (255,255,170));
				parentView.textNameValid.setText("**Requirement must have name in order to save**");
			}
			if(parentView.textDescription.getText().equals(""))
			{
				parentView.textDescription.setBackground(new Color (255,255,170));
				parentView.textDescriptionValid.setText("**Requirement must have description in order to save**");
			}
		}		
	}

}
