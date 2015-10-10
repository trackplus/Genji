/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

package com.aurel.track.move;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.junit.BeforeClass;
import org.junit.Test;

import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemLinkDAO;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.LAG_FORMAT;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.PREDECESSOR_ELEMENT_TYPE;
import com.aurel.track.item.ItemBL;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.util.DateTimeUtils;
import com.ibm.icu.util.Calendar;

public class ItemMoveBLTest {

	public Integer WORK_ITEM_ID_TO_MOVE;
	public TWorkItemBean WORK_ITEM_BEAN_TO_MOVE;
	public TWorkItemLinkBean WORK_ITEM_LINK_BEAN;
	public TPersonBean PERSON_BEAN;
	public Locale LOCALE;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@BeforeClass
	public void beforeClass() {
		WORK_ITEM_ID_TO_MOVE = Integer.valueOf(1000);
		WORK_ITEM_BEAN_TO_MOVE = ItemBL.loadWorkItemWithCustomFields(WORK_ITEM_ID_TO_MOVE);
		PERSON_BEAN = new TPersonBean();
		LOCALE = Locale.getDefault();
		WorkItemLinkDAO workItemLinkDao = DAOFactory.getFactory().getWorkItemLinkDAO();
		List<TWorkItemLinkBean>links = workItemLinkDao.loadByWorkItemSucc(WORK_ITEM_BEAN_TO_MOVE.getObjectID());
		assertFalse(links == null);
		assertFalse(links.isEmpty());
		WORK_ITEM_LINK_BEAN = links.get(0);
	}

	@Test
	public void removeViolatedParentDependenciesTest() {
		Set<Integer>idsToRemove = new HashSet<Integer>();
		ItemMoveBL.removeViolatedParentDependencies(idsToRemove);
	}

	@Test
	public void setStartDateTest() {
		TWorkItemBean bean = new TWorkItemBean();
		Date now = new Date();
		ItemMoveBL.setStartDate(bean, now);
	}

	@Test
	public void setEndDateTest() {
		TWorkItemBean bean = new TWorkItemBean();
		Date now = new Date();
		ItemMoveBL.setEndDate(bean, now);
	}

	@Test
	public void getStartDate() {
		TWorkItemBean bean = new TWorkItemBean();
		Date now = new Date();
		bean.setStartDate(now);
		bean.setTopDownStartDate(now);
		assertFalse(DateTimeUtils.compareTwoDatesWithoutTimeValue(now, ItemMoveBL.getStartDate(bean)) != 0);
	}

	@Test
	public void getEndDate() {
		TWorkItemBean bean = new TWorkItemBean();
		Date now = new Date();
		bean.setEndDate(now);
		bean.setTopDownEndDate(now);
		assertFalse(DateTimeUtils.compareTwoDatesWithoutTimeValue(now, ItemMoveBL.getEndDate(bean)) != 0);
	}

	@Test
	public void tupleDatesWereChangedTest() {
		Node s = new Node(new Date(), 1000, Node.NODE_TYPES.START_DATE);
		Node t = new Node(new Date(), 2000, Node.NODE_TYPES.END_DATE);
		Edge e = new Edge(Edge.EDGE_TYPES.DURATION);
		Tuple tuple = new Tuple(s, t, e);
		assertFalse(ItemMoveBL.tupleDatesWereChanged(tuple) == false);
	}

	@Test
	public void hasWorkItemBeanStartAndEndDateTest() {
		TWorkItemBean bean = new TWorkItemBean();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add( Calendar.DAY_OF_YEAR, -1);
		Date yesterday = cal.getTime();
		Date now = new Date();
		bean.setStartDate(yesterday);
		bean.setTopDownStartDate(yesterday);
		bean.setEndDate(now);
		bean.setTopDownEndDate(now);
		assertFalse(ItemMoveBL.hasWorkItemBeanStartAndEndDate(bean) == false);
	}

