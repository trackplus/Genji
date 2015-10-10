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

import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;

/**
 * This is the Data Access Object (DAO) interface for the actual estimated
 * remaining work and cost for an issue.
 * @author Tamas Ruff
 *
 */
public interface ActualEstimatedBudgetDAO {
	/**
	 * Loads the last ActualEstimatedBudgetBean by workItemKey 
	 * @param objectID
	 * @return
	 */
	TActualEstimatedBudgetBean loadByWorkItemKey(Integer objectID);
		
	/**
	 * Loads a ActualEstimatedBudgetBean list by workItemKeys
	 * @param workItemKeys
	 * @return
	 */
	List<TActualEstimatedBudgetBean> loadByWorkItemKeys(int[] workItemKeys);

	/**
	 * Loads the remaining values for a tree filter
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @param computedValueTypes
	 * @return
	 */
	List<TActualEstimatedBudgetBean> loadByTreeFilter(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID);
	
	/**
	 * Get the remaining values for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @param computedValueTypes
	 * @return
	 */
	List<TActualEstimatedBudgetBean> loadByTQLFilter(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors);
		
	/**
	 * Saves an ActualEstimatedBudgetBean in the TActualEstimatedBudget table
	 * @param baseLineBean
	 * @return
	 */
	Integer save(TActualEstimatedBudgetBean actualEstimatedbudgetBean);
	
	/**
	 * Deletes a remaining plan by primary key
	 * @param workItemID
	 */
	void deleteByWorkItem(Integer workItemID);
}
