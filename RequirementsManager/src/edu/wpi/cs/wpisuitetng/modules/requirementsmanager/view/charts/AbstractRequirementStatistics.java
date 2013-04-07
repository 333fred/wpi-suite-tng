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

public abstract class AbstractRequirementStatistics {

	Map<String, Integer> data;
	
	public AbstractRequirementStatistics(){
		
		this.data = new HashMap<String, Integer>();
		this.update();
		
	}
	
	/**
	 * method to force reacquisition of data
	 */
	public abstract void update();
	
	public PieDataset toPieDataset(){
		
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		
		for(String key : data.keySet()){
			pieDataset.setValue(key, data.get(key));
		}
		
		return pieDataset;
	
	}
	
	public CategoryDataset toCategoryDataset(){
		
		DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
		
		for(String key : data.keySet()){
			categoryDataset.setValue(data.get(key), "Requirements", key);
		}
		
		return categoryDataset;
		
	}
	
}
