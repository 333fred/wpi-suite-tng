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

import java.awt.event.ActionEvent;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts.StatView.*;

public class StatViewTest {

	StatView stats = StatView.getInstance();

	@Test
	public void testDefaultEnums() {
		assertEquals(stats.getChartType(), ChartType.PIE);
		assertEquals(stats.getChartDataType(), DataType.STATUS);
	}

	@Test
	public void setSelectedValuesTest() {
		stats.updateChartDataType(DataType.ITERATION);
		stats.updateChartType(ChartType.BAR);
		stats.setSelectedItems();
		assertEquals(stats.getChartType(), ChartType.BAR);
		assertEquals(stats.getChartDataType(), DataType.ITERATION);
	}

}
