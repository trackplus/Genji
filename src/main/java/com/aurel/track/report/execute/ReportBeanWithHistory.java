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



package com.aurel.track.report.execute;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.item.history.BudgetCostHistoryBean;
import com.aurel.track.item.history.HistoryBean;
import com.aurel.track.item.history.HistoryValues;


/**
 * This class represents a single row of a report 
 * with the history and effort budget and cost information
 * @author Tamas Ruff
 *
 */
public class ReportBeanWithHistory extends ReportBean implements Serializable {
	private static final long serialVersionUID = 2500245193184052246L;	
	
	private Map<Integer, Map<Integer, HistoryValues>> historyValuesMap;
	private List<HistoryValues> comments;	
	private List<TBudgetBean> plannedValueHistory;
	private List<TBudgetBean> budgetHistory;
	private List<TCostBean> costs;	
	private TActualEstimatedBudgetBean actualEstimatedBudgetBean;
	private List<TAttachmentBean> attachments; 
	
	private static final Logger LOGGER = LogManager.getLogger(ReportBeanWithHistory.class);
	
	public ReportBeanWithHistory(ReportBean reportBean) {
		super();
		try {
			PropertyUtils.copyProperties(this, reportBean);
		} catch (Exception e) {
			LOGGER.error("Constructing a ReportBeanWithHistory from ReportBean failed with " + e.getMessage(), e);
		}
	}
	
	/**
	 * @return Returns the budgetHistory.
	 */
	public List<TBudgetBean> getPlannedValueHistory() {
		return plannedValueHistory;
	}

	/**
	 * @param budgetHistory The budgetHistory to set.
	 */
	public void setPlannedValueHistory(List<TBudgetBean> budgetHistory) {
		this.plannedValueHistory = budgetHistory;
	}
	
	
	public List<TBudgetBean> getBudgetHistory() {
		return budgetHistory;
	}

	public void setBudgetHistory(List<TBudgetBean> budgetHistory) {
		this.budgetHistory = budgetHistory;
	}

	/**
	 * @return the costs
	 */
	public List<TCostBean> getCosts() {
		return costs;
	}

	/**
	 * @param costs the costs to set
	 */
	public void setCosts(List<TCostBean> costs) {
		this.costs = costs;
	}

	/**
	 * @return the actualEstimatedBudgetBean
	 */
	public TActualEstimatedBudgetBean getActualEstimatedBudgetBean() {
		return actualEstimatedBudgetBean;
	}

	/**
	 * @param actualEstimatedBudgetBean the actualEstimatedBudgetBean to set
	 */
	public void setActualEstimatedBudgetBean(
			TActualEstimatedBudgetBean actualEstimatedBudgetBean) {
		this.actualEstimatedBudgetBean = actualEstimatedBudgetBean;
	}

	public Map<Integer, Map<Integer, HistoryValues>> getHistoryValuesMap() {
		return historyValuesMap;
	}

	public void setHistoryValuesMap(
			Map<Integer, Map<Integer, HistoryValues>> historyValuesMap) {
		this.historyValuesMap = historyValuesMap;
	}

	public List<HistoryValues> getComments() {
		return comments;
	}

	public void setComments(List<HistoryValues> comments) {
		this.comments = comments;
	}

	public List<TAttachmentBean> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<TAttachmentBean> attachments) {
		this.attachments = attachments;
	}
	
}
