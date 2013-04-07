/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Gorowara
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.util.Map;
import java.util.HashMap;

import org.jfree.data.general.PieDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public abstract class AbstractRequirementStatistics<T extends Comparable<T>>{

	Map<T, Integer> data;
	
	public AbstractRequirementStatistics(){
		
		this.data = new HashMap<T, Integer>();
		this.update();
		
	}
	
	/**
	 * method to force reacquisition of data
	 */
	public abstract void update();
	
	public PieDataset toPieDataset(){
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		for(T key : data.keySet()){
			dataset.setValue(key, data.get(key));
		}
		
		return dataset;
		
	}
	
	public CategoryDataset toCategoryDataset(){
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(T key: data.keySet()){
			dataset.setValue(data.get(key), key, key);
		}
			
		return dataset;
		
	}
	
	
	
}
