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


package com.aurel.track.item.budgetCost;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.item.history.BudgetCostHistoryBean;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.DateTimeUtils;

public class BudgetPlanExpenseJSON {
	
		
	
		
		
		
		
	
	
	public static interface EXPENSE_LIST_JSON {
		static String DATE = "date";
		static String EFFORT_DATE = "effortDate";
		static String WORK = "work";
		static String COST = "cost";
		static String EDITABLE = "editable";
		static String SUBJECT = "subject";
		static String ACCOUNT = "account";
		static String AUTHOR = "author";
	}
	
	
	
	/**
	 * Encode the expenses for the TReportLayoutBean.PSEUDO_COLUMNS.COST_LIST:
	 * @param costs
	 * @param locale
	 * @return
	 */
	public static String encodeExpensesForReportBean(List<TCostBean> costs,Locale locale) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<TCostBean> iterator = costs.iterator(); iterator.hasNext();) {
			TCostBean costBean = iterator.next();
			sb.append("{");
			JSONUtility.appendStringValue(sb, EXPENSE_LIST_JSON.DATE, DateTimeUtils.getInstance().formatGUIDateTime(costBean.getLastEdit(),locale));
			JSONUtility.appendStringValue(sb, EXPENSE_LIST_JSON.EFFORT_DATE, DateTimeUtils.getInstance().formatGUIDate(costBean.getEffortdate(),locale));
			JSONUtility.appendStringValue(sb, EXPENSE_LIST_JSON.WORK, HistoryLoaderBL.formatEffort(costBean, locale));
			JSONUtility.appendStringValue(sb, EXPENSE_LIST_JSON.COST, HistoryLoaderBL.formatCost(costBean,locale));
			JSONUtility.appendStringValue(sb, EXPENSE_LIST_JSON.SUBJECT, costBean.getSubject());
			JSONUtility.appendStringValue(sb, EXPENSE_LIST_JSON.ACCOUNT, costBean.getAccountName());
			JSONUtility.appendStringValue(sb, EXPENSE_LIST_JSON.AUTHOR, costBean.getChangedByName());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.DESCRIPTION, costBean.getDescription());
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, costBean.getObjectID());
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	
	
	
	/**
	 * Encode the budget/plan for the TReportLayoutBean.PSEUDO_COLUMNS.BUDGETHISTORY_LIST:
	 * @param budgetHistoryList
	 * @param locale
	 * @return
	 */
	public static String encodeBudgetPlanHistoryForReportBean(List<BudgetCostHistoryBean> budgetHistoryList,Locale locale) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (int i = 0; i < budgetHistoryList.size(); i++) {
			BudgetCostHistoryBean budgetCostHistoryBean = (BudgetCostHistoryBean)budgetHistoryList.get(i);
			if(i>0){
				sb.append(",");
			}
			sb.append("{");
			JSONUtility.appendStringValue(sb, "date", DateTimeUtils.getInstance().formatGUIDateTime(budgetCostHistoryBean.getLastEdit(),locale));
			JSONUtility.appendStringValue(sb, "work", HistoryLoaderBL.formatEffort((TBudgetBean)budgetCostHistoryBean, locale));
			JSONUtility.appendStringValue(sb, "cost", HistoryLoaderBL.formatCost((TBudgetBean)budgetCostHistoryBean,locale));
			JSONUtility.appendStringValue(sb, "author", budgetCostHistoryBean.getChangedByName());
			sb.append("id:").append(budgetCostHistoryBean.getObjectID());

			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}

	
	
	
}
