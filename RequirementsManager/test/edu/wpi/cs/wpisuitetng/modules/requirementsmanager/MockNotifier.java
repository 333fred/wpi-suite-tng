/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	@author Fredric
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;

public class MockNotifier implements ISaveNotifier {

		private boolean success;
		private boolean error;
		private boolean failure;
		
		public MockNotifier() {
			success = false;
			error = false;
			failure = false;
		}
		
		/**
		 * @return the success
		 */
		public boolean isSuccess() {
			return success;
		}

		/**
		 * @return the error
		 */
		public boolean isError() {
			return error;
		}

		/**
		 * @return the failure
		 */
		public boolean isFailure() {
			return failure;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void responseSuccess() {
			success = true;			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void responseError(int statusCode, String statusMessage) {
			error = true;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fail(Exception exception) {
			failure = true;
		}
		
	}