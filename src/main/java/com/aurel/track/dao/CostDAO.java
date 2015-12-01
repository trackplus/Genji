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


package com.aurel.track.dao;

import java.util.Date;
import java.util.List;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TCostBean;

/**
 * DAO for Cost
 * @author Tamas Ruff
 *
 */
public interface CostDAO {
	
	/**
	 * Loads an expense by primary key
	 * @param objectID
	 * @return
	 */
	TCostBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Gets all expenses
	 * @return
	 */
	List<TCostBean> loadAll();
	
	/**
	 * Gets all indexable expenses
	 * @return
	 */
	List<TCostBean> loadAllIndexable();
	
	/**
	 * Loads the cost beans by objectIDs
	 * @param objectIDs
	 * @return
	 */
	List<TCostBean> loadByKeys(List<Integer> objectIDs);
	
	/**
	 * Loads a the CostBeans by workItemKeys
	 * Loads all costs independently whether the user has or not "view all expenses" role
	 * The list will be filtered out later on because it is too costly to filter them for each workItem 
	 * @param workItemKeys 
	 * @param personIDsArr
	 * @param fromDate
	 * @param toDate
	 * @param accounts
	 * @param ascendingDate
	 * @return
	 */
	List<TCostBean> loadByWorkItemKeys(int[] workItemKeys, Integer[] personIDsArr, 
			Date fromDate, Date toDate, List<Integer> accounts, boolean ascendingDate);

	/**
	 * Gets the costs by tree filter for activity stream
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @param limit
	 * @param fromDate
	 * @param toDate
	 * @param changeTypes
	 * @param changedByPersons
	 * @return
	 */
	List<TCostBean> loadActivityStreamCosts(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID,
			Integer limit, Date fromDate, Date toDate, List<Integer> changedByPersons); 
			
	/**
	 * Get the total work or total cost expenses for a workItem 
	 * @param workItemKey
	 * @param work work or cost
	 * @return
	 */
	double getSumExpenseByWorkItem(Integer workItemKey, boolean work);
	
	/**
	 * Gets the total work or total cost expenses for a workItem booked by a list of persons
	 * @param workItemKey
	 * @param persons
	 * @param work work or cost
	 * @return
	 */
	double getSumExpenseByWorkItemAndPersons(Integer workItemKey, Integer[] persons, boolean work);
	
	/**
	 * Saves a CostBean in the TCost table
	 * @param costBean
	 * @return
	 */
	Integer save(TCostBean costBean);

	/**
	 * Deletes a record from the TCost table
	 * @param objectID
	 */
	void delete(Integer objectID);
	
	/**
	 * Get the cost beans for a workItem 
	 * @param workItemID
	 * @param personID if specified filter by person
	 * @return
	 */
	List<TCostBean> getByWorkItemAndPerson(Integer workItemID, Integer personID);
	
	/**
	 * Gets a list of TComputedValuesBean with the sum of cost/time values grouped by workItems 
	 * @return
	 */
	List<TComputedValuesBean> loadExpenseGroupedByWorkItem();
	
	/**
	 * Gets a list of TComputedValuesBean with the sum of cost/time values grouped by workItems and persons
	 * @return
	 */
	List<TComputedValuesBean> loadExpenseGroupedByWorkItemAndPerson();
	
	/**
	 * Gets a list of TCostBean with the sum of expense values grouped by workItems
	 * @param workItemIDs 
	 * @return
	 */
	List<TCostBean> loadSumExpensesForWorkItems(List<Integer> workItemIDs);
	
	/**
	 * Gets a list of TCostBean with the sum of expense values grouped by workItems and persons
	 * @param workItemIDs
	 * @return
	 */
	List<TCostBean> loadSumExpensesForWorkItemsAndPersons(List<Integer> workItemIDs);
	
}
