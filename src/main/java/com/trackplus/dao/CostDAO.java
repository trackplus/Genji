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


package com.trackplus.dao;

import java.util.Date;
import java.util.List;

import com.trackplus.model.Tcomputedvalue;
import com.trackplus.model.Tcost;

/**
 * DAO for Cost
 * 
 * @author Tamas Ruff
 * 
 */
public interface CostDAO {

	/**
	 * Loads an expense by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tcost loadByPrimaryKey(Integer objectid);

	/**
	 * Gets all expenses
	 * 
	 * @return
	 */
	List<Tcost> loadAll();

	/**
	 * Gets all indexable expenses
	 * 
	 * @return
	 */
	List<Tcost> loadAllIndexable();

	/**
	 * Loads a the Costs by workItemKeys Loads all costs independently whether
	 * the user has or not "view all expenses" role The list will be filtered
	 * out later on because it is too costly to filter them for each workItem
	 * 
	 * @param workItemKeys
	 * @param personIDsArr
	 * @param fromDate
	 * @param toDate
	 * @param accounts
	 * @param ascendingDate
	 * @return
	 */
	List<Tcost> loadByWorkItemKeys(int[] workitemkeys, Integer[] personidsarr,
			Date fromDate, Date toDate, List<Integer> accounts,
			boolean ascendingDate);

	/**
	 * Get the total work or total cost expenses for a workItem
	 * 
	 * @param workItemKey
	 * @param work
	 *            work or cost
	 * @return
	 */
	double getSumExpenseByWorkItem(Integer workItemKey, boolean work);

	/**
	 * Gets the total work or total cost expenses for a workItem booked by a
	 * list of persons
	 * 
	 * @param workItemKey
	 * @param persons
	 * @param work
	 *            work or cost
	 * @return
	 */
	double getSumExpenseByWorkItemAndPersons(Integer workItemKey,
			Integer[] persons, boolean work);

	/**
	 * Saves a CostBean in the Tcost table
	 * 
	 * @param costBean
	 * @return
	 */
	Integer save(Tcost cost);

	/**
	 * Deletes a record from the Tcost table
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Get the cost s for a workItem
	 * 
	 * @param workItemID
	 * @param personID
	 *            if specified filter by person
	 * @return
	 */
	List<Tcost> getByWorkitemAndperson(Integer workitemid, Integer personid);

	/**
	 * Gets a list of Tcomputedvalues with the sum of cost/time values grouped
	 * by workitems
	 * 
	 * @return
	 */
	List<Tcomputedvalue> loadExpenseGroupedByWorkitem();

	/**
	 * Gets a list of Tcomputedvalues with the sum of cost/time values grouped
	 * by workitems and persons
	 * 
	 * @return
	 */
	List<Tcomputedvalue> loadExpenseGroupedByWorkitemAndPerson();

	/**
	 * Gets a list of Tcost with the sum of expense values grouped by workitems
	 * 
	 * @param workItemIDs
	 * @return
	 */
	List<Tcost> loadSumExpensesForWorkitems(List<Integer> workitemids);

	/**
	 * Gets a list of Tcost with the sum of expense values grouped by workitems
	 * and persons
	 * 
	 * @param workItemIDs
	 * @return
	 */
	List<Tcost> loadSumExpensesForWorkitemsAndPersons(List<Integer> workitemids);
}
