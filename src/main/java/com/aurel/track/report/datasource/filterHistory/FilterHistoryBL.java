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

package com.aurel.track.report.datasource.filterHistory;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.persist.ReportBeanHistoryLoader;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanWithHistory;
import com.aurel.track.util.GeneralUtils;

public class FilterHistoryBL {

	public static List<ReportBeanWithHistory> addHistoryData(List<ReportBean> reportBeanList, 
			Date fromDate, Date toDate, Boolean excludeWithoutHistory,
			Integer[] fields, Integer selectedPersonID, TPersonBean loggedInPerson, Locale locale) {		
		Set<Integer> fieldsSet = GeneralUtils.createSetFromIntArr(GeneralUtils.createIntArrFromIntegerArr(fields));
		List<Integer> selectedPersons = null;
		if (selectedPersonID!=null) {
			selectedPersons = new LinkedList<Integer>();
			selectedPersons.add(selectedPersonID);
		}
		boolean includeComments = false;
		boolean includeBudget = false;
		boolean includePlan = false;
		boolean includeCost = false;
		boolean includeFieldChange = false;
		Integer[] systemOrCustomFieldIDs = null;
		if (fieldsSet.isEmpty()) {
			//no field selected means all fields
			includeComments = true;
			includeBudget = true;
			includePlan = true;
			includeCost = true;
			includeFieldChange = true;
		} else {
			//at least one field is selected
			if (fieldsSet.contains(SystemFields.INTEGER_COMMENT)) {
				fieldsSet.remove(SystemFields.INTEGER_COMMENT);
				includeComments = true;
			}
			if (fieldsSet.contains(SystemFields.INTEGER_BUDGET_HISTORY)) {
				fieldsSet.remove(SystemFields.INTEGER_BUDGET_HISTORY);
				includeBudget = true;
			}
			if (fieldsSet.contains(SystemFields.INTEGER_PLAN_HISTORY)) {
				fieldsSet.remove(SystemFields.INTEGER_PLAN_HISTORY);
				includePlan = true;
			}
			
			if (fieldsSet.contains(SystemFields.INTEGER_COST_HISTORY)) {
				fieldsSet.remove(SystemFields.INTEGER_COST_HISTORY);
				includeCost = true;
			}
			if (!fieldsSet.isEmpty()) {
				systemOrCustomFieldIDs = GeneralUtils.createIntegerArrFromSet(fieldsSet);
				includeFieldChange = true;
			}
		}
		List<ReportBeanWithHistory> reportBeanWithHistoryList = ReportBeanHistoryLoader.getReportBeanWithHistoryList(
				reportBeanList, locale, false, includeComments, includeFieldChange, 
				systemOrCustomFieldIDs, includeBudget, includePlan, includeCost, true, false, loggedInPerson.getObjectID(), selectedPersons,
				fromDate, toDate, true, LONG_TEXT_TYPE.ISPLAIN);
		if (excludeWithoutHistory!=null && excludeWithoutHistory.booleanValue()) {
			//filter out those values which does not have any filtered history entry
			if (reportBeanWithHistoryList!=null && !reportBeanWithHistoryList.isEmpty()) {
				Iterator<ReportBeanWithHistory> iterator = reportBeanWithHistoryList.iterator();
				while (iterator.hasNext()) {
					ReportBeanWithHistory reportBeanWithHistory = iterator.next();
					if (!hasHistory(reportBeanWithHistory)) {
						iterator.remove();
					}
				}
			}
		} 
		return reportBeanWithHistoryList;
	}
	
	private static boolean hasHistory(ReportBeanWithHistory reportBeanWithHistory) {
		if (reportBeanWithHistory.getHistoryValuesMap()!=null && !reportBeanWithHistory.getHistoryValuesMap().isEmpty()) {
			return true;
		}
		if (reportBeanWithHistory.getComments()!=null && !reportBeanWithHistory.getComments().isEmpty()) {
			return true;
		}
		if (reportBeanWithHistory.getPlannedValueHistory()!=null && !reportBeanWithHistory.getPlannedValueHistory().isEmpty()) {
			return true;
		}
		if (reportBeanWithHistory.getCosts()!=null && !reportBeanWithHistory.getCosts().isEmpty()) {
			return true;
		}
		return false;
	}
	
}
