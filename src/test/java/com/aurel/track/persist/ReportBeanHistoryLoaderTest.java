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

package com.aurel.track.persist;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.torque.util.Criteria;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanWithHistory;

public class ReportBeanHistoryLoaderTest {
	List<ReportBean> reportBeanList;
	Locale locale;
	Integer[] fieldIDs;
	Integer[] personIDsArr;	//{10,20,30};

	
	List<Integer> filterByPersons;
	Date fromDate = new Date(2015, 8, 10);
	Date toDate = new Date(2016,9,11);
	LONG_TEXT_TYPE longtextType;
	
	Criteria criteria,returnedCriteria;
	String changedByField = "changedByField";
	String lastEditField = "lastEditField";
	
	
	@Before
	public void setUp(){
		//assertEquals(<SqlEnum>">=", Criteria.GREATER_EQUAL);
		//Criteria.Criterion crit = criteria.getNewCriterion(lastEditField, fromDate, Criteria.GREATER_EQUAL);
		//when(criteria.getNewCriterion(any(String.class),any(Object.class),any(SqlEnum.class))).thenReturn(crit);
	}
	
	@Test
	public void testGetReportBeanWithHistoryList(){
		List<ReportBeanWithHistory> reportBeanwHistoryList;
		reportBeanwHistoryList = ReportBeanHistoryLoader.getReportBeanWithHistoryList(reportBeanList, locale, false, false, false, fieldIDs, false, false, false, false, false, 2, filterByPersons, fromDate, toDate, false, longtextType);
		assertNotNull(reportBeanwHistoryList);
	}
	
	@Test
	@Ignore
	public void testAddFilterConditions() {
		
		//returnedCriteria = ReportBeanHistoryLoader.addFilterConditions(criteria, personIDsArr, fromDate, toDate, changedByField, lastEditField);
		//assertNotNull(returnedCriteria);
	}

}
