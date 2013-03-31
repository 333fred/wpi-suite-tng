package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.IterationActionMode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entititymanagers.RequirementsEntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

public class IterationValidator {

	public List<ValidationIssue> validate(Session s, Iteration i,
			IterationActionMode mode, Data db) {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();

		// If the iteration is null, then we have an error
		if (i == null) {
			issues.add(new ValidationIssue("Iteration cannot be null"));
			return issues;
		}

		// Detect if the iteration has been given a name
		if (i.getName() == null || i.getName() == ""){
			issues.add(new ValidationIssue("Iteration must have a name"));
		}
		
		// If the iteration is being created, then we must have no requirements
		if (mode == IterationActionMode.CREATE) {
			i.setRequirements(new ArrayList<Integer>());
		} else {
			// If we are not creating, make sure that requirements aren't null
			if (i.getRequirements() == null) {
				i.setRequirements(new ArrayList<Integer>());
			} else {
				// Loop through all the requirement ids and make sure that they
				// all exist
				List<Requirement> reqs = Arrays
						.asList(new RequirementsEntityManager(db).getAll(s));
				for (Integer id : i.getRequirements()) {
					boolean detected = false;
					for (Requirement req : reqs) {
						if (req.getrUID() == id) {
							detected = true;
						}
					}
					if (!detected){
						issues.add(new ValidationIssue("Invalid Requirement ID " + id));
					}
				}
			}
		}
		
		// Make sure that the start and end dates are correct
		if(!i.validateDate()){
			issues.add(new ValidationIssue("Iteration dates must not overlap"));
		}

		return issues;
	}

}
