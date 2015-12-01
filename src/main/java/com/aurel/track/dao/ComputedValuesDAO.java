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

import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;

public interface ComputedValuesDAO {
	
	/**
	 * Loads a computedValuesBean from the 
	 * TComputedValues table by primary key
	 * @param objectID
	 * @return
	 */
	TComputedValuesBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table by workItem, types and person
	 * @param workItemID
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	TComputedValuesBean loadByWorkItemAndTypesAndPerson(Integer workItemID, int effortType, int computedValueType, Integer person);
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table for workItems, types and person
	 * @param workItemIDs
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	List<TComputedValuesBean> loadByWorkItemsAndTypesAndPerson(int[] workItemIDs, int effortType, int computedValueType, Integer person);
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table for workItems and types
	 * @param workItemIDs
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	List<TComputedValuesBean> loadByWorkItemsAndTypes(int[] workItemIDs, int effortType, int computedValueType);
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table for workItemIDs, computedValueType and person
	 * @param workItemIDs
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	List<TComputedValuesBean> loadByWorkItemsAndTypesAndPerson(int[] workItemIDs, int computedValueType, Integer person);
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table for workItemIDs and types
	 * @param workItemIDs
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	List<TComputedValuesBean> loadByWorkItemsAndTypes(int[] workItemIDs, int[] computedValueType);
	
	/**
	 * Loads the computed values for a tree filter for a person
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @param computedValueType
	 * @param myExpenses
	 * @return
	 */
	List<TComputedValuesBean> loadByTreeFilterForPerson(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID, int computedValueType, boolean myExpenses);
	
	/**
	 * Loads the computed values for a tree filter
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @param computedValueTypes
	 * @return
	 */
	List<TComputedValuesBean> loadByTreeFilter(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID, int[] computedValueTypes);
	
	/**
	 * Get the computed values for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @param computedValueType
	 * @param myExpenses
	 * @return
	 */
	List<TComputedValuesBean> loadByTQLFilterForPerson(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors, int computedValueType, boolean myExpenses);
	
	/**
	 * Get the computed values for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @param computedValueTypes
	 * @return
	 */
	List<TComputedValuesBean> loadByTQLFilter(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors, int[] computedValueTypes);
	
	/**
	 * Saves a new/existing computedValuesBean in the TComputedValues table
	 * @param computedValuesBean
	 * @return the created optionID
	 */
	Integer save(TComputedValuesBean computedValuesBean);
	
	/**
	 * Deletes a record from the TComputedValues table
	 * @param objectID
	 */
	void delete(Integer objectID);
	
	/**
	 * Deletes all records from the TComputedValues table
	 * @param objectID
	 */
	void deleteAll();	
}
