/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Alex
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import static org.junit.Assert.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.junit.Before;
import org.junit.Test;

public class IterationRequirementsStatsTest {

	IterationRequirementStatistics data = new IterationRequirementStatistics();
	
	@Test
	public void iterationLineChart(){
		JFreeChart chart = ChartFactory.createLineChart("Requirements by Iteration", "Iteration", "Requirements", data.toCategoryDataset("Iteration"), PlotOrientation.VERTICAL, false, false, false);
		CategoryPlot xyPlot = (CategoryPlot) chart.getPlot();
		NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
		range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		assertEquals(data.buildLineChart(), chart);
	}
	
}
