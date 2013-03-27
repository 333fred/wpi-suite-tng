package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.Color;
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

	public SaveRequirementAction(Requirement requirement, DetailPanel parentView) {
		super("Save Requirement");
		this.requirement = requirement;
		this.parentView = parentView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AddRequirementController controller = new AddRequirementController(this.parentView);

		if(!parentView.getTextName().getText().equals(""))
		{
			parentView.getTextName().setBackground(Color.WHITE);
			parentView.getTextNameValid().setText("");
		}
		
		if(!parentView.getTextDescription().getText().equals("")) 
		{
			parentView.getTextDescription().setBackground(Color.WHITE);
			parentView.getTextDescriptionValid().setText("");
		}
		
		if(!parentView.getTextName().getText().equals("") && !parentView.getTextDescription().getText().equals("")) 
		{
			requirement.setName(parentView.getTextName().getText());
			requirement.setDescription(parentView.getTextDescription().getText());
			try {
				requirement.setIteration(Integer.parseInt(parentView.getTextIteration().getText()));	

				requirement.setPriority(Priority.valueOf(parentView.getComboBoxPriority().getSelectedItem().toString().toUpperCase().replaceAll(" ", "_")));
				requirement.setType(Type.valueOf(parentView.getComboBoxType().getSelectedItem().toString().toUpperCase().replaceAll(" ", "_")));
				requirement.setStatus(Status.valueOf(parentView.getComboBoxStatus().getSelectedItem().toString().toUpperCase().replaceAll(" ", "_")));
				
				controller.AddRequirement(requirement);
			//Done by the observer now //this.parentView.getMainTabController().closeCurrentTab();
			} catch (NumberFormatException excep) {
				parentView.displaySaveError("Iteration must be an integer value");
			}
		}
		else {
			if(parentView.getTextName().getText().equals(""))
			{
				parentView.getTextName().setBackground(new Color(243, 243, 209));
				parentView.getTextNameValid().setText("** Field must be non-blank **");
			}
			if(parentView.getTextDescription().getText().equals(""))
			{
				parentView.getTextDescription().setBackground(new Color(243, 243, 209));
				parentView.getTextDescriptionValid().setText("** Field must be non-blank **");
			}
		}
	}
}
