package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

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

    public EditRequirementAction(Requirement requirement, DetailPanel parentView) {
		super("Save Requirement");
		this.requirement = requirement;
		this.parentView = parentView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SaveRequirementController controller = new SaveRequirementController(this.parentView);
		
		if(!parentView.getTextName().getText().trim().equals(""))
		{
			parentView.getTextName().setBackground(Color.WHITE);
			parentView.getTextNameValid().setText("");
		}
		
		if(!parentView.getTextDescription().getText().trim().equals("")) 
		{
			parentView.getTextDescription().setBackground(Color.WHITE);
			parentView.getTextDescriptionValid().setText("");
		}
		
		if(!parentView.getTextName().getText().trim().equals("") && !parentView.getTextDescription().getText().trim().equals("")) 
		{
			requirement.setName(parentView.getTextName().getText().trim());
			requirement.setDescription(parentView.getTextDescription().getText().trim());
			try {
				requirement.setIteration(Integer.parseInt(parentView.getTextIteration().getText()));
	
				/*
				switch (parentView.getComboBoxPriority().getSelectedIndex()) {
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
	
				
				switch (parentView.getComboBoxType().getSelectedIndex()) {
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
				*/
				try {
					requirement.setPriority(Priority.valueOf(parentView.getComboBoxPriority().getSelectedItem().toString().toUpperCase().replaceAll(" ", "_")));
				} catch(IllegalArgumentException except) {
					requirement.setPriority(Priority.BLANK);
				}
				
				try {
					requirement.setType(Type.valueOf(parentView.getComboBoxType().getSelectedItem().toString().toUpperCase().replaceAll(" ", "_")));
				} catch(IllegalArgumentException except) {
					requirement.setType(Type.BLANK);
				}
				
				requirement.setStatus(Status.valueOf(parentView.getComboBoxStatus().getSelectedItem().toString().toUpperCase().replaceAll(" ", "_")));
			
				controller.SaveRequirement(requirement,true);
			//Done by the observer now //this.parentView.getMainTabController().closeCurrentTab();
			} catch (NumberFormatException excep) {
				parentView.displaySaveError("Iteration must be an integer value");
			}
		}
		else {
			if(parentView.getTextName().getText().trim().equals(""))
			{
				parentView.getTextName().setBackground(new Color(243, 243, 209));
				parentView.getTextNameValid().setText("** Field must be non-blank **");
			}
			if(parentView.getTextDescription().getText().trim().equals(""))
			{
				parentView.getTextDescription().setBackground(new Color(243, 243, 209));
				parentView.getTextDescriptionValid().setText("** Field must be non-blank **");

			}
		}		
	}

}
