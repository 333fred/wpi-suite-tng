/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

/**
 * @author Kyle
 *
 */

import static org.junit.Assert.*;

import java.util.List;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

public class IterationTest {

	Iteration i1;
	Iteration i1copy;
	Iteration i2;
	Project project;
	
	Date startDate = new Date(3, 26, 2013);
	Date startDate2 = new Date(3, 20, 2013);
	Date endDate = new Date(3, 29, 2013);
	Date endDate2 = new Date(3, 24, 2013);
	int idNum = 1;
	int idNum2 = 2;
	
	@Before
	public void setUp() {
	i1 = new Iteration(startDate, endDate, idNum);
	i1copy = new Iteration(startDate, endDate, idNum);
	i2 = new Iteration(startDate2, endDate2, idNum2);
	project = new Project("test", "1");
	}

	//TODO: Test identify once implemented
	
	//TODO: Test fromJSON once implemented
	
	@Test
	public void testSetProject() {
		i1.setProject(project);
		assertSame(project, i1.getProject());
	}
	
	@Test
	public void testSetStartDate() {
		i1.setStartDate(startDate2);
		assertSame(startDate2,i1.getStartDate());
	}
	
	@Test
	public void testSetEndDate() {
		i1.setEndDate(endDate2);
		assertSame(endDate2,i1.getEndDate());
	}
	
	@Test
	public void testIdNum() {
		i1.setIdNum(idNum2);
		assertSame(idNum2,i1.getIdNum());
	}
	
	@Test
	public void testGetStartDate() {
		assertSame(startDate,i1.getStartDate());
	}
	
	@Test
	public void testGetEndDate() {
		assertSame(endDate,i1.getEndDate());
	}
	
}
