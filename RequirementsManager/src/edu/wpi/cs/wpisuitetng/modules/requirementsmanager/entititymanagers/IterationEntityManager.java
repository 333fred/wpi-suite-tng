package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entititymanagers;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.logger.ModelMapper;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators.IterationValidator;

public class IterationEntityManager implements EntityManager<Iteration> {

	Data db;
	IterationValidator validator;
	ModelMapper updateMapper;
	
	public IterationEntityManager(Data db){
		this.db = db;
		this.validator = new IterationValidator();
		this.updateMapper = new ModelMapper();
	}
	
	@Override
	public Iteration makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iteration[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iteration[] getAll(Session s) throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iteration update(Session s, String content) throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Session s, Iteration model) throws WPISuiteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int Count() throws WPISuiteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

}
