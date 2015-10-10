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


package com.aurel.track.dao;

import java.util.Date;
import java.util.List;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TBudgetBean;

/**
 * This defines the Data Access Object (DAO) interface for planned values.
 * @author Tamas Ruff
 *
 */
public interface BudgetDAO {
	/**
	 * Loads the last BudgetBean by workItemKey 
	 * @param objectID
	 * @param plan
	 * @return
	 */
	TBudgetBean loadLastByWorkItem(Integer objectID, boolean plan);
	
	/**
	 * Load all budgets
	 * @return
	 */
	void updateWithoutBudgetType();
		
	/**
	 * Loads a BudgetBean list by workItemKeys
	 * @param workItemKeys
	 * @param changedByPersons
	 * @param plan
	 * @param fromDate
	 * @param toDate	
	 * @return
	 */
	List<TBudgetBean> loadByWorkItemKeys(int[] workItemKeys, List<Integer> changedByPersons, Boolean plan, Date fromDate, Date toDate);
		
	/**
	 * Gets the budgets/plans by tree filter for activity stream
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @param limit
	 * @param fromDate
	 * @param toDate
	 * @param changedByPersons
	 * @param plan
	 * @return
	 */
	List<TBudgetBean> loadloadActivityStreamBugetsPlans(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID,
			Integer limit, Date fromDate, Date toDate, List<Integer> changedByPersons, Boolean plan);
			
	/**
	 * Saves a BudgetBean in the TBudget table
	 * @param budgetBean
	 * @return
	 */
	Integer save(TBudgetBean budgetBean);
	
	/**
	 * Get the budget and planned value history for an item
	 * @param workItemID
	 * @param budgetType
	 * @return
	 */
	List<TBudgetBean> getByWorkItem(Integer workItemID, Integer budgetType);
	
	/**
	 * Gets a list of TBudgetBeans with the last plan value for each workItem
	 * @return
	 */
	List<TBudgetBean> loadLastPlanForWorkItems();
	
	/**
	 * Gets a list of TBudgetBeans with the last plan value for each workItem
	 * @param workItemIDs
	 * @return
	 */
	List<TBudgetBean> loadLastPlanForWorkItems(List<Integer> workItemIDs);
	
	/**
	 * Gets a list of TBudgetBeans by IDs
	 * @param objectIDs
	 * @return
	 */
	List<TBudgetBean> getByIDs(List<Integer> objectIDs);
	
	/**
	 * Gets all indexable budgetBeans
	 * @return
	 */
	List<TBudgetBean> loadAllIndexable();
}
