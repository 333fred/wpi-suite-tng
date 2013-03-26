package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entititymanagers;

import java.util.ArrayList;
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
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.RequirementActionMode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.logger.Logger;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.logger.Logger.Event;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.logger.Logger.EventType;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Requirement makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {

		final Requirement newRequirement = Requirement.fromJSON(content);

		newRequirement.setrUID(Count() + 1); // we have to set the UID

		List<ValidationIssue> issues = validator.validate(s, newRequirement,
				RequirementActionMode.CREATE);
		if (issues.size() > 0) {
			// TODO: pass errors to client through exception
			for (ValidationIssue issue : issues) {
				System.out.println("Validation issue: " + issue.getMessage());
			}
			throw new BadRequestException();
		}

		// Log the creation of a new requirement
		newRequirement.logCreation(s);

		if (!db.save(newRequirement, s.getProject())) {
			throw new WPISuiteException();
		}

		return newRequirement;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Requirement[] getEntity(Session s, String id)
			throws NotFoundException {

		final int intId = Integer.parseInt(id);
		if (intId < 1) {
			throw new NotFoundException();
		}

		Requirement[] requirements = null;
		try {
			requirements = db.retrieve(Requirement.class, "rUID", intId,
					s.getProject()).toArray(new Requirement[0]);
		} catch (WPISuiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (requirements.length < 1 || requirements[0] == null) {
			throw new NotFoundException();
		}
		return requirements;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Requirement[] getAll(Session s) {
		return db.retrieveAll(new Requirement(), s.getProject()).toArray(
				new Requirement[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(Session s, Requirement model) {
		db.save(model, s.getProject());
	}

	/**
	 * Ensures that a given user has given permissions
	 * 
	 * @param session
	 *            the session of the current user
	 * @param role
	 *            the role we are checking
	 * @throws WPISuiteException
	 *             the unauthorized exception
	 */
	private void ensureRole(Session session, Role role)
			throws WPISuiteException {
		User user = (User) db.retrieve(User.class, "username",
				session.getUsername()).get(0);
		if (!user.getRole().equals(role)) {
			throw new UnauthorizedException();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		// TODO should user need to be Admin?
		ensureRole(s, Role.ADMIN);
		return (db.delete(getEntity(s, id)[0]) != null) ? true : false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		ensureRole(s, Role.ADMIN);
		db.deleteAll(new Requirement(), s.getProject());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int Count() {
		// Retrieve all from this project
		return db.retrieveAll(new Requirement()).size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Requirement update(Session s, String content)
			throws WPISuiteException {

		// Get updated requirements
		Requirement updatedRequirement = Requirement.fromJSON(content);
		Requirement oldReq;

		// Validate the requirement
		List<ValidationIssue> issues = validator.validate(s,
				updatedRequirement, RequirementActionMode.EDIT);
		if (issues.size() > 0) {
			throw new BadRequestException();
		}

		// Attempt to get the old version of the requirement
		try {
			oldReq = getRequirement(updatedRequirement.getrUID(), s);
		} catch (RequirementNotFoundException ex) {
			System.out.println("Error: Requirement "
					+ updatedRequirement.getrUID() + " not found.");
			throw new WPISuiteException();
		}

		// Attempt to update the old requirement
		oldReq = updateRequirement(oldReq, updatedRequirement, s);

		// Save the requirement, and throw an exception if if fails
		if (!db.save(oldReq, s.getProject())) {
			throw new WPISuiteException();
		}

		return oldReq;
	}

	/**
	 * Gets a requirement given an id
	 * 
	 * @param id
	 *            The ID of the requirement to find
	 * @param s
	 *            The session the requirement is in
	 * @return The requested requirement
	 * @throws RequirementNotFoundException
	 *             If the requirement cannot be found, or is invalid, we throw
	 *             this
	 */
	public Requirement getRequirement(int id, Session s)
			throws RequirementNotFoundException {
		try {
			// Attempt to get the requirement
			List<Model> requirement = db.retrieve(Requirement.class, "rUID",
					id, s.getProject());

			// Check that the requirement was actually found
			if (requirement.size() < 1 || requirement.get(0) == null) {
				throw new RequirementNotFoundException(id);
			}

			// Make sure we actually got a requirement, and return if we have
			// Otherwise, throw an exception
			if (requirement.get(0) instanceof Requirement) {
				return ((Requirement) requirement.get(0));
			} else {
				throw new WPISuiteException();
			}
		} catch (WPISuiteException e) {
			// If we thew an exception, we didn't find the requirement, so throw
			// a requirement not found exception
			throw new RequirementNotFoundException(id);
		}
	}

	/**
	 * Updates a given requirement with all diffs, and logs all changes
	 * 
	 * @param oldReq
	 *            The old version of the requirement
	 * @param newReq
	 *            The new requirement
	 * @return the updated requirement
	 */
	public Requirement updateRequirement(Requirement oldReq,
			Requirement newReq, Session s) {

		// Update all the fields in the old requirement with the new fields.
		// Collect changes for the log
		List<Event> events = new ArrayList<Event>();
		Logger logger = oldReq.getLogger();

		// Description
		if (!oldReq.getDescription().equals(newReq.getDescription())) {
			events.add(logger.new Event(oldReq.getDescription(), newReq
					.getDescription(), EventType.DESC_CHANGE));
			oldReq.setDescription(newReq.getDescription());
		}
		// Effort
		if (oldReq.getEffort() != newReq.getEffort()) {
			events.add(logger.new Event(oldReq.getEffort(), newReq.getEffort(),
					EventType.EFFORT_CHANGE));
			oldReq.setEffort(newReq.getEffort());
		}
		// Iteration
		if (oldReq.getIteration() != newReq.getIteration()) {
			events.add(logger.new Event(oldReq.getIteration(), newReq
					.getIteration(), EventType.ITER_CHANGE));
			oldReq.setIteration(newReq.getIteration());
		}
		// Name
		if (!oldReq.getName().equals(newReq.getName())) {
			events.add(logger.new Event(oldReq.getName(), newReq.getName(),
					EventType.NAME_CHANGE));
			oldReq.setName(newReq.getName());
		}
		// Notes
		if (!oldReq.getNotes().equals(newReq.getNotes())) {
			events.add(logger.new Event(oldReq.getNotes(), newReq.getNotes(),
					EventType.NOTE_CHANGE));
			oldReq.setNotes(newReq.getNotes());
		}
		// Priority
		if (oldReq.getPriority() != newReq.getPriority()) {
			events.add(logger.new Event(oldReq.getPriority(), newReq
					.getPriority(), EventType.PRIORITY_CHANGE));
			oldReq.setPriority(newReq.getPriority());
		}
		// Parents
		if (!oldReq.getpUID().equals(newReq.getpUID())) {
			events.add(logger.new Event(oldReq.getpUID(), newReq.getpUID(),
					EventType.PARENT_CHANGE));
			oldReq.setpUID(newReq.getpUID());
		}
		// Release Number
		if (oldReq.getReleaseNum() != newReq.getReleaseNum()) {
			events.add(logger.new Event(oldReq.getReleaseNum(), newReq
					.getReleaseNum(), EventType.RELEASE_CHANGE));
			oldReq.setReleaseNum(newReq.getReleaseNum());
		}
		// Status
		if (oldReq.getStatus() != newReq.getStatus()) {
			events.add(logger.new Event(oldReq.getStatus(), newReq.getStatus(),
					EventType.STATUS_CHANGE));
			oldReq.setStatus(newReq.getStatus());
		}
		// Subrequirements
		if (!oldReq.getSubRequirements().equals(newReq.getSubRequirements())) {
			events.add(logger.new Event(oldReq.getSubRequirements(), newReq
					.getSubRequirements(), EventType.SUB_CHANGE));
			oldReq.setSubRequirements(newReq.getSubRequirements());
		}
		// Assignees
		if (!oldReq.getUsers().equals(newReq.getUsers())) {
			events.add(logger.new Event(oldReq.getUsers(), newReq.getUsers(),
					EventType.ASSIGN_CHANGE));
			oldReq.setUsers(newReq.getUsers());
		}
		// Type
		if (oldReq.getType() != newReq.getType()) {
			events.add(logger.new Event(oldReq.getType(), newReq.getType(),
					EventType.TYPE_CHANGE));
			oldReq.setType(newReq.getType());
		}

		// Add all of the changes to the log
		oldReq.logEvents(events, s);

		return oldReq;
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
