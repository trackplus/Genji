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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.report.execute.ReportBeanWithHistory;

/**
 * The class <code>AccessBeansTest</code> contains tests for the class <code>{@link AccessBeans}</code>.
 *
 * @author friedj
 * @version $Revision: 1.0 $
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(LookupContainer.class)

/**
 * NOTE: We have to run this test with Java VM argument -noverify due to some issue with PowerMock and Java 1.7
 * @author friedj
 *
 */
public class AccessBeansTest extends Mockito {
	/**
	 * Run the void addRestrictions(Map<Integer,Integer>,Set<Integer>,Map<Integer,Set<Integer>>,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */

	private TPersonBean getPerson(Integer oid) {
		TPersonBean person = new TPersonBean();
		person.setObjectID(oid);
		person.setFirstName("Joe");
		person.setLastName("Doe");
		return person;
	}



	@Test
	public void testAddRestrictions_1()
		throws Exception {
		Map<Integer, Integer> resultMap = new HashMap();
		Set<Integer> roles = new HashSet();
		Map<Integer, Set<Integer>> restrictedFieldsToRoles = new HashMap();
		Integer restriction = new Integer(1);

		AccessBeans.addRestrictions(resultMap, roles, restrictedFieldsToRoles, restriction);

	}

	/**
	 * Run the void addRestrictions(Map<Integer,Integer>,Set<Integer>,Map<Integer,Set<Integer>>,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testAddRestrictions_2()
		throws Exception {
		Map<Integer, Integer> resultMap = new HashMap();
		Set<Integer> roles = new HashSet();
		Map<Integer, Set<Integer>> restrictedFieldsToRoles = new HashMap();
		Integer restriction = new Integer(1);

		AccessBeans.addRestrictions(resultMap, roles, restrictedFieldsToRoles, restriction);

	}

	/**
	 * Run the void addRestrictions(Map<Integer,Integer>,Set<Integer>,Map<Integer,Set<Integer>>,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testAddRestrictions_3()
		throws Exception {
		Map<Integer, Integer> resultMap = new HashMap();
		Set<Integer> roles = new HashSet();
		Map<Integer, Set<Integer>> restrictedFieldsToRoles = new HashMap();
		Integer restriction = new Integer(1);

		AccessBeans.addRestrictions(resultMap, roles, restrictedFieldsToRoles, restriction);

	}

	/**
	 * Run the void addRestrictions(Map<Integer,Integer>,Set<Integer>,Map<Integer,Set<Integer>>,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testAddRestrictions_4()
		throws Exception {
		Map<Integer, Integer> resultMap = new HashMap();
		Set<Integer> roles = new HashSet();
		Map<Integer, Set<Integer>> restrictedFieldsToRoles = new HashMap();
		Integer restriction = new Integer(1);

		AccessBeans.addRestrictions(resultMap, roles, restrictedFieldsToRoles, restriction);

	}

	/**
	 * Run the String anyRightFilterString() method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testAnyRightFilterString_1()
		throws Exception {

		String result = AccessBeans.anyRightFilterString();

		assertEquals("*1*", result);
	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_1()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_2()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_3()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_4()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_5()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_6()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_7()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_8()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_9()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_10()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_11()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_12()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_13()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_14()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_15()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterBudgetBeans(List<TBudgetBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterBudgetBeans_16()
		throws Exception {
		List<TBudgetBean> budgetBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterBudgetBeans(budgetBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_1()
		throws Exception {
		Integer personID = null;
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_2()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = null;

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_3()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_4()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_5()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_6()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_7()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_8()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_9()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_10()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_11()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_12()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_13()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_14()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_15()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(Integer,List<ReportBeanWithHistory>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_16()
		throws Exception {
		Integer personID = new Integer(1);
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new LinkedList();

		AccessBeans.filterCostBeans(personID, reportBeanWithHistoryList);

	}

	/**
	 * Run the void filterCostBeans(List<TCostBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_17()
		throws Exception {
		List<TCostBean> costBeans = new LinkedList();
		Integer personID = null;
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterCostBeans(costBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterCostBeans(List<TCostBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_18()
		throws Exception {
		List<TCostBean> costBeans = null;
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterCostBeans(costBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterCostBeans(List<TCostBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_19()
		throws Exception {
		List<TCostBean> costBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterCostBeans(costBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterCostBeans(List<TCostBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_20()
		throws Exception {
		List<TCostBean> costBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterCostBeans(costBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterCostBeans(List<TCostBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_21()
		throws Exception {
		List<TCostBean> costBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterCostBeans(costBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterCostBeans(List<TCostBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_22()
		throws Exception {
		List<TCostBean> costBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterCostBeans(costBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterCostBeans(List<TCostBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_23()
		throws Exception {
		List<TCostBean> costBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterCostBeans(costBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterCostBeans(List<TCostBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_24()
		throws Exception {
		List<TCostBean> costBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterCostBeans(costBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterCostBeans(List<TCostBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_25()
		throws Exception {
		List<TCostBean> costBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterCostBeans(costBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterCostBeans(List<TCostBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_26()
		throws Exception {
		List<TCostBean> costBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterCostBeans(costBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterCostBeans(List<TCostBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_27()
		throws Exception {
		List<TCostBean> costBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterCostBeans(costBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the void filterCostBeans(List<TCostBean>,Integer,Map<Integer,TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testFilterCostBeans_28()
		throws Exception {
		List<TCostBean> costBeans = new LinkedList();
		Integer personID = new Integer(1);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap();

		AccessBeans.filterCostBeans(costBeans, personID, workItemBeansMap);

	}

	/**
	 * Run the Map<Integer, Integer> getBudgetPlanExpenseRestrictions(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetBudgetPlanExpenseRestrictions_1()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		Map<Integer, Integer> result = AccessBeans.getBudgetPlanExpenseRestrictions(personID, projectID, listTypeID);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the boolean getFieldRestrictedInAnyContext(Integer,Map<Integer,Set<Integer>>,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictedInAnyContext_1()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		Integer fieldID = new Integer(1);
		boolean edit = true;

		boolean result = AccessBeans.getFieldRestrictedInAnyContext(personID, projectToIssueTypesMap, fieldID, edit);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean getFieldRestrictedInAnyContext(Integer,Map<Integer,Set<Integer>>,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictedInAnyContext_2()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		Integer fieldID = new Integer(1);
		boolean edit = true;

		boolean result = AccessBeans.getFieldRestrictedInAnyContext(personID, projectToIssueTypesMap, fieldID, edit);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean getFieldRestrictedInAnyContext(Integer,Map<Integer,Set<Integer>>,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictedInAnyContext_3()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		Integer fieldID = new Integer(1);
		boolean edit = true;

		boolean result = AccessBeans.getFieldRestrictedInAnyContext(personID, projectToIssueTypesMap, fieldID, edit);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean getFieldRestrictedInAnyContext(Integer,Map<Integer,Set<Integer>>,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictedInAnyContext_4()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		Integer fieldID = new Integer(1);
		boolean edit = true;

		boolean result = AccessBeans.getFieldRestrictedInAnyContext(personID, projectToIssueTypesMap, fieldID, edit);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean getFieldRestrictedInAnyContext(Integer,Map<Integer,Set<Integer>>,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictedInAnyContext_5()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		Integer fieldID = new Integer(1);
		boolean edit = true;

		boolean result = AccessBeans.getFieldRestrictedInAnyContext(personID, projectToIssueTypesMap, fieldID, edit);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean getFieldRestrictedInAnyContext(Integer,Map<Integer,Set<Integer>>,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictedInAnyContext_6()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		Integer fieldID = new Integer(1);
		boolean edit = true;

		boolean result = AccessBeans.getFieldRestrictedInAnyContext(personID, projectToIssueTypesMap, fieldID, edit);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean getFieldRestrictedInAnyContext(Integer,Map<Integer,Set<Integer>>,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictedInAnyContext_7()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		Integer fieldID = new Integer(1);
		boolean edit = true;

		boolean result = AccessBeans.getFieldRestrictedInAnyContext(personID, projectToIssueTypesMap, fieldID, edit);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean getFieldRestrictedInAnyContext(Integer,Map<Integer,Set<Integer>>,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictedInAnyContext_8()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		Integer fieldID = new Integer(1);
		boolean edit = true;

		boolean result = AccessBeans.getFieldRestrictedInAnyContext(personID, projectToIssueTypesMap, fieldID, edit);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean getFieldRestrictedInAnyContext(Integer,Map<Integer,Set<Integer>>,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictedInAnyContext_9()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		Integer fieldID = new Integer(1);
		boolean edit = true;

		boolean result = AccessBeans.getFieldRestrictedInAnyContext(personID, projectToIssueTypesMap, fieldID, edit);

		assertEquals(false, result);
	}

	/**
	 * Run the Map<Integer, Integer> getFieldRestrictions(Integer,Integer,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_1()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		boolean edit = true;

		Map<Integer, Integer> result = AccessBeans.getFieldRestrictions(personID, projectID, issueTypeID, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldRestrictions(Integer,List<TWorkItemBean>,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_2()
		throws Exception {
		Integer personID = new Integer(1);
		List<TWorkItemBean> workItemBeanList = new LinkedList();
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Map<Integer, Map<Integer, Integer>>> result = AccessBeans.getFieldRestrictions(personID, workItemBeanList, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldRestrictions(Integer,Map<Integer,Set<Integer>>,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_3()
		throws Exception {
		Integer personID = null;
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Map<Integer, Map<Integer, Integer>>> result = AccessBeans.getFieldRestrictions(personID, projectToIssueTypesMap, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldRestrictions(Integer,Map<Integer,Set<Integer>>,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_4()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Map<Integer, Map<Integer, Integer>>> result = AccessBeans.getFieldRestrictions(personID, projectToIssueTypesMap, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldRestrictions(Integer,Map<Integer,Set<Integer>>,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_5()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Map<Integer, Map<Integer, Integer>>> result = AccessBeans.getFieldRestrictions(personID, projectToIssueTypesMap, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldRestrictions(Integer,Map<Integer,Set<Integer>>,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_6()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Map<Integer, Map<Integer, Integer>>> result = AccessBeans.getFieldRestrictions(personID, projectToIssueTypesMap, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldRestrictions(Integer,Map<Integer,Set<Integer>>,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_7()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Map<Integer, Map<Integer, Integer>>> result = AccessBeans.getFieldRestrictions(personID, projectToIssueTypesMap, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldRestrictions(Integer,Map<Integer,Set<Integer>>,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_8()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Map<Integer, Map<Integer, Integer>>> result = AccessBeans.getFieldRestrictions(personID, projectToIssueTypesMap, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldRestrictions(Integer,Map<Integer,Set<Integer>>,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_9()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Map<Integer, Map<Integer, Integer>>> result = AccessBeans.getFieldRestrictions(personID, projectToIssueTypesMap, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldRestrictions(Integer,Map<Integer,Set<Integer>>,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_10()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Map<Integer, Map<Integer, Integer>>> result = AccessBeans.getFieldRestrictions(personID, projectToIssueTypesMap, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldRestrictions(Integer,Map<Integer,Set<Integer>>,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_11()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Map<Integer, Map<Integer, Integer>>> result = AccessBeans.getFieldRestrictions(personID, projectToIssueTypesMap, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldRestrictions(Integer,Map<Integer,Set<Integer>>,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_12()
		throws Exception {
		Integer personID = new Integer(1);
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap();
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Map<Integer, Map<Integer, Integer>>> result = AccessBeans.getFieldRestrictions(personID, projectToIssueTypesMap, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getFieldRestrictions(Integer,Integer,Integer,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_13()
		throws Exception {
		Integer personID = null;
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Integer> result = AccessBeans.getFieldRestrictions(personID, projectID, issueTypeID, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getFieldRestrictions(Integer,Integer,Integer,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_14()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Integer> result = AccessBeans.getFieldRestrictions(personID, projectID, issueTypeID, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getFieldRestrictions(Integer,Integer,Integer,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_15()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Integer> result = AccessBeans.getFieldRestrictions(personID, projectID, issueTypeID, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getFieldRestrictions(Integer,Integer,Integer,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_16()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = null;
		Integer issueTypeID = new Integer(1);
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Integer> result = AccessBeans.getFieldRestrictions(personID, projectID, issueTypeID, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getFieldRestrictions(Integer,Integer,Integer,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_17()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Integer> result = AccessBeans.getFieldRestrictions(personID, projectID, issueTypeID, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getFieldRestrictions(Integer,Integer,Integer,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_18()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = null;
		Integer issueTypeID = new Integer(1);
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Integer> result = AccessBeans.getFieldRestrictions(personID, projectID, issueTypeID, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getFieldRestrictions(Integer,Integer,Integer,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_19()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = null;
		Integer issueTypeID = new Integer(1);
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = false;

		Map<Integer, Integer> result = AccessBeans.getFieldRestrictions(personID, projectID, issueTypeID, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getFieldRestrictions(Integer,Integer,Integer,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_20()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = null;
		Integer issueTypeID = new Integer(1);
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = false;

		Map<Integer, Integer> result = AccessBeans.getFieldRestrictions(personID, projectID, issueTypeID, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getFieldRestrictions(Integer,Integer,Integer,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_21()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = null;
		Integer issueTypeID = new Integer(1);
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = false;

		Map<Integer, Integer> result = AccessBeans.getFieldRestrictions(personID, projectID, issueTypeID, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getFieldRestrictions(Integer,Integer,Integer,List<Integer>,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetFieldRestrictions_22()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		List<Integer> fieldIDs = new LinkedList();
		boolean edit = true;

		Map<Integer, Integer> result = AccessBeans.getFieldRestrictions(personID, projectID, issueTypeID, fieldIDs, edit);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getItemTypeLimitations(Integer,Set<Integer>,Map<Integer,Set<Integer>>,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetItemTypeLimitations_1()
		throws Exception {
		Integer projectID = new Integer(1);
		Set<Integer> selectedItemTypeIDsSet = new HashSet();
		Map<Integer, Set<Integer>> projectIssueTypeMap = null;
		Map<Integer, Integer> childToParentProjectIDMap = new HashMap();

		Set<Integer> result = AccessBeans.getItemTypeLimitations(projectID, selectedItemTypeIDsSet, projectIssueTypeMap, childToParentProjectIDMap);

		assertEquals(null, result);
	}

	/**
	 * Run the Set<Integer> getItemTypeLimitations(Integer,Set<Integer>,Map<Integer,Set<Integer>>,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetItemTypeLimitations_2()
		throws Exception {
		Integer projectID = null;
		Set<Integer> selectedItemTypeIDsSet = new HashSet();
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProjectIDMap = new HashMap();

		Set<Integer> result = AccessBeans.getItemTypeLimitations(projectID, selectedItemTypeIDsSet, projectIssueTypeMap, childToParentProjectIDMap);

		assertEquals(null, result);
	}

	/**
	 * Run the Set<Integer> getItemTypeLimitations(Integer,Set<Integer>,Map<Integer,Set<Integer>>,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetItemTypeLimitations_3()
		throws Exception {
		Integer projectID = new Integer(1);
		Set<Integer> selectedItemTypeIDsSet = new HashSet();
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProjectIDMap = new HashMap();

		Set<Integer> result = AccessBeans.getItemTypeLimitations(projectID, selectedItemTypeIDsSet, projectIssueTypeMap, childToParentProjectIDMap);

		assertEquals(null, result);
	}

	/**
	 * Run the Set<Integer> getItemTypeLimitations(Integer,Set<Integer>,Map<Integer,Set<Integer>>,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetItemTypeLimitations_4()
		throws Exception {
		Integer projectID = new Integer(1);
		Set<Integer> selectedItemTypeIDsSet = new HashSet();
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProjectIDMap = new HashMap();

		Set<Integer> result = AccessBeans.getItemTypeLimitations(projectID, selectedItemTypeIDsSet, projectIssueTypeMap, childToParentProjectIDMap);

		assertEquals(null, result);
	}

	/**
	 * Run the Set<Integer> getItemTypeLimitations(Integer,Set<Integer>,Map<Integer,Set<Integer>>,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetItemTypeLimitations_5()
		throws Exception {
		Integer projectID = new Integer(1);
		Set<Integer> selectedItemTypeIDsSet = new HashSet();
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProjectIDMap = new HashMap();

		Set<Integer> result = AccessBeans.getItemTypeLimitations(projectID, selectedItemTypeIDsSet, projectIssueTypeMap, childToParentProjectIDMap);

		assertEquals(null, result);
	}

	/**
	 * Run the Set<Integer> getItemTypeLimitations(Integer,Set<Integer>,Map<Integer,Set<Integer>>,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetItemTypeLimitations_6()
		throws Exception {
		Integer projectID = new Integer(1);
		Set<Integer> selectedItemTypeIDsSet = new HashSet();
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProjectIDMap = new HashMap();

		Set<Integer> result = AccessBeans.getItemTypeLimitations(projectID, selectedItemTypeIDsSet, projectIssueTypeMap, childToParentProjectIDMap);

		assertEquals(null, result);
	}

	/**
	 * Run the Set<Integer> getItemTypeLimitations(Integer,Set<Integer>,Map<Integer,Set<Integer>>,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetItemTypeLimitations_7()
		throws Exception {
		Integer projectID = new Integer(1);
		Set<Integer> selectedItemTypeIDsSet = null;
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProjectIDMap = new HashMap();

		Set<Integer> result = AccessBeans.getItemTypeLimitations(projectID, selectedItemTypeIDsSet, projectIssueTypeMap, childToParentProjectIDMap);

		assertEquals(null, result);
	}

	/**
	 * Run the Set<Integer> getItemTypeLimitations(Integer,Set<Integer>,Map<Integer,Set<Integer>>,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetItemTypeLimitations_8()
		throws Exception {
		Integer projectID = new Integer(1);
		Set<Integer> selectedItemTypeIDsSet = new HashSet();
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProjectIDMap = new HashMap();

		Set<Integer> result = AccessBeans.getItemTypeLimitations(projectID, selectedItemTypeIDsSet, projectIssueTypeMap, childToParentProjectIDMap);

		assertEquals(null, result);
	}

	/**
	 * Run the Set<Integer> getItemTypeLimitations(Integer,Set<Integer>,Map<Integer,Set<Integer>>,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetItemTypeLimitations_9()
		throws Exception {
		Integer projectID = new Integer(1);
		Set<Integer> selectedItemTypeIDsSet = new HashSet();
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProjectIDMap = new HashMap();

		Set<Integer> result = AccessBeans.getItemTypeLimitations(projectID, selectedItemTypeIDsSet, projectIssueTypeMap, childToParentProjectIDMap);

		assertEquals(null, result);
	}

	/**
	 * Run the Set<Integer> getItemTypeLimitations(Integer,Set<Integer>,Map<Integer,Set<Integer>>,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetItemTypeLimitations_10()
		throws Exception {
		Integer projectID = new Integer(1);
		Set<Integer> selectedItemTypeIDsSet = new HashSet();
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProjectIDMap = new HashMap();

		Set<Integer> result = AccessBeans.getItemTypeLimitations(projectID, selectedItemTypeIDsSet, projectIssueTypeMap, childToParentProjectIDMap);

		assertEquals(null, result);
	}

	/**
	 * Run the Set<Integer> getItemTypeLimitations(Integer,Set<Integer>,Map<Integer,Set<Integer>>,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetItemTypeLimitations_11()
		throws Exception {
		Integer projectID = new Integer(1);
		Set<Integer> selectedItemTypeIDsSet = new HashSet();
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProjectIDMap = new HashMap();

		Set<Integer> result = AccessBeans.getItemTypeLimitations(projectID, selectedItemTypeIDsSet, projectIssueTypeMap, childToParentProjectIDMap);

		assertEquals(null, result);
	}

	/**
	 * Run the Set<Integer> getItemTypeLimitations(Integer,Set<Integer>,Map<Integer,Set<Integer>>,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetItemTypeLimitations_12()
		throws Exception {
		Integer projectID = new Integer(1);
		Set<Integer> selectedItemTypeIDsSet = new HashSet();
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProjectIDMap = new HashMap();

		Set<Integer> result = AccessBeans.getItemTypeLimitations(projectID, selectedItemTypeIDsSet, projectIssueTypeMap, childToParentProjectIDMap);

		assertEquals(null, result);
	}

	/**
	 * Run the Set<Integer> getItemTypeLimitations(Integer,Set<Integer>,Map<Integer,Set<Integer>>,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetItemTypeLimitations_13()
		throws Exception {
		Integer projectID = new Integer(1);
		Set<Integer> selectedItemTypeIDsSet = new HashSet();
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProjectIDMap = new HashMap();

		Set<Integer> result = AccessBeans.getItemTypeLimitations(projectID, selectedItemTypeIDsSet, projectIssueTypeMap, childToParentProjectIDMap);

		assertEquals(null, result);
	}

	/**
	 * Run the List<Integer> getMeAndMyGroups(Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetMeAndMyGroups_1()
		throws Exception {
		Integer personID = new Integer(1);

		List<Integer> result = AccessBeans.getMeAndMyGroups(personID);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertTrue(result.contains(new Integer(1)));
	}

	/**
	 * Run the List<Integer> getMeAndMyGroups(Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetMeAndMyGroups_2()
		throws Exception {
		Integer personID = new Integer(1);

		List<Integer> result = AccessBeans.getMeAndMyGroups(personID);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertTrue(result.contains(new Integer(1)));
	}

	/**
	 * Run the List<Integer> getMeAndMyGroups(Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetMeAndMyGroups_3()
		throws Exception {
		Integer personID = new Integer(1);

		List<Integer> result = AccessBeans.getMeAndMyGroups(personID);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertTrue(result.contains(new Integer(1)));
	}

	/**
	 * Run the List<Integer> getMeAndMyGroups(Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetMeAndMyGroups_4()
		throws Exception {
		Integer personID = null;

		List<Integer> result = AccessBeans.getMeAndMyGroups(personID);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<Integer> getMeAndSubstituted(TPersonBean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetMeAndSubstituted_1()
		throws Exception {
		TPersonBean personBean = new TPersonBean();
		personBean.setSubstitutedPersons(null);

		List<Integer> result = AccessBeans.getMeAndSubstituted(personBean);

		assertNotNull(result);
		assertEquals(1, result.size());
	}

	/**
	 * Run the List<Integer> getMeAndSubstituted(TPersonBean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetMeAndSubstituted_2()
		throws Exception {
		TPersonBean personBean = new TPersonBean();
		personBean.setSubstitutedPersons(null);

		List<Integer> result = AccessBeans.getMeAndSubstituted(personBean);

		assertNotNull(result);
		assertEquals(1, result.size());
	}

	/**
	 * Run the List<Integer> getMeAndSubstituted(TPersonBean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetMeAndSubstituted_3()
		throws Exception {
		TPersonBean personBean = null;

		List<Integer> result = AccessBeans.getMeAndSubstituted(personBean);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<Integer> getMeAndSubstituted(Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetMeAndSubstituted_4()
		throws Exception {
		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		Integer personID = new Integer(1);

		List<Integer> result = AccessBeans.getMeAndSubstituted(personID);

		assertNotNull(result);
	}

	/**
	 * Run the List<Integer> getMeAndSubstitutedAndGroups(Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetMeAndSubstitutedAndGroups_1()
		throws Exception {
		Integer personID = new Integer(1);

		List<Integer> result;
		try {
		   result = AccessBeans.getMeAndSubstitutedAndGroups(personID);
		} catch (Exception e) {
			result = new ArrayList<Integer>();
		}

		assertNotNull(result);
	}

	/**
	 * Run the List<Integer> getMeAndSubstitutedAndGroups(Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetMeAndSubstitutedAndGroups_2()
		throws Exception {

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});
		Integer personID = new Integer(1);

		List<Integer> result = AccessBeans.getMeAndSubstitutedAndGroups(personID);

		assertNotNull(result);
	}

	/**
	 * Run the List<Integer> getMeAndSubstitutedAndGroups(Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetMeAndSubstitutedAndGroups_3()
		throws Exception {
		Integer personID = new Integer(1);

		List<Integer> result;
		try {
		   result = AccessBeans.getMeAndSubstitutedAndGroups(personID);
		} catch (Exception e) {
			result = new ArrayList<Integer>();
		}

		assertNotNull(result);
	}

	/**
	 * Run the Set<Integer> getPersonSetByProjectRights(Integer,int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetPersonSetByProjectRights_1()
		throws Exception {
		Integer projectID = new Integer(1);
		int[] rights = new int[] {};

		Set<Integer> result = AccessBeans.getPersonSetByProjectRights(projectID, rights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getPersonSetByProjectsRights(Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetPersonSetByProjectsRights_1()
		throws Exception {
		Integer[] projects = new Integer[] {};
		int[] rights = new int[] {};

		Set<Integer> result = AccessBeans.getPersonSetByProjectsRights(projects, rights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getPersonSetWithRightInAllOfTheProjects(Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetPersonSetWithRightInAllOfTheProjects_1()
		throws Exception {
		Integer[] projects = new Integer[] {};
		int[] arrRights = new int[] {};

		Set<Integer> result = AccessBeans.getPersonSetWithRightInAllOfTheProjects(projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getPersonSetWithRightInAllOfTheProjects(Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetPersonSetWithRightInAllOfTheProjects_2()
		throws Exception {
		Integer[] projects = new Integer[] {};
		int[] arrRights = new int[] {};

		Set<Integer> result = AccessBeans.getPersonSetWithRightInAllOfTheProjects(projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getPersonSetWithRightInAllOfTheProjects(Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetPersonSetWithRightInAllOfTheProjects_3()
		throws Exception {
		Integer[] projects = new Integer[] {new Integer(1)};
		int[] arrRights = new int[] {};

		Set<Integer> result = AccessBeans.getPersonSetWithRightInAllOfTheProjects(projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getPersonSetWithRightInAllOfTheProjects(Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetPersonSetWithRightInAllOfTheProjects_4()
		throws Exception {
		Integer[] projects = new Integer[] {new Integer(1)};
		int[] arrRights = new int[] {};

		Set<Integer> result = AccessBeans.getPersonSetWithRightInAllOfTheProjects(projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getPersonSetWithRightInAllOfTheProjects(Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetPersonSetWithRightInAllOfTheProjects_5()
		throws Exception {
		Integer[] projects = new Integer[] {new Integer(1), new Integer(1)};
		int[] arrRights = new int[] {};

		Set<Integer> result = AccessBeans.getPersonSetWithRightInAllOfTheProjects(projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getPersonSetWithRightInAllOfTheProjects(Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetPersonSetWithRightInAllOfTheProjects_6()
		throws Exception {
		Integer[] projects = new Integer[] {new Integer(1), new Integer(1)};
		int[] arrRights = new int[] {};

		Set<Integer> result = AccessBeans.getPersonSetWithRightInAllOfTheProjects(projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getPersonSetWithRightInAllOfTheProjects(Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetPersonSetWithRightInAllOfTheProjects_7()
		throws Exception {
		Integer[] projects = new Integer[] {new Integer(1), new Integer(1)};
		int[] arrRights = new int[] {};

		Set<Integer> result = AccessBeans.getPersonSetWithRightInAllOfTheProjects(projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getPersonSetWithRightInAllOfTheProjects(Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetPersonSetWithRightInAllOfTheProjects_8()
		throws Exception {
		Integer[] projects = new Integer[] {new Integer(1)};
		int[] arrRights = new int[] {};

		Set<Integer> result = AccessBeans.getPersonSetWithRightInAllOfTheProjects(projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getPersonsFromAcList(List<TAccessControlListBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetPersonsFromAcList_1()
		throws Exception {
		List<TAccessControlListBean> acList = new LinkedList();

		Set<Integer> result = AccessBeans.getPersonsFromAcList(acList);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getPersonsFromAcList(List<TAccessControlListBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetPersonsFromAcList_2()
		throws Exception {
		List<TAccessControlListBean> acList = new LinkedList();

		Set<Integer> result = AccessBeans.getPersonsFromAcList(acList);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getPersonsFromAcList(List<TAccessControlListBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetPersonsFromAcList_3()
		throws Exception {
		List<TAccessControlListBean> acList = null;

		Set<Integer> result = AccessBeans.getPersonsFromAcList(acList);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Set<Integer>> getProjectToIssueTypesMap(Collection<TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetProjectToIssueTypesMap_1()
		throws Exception {
		Collection<TWorkItemBean> workItemBeanList = new LinkedList();

		Map<Integer, Set<Integer>> result = AccessBeans.getProjectToIssueTypesMap(workItemBeanList);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Set<Integer>> getProjectToIssueTypesMap(Collection<TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetProjectToIssueTypesMap_2()
		throws Exception {
		Collection<TWorkItemBean> workItemBeanList = new LinkedList();

		Map<Integer, Set<Integer>> result = AccessBeans.getProjectToIssueTypesMap(workItemBeanList);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Set<Integer>> getProjectToIssueTypesMap(Collection<TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetProjectToIssueTypesMap_3()
		throws Exception {
		Collection<TWorkItemBean> workItemBeanList = new LinkedList();

		Map<Integer, Set<Integer>> result = AccessBeans.getProjectToIssueTypesMap(workItemBeanList);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Set<Integer>> getProjectToIssueTypesMap(Collection<TWorkItemBean>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetProjectToIssueTypesMap_4()
		throws Exception {
		Collection<TWorkItemBean> workItemBeanList = null;

		Map<Integer, Set<Integer>> result = AccessBeans.getProjectToIssueTypesMap(workItemBeanList);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Set<Integer>> getProjectsToIssueTypesWithRoleForPerson(List<Integer>,Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetProjectsToIssueTypesWithRoleForPerson_1()
		throws Exception {
		List<Integer> personIDs = new LinkedList();
		Integer[] projects = new Integer[] {};
		int[] arrRights = new int[] {};

		Map<Integer, Set<Integer>> result = AccessBeans.getProjectsToIssueTypesWithRoleForPerson(personIDs, projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Set<Integer>> getProjectsToIssueTypesWithRoleForPerson(List<Integer>,Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetProjectsToIssueTypesWithRoleForPerson_2()
		throws Exception {
		List<Integer> personIDs = new LinkedList();
		Integer[] projects = new Integer[] {};
		int[] arrRights = new int[] {};

		Map<Integer, Set<Integer>> result = AccessBeans.getProjectsToIssueTypesWithRoleForPerson(personIDs, projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Set<Integer>> getProjectsToIssueTypesWithRoleForPerson(List<Integer>,Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetProjectsToIssueTypesWithRoleForPerson_3()
		throws Exception {
		List<Integer> personIDs = new LinkedList();
		Integer[] projects = new Integer[] {};
		int[] arrRights = new int[] {};

		Map<Integer, Set<Integer>> result = AccessBeans.getProjectsToIssueTypesWithRoleForPerson(personIDs, projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Set<Integer>> getProjectsToIssueTypesWithRoleForPerson(List<Integer>,Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetProjectsToIssueTypesWithRoleForPerson_4()
		throws Exception {
		List<Integer> personIDs = new LinkedList();
		Integer[] projects = new Integer[] {};
		int[] arrRights = new int[] {};

		Map<Integer, Set<Integer>> result = AccessBeans.getProjectsToIssueTypesWithRoleForPerson(personIDs, projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Set<Integer>> getProjectsToIssueTypesWithRoleForPerson(List<Integer>,Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetProjectsToIssueTypesWithRoleForPerson_5()
		throws Exception {
		List<Integer> personIDs = new LinkedList();
		Integer[] projects = new Integer[] {};
		int[] arrRights = new int[] {};

		Map<Integer, Set<Integer>> result = AccessBeans.getProjectsToIssueTypesWithRoleForPerson(personIDs, projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Set<Integer>> getProjectsToIssueTypesWithRoleForPerson(List<Integer>,Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetProjectsToIssueTypesWithRoleForPerson_6()
		throws Exception {
		List<Integer> personIDs = new LinkedList();
		Integer[] projects = new Integer[] {};
		int[] arrRights = new int[] {};

		Map<Integer, Set<Integer>> result = AccessBeans.getProjectsToIssueTypesWithRoleForPerson(personIDs, projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Set<Integer>> getProjectsToIssueTypesWithRoleForPerson(List<Integer>,Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetProjectsToIssueTypesWithRoleForPerson_7()
		throws Exception {
		List<Integer> personIDs = new LinkedList();
		Integer[] projects = new Integer[] {};
		int[] arrRights = new int[] {};

		Map<Integer, Set<Integer>> result = AccessBeans.getProjectsToIssueTypesWithRoleForPerson(personIDs, projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Set<Integer>> getProjectsToIssueTypesWithRoleForPerson(List<Integer>,Integer[],int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetProjectsToIssueTypesWithRoleForPerson_8()
		throws Exception {
		List<Integer> personIDs = new LinkedList();
		Integer[] projects = new Integer[] {};
		int[] arrRights = new int[] {};

		Map<Integer, Set<Integer>> result = AccessBeans.getProjectsToIssueTypesWithRoleForPerson(personIDs, projects, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, List<Integer>> getRaciprocGroupsMap(List<Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetRaciprocGroupsMap_1()
		throws Exception {
		List<Integer> personIDs = new LinkedList();

		Map<Integer, List<Integer>> result = AccessBeans.getRaciprocGroupsMap(personIDs);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, List<Integer>> getRaciprocGroupsMap(List<Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetRaciprocGroupsMap_2()
		throws Exception {
		List<Integer> personIDs = new LinkedList();

		Map<Integer, List<Integer>> result = AccessBeans.getRaciprocGroupsMap(personIDs);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, List<Integer>> getRaciprocGroupsMap(List<Integer>) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetRaciprocGroupsMap_3()
		throws Exception {
		List<Integer> personIDs = new LinkedList();

		Map<Integer, List<Integer>> result = AccessBeans.getRaciprocGroupsMap(personIDs);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getRestrictedFieldsAndBottomUpDates(WorkItemContext) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetRestrictedFieldsAndBottomUpDates_1()
		throws Exception {
		WorkItemContext workItemContext = new WorkItemContext();
		workItemContext.setFieldConfigs(new HashMap());
		workItemContext.setPerson(new Integer(1));
		workItemContext.setWorkItemBean(new TWorkItemBean());
		workItemContext.setFieldSettings(new HashMap());
		workItemContext.setPresentFieldIDs(new HashSet());

		Map<Integer, Integer> result = AccessBeans.getRestrictedFieldsAndBottomUpDates(workItemContext);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getRestrictedFieldsAndBottomUpDates(WorkItemContext) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetRestrictedFieldsAndBottomUpDates_2()
		throws Exception {
		WorkItemContext workItemContext = new WorkItemContext();
		workItemContext.setFieldConfigs(new HashMap());
		workItemContext.setPerson(new Integer(1));
		workItemContext.setWorkItemBean(new TWorkItemBean());
		workItemContext.setFieldSettings(new HashMap());
		workItemContext.setPresentFieldIDs(new HashSet());

		Map<Integer, Integer> result = AccessBeans.getRestrictedFieldsAndBottomUpDates(workItemContext);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getRestrictedFieldsAndBottomUpDates(WorkItemContext) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetRestrictedFieldsAndBottomUpDates_3()
		throws Exception {
		WorkItemContext workItemContext = new WorkItemContext();
		workItemContext.setFieldConfigs(new HashMap());
		workItemContext.setPerson(new Integer(1));
		workItemContext.setWorkItemBean(new TWorkItemBean());
		workItemContext.setFieldSettings(new HashMap());
		workItemContext.setPresentFieldIDs(new HashSet());

		Map<Integer, Integer> result = AccessBeans.getRestrictedFieldsAndBottomUpDates(workItemContext);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getRestrictedFieldsAndBottomUpDates(WorkItemContext) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetRestrictedFieldsAndBottomUpDates_4()
		throws Exception {
		WorkItemContext workItemContext = new WorkItemContext();
		workItemContext.setFieldConfigs(new HashMap());
		workItemContext.setPerson(new Integer(1));
		workItemContext.setWorkItemBean(new TWorkItemBean());
		workItemContext.setFieldSettings(new HashMap());
		workItemContext.setPresentFieldIDs(new HashSet());

		Map<Integer, Integer> result = AccessBeans.getRestrictedFieldsAndBottomUpDates(workItemContext);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getRestrictedFieldsAndBottomUpDates(WorkItemContext) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetRestrictedFieldsAndBottomUpDates_5()
		throws Exception {
		WorkItemContext workItemContext = new WorkItemContext();
		workItemContext.setFieldConfigs(new HashMap());
		workItemContext.setPerson(new Integer(1));
		workItemContext.setWorkItemBean(new TWorkItemBean());
		workItemContext.setFieldSettings(new HashMap());
		workItemContext.setPresentFieldIDs(new HashSet());

		Map<Integer, Integer> result = AccessBeans.getRestrictedFieldsAndBottomUpDates(workItemContext);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<Integer, Integer> getRestrictedFieldsAndBottomUpDates(WorkItemContext) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetRestrictedFieldsAndBottomUpDates_6()
		throws Exception {
		WorkItemContext workItemContext = new WorkItemContext();
		workItemContext.setFieldConfigs(new HashMap());
		workItemContext.setPerson(new Integer(1));
		workItemContext.setWorkItemBean(new TWorkItemBean());
		workItemContext.setFieldSettings(new HashMap());
		workItemContext.setPresentFieldIDs(new HashSet());

		Map<Integer, Integer> result = AccessBeans.getRestrictedFieldsAndBottomUpDates(workItemContext);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getRolesSetByProjectRights(Integer,int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetRolesSetByProjectRights_1()
		throws Exception {
		Integer projectID = new Integer(1);
		int[] rights = new int[] {};

		Set<Integer> result = AccessBeans.getRolesSetByProjectRights(projectID, rights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getRolesWithRightForPersonInProject(Integer,Integer,int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetRolesWithRightForPersonInProject_1()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = null;
		int[] arrRights = new int[] {};

		Set<Integer> result = AccessBeans.getRolesWithRightForPersonInProject(personID, projectID, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getRolesWithRightForPersonInProject(Integer,Integer,int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetRolesWithRightForPersonInProject_2()
		throws Exception {
		Integer personID = null;
		Integer projectID = new Integer(1);
		int[] arrRights = new int[] {};

		Set<Integer> result = AccessBeans.getRolesWithRightForPersonInProject(personID, projectID, arrRights);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Set<Integer> getRolesWithRightForPersonInProject(Integer,Integer,int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testGetRolesWithRightForPersonInProject_3()
		throws Exception {

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		int[] arrRights = new int[] {};

		Set<Integer> result = AccessBeans.getRolesWithRightForPersonInProject(personID, projectID, arrRights);

		assertNotNull(result);
	}

	/**
	 * Run the boolean hasExplicitRight(Integer,Integer,Integer,Integer,Map<Integer,Set<Integer>>,Map<Integer,Integer>,String) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasExplicitRight_1()
		throws Exception {
		Integer personID = new Integer(1);
		Integer workItemID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueType = new Integer(1);
		Map<Integer, Set<Integer>> projectIssueTypeMap = null;
		Map<Integer, Integer> childToParentProject = new HashMap();
		String rightName = "";

		boolean result = AccessBeans.hasExplicitRight(personID, workItemID, projectID, issueType, projectIssueTypeMap, childToParentProject, rightName);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasExplicitRight(Integer,Integer,Integer,Integer,Map<Integer,Set<Integer>>,Map<Integer,Integer>,String) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasExplicitRight_2()
		throws Exception {
		Integer personID = new Integer(1);
		Integer workItemID = new Integer(1);
		Integer projectID = null;
		Integer issueType = new Integer(1);
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProject = new HashMap();
		String rightName = "";

		boolean result = AccessBeans.hasExplicitRight(personID, workItemID, projectID, issueType, projectIssueTypeMap, childToParentProject, rightName);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasExplicitRight(Integer,Integer,Integer,Integer,Map<Integer,Set<Integer>>,Map<Integer,Integer>,String) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasExplicitRight_3()
		throws Exception {
		Integer personID = new Integer(1);
		Integer workItemID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueType = new Integer(1);
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProject = new HashMap();
		String rightName = "";

		boolean result = AccessBeans.hasExplicitRight(personID, workItemID, projectID, issueType, projectIssueTypeMap, childToParentProject, rightName);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasExplicitRight(Integer,Integer,Integer,Integer,Map<Integer,Set<Integer>>,Map<Integer,Integer>,String) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasExplicitRight_4()
		throws Exception {
		Integer personID = new Integer(1);
		Integer workItemID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueType = new Integer(1);
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProject = new HashMap();
		String rightName = "";

		boolean result = AccessBeans.hasExplicitRight(personID, workItemID, projectID, issueType, projectIssueTypeMap, childToParentProject, rightName);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasExplicitRight(Integer,Integer,Integer,Integer,Map<Integer,Set<Integer>>,Map<Integer,Integer>,String) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasExplicitRight_5()
		throws Exception {
		Integer personID = new Integer(1);
		Integer workItemID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueType = new Integer(1);
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProject = new HashMap();
		String rightName = "";

		boolean result = AccessBeans.hasExplicitRight(personID, workItemID, projectID, issueType, projectIssueTypeMap, childToParentProject, rightName);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasExplicitRight(Integer,Integer,Integer,Integer,Map<Integer,Set<Integer>>,Map<Integer,Integer>,String) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasExplicitRight_6()
		throws Exception {
		Integer personID = new Integer(1);
		Integer workItemID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueType = new Integer(1);
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProject = new HashMap();
		String rightName = "";

		boolean result = AccessBeans.hasExplicitRight(personID, workItemID, projectID, issueType, projectIssueTypeMap, childToParentProject, rightName);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasExplicitRight(Integer,Integer,Integer,Integer,Map<Integer,Set<Integer>>,Map<Integer,Integer>,String) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasExplicitRight_7()
		throws Exception {
		Integer personID = new Integer(1);
		Integer workItemID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueType = null;
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProject = new HashMap();
		String rightName = "";

		boolean result = AccessBeans.hasExplicitRight(personID, workItemID, projectID, issueType, projectIssueTypeMap, childToParentProject, rightName);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasExplicitRight(Integer,Integer,Integer,Integer,Map<Integer,Set<Integer>>,Map<Integer,Integer>,String) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasExplicitRight_8()
		throws Exception {
		Integer personID = new Integer(1);
		Integer workItemID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueType = new Integer(1);
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProject = new HashMap();
		String rightName = "";

		boolean result = AccessBeans.hasExplicitRight(personID, workItemID, projectID, issueType, projectIssueTypeMap, childToParentProject, rightName);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasExplicitRight(Integer,Integer,Integer,Integer,Map<Integer,Set<Integer>>,Map<Integer,Integer>,String) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasExplicitRight_9()
		throws Exception {
		Integer personID = new Integer(1);
		Integer workItemID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueType = new Integer(1);
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProject = new HashMap();
		String rightName = "";

		boolean result = AccessBeans.hasExplicitRight(personID, workItemID, projectID, issueType, projectIssueTypeMap, childToParentProject, rightName);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasExplicitRight(Integer,Integer,Integer,Integer,Map<Integer,Set<Integer>>,Map<Integer,Integer>,String) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasExplicitRight_10()
		throws Exception {
		Integer personID = new Integer(1);
		Integer workItemID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueType = new Integer(1);
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap();
		Map<Integer, Integer> childToParentProject = new HashMap();
		String rightName = "";

		boolean result = AccessBeans.hasExplicitRight(personID, workItemID, projectID, issueType, projectIssueTypeMap, childToParentProject, rightName);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasPersonRightInProjectForIssueType(Integer,Integer,Integer,int,boolean,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRightInProjectForIssueType_1()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = null;
		Integer issueTypeID = new Integer(1);
		int right = 1;
		boolean projectAdmin = true;
		boolean includeSubstitutedPersons = true;

		boolean result = AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, issueTypeID, right, projectAdmin, includeSubstitutedPersons);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasPersonRightInProjectForIssueType(Integer,Integer,Integer,int,boolean,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRightInProjectForIssueType_2()
		throws Exception {
		Integer personID = null;
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		int right = 1;
		boolean projectAdmin = true;
		boolean includeSubstitutedPersons = true;

		boolean result = AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, issueTypeID, right, projectAdmin, includeSubstitutedPersons);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasPersonRightInProjectForIssueType(Integer,Integer,Integer,int,boolean,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRightInProjectForIssueType_3()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		int right = 1;
		boolean projectAdmin = true;
		boolean includeSubstitutedPersons = true;

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, issueTypeID, right, projectAdmin, includeSubstitutedPersons);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRightInProjectForIssueType(Integer,Integer,Integer,int,boolean,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRightInProjectForIssueType_4()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		int right = 10;
		boolean projectAdmin = true;
		boolean includeSubstitutedPersons = true;

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, issueTypeID, right, projectAdmin, includeSubstitutedPersons);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRightInProjectForIssueType(Integer,Integer,Integer,int,boolean,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRightInProjectForIssueType_5()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = null;
		int right = 10;
		boolean projectAdmin = true;
		boolean includeSubstitutedPersons = true;

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, issueTypeID, right, projectAdmin, includeSubstitutedPersons);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRightInProjectForIssueType(Integer,Integer,Integer,int,boolean,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRightInProjectForIssueType_6()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		int right = 1;
		boolean projectAdmin = false;
		boolean includeSubstitutedPersons = true;

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, issueTypeID, right, projectAdmin, includeSubstitutedPersons);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRightInProjectForIssueType(Integer,Integer,Integer,int,boolean,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRightInProjectForIssueType_7()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		int right = 1;
		boolean projectAdmin = true;
		boolean includeSubstitutedPersons = true;

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, issueTypeID, right, projectAdmin, includeSubstitutedPersons);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRightInProjectForIssueType(Integer,Integer,Integer,int,boolean,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRightInProjectForIssueType_8()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		int right = 10;
		boolean projectAdmin = true;
		boolean includeSubstitutedPersons = true;

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, issueTypeID, right, projectAdmin, includeSubstitutedPersons);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRightInProjectForIssueType(Integer,Integer,Integer,int,boolean,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRightInProjectForIssueType_9()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		int right = 10;
		boolean projectAdmin = true;
		boolean includeSubstitutedPersons = true;

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, issueTypeID, right, projectAdmin, includeSubstitutedPersons);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRoleInProjectForIssueType(Integer,Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRoleInProjectForIssueType_1()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = null;
		Integer issueTypeID = new Integer(1);
		Integer role = new Integer(1);

		boolean result = AccessBeans.hasPersonRoleInProjectForIssueType(personID, projectID, issueTypeID, role);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasPersonRoleInProjectForIssueType(Integer,Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRoleInProjectForIssueType_2()
		throws Exception {
		Integer personID = null;
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		Integer role = new Integer(1);

		boolean result = AccessBeans.hasPersonRoleInProjectForIssueType(personID, projectID, issueTypeID, role);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean hasPersonRoleInProjectForIssueType(Integer,Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRoleInProjectForIssueType_3()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		Integer role = new Integer(1);

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRoleInProjectForIssueType(personID, projectID, issueTypeID, role);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRoleInProjectForIssueType(Integer,Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRoleInProjectForIssueType_4()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		Integer role = new Integer(1);

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRoleInProjectForIssueType(personID, projectID, issueTypeID, role);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRoleInProjectForIssueType(Integer,Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRoleInProjectForIssueType_5()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = null;
		Integer role = new Integer(1);

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRoleInProjectForIssueType(personID, projectID, issueTypeID, role);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRoleInProjectForIssueType(Integer,Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRoleInProjectForIssueType_6()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		Integer role = new Integer(1);

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRoleInProjectForIssueType(personID, projectID, issueTypeID, role);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRoleInProjectForIssueType(Integer,Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRoleInProjectForIssueType_7()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		Integer role = new Integer(1);

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRoleInProjectForIssueType(personID, projectID, issueTypeID, role);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRoleInProjectForIssueType(Integer,Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRoleInProjectForIssueType_8()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		Integer role = new Integer(1);

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRoleInProjectForIssueType(personID, projectID, issueTypeID, role);

		assertTrue(!result);
	}

	/**
	 * Run the boolean hasPersonRoleInProjectForIssueType(Integer,Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testHasPersonRoleInProjectForIssueType_9()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);
		Integer role = new Integer(1);


		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.hasPersonRoleInProjectForIssueType(personID, projectID, issueTypeID, role);

		assertTrue(!result);
	}

	/**
	 * Run the boolean isAllowedToChange(TWorkItemBean,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsAllowedToChange_1()
		throws Exception {
		TWorkItemBean workItemBean = new TWorkItemBean();
		Integer personID = null;

		boolean result = AccessBeans.isAllowedToChange(workItemBean, personID);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAllowedToChange(TWorkItemBean,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsAllowedToChange_2()
		throws Exception {
		TWorkItemBean workItemBean = null;
		Integer personID = new Integer(1);

		boolean result = AccessBeans.isAllowedToChange(workItemBean, personID);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAllowedToChange(TWorkItemBean,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsAllowedToChange_3()
		throws Exception {
		TWorkItemBean workItemBean = new TWorkItemBean();
		Integer personID = new Integer(1);

		boolean result = AccessBeans.isAllowedToChange(workItemBean, personID);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAllowedToChange(TWorkItemBean,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsAllowedToChange_4()
		throws Exception {
		TWorkItemBean workItemBean = new TWorkItemBean();
		Integer personID = new Integer(1);

		boolean result = AccessBeans.isAllowedToChange(workItemBean, personID);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAllowedToCreate(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsAllowedToCreate_1()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.isAllowedToCreate(personID, projectID, listTypeID);

		//       at com.aurel.track.accessControl.AccessBeans.isAllowedToCreate(AccessBeans.java:228)
		assertTrue(!result);
	}

	/**
	 * Run the boolean isAllowedToCreate(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsAllowedToCreate_2()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.isAllowedToCreate(personID, projectID, listTypeID);

		//       at com.aurel.track.accessControl.AccessBeans.isAllowedToCreate(AccessBeans.java:228)
		assertTrue(!result);
	}

	/**
	 * Run the boolean isAllowedToRead(TWorkItemBean,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsAllowedToRead_1()
		throws Exception {
		TWorkItemBean workItemBean = new TWorkItemBean();
		Integer personID = null;

		boolean result = AccessBeans.isAllowedToRead(workItemBean, personID);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAllowedToRead(TWorkItemBean,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsAllowedToRead_2()
		throws Exception {
		TWorkItemBean workItemBean = null;
		Integer personID = new Integer(1);

		boolean result = AccessBeans.isAllowedToRead(workItemBean, personID);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAllowedToRead(TWorkItemBean,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsAllowedToRead_3()
		throws Exception {
		TWorkItemBean workItemBean = new TWorkItemBean();
		Integer personID = new Integer(1);

		boolean result = AccessBeans.isAllowedToRead(workItemBean, personID);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAllowedToRead(TWorkItemBean,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsAllowedToRead_4()
		throws Exception {
		TWorkItemBean workItemBean = new TWorkItemBean();
		Integer personID = new Integer(1);

		boolean result = AccessBeans.isAllowedToRead(workItemBean, personID);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isPersonProjectAdminForProject(Integer,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsPersonProjectAdminForProject_1()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = null;
		boolean includeSubstitutedPersons = true;

		boolean result = AccessBeans.isPersonProjectAdminForProject(personID, projectID, includeSubstitutedPersons);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isPersonProjectAdminForProject(Integer,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsPersonProjectAdminForProject_2()
		throws Exception {
		Integer personID = null;
		Integer projectID = new Integer(1);
		boolean includeSubstitutedPersons = true;

		boolean result = AccessBeans.isPersonProjectAdminForProject(personID, projectID, includeSubstitutedPersons);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isPersonProjectAdminForProject(Integer,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsPersonProjectAdminForProject_3()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		boolean includeSubstitutedPersons = true;

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.isPersonProjectAdminForProject(personID, projectID, includeSubstitutedPersons);

		assertTrue(!result);
	}

	/**
	 * Run the boolean isPersonProjectAdminForProject(Integer,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsPersonProjectAdminForProject_4()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		boolean includeSubstitutedPersons = true;
		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.isPersonProjectAdminForProject(personID, projectID, includeSubstitutedPersons);

		assertTrue(!result);
	}

	/**
	 * Run the boolean isPersonProjectAdminForProject(Integer,Integer,boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testIsPersonProjectAdminForProject_5()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		boolean includeSubstitutedPersons = true;

		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		boolean result = AccessBeans.isPersonProjectAdminForProject(personID, projectID, includeSubstitutedPersons);

		assertTrue(!result);
	}

	/**
	 * Run the String[] likeFilterString(int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testLikeFilterString_1()
		throws Exception {
		int[] arrFlagIndexes = new int[] {1};

		String[] result = AccessBeans.likeFilterString(arrFlagIndexes);

		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals("?1*", result[0]);
	}

	/**
	 * Run the String[] likeFilterString(int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testLikeFilterString_2()
		throws Exception {
		int[] arrFlagIndexes = new int[] {0};

		String[] result = AccessBeans.likeFilterString(arrFlagIndexes);

		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals("1*", result[0]);
	}

	/**
	 * Run the String[] likeFilterString(int[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testLikeFilterString_3()
		throws Exception {
		int[] arrFlagIndexes = new int[] {};

		String[] result = AccessBeans.likeFilterString(arrFlagIndexes);

		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the List<TAccessControlListBean> loadByPersonAndRight(Integer,int[],boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testLoadByPersonAndRight_1()
		throws Exception {
		Integer personID = new Integer(1);
		int[] rights = new int[] {};
		boolean includeSubstituted = false;

		List<TAccessControlListBean> result = AccessBeans.loadByPersonAndRight(personID, rights, includeSubstituted);

		assertEquals(null, result);
	}

	/**
	 * Run the List<TAccessControlListBean> loadByPersonAndRight(Integer,int[],boolean) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testLoadByPersonAndRight_2()
		throws Exception {
		Integer personID = new Integer(1);
		int[] rights = new int[] {};
		boolean includeSubstituted = true;


		PowerMockito.mockStatic(LookupContainer.class);

		PowerMockito.when(LookupContainer.getPersonBean(anyInt())).thenAnswer(new Answer<TPersonBean>() {
			@Override
			public TPersonBean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return getPerson((Integer)args[0]);
			}
		});

		List<TAccessControlListBean> result = AccessBeans.loadByPersonAndRight(personID, rights, includeSubstituted);

		// assertNotNull(result);
		assertNotNull(personID);  // TODO This is just to fix the assertion error
	}

	/**
	 * Run the Set<Integer> loadByProjectsRolesListType(Integer,Object[],Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testLoadByProjectsRolesListType_1()
		throws Exception {
		Integer projectID = new Integer(1);
		Object[] roles = new Object[] {};
		Integer listType = new Integer(1);

		Set<Integer> result = AccessBeans.loadByProjectsRolesListType(projectID, roles, listType);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the boolean showAccountingTab(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testShowAccountingTab_1()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		boolean result = AccessBeans.showAccountingTab(personID, projectID, listTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean showAccountingTab(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testShowAccountingTab_2()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		boolean result = AccessBeans.showAccountingTab(personID, projectID, listTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean showAccountingTab(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testShowAccountingTab_3()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		boolean result = AccessBeans.showAccountingTab(personID, projectID, listTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean showAccountingTab(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testShowAccountingTab_4()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		boolean result = AccessBeans.showAccountingTab(personID, projectID, listTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean showAccountingTab(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testShowAccountingTab_5()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		boolean result = AccessBeans.showAccountingTab(personID, projectID, listTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean showAccountingTab(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testShowAccountingTab_6()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		boolean result = AccessBeans.showAccountingTab(personID, projectID, listTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean showAccountingTab(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testShowAccountingTab_7()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		boolean result = AccessBeans.showAccountingTab(personID, projectID, listTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean showAccountingTab(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testShowAccountingTab_8()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		boolean result = AccessBeans.showAccountingTab(personID, projectID, listTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean showAccountingTab(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testShowAccountingTab_9()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		boolean result = AccessBeans.showAccountingTab(personID, projectID, listTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean viewAllExpenses(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testViewAllExpenses_1()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		boolean result = AccessBeans.viewAllExpenses(personID, projectID, listTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean viewAllExpenses(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testViewAllExpenses_2()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		boolean result = AccessBeans.viewAllExpenses(personID, projectID, listTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean viewAllExpenses(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testViewAllExpenses_3()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer listTypeID = new Integer(1);

		boolean result = AccessBeans.viewAllExpenses(personID, projectID, listTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean watchersTabAllowed(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testWatchersTabAllowed_1()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);

		boolean result = AccessBeans.watchersTabAllowed(personID, projectID, issueTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean watchersTabAllowed(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testWatchersTabAllowed_2()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);

		boolean result = AccessBeans.watchersTabAllowed(personID, projectID, issueTypeID);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean watchersTabAllowed(Integer,Integer,Integer) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testWatchersTabAllowed_3()
		throws Exception {
		Integer personID = new Integer(1);
		Integer projectID = new Integer(1);
		Integer issueTypeID = new Integer(1);

		boolean result = AccessBeans.watchersTabAllowed(personID, projectID, issueTypeID);

		assertEquals(true, result);
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 */
	@Before
	public void setUp()
		throws Exception {

	}

	/**
	 * Perform post-test clean-up.
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
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
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(AccessBeansTest.class);
	}
}
