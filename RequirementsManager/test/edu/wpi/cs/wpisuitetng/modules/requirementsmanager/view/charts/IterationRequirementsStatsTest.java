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

import static org.junit.Assert.assertEquals;

import org.jfree.chart.ChartFactory;
import org.junit.Test;

public class IterationRequirementsStatsTest {

	IterationRequirementStatistics data = new IterationRequirementStatistics();

	@Test
	public void iterationPieChart() {
		assertEquals(ChartFactory.createPieChart3D("Requirements by Iteration",	data.toPieDataset(), true, false, false),
				data.buildPieChart());
	}
}
