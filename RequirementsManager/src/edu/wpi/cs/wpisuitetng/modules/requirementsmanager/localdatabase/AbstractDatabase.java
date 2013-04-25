/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric Silberberg
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * Abstract implementation of a database to move common code together
 * 
 * @param <T>
 *            the model this database will support
 */

public abstract class AbstractDatabase<T extends AbstractModel> implements
		IDatabase<T> {
	
	/**
	 * A runnable that safely runs an update on the swing thread to prevent
	 * illegal state exceptions
	 */
	protected class SwingUpdate implements Runnable {
		
		IDatabaseListener listener;
		
		/**
		 * Creates a new swing update runnable
		 * 
		 * @param listener
		 */
		protected SwingUpdate(final IDatabaseListener listener) {
			this.listener = listener;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			listener.update();
		}
		
	}
	
	/** The currently registered listeners */
	protected List<IDatabaseListener> listeners;
	/** The thread that runs this class */
	protected Thread thread;
	
	/** The number of seconds between updates */
	protected int secs;
	
	/**
	 * Creates a new abstract database
	 * 
	 * @param secs
	 *            the number of seconds between updates
	 */
	protected AbstractDatabase(final int secs) {
		this.secs = secs;
		listeners = new ArrayList<IDatabaseListener>();
		thread = new Thread(this, this.getClass().getName() + "-Thread");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerListener(final IDatabaseListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeListener(final IDatabaseListener listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		if (thread == null) {
			thread = new Thread(this, this.getClass().getName() + "-Thread");
		}
		thread.setDaemon(true);
		if (thread.isAlive()) {
			return;
		}
		thread.start();
	}
	
	/**
	 * Safely calls update on a given listener
	 * 
	 * @param listener
	 *            the listener to safely update
	 */
	protected void updateListener(final IDatabaseListener listener) {
		SwingUtilities.invokeLater(new SwingUpdate(listener));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateListeners() {
		final List<IDatabaseListener> toRem = new ArrayList<IDatabaseListener>();
		// Update all listeners, and check for removal
		for (final IDatabaseListener l : listeners) {
			updateListener(l);
			if (l.shouldRemove()) {
				toRem.add(l);
			}
		}
		// Remove all expired listeners
		for (final IDatabaseListener l : toRem) {
			listeners.remove(l);
		}
	}
	
}
