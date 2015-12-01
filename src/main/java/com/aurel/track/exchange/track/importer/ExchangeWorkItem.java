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


package com.aurel.track.exchange.track.importer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.exchange.track.ExchangeHistoryTransactionEntry;

public class ExchangeWorkItem {
	
	private String uuid;
	
	private Integer workItemID;
	
	/**
	 * actual field values map: fieldID_parameterCode to String value or List<String> (for multiple selects) 
	 * 
	 */
	private Map<String, Object> actualFieldValuesMap = new HashMap<String, Object>();
	
	private List<ExchangeHistoryTransactionEntry> historyValues = new ArrayList<ExchangeHistoryTransactionEntry>();

	/**
	 * consultants list
	 */
	private List<Integer> consultedList;
	
	/**
	 * informants list
	 */
	private List<Integer> informedList;
	
	/**
	 * budget beans list
	 */
	private List<TBudgetBean> budgetBeanList;
	
	/**
	 * budget beans list
	 */
	private List<TBudgetBean> plannedValueBeanList;
		
	/**
	 * remaining budget bean
	 */
	private TActualEstimatedBudgetBean actualEstimatedBudgetBean;
	
	/**
	 * expense beans list
	 */
	private List<TCostBean> expenseBeanList;
	
	/**
	 * attachment beans list
	 */
	private List<TAttachmentBean> attachmentBeanList;
	
	public Integer getWorkItemID() {
		return workItemID;
	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Map<String, Object> getActualFieldValuesMap() {
		return actualFieldValuesMap;
	}

	public void setActualFieldValuesMap(
			Map<String, Object> actualFieldValuesMap) {
		this.actualFieldValuesMap = actualFieldValuesMap;
	}
	
	public List<ExchangeHistoryTransactionEntry> getHistoryValues() {
		return historyValues;
	}

	public void setHistoryValues(List<ExchangeHistoryTransactionEntry> historyValues) {
		this.historyValues = historyValues;
	}

	
	public List<Integer> getConsultedList() {
		return consultedList;
	}

	public void setConsultedList(List<Integer> consultedList) {
		this.consultedList = consultedList;
	}

	public List<Integer> getInformedList() {
		return informedList;
	}

	public void setInformedList(List<Integer> informedList) {
		this.informedList = informedList;
	}

	public List<TBudgetBean> getBudgetBeanList() {
		return budgetBeanList;
	}

	public void setBudgetBeanList(List<TBudgetBean> budgetBeanList) {
		this.budgetBeanList = budgetBeanList;
	}

	public List<TBudgetBean> getPlannedValueBeanList() {
		return plannedValueBeanList;
	}

	public void setPlannedValueBeanList(List<TBudgetBean> plannedValueBeanList) {
		this.plannedValueBeanList = plannedValueBeanList;
	}

	public TActualEstimatedBudgetBean getActualEstimatedBudgetBean() {
		return actualEstimatedBudgetBean;
	}

	public void setActualEstimatedBudgetBean(
			TActualEstimatedBudgetBean actualEstimatedBudgetBean) {
		this.actualEstimatedBudgetBean = actualEstimatedBudgetBean;
	}

	public List<TCostBean> getExpenseBeanList() {
		return expenseBeanList;
	}

	public void setExpenseBeanList(List<TCostBean> expenseBeanList) {
		this.expenseBeanList = expenseBeanList;
	}

	public List<TAttachmentBean> getAttachmentBeanList() {
		return attachmentBeanList;
	}

	public void setAttachmentBeanList(List<TAttachmentBean> attachmentBeanList) {
		this.attachmentBeanList = attachmentBeanList;
	}
	
}
