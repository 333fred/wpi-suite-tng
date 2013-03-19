package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entititymanagers;

import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators.RequirementValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators.ValidationIssue;

public class RequirementsEntityManager implements EntityManager<Requirement> {

	Data db;
	RequirementValidator validator;


	/**
	 * Create a RequirementsEntityManager
	 * 
	 * @param data
	 *            The Data instance to use
	 */
	public RequirementsEntityManager(Data data) {
		db = data;
		validator = new RequirementValidator(db);
	}
	
	//TODO testing - these are basically copied from DefectManager right now so they might not fully apply

	@Override
	public Requirement makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		
		final Requirement newRequirement = Requirement.fromJSON(content);
		
		newRequirement.setrUID(Count()+1); //we have to set the UID
		
		List<ValidationIssue> issues = validator.validate(s, newRequirement);
		if(issues.size() > 0) {
			// TODO: pass errors to client through exception
			for (ValidationIssue issue : issues) {
				System.out.println("Validation issue: " + issue.getMessage());
			}
			throw new BadRequestException();
		}

		if (!db.save(newRequirement, s.getProject())) {
			throw new WPISuiteException();
		}
		return newRequirement;
	}

	@Override
	public Requirement[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {
		
		final int intId = Integer.parseInt(id);
		if(intId < 1) {
			throw new NotFoundException();
		}
		
		Requirement[] requirements = null;
		try {
			requirements = db.retrieve(Requirement.class, "rUID", intId, s.getProject()).toArray(new Requirement[0]);
		} catch (WPISuiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(requirements.length < 1 || requirements[0] == null) {
			throw new NotFoundException();
		}
		return requirements;
	}

	@Override
	public Requirement[] getAll(Session s) throws WPISuiteException {
		return db.retrieveAll(new Requirement(), s.getProject()).toArray(new Requirement[0]);
	}

	@Override
	public void save(Session s, Requirement model) throws WPISuiteException {
		db.save(model, s.getProject());
	}

	private void ensureRole(Session session, Role role) throws WPISuiteException {
		User user = (User) db.retrieve(User.class, "username", session.getUsername()).get(0);
		if(!user.getRole().equals(role)) {
			throw new UnauthorizedException();
		}
	}
	
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		// TODO should user need to be Admin?
		ensureRole(s, Role.ADMIN);
		return (db.delete(getEntity(s, id)[0]) != null) ? true : false;
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		ensureRole(s, Role.ADMIN);
		db.deleteAll(new Requirement(), s.getProject());
	}

	@Override
	public int Count() throws WPISuiteException {
		// note that this is not project-specific - ids are unique across projects
		return db.retrieveAll(new Requirement()).size();
	}
	
	@Override
	public Requirement update(Session s, String content)
			throws WPISuiteException {
		
		Requirement updatedRequirement = Requirement.fromJSON(content);

		List<ValidationIssue> issues = validator.validate(s, updatedRequirement);
		if(issues.size() > 0) {
			throw new BadRequestException();
		}
		
		//TODO Add ability to update

		throw new NotImplementedException();
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		throw new NotImplementedException();
	}
	
	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		throw new NotImplementedException();
	}

}
