/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Alex Woodyard
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts.StatView.ChartType;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts.StatView.DataType;

public class StatViewTest {
	
	StatView stats;
	
	@Test
	public void setSelectedValuesTest() {
		stats.updateChartDataType(DataType.ITERATION);
		stats.updateChartType(ChartType.BAR);
		stats.updateSelectedItems();
		Assert.assertEquals(stats.getChartType(), ChartType.BAR);
		Assert.assertEquals(stats.getChartDataType(), DataType.ITERATION);
	}
	
	@Before
	public void setup() {
		StatView.init();
		stats = StatView.getInstance();
	}
	
	@Test
	public void testDefaultEnums() {
		Assert.assertEquals(stats.getChartType(), ChartType.PIE);
		Assert.assertEquals(stats.getChartDataType(), DataType.STATUS);
	}
	
}