	@Test
	public void hasReportBeanStartAndEndDateTest() {
		ReportBean rp = new ReportBean();
		TWorkItemBean bean = new TWorkItemBean();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add( Calendar.DAY_OF_YEAR, -1);
		Date yesterday = cal.getTime();
		Date now = new Date();
		bean.setStartDate(yesterday);
		bean.setTopDownStartDate(yesterday);
		bean.setEndDate(now);
		bean.setTopDownEndDate(now);
		rp.setWorkItemBean(bean);
		assertFalse(ItemMoveBL.hasReportBeanStartAndEndDate(rp) == false);
	}

	@Test
	public void getHoursPerWorkingDayForWorkItemTest() {
		Double hoursPerWorkingDay = ItemMoveBL.getHoursPerWorkingDayForWorkItem(WORK_ITEM_ID_TO_MOVE);
		assertFalse(hoursPerWorkingDay == null);
	}

	@Test
	//TODO check this test case
	public void getLinkLagInDaysTest(TWorkItemLinkBean actualLinkBean, Double hoursPerWorkday) {
		int linkLagInDays = ItemMoveBL.getLinkLagInDays(WORK_ITEM_LINK_BEAN, 8.0);
	}

	@Test
	public void isLinkValidConsideringLinkLagTest1() {
		TWorkItemLinkBean testLink = new TWorkItemLinkBean();
		testLink.setLinkLag(0.0);
		testLink.setLinkLagFormat(LAG_FORMAT.h);
		testLink.setIntegerValue1(PREDECESSOR_ELEMENT_TYPE.FS);
		Date source = null;
		Date target = null;
		try {
			source = sdf.parse("2015-08-08");
			target = sdf.parse("2015-08-08");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		assertFalse(source == null || target == null);
		boolean isValid = ItemMoveBL.isLinkViolatesConsideringLinkLag(source, target, testLink);
		assertFalse(isValid == true);
	}

	@Test
	public void isLinkValidConsideringLinkLagTest2() {
		TWorkItemLinkBean testLink = new TWorkItemLinkBean();
		testLink.setLinkLag(2.0);
		testLink.setLinkLagFormat(LAG_FORMAT.h);
		testLink.setIntegerValue1(PREDECESSOR_ELEMENT_TYPE.FS);
		Date source = null;
		Date target = null;
		try {
			source = sdf.parse("2015-08-08");
			target = sdf.parse("2015-08-08");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		assertFalse(source == null || target == null);
		boolean isValid = ItemMoveBL.isLinkViolatesConsideringLinkLag(source, target, testLink);
		assertFalse(isValid == true);
	}

	@Test
	public void isLinkValidConsideringLinkLagTest3() {
		TWorkItemLinkBean testLink = new TWorkItemLinkBean();
		testLink.setLinkLag(2.0);
		testLink.setLinkLagFormat(LAG_FORMAT.h);
		testLink.setIntegerValue1(PREDECESSOR_ELEMENT_TYPE.FS);
		Date source = null;
		Date target = null;
		try {
			source = sdf.parse("2015-08-08");
			target = sdf.parse("2015-08-11");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		assertFalse(source == null || target == null);
		boolean isValid = ItemMoveBL.isLinkViolatesConsideringLinkLag(source, target, testLink);
		assertFalse(isValid == false);
	}

	@Test
	public void isLinkValidConsideringLinkLagTest4() {
		TWorkItemLinkBean testLink = new TWorkItemLinkBean();
		testLink.setLinkLag(-2.0);
		testLink.setLinkLagFormat(LAG_FORMAT.h);
		testLink.setIntegerValue1(PREDECESSOR_ELEMENT_TYPE.FS);
		Date source = null;
		Date target = null;
		try {
			source = sdf.parse("2015-08-08");
			target = sdf.parse("2015-08-11");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		assertFalse(source == null || target == null);
		boolean isValid = ItemMoveBL.isLinkViolatesConsideringLinkLag(source, target, testLink);
		assertFalse(isValid == false);
	}

	@Test
	public void isLinkValidConsideringLinkLagTest6() {
		TWorkItemLinkBean testLink = new TWorkItemLinkBean();
		testLink.setLinkLag(-2.0);
		testLink.setLinkLagFormat(LAG_FORMAT.h);
		testLink.setIntegerValue1(PREDECESSOR_ELEMENT_TYPE.FS);
		Date source = null;
		Date target = null;
		try {
			source = sdf.parse("2015-08-08");
			target = sdf.parse("2015-08-06");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		assertFalse(source == null || target == null);
		boolean isValid = ItemMoveBL.isLinkViolatesConsideringLinkLag(source, target, testLink);
		assertFalse(isValid == true);
	}

	@Test
	public void isLinkValidConsideringLinkLagTest5() {
		TWorkItemLinkBean testLink = new TWorkItemLinkBean();
		testLink.setLinkLag(2.0);
		testLink.setLinkLagFormat(LAG_FORMAT.h);
		testLink.setIntegerValue1(PREDECESSOR_ELEMENT_TYPE.FS);
		Date source = null;
		Date target = null;
		try {
			source = sdf.parse("2015-08-08");
			target = sdf.parse("2015-08-09");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		assertFalse(source == null || target == null);
		boolean isValid = ItemMoveBL.isLinkViolatesConsideringLinkLag(source, target, testLink);
		assertFalse(isValid == true);
	}

	@Test
	public void getNumberOfDaysBetweenDatesTest() {
		try {
			Date source = sdf.parse("2015-08-08");
			Date target = sdf.parse("2015-08-09");
			Integer numberOfDays = ItemMoveBL.getNumberOfDaysBetweenDates(source, target);
			assertFalse(numberOfDays == null);
			assertFalse(numberOfDays != 1);
		}catch(Exception ex) {
			assertFalse(ex.getMessage() != null);
			ex.printStackTrace();
		}
	}

	@Test
	public void getNumberOfSaturdaysFromIntervalTest() {
		try {
			Date source = sdf.parse("2015-08-17");
			Date target = sdf.parse("2015-08-31");
			Integer numberOfSaturdaysFromInterval = ItemMoveBL.getNumberOfSaturdaysFromInterval(source, target);
			assertFalse(numberOfSaturdaysFromInterval == null);
			assertFalse(numberOfSaturdaysFromInterval != 2);
		}catch(Exception ex) {
			assertFalse(ex.getMessage() != null);
			ex.printStackTrace();
		}
	}

	@Test
	public void getNumberOfFreeDaysFromIntervallTest() {
		try {
			Date source = sdf.parse("2015-08-17");
			Date target = sdf.parse("2015-08-31");
			Integer numberOfFreeDaysFromIntervall = ItemMoveBL.getNumberOfFreeDaysFromIntervall(source, target);
			assertFalse(numberOfFreeDaysFromIntervall == null);
		}catch(Exception ex) {
			assertFalse(ex.getMessage() != null);
			ex.printStackTrace();
		}
	}

	@Test
	public void getNumberOfSundaysFromIntervalTest() {
		try {
			Date source = sdf.parse("2015-08-17");
			Date target = sdf.parse("2015-08-31");
			Integer numberOfSaturdaysFromInterval = ItemMoveBL.getNumberOfSundaysFromInterval(source, target);
			assertFalse(numberOfSaturdaysFromInterval == null);
			assertFalse(numberOfSaturdaysFromInterval != 2);
		}catch(Exception ex) {
			assertFalse(ex.getMessage() != null);
			ex.printStackTrace();
		}
	}

	@Test
	public void getNumberOfWorkDaysBetweenDates() {
		try {
			Date source = sdf.parse("2015-08-17");
			Date target = sdf.parse("2015-08-31");
			int numberOfWorkDaysBetweenDates = ItemMoveBL.getNumberOfWorkDaysBetweenDates(source, target);
			assertFalse(numberOfWorkDaysBetweenDates != 10);
		}catch(Exception ex) {
			assertFalse(ex.getMessage() != null);
			ex.printStackTrace();
		}
	}

	@Test
	public void stepForwardTest() {
		try {
			Date source = sdf.parse("2015-08-24");
			Date newDate = ItemMoveBL.stepForward(source, 5, true);
			Date expectedDate = sdf.parse("2015-08-31");
			assertFalse(newDate == null);
			assertFalse(DateTimeUtils.compareTwoDatesWithoutTimeValue(newDate, expectedDate) != 0);
		}catch(Exception ex) {
			assertFalse(ex.getMessage() != null);
			ex.printStackTrace();
		}
	}

	@Test
	public void stepForwardTest2() {
		try {
			Date source = sdf.parse("2015-08-24");
			Date newDate = ItemMoveBL.stepForward(source, 5, true);
			Date expectedDate = sdf.parse("2015-08-29");
			assertFalse(newDate == null);
			assertFalse(DateTimeUtils.compareTwoDatesWithoutTimeValue(newDate, expectedDate) != 0);
		}catch(Exception ex) {
			assertFalse(ex.getMessage() != null);
			ex.printStackTrace();
		}
	}

	@Test
	public void stepBackTest() {
		try {
			Date source = sdf.parse("2015-08-28");
			Date newDate = ItemMoveBL.stepBack(source, 5, true);
			Date expectedDate = sdf.parse("2015-08-23");
			assertFalse(newDate == null);
			assertFalse(DateTimeUtils.compareTwoDatesWithoutTimeValue(newDate, expectedDate) != 0);
		}catch(Exception ex) {
			assertFalse(ex.getMessage() != null);
			ex.printStackTrace();
		}
	}

	@Test
	public void stepBackTest2() {
		try {
			Date source = sdf.parse("2015-08-28");
			Date newDate = ItemMoveBL.stepBack(source, 5, true);
			Date expectedDate = sdf.parse("2015-08-21");
			assertFalse(newDate == null);
			assertFalse(DateTimeUtils.compareTwoDatesWithoutTimeValue(newDate, expectedDate) != 0);
		}catch(Exception ex) {
			assertFalse(ex.getMessage() != null);
			ex.printStackTrace();
		}
	}

	@Test
	public void setPresentedFieldsTest() {
		HashSet<Integer>prFields = new HashSet<Integer>();
		ItemMoveBL.setPresentedFields(prFields);
		assertFalse(prFields == null);
		assertFalse(prFields.size() == 0);
	}

	@Test
	public void saveWorkitemBeanTest() {
		Node s = new Node(new Date(), WORK_ITEM_ID_TO_MOVE, Node.NODE_TYPES.START_DATE);
		Node t = new Node(new Date(), WORK_ITEM_ID_TO_MOVE, Node.NODE_TYPES.END_DATE);
		Edge e = new Edge(Edge.EDGE_TYPES.DURATION);
		Tuple tuple = new Tuple(s, t, e);
		ItemMoveBL.saveWorkitemBean(tuple);
	}

	@Test
	public void moveOneTupleSourceAndTargetNodeConsideringDirectionTest() {
		Node s = new Node(new Date(), WORK_ITEM_ID_TO_MOVE, Node.NODE_TYPES.START_DATE);
		Node t = new Node(new Date(), WORK_ITEM_ID_TO_MOVE, Node.NODE_TYPES.END_DATE);
		Edge e = new Edge(Edge.EDGE_TYPES.DURATION);
		Tuple tuple = new Tuple(s, t, e);
		ItemMoveBL.moveOneTupleSourceAndTargetNodeConsideringDirection(tuple, 10);
	}

	@Test
	public void moveBelowElementsTest() {
		Node s = new Node(new Date(), WORK_ITEM_ID_TO_MOVE, Node.NODE_TYPES.START_DATE);
		Node t = new Node(new Date(), WORK_ITEM_ID_TO_MOVE, Node.NODE_TYPES.END_DATE);
		Edge e = new Edge(Edge.EDGE_TYPES.DURATION);
		Tuple tuple = new Tuple(s, t, e);
		ItemMoveBL.moveBelowElements(tuple);
	}

	@Test
	public void createMapFromReportBeanListTest() {
		Integer projectID = WORK_ITEM_BEAN_TO_MOVE.getProjectID() * -1;
		QueryContext queryContext = new QueryContext();
		queryContext.setQueryType(ItemNavigatorBL.QUERY_TYPE.PROJECT_RELEASE);
		queryContext.setQueryID(projectID);
		TPersonBean personBean = new TPersonBean();
		Locale locale = Locale.getDefault();
		try {
			List<ReportBean> repoBeanList = ItemNavigatorBL.executeQuery(personBean, locale, queryContext);
			assertFalse(repoBeanList == null);
			ItemMoveBL.createMapFromReportBeanList(repoBeanList);
		}catch(Exception ex) {
			assertFalse(ex.getMessage() != null);
			ex.printStackTrace();
		}
	}

	@Test
	public void parseBeansTest() {
		Integer projectID = WORK_ITEM_BEAN_TO_MOVE.getProjectID() * -1;
		QueryContext queryContext = new QueryContext();
		queryContext.setQueryType(ItemNavigatorBL.QUERY_TYPE.PROJECT_RELEASE);
		queryContext.setQueryID(projectID);
		TPersonBean personBean = new TPersonBean();
		Locale locale = Locale.getDefault();
		try {
			List<ReportBean> repoBeanList = ItemNavigatorBL.executeQuery(personBean, locale, queryContext);
			assertFalse(repoBeanList == null);
			ReportBeans reportBeans = new ReportBeans(repoBeanList, locale);
			ItemMoveBL.parseBeans(reportBeans.getReportBeansFirstLevel());
		}catch(Exception ex) {
			assertFalse(ex.getMessage() != null);
			ex.printStackTrace();
		}

	}

	@Test
	public void moveItemTest() {
		Date startDate = ItemMoveBL.getStartDate(WORK_ITEM_BEAN_TO_MOVE);
		Date endDate = ItemMoveBL.getEndDate(WORK_ITEM_BEAN_TO_MOVE);
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);

		cal.add( Calendar.DAY_OF_YEAR, 1);
		Date newStartDate = cal.getTime();

		cal.setTime(endDate);
		cal.add( Calendar.DAY_OF_YEAR, 1);
		Date newEndDate = cal.getTime();
		ItemMoveBL.moveItem(WORK_ITEM_BEAN_TO_MOVE, newStartDate, newEndDate, true, false, null,  PERSON_BEAN, LOCALE);
	}

	@Test
	public void moveItemTest2() {
		Date startDate = ItemMoveBL.getStartDate(WORK_ITEM_BEAN_TO_MOVE);
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);

		cal.add( Calendar.DAY_OF_YEAR, 1);
		Date newStartDate = cal.getTime();

		Date newEndDate = ItemMoveBL.getEndDate(WORK_ITEM_BEAN_TO_MOVE);
		ItemMoveBL.moveItem(WORK_ITEM_BEAN_TO_MOVE, newStartDate, newEndDate, true, false, null, PERSON_BEAN, LOCALE);
	}

	@Test
	public void moveItemTest3() {
		Date endDate = ItemMoveBL.getEndDate(WORK_ITEM_BEAN_TO_MOVE);
		Date newStartDate = ItemMoveBL.getStartDate(WORK_ITEM_BEAN_TO_MOVE);
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add( Calendar.DAY_OF_YEAR, 1);
		Date newEndDate = cal.getTime();
		ItemMoveBL.moveItem(WORK_ITEM_BEAN_TO_MOVE, newStartDate, newEndDate, true, false, null, PERSON_BEAN, LOCALE);
	}
}

