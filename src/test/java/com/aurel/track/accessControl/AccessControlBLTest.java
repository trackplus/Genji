/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

package com.aurel.track.accessControl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.aurel.track.beans.TAccessControlListBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.workingdogs.village.Record;

/**
 * The class <code>AccessControlBLTest</code> contains tests for the class <code>{@link AccessControlBL}</code>.
 *
 * @version $Revision: 1.0 $
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(LookupContainer.class)

/**
 * NOTE: We have to run this test with Java VM argument -noverify due to some issue with PowerMock and Java 1.7
 * @author friedj
 *
 */
public class AccessControlBLTest extends Mockito {
	
	private TPersonBean getPerson(Integer oid) {
		TPersonBean person = new TPersonBean();
		person.setObjectID(oid);
		person.setFirstName("Joe");
		person.setLastName("Doe");
		return person;		
	}
	
	/**
	 * Run the void deleteByProjectRolePerson(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testDeleteByProjectRolePerson_1()
		throws Exception {
		Integer projectID = new Integer(1);
		Integer roleID = new Integer(1);
		Integer personID = new Integer(1);

		AccessControlBL.deleteByProjectRolePerson(projectID, roleID, personID);

		// add additional test code here
	}

	/**
	 * Run the Set<Integer> getPersonSetByProjectRole(Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testGetPersonSetByProjectRole_1()
		throws Exception {
		Integer projectID = new Integer(1);
		Integer roleID = new Integer(1);

		Set<Integer> result = AccessControlBL.getPersonSetByProjectRole(projectID, roleID);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<Record> getProjectIssueTypeRecords(List<Integer>,Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testGetProjectIssueTypeRecords_1()
		throws Exception {
		List<Integer> personIDs = new LinkedList();
		Integer[] projectIDs = new Integer[] {};
		int[] arrRights = new int[] {};

		List<Record> result = AccessControlBL.getProjectIssueTypeRecords(personIDs, projectIDs, arrRights);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the boolean hasPersonRightInAnyProjectWithStatusFlag(Integer,int[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testHasPersonRightInAnyProjectWithStatusFlag_1()
		throws Exception {
		Integer personID = new Integer(1);
		int[] arrRights = new int[] {};
		int[] projectStatusFlag = new int[] {};
		
		PowerMockito.mockStatic(LookupContainer.class);
		
		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});	

		boolean result = AccessControlBL.hasPersonRightInAnyProjectWithStatusFlag(personID, arrRights, projectStatusFlag);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRightInAnyProjectWithStatusFlag(Integer,int[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testHasPersonRightInAnyProjectWithStatusFlag_2()
		throws Exception {
		Integer personID = new Integer(1);
		int[] arrRights = new int[] {};
		int[] projectStatusFlag = new int[] {};
		
		PowerMockito.mockStatic(LookupContainer.class);
		
		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});	

		boolean result = AccessControlBL.hasPersonRightInAnyProjectWithStatusFlag(personID, arrRights, projectStatusFlag);

	assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRightInNonPrivateProject(Integer,int[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testHasPersonRightInNonPrivateProject_1()
		throws Exception {
		Integer personID = new Integer(1);
		int[] arrRights = new int[] {};
		
		PowerMockito.mockStatic(LookupContainer.class);
		
		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});	

		boolean result = AccessControlBL.hasPersonRightInNonPrivateProject(personID, arrRights);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRightInNonPrivateProject(Integer,int[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testHasPersonRightInNonPrivateProject_2()
		throws Exception {
		Integer personID = new Integer(1);
		int[] arrRights = new int[] {};
		
		PowerMockito.mockStatic(LookupContainer.class);
		
		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});	

		boolean result = AccessControlBL.hasPersonRightInNonPrivateProject(personID, arrRights);

		assertTrue(!result);
	}

	/**
	 * Run the List<TAccessControlListBean> loadByPerson(Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testLoadByPerson_1()
		throws Exception {
		Integer personID = new Integer(1);

		List<TAccessControlListBean> result = AccessControlBL.loadByPerson(personID);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the List<TAccessControlListBean> loadByPersonProjectsRight(List<Integer>,List<Integer>,int[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testLoadByPersonProjectsRight_1()
		throws Exception {
		List<Integer> personIDs = new LinkedList();
		List<Integer> projectIDs = new LinkedList();
		int[] rights = new int[] {};

		List<TAccessControlListBean> result = AccessControlBL.loadByPersonProjectsRight(personIDs, projectIDs, rights);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<TAccessControlListBean> loadByPersonProjectsRole(List<Integer>,List<Integer>,Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testLoadByPersonProjectsRole_1()
		throws Exception {
		List<Integer> personIDs = new LinkedList();
		List<Integer> projectIDs = new LinkedList();
		Integer role = new Integer(1);

		List<TAccessControlListBean> result = AccessControlBL.loadByPersonProjectsRole(personIDs, projectIDs, role);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<TAccessControlListBean> loadByPersonRightInAnyProjectWithStatusFlag(List<Integer>,int[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testLoadByPersonRightInAnyProjectWithStatusFlag_1()
		throws Exception {
		List<Integer> personIDs = new LinkedList();
		int[] arrRights = new int[] {};
		int[] projectStatusFlag = new int[] {};

		List<TAccessControlListBean> result = AccessControlBL.loadByPersonRightInAnyProjectWithStatusFlag(personIDs, arrRights, projectStatusFlag);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<TAccessControlListBean> loadByPersons(List<Integer>) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testLoadByPersons_1()
		throws Exception {
		List<Integer> personIDs = new LinkedList();

		List<TAccessControlListBean> result = AccessControlBL.loadByPersons(personIDs);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<TAccessControlListBean> loadByPersonsAndProjectStatusFlag(List<Integer>,int[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testLoadByPersonsAndProjectStatusFlag_1()
		throws Exception {
		List<Integer> personIDs = new LinkedList();
		int[] projectStatusFlag = new int[] {};

		List<TAccessControlListBean> result = AccessControlBL.loadByPersonsAndProjectStatusFlag(personIDs, projectStatusFlag);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<TAccessControlListBean> loadByPersonsAndProjects(List<Integer>,List<Integer>) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testLoadByPersonsAndProjects_1()
		throws Exception {
		List<Integer> selectedPersons = new LinkedList();
		List<Integer> selectedProjects = new LinkedList();

		List<TAccessControlListBean> result = AccessControlBL.loadByPersonsAndProjects(selectedPersons, selectedProjects);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<TAccessControlListBean> loadByProject(Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testLoadByProject_1()
		throws Exception {
		Integer projectID = new Integer(1);

		List<TAccessControlListBean> result = AccessControlBL.loadByProject(projectID);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the List<TAccessControlListBean> loadByProjectAndRole(Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testLoadByProjectAndRole_1()
		throws Exception {
		Integer projectID = new Integer(1);
		Integer roleID = new Integer(1);

		List<TAccessControlListBean> result = AccessControlBL.loadByProjectAndRole(projectID, roleID);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the List<TAccessControlListBean> loadByProjectsAndRights(Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testLoadByProjectsAndRights_1()
		throws Exception {
		Integer[] projectIDs = new Integer[] {};
		int[] arrRights = new int[] {};

		List<TAccessControlListBean> result = AccessControlBL.loadByProjectsAndRights(projectIDs, arrRights);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<TAccessControlListBean> loadByProjectsRolesListType(List<Integer>,Object[],Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testLoadByProjectsRolesListType_1()
		throws Exception {
		List<Integer> projectIDs = new LinkedList();
		Object[] roles = new Object[] {};
		Integer listType = new Integer(1);

		List<TAccessControlListBean> result = AccessControlBL.loadByProjectsRolesListType(projectIDs, roles, listType);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Integer loadNumberOfPersonsInRoleForProject(Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testLoadNumberOfPersonsInRoleForProject_1()
		throws Exception {
		Integer projectID = new Integer(1);
		Integer roleID = new Integer(1);

		Integer result = AccessControlBL.loadNumberOfPersonsInRoleForProject(projectID, roleID);

		// add additional test code here
		assertNotNull(result);
		assertEquals("0", result.toString());
		assertEquals((byte) 0, result.byteValue());
		assertEquals((short) 0, result.shortValue());
		assertEquals(0, result.intValue());
		assertEquals(0L, result.longValue());
		assertEquals(0.0f, result.floatValue(), 1.0f);
		assertEquals(0.0, result.doubleValue(), 1.0);
	}

	/**
	 * Run the Map<Integer, Integer> loadNumberOfPersonsInRolesForProject(Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testLoadNumberOfPersonsInRolesForProject_1()
		throws Exception {
		Integer projectID = new Integer(1);

		Map<Integer, Integer> result = AccessControlBL.loadNumberOfPersonsInRolesForProject(projectID);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the void save(TAccessControlListBean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testSave_1()
		throws Exception {
		TAccessControlListBean accessControlListBean = new TAccessControlListBean();

		AccessControlBL.save(accessControlListBean);

		// add additional test code here
	}

	/**
	 * Run the void save(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Test
	public void testSave_2()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer roleID = new Integer(1);

		AccessControlBL.save(personID, projectID, roleID);

		// add additional test code here
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@Before
	public void setUp()
		throws Exception {
		// add additional set up code here
	}

	/**
	 * Perform post-test clean-up.
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	@After
	public void tearDown()
		throws Exception {
		// Add additional tear down code here
	}

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 18.04.15 08:40
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(AccessControlBLTest.class);
	}
}
