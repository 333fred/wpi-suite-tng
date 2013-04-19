package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

public class DeleteFilterRequestObserver implements RequestObserver {

	private ISaveNotifier notifier;

	/**
	 * Creates a request observer with the given controller as a callback
	 * 
	 * @param controller
	 *            the controller to callback
	 */
	public DeleteFilterRequestObserver(ISaveNotifier notifier) {
		this.notifier = notifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		/*
		 * we are deletting this filter, not sure if db call actualy returns a
		 * filter? if (response.getStatusCode() == 200) { Filter filter =
		 * Filter.fromJSON(response.getBody());
		 * FilterDatabase.getInstance().add(filter); }
		 */

		ResponseModel response = iReq.getResponse();
		notifier.responseSuccess();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseError(IRequest iReq) {
		notifier.responseError(iReq.getResponse().getStatusCode(), iReq
				.getResponse().getStatusMessage());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		notifier.fail(exception);

	}

}
