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

package com.aurel.track.admin.customize.category.filter.execute.loadItems;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.item.budgetCost.ComputedValueBL;
import com.aurel.track.item.budgetCost.RemainingPlanBL;
import com.aurel.track.item.consInf.ConsInfBL;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ReportBean;

/**
 * Loads the TQL filter items
 * @author Tamas
 *
 */
public class LoadTQLFilterItems {
	private static final Logger LOGGER = LogManager.getLogger(LoadTQLFilterItems.class);
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	/**
	 * Get a list of ReportBeans filtered by the TQL filter and projectID or releaseID
	 * @param userQuery
	 * @param personBean
	 * @param errors
	 * @param locale
	 * @return
	 */
	public static List<ReportBean> getTQLFilterReportBeans(String userQuery,
		TPersonBean personBean, List<ErrorData> errors, Locale locale, Integer projectID, Integer entityFlag) {
		return getTQLFilterReportBeans(userQuery, personBean, errors, locale, true, projectID, entityFlag, true, true, true, true, true, true, true, true, true);
	}
	/**
	 * Get a list of ReportBeans filtered by the TQL filter and projectID or releaseID
	 * @param userQuery
	 * @param personBean
	 * @param errors
	 * @param locale
	 * @return
	 */
	public static List<ReportBean> getTQLFilterReportBeans(String userQuery,
		TPersonBean personBean, List<ErrorData> errors, Locale locale, boolean editFlagNeeded, Integer projectID, Integer entityFlag,
		boolean withCustomAttributes, boolean withWatchers,
		boolean withMyExpenses, boolean withTotalExpenses,
		boolean withBudgetPlan, boolean withRemainingPlan,
		boolean withAttachment, boolean withLinks, boolean withParents) {
		List<TWorkItemBean> workItemBeanListDB = prepareTQLFilterItems(userQuery, personBean, errors, locale, editFlagNeeded, null, null);
		List<TWorkItemBean> workItemBeanList = LoadItemsUtil.filterByProjectAndEntityFlag(workItemBeanListDB, projectID, entityFlag);
		List<TAttributeValueBean> attributeValueBeanList = null;
		if (withCustomAttributes) {
			attributeValueBeanList = AttributeValueBL.loadTQLFilterAttributeValues(userQuery, personBean, locale, errors);
		}
		List<TNotifyBean> consInfList = null;
		if (withWatchers) {
			consInfList = ConsInfBL.loadTQLFilterWatchers(userQuery, personBean, locale, errors);
		}
		List<TComputedValuesBean> myExpenseList = null;
		if (withMyExpenses) {
			myExpenseList = ComputedValueBL.loadByTQLFilterForPerson(userQuery, personBean, locale, errors, TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, true);
		}
		List<TComputedValuesBean> totalExpenseList = null;
		if (withTotalExpenses) {
			totalExpenseList = ComputedValueBL.loadByTQLFilterForPerson(userQuery, personBean, locale, errors, TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, false);
		}
		List<TComputedValuesBean> budgetAndPlanList = null;
		if (withBudgetPlan) {
		int[] computedValueTypes = null;
		boolean budgetActive = ApplicationBean.getInstance().getBudgetActive();
		if (budgetActive) {
			computedValueTypes = new int[] {TComputedValuesBean.COMPUTEDVALUETYPE.PLAN, TComputedValuesBean.COMPUTEDVALUETYPE.BUDGET};
		} else {
			computedValueTypes = new int[] {TComputedValuesBean.COMPUTEDVALUETYPE.PLAN};
		}
			budgetAndPlanList = ComputedValueBL.loadByTQLFilter(userQuery, personBean, locale, errors, computedValueTypes);
		}
		List<TActualEstimatedBudgetBean> remainingPlanList = null;
		if (withRemainingPlan) {
			remainingPlanList = RemainingPlanBL.loadByTQLFilter(userQuery, personBean, locale, errors);
		}
		List<TAttachmentBean> attachmentList = null;
		if (withAttachment) {
			attachmentList = AttachBL.loadTQLFilterAttachments(userQuery, personBean, locale, errors);
		}
		List<TWorkItemLinkBean> itemLinkList = null;
		if (withLinks) {
			itemLinkList = ItemLinkBL.loadTQLFilterLinks(userQuery, personBean, locale, errors);
		}
		Set<Integer> parentIDsSet = null;
		if (withParents) {
			boolean summaryItemsBehavior = ApplicationBean.getInstance().getSiteBean().getSummaryItemsBehavior();
			if (summaryItemsBehavior) {
				parentIDsSet = workItemDAO.loadTQLFilterParentIDs(userQuery, personBean, locale, errors);
			}
		}
		return ReportBeanLoader.populateReportBeans(workItemBeanList, attributeValueBeanList, personBean.getObjectID(), locale,
				consInfList, myExpenseList, totalExpenseList, budgetAndPlanList, remainingPlanList,
				attachmentList, itemLinkList, parentIDsSet);
	}
	
	/**
	 *  Get a list of WorkItemBeans filtered by the TQL filter and dates
	 * @param userQuery
	 * @param personBean
	 * @param errors
	 * @param locale
	 * @param withCustomAttributes
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<TWorkItemBean> getTQLFilterWorkItemBeans(String userQuery,
			TPersonBean personBean, List<ErrorData> errors, Locale locale, boolean withCustomAttributes,Date startDate,Date endDate) {
		List<TWorkItemBean> workItemBeanList = prepareTQLFilterItems(userQuery, personBean, errors, locale, false, startDate,endDate);
		if (withCustomAttributes) {	
			List<TAttributeValueBean> attributeValueBeanList = AttributeValueBL.loadTQLFilterAttributeValues(userQuery, personBean, locale, errors);				
			return LoadItemsUtil.loadCustomFields(workItemBeanList, attributeValueBeanList);
		} else {
			return workItemBeanList;
		}
	}

	/**
	 * Loads the TQL filter result
	 * @param filterString
	 * @param personBean
	 * @param errors
	 * @param locale
	 * @param editFlagNeeded
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private static List<TWorkItemBean> prepareTQLFilterItems(String filterString,
			TPersonBean personBean, List<ErrorData> errors, Locale locale, boolean editFlagNeeded, Date startDate, Date endDate) {
		List<TWorkItemBean> workItemBeanList = workItemDAO.loadTQLFilterItems(filterString, personBean, locale, errors, startDate, endDate);
		if (workItemBeanList==null || workItemBeanList.isEmpty()) {
			return new ArrayList<TWorkItemBean>();
		}
		LOGGER.debug("TQL expression " + filterString);
		LOGGER.debug("Number of workItems before filtering " + workItemBeanList.size());
		LoadItemsUtil.removeArchivedOrDeleted(workItemBeanList);
		LOGGER.debug("Number of workItems after removing archived/deleted ones " + workItemBeanList.size());
		workItemBeanList = AccessBeans.filterWorkItemBeans(personBean.getObjectID(), workItemBeanList, editFlagNeeded);
		LOGGER.debug("Number of workItems after filtering " + workItemBeanList.size());
		return workItemBeanList;
	}	

}
