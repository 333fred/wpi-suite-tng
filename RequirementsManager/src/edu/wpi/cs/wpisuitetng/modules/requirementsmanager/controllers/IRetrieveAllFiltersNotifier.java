package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;

public interface IRetrieveAllFiltersNotifier {

	
	public void receivedData(Filter[] filters);
}
