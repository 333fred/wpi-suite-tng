package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;


import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.AddRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SaveIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * 
 * @author Swagasaurus
 * 
 * Action to save a requirement
 */
public class SaveRequirementAction extends AbstractAction {

	private Requirement requirement;

	private DetailPanel parentView;

	public SaveRequirementAction(Requirement requirement, DetailPanel parentView) {
		super("Save Requirement");
		this.requirement = requirement;
		this.parentView = parentView;
	}

	/**
	 * Method to save a requirement if it is of acceptable format
	 * 
	 * @e any ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		AddRequirementController controller = new AddRequirementController(this.parentView);

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
			requirement.setDescription(parentView.getTextDescription().getText());
			requirement.setUsers(parentView.getAssignedUsers());
			
			try {
				requirement.setIteration(IterationDatabase.getInstance().getIteration(parentView.getTextIteration().getSelectedItem().toString()).getId());
				
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
				
				controller.AddRequirement(requirement);
			} catch (NumberFormatException except) {
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
	
	public Requirement getRequirement() {
		return requirement;
	}
}
