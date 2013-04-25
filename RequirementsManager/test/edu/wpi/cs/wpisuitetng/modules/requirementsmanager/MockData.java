/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

/**
 * A mock data implementation for server-side testing.
 */
public class MockData implements Data {
	
	private final Set<Object> objects;
	
	/**
	 * Create a MockData instance initially containing the given set of objects
	 * 
	 * @param objects
	 *            The set of objects this "database" starts with
	 */
	public MockData(final Set<Object> objects) {
		this.objects = objects;
	}
	
	@SuppressWarnings ("rawtypes")
	@Override
	public List<Model> andRetrieve(final Class arg0, final String[] arg1,
			final List<Object> arg2) throws WPISuiteException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings ("rawtypes")
	@Override
	public List<Model> complexRetrieve(final Class arg0, final String[] arg1,
			final List<Object> arg2, final Class arg3, final String[] arg4,
			final List<Object> arg5) throws WPISuiteException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public <T> T delete(final T arg0) {
		if (objects.contains(arg0)) {
			objects.remove(arg0);
			return arg0;
		}
		return null;
	}
	
	@SuppressWarnings ("unchecked")
	@Override
	public <T> List<T> deleteAll(final T arg0) {
		final List<T> deleted = new ArrayList<T>();
		for (final Object obj : objects) {
			if (arg0.getClass().isInstance(obj)) {
				deleted.add((T) obj);
			}
		}
		// can't remove in the loop, otherwise you get an exception
		objects.removeAll(deleted);
		return deleted;
	}
	
	@Override
	public <T> List<Model> deleteAll(final T arg0, final Project arg1) {
		final List<Model> toDelete = retrieveAll(arg0, arg1);
		objects.removeAll(toDelete);
		return toDelete;
	}
	
	private List<Model> filterByProject(final List<Model> models,
			final Project project) {
		final List<Model> filteredModels = new ArrayList<Model>();
		for (final Model m : models) {
			if (m.getProject().getName().equalsIgnoreCase(project.getName())) {
				filteredModels.add(m);
			}
		}
		return filteredModels;
	}
	
	@SuppressWarnings ("rawtypes")
	@Override
	public List<Model> orRetrieve(final Class arg0, final String[] arg1,
			final List<Object> arg2) throws WPISuiteException,
			IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings ("rawtypes")
	@Override
	public List<Model> retrieve(final Class type, final String fieldName,
			final Object value) {
		final List<Model> rv = new ArrayList<Model>();
		for (final Object obj : objects) {
			if (!type.isInstance(obj)) {
				continue;
			}
			final Method[] methods = obj.getClass().getMethods();
			for (final Method method : methods) {
				if (method.getName().equalsIgnoreCase("get" + fieldName)) {
					try {
						if (method.invoke(obj).equals(value)) {
							rv.add((Model) obj);
						}
					} catch (final IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return rv;
	}
	
	@SuppressWarnings ("rawtypes")
	@Override
	public List<Model> retrieve(final Class arg0, final String arg1,
			final Object arg2, final Project arg3) throws WPISuiteException {
		return filterByProject(retrieve(arg0, arg1, arg2), arg3);
	}
	
	@SuppressWarnings ("unchecked")
	@Override
	public <T> List<T> retrieveAll(final T arg0) {
		final List<T> all = new ArrayList<T>();
		for (final Object obj : objects) {
			if (arg0.getClass().isInstance(obj)) {
				all.add((T) obj);
			}
		}
		return all;
	}
	
	@SuppressWarnings ("unchecked")
	@Override
	public <T> List<Model> retrieveAll(final T arg0, final Project arg1) {
		return filterByProject((List<Model>) retrieveAll(arg0), arg1);
	}
	
	@Override
	public <T> boolean save(final T arg0) {
		objects.add(arg0);
		return true;
	}
	
	@Override
	public <T> boolean save(final T arg0, final Project arg1) {
		((Model) arg0).setProject(arg1);
		save(arg0);
		return true;
	}
	
	@SuppressWarnings ("rawtypes")
	@Override
	public void update(final Class arg0, final String arg1, final Object arg2,
			final String arg3, final Object arg4) {
		// TODO Auto-generated method stub
		
	}
	
}
