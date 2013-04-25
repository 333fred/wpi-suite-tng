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

import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public abstract class AbstractRequirementStatistics {
	
	Map<String, Integer> data;
	
	protected AbstractRequirementStatistics() {
		data = new HashMap<String, Integer>();
		update();
	}
	
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
	
	public abstract JFreeChart buildPieChart();
	
	protected JFreeChart buildPieChart(final String title) {
		return ChartFactory.createPieChart3D(title, toPieDataset(), true,
				false, false);
	}
	
	public CategoryDataset toCategoryDataset(final String category) {
		final DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
		for (final String key : data.keySet()) {
			categoryDataset.setValue(data.get(key), category, key);
		}
		return categoryDataset;
	}
	
	public PieDataset toPieDataset() {
		final DefaultPieDataset pieDataset = new DefaultPieDataset();
		for (final String key : data.keySet()) {
			if (data.get(key) != 0) {// remove zero elements
				pieDataset.setValue(key, data.get(key));
			}
		}
		return pieDataset;
	}
	
	/**
	 * method to force reacquisition of data
	 */
	public abstract void update();
	
}