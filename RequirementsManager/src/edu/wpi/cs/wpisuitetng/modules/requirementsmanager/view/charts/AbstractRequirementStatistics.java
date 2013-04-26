/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *	Alex Gorowara
 *	Steven Kordell
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * Abstract superclass for all requirement statistics
 */

public abstract class AbstractRequirementStatistics {
	
	protected List<StringIntegerPair> data;
		
	protected AbstractRequirementStatistics() {
		data = new ArrayList<StringIntegerPair>();
		update();
	}
	
	/**
	 * Builds the bar chart associated with this view
	 * 
	 * @return the constructed bar chart
	 */
	public abstract JFreeChart buildBarChart();
	
	protected JFreeChart buildBarChart(final String title,
			final String category, final String axisLabel) {
		final JFreeChart chart = ChartFactory.createBarChart(title, category,
				axisLabel, toCategoryDataset(category),
				PlotOrientation.VERTICAL, true, false, false);
		final CategoryPlot xyPlot = (CategoryPlot) chart.getPlot();
		final NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
		range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		return chart;
	}
	
	/**
	 * Builds the line chart associated with this view
	 * 
	 * @return the constructed line chart
	 */
	public abstract JFreeChart buildLineChart();
	
	protected JFreeChart buildLineChart(final String title,
			final String category, final String axisLabel) {
		final JFreeChart chart = ChartFactory.createLineChart(title, category,
				axisLabel, toCategoryDataset(category),
				PlotOrientation.VERTICAL, false, false, false);
		final CategoryPlot xyPlot = (CategoryPlot) chart.getPlot();
		final NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
		range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		return chart;
	}
	
	/**
	 * Builds the pie chart associated with this view
	 * 
	 * @return the constructed pie chart
	 */
	public abstract JFreeChart buildPieChart();
	
	protected JFreeChart buildPieChart(final String title) {
		return ChartFactory.createPieChart3D(title, toPieDataset(), true,
				false, false);
	}
	
	/**
	 * Returns the dataset associated with a given category
	 * 
	 * @param category
	 *            the category to look for
	 * @return the dataset
	 */
	public CategoryDataset toCategoryDataset(final String category) {
		final DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
		
		for (final StringIntegerPair pair : data) {
			//System.out.println("Adding some stuff to categoryData set: " + data.get(key) + " Key: " + key + " Category: " + category);
			categoryDataset.addValue(pair.getInterger(), category, pair.getString());
		}
		return categoryDataset;
	}
	
	/**
	 * Gets the pie dataset associated with this statistic
	 * 
	 * @return the dataset
	 */
	public PieDataset toPieDataset() {
		final DefaultPieDataset pieDataset = new DefaultPieDataset();
		for (final StringIntegerPair pair : data) {
			if (pair.getInterger() != 0) {// remove zero elements
				pieDataset.setValue(pair.getString(), pair.getInterger());
			}
		}
		return pieDataset;
	}
	
	/**
	 * method to force reacquisition of data
	 */
	public abstract void update();
	
}