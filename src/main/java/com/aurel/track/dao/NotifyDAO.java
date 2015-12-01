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
import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;



public interface NotifyDAO {
	
	/**
	 * Whether the person has a specific raci Role for a workItem directly (not through group)
	 * @param workItemKey
	 * @param personKey
	 * @param raciRole if null it doesn't matter
	 * @return
	 * @throws Exception
	 */
	boolean hasDirectRaciRole(Integer workItemKey, Integer personKey, String raciRole);
	
	/**
	 * Whether any person from list has a specific raci Role for a workItem directly (not through group)
	 * @param workItemKey
	 * @param personIDs
	 * @param raciRole if null it doesn't matter
	 * @return
	 * @throws Exception
	 */
	boolean anyPersonHasDirectRaciRole(Integer workItemKey, List<Integer> personIDs, String raciRole);
	
	/**
	 * Whether any person from list has a specific raci Role for a workItem through group
	 * @param workItemKey
	 * @param personIDs
	 * @param raciRole if null it doesn't matter
	 * @return
	 * @throws Exception
	 */
	boolean anyPersonHasIndirectRaciRole(Integer workItemKey, List<Integer> personIDs, String raciRole);
	
	/**
	 * Deletes a raci role for a list of persons for a workItem
	 * @param workItemKey
	 * @param selectedConsultants
	 * @param raciRole
	 */
	void deleteByWorkItemAndPersonsAndRaciRole(Integer workItemKey, Integer[] selectedPersons, String raciRole);
	
	/**
	 * Deletes all persons from a raci role for a workItem
	 * @param workItemKey	
	 * @param raciRole
	 */
	void deleteByWorkItemAndRaciRole(Integer workItemKey, String raciRole);
	
	/**
	 * Inserts a raci role for a person in a workItem
	 * First verifies whether the role does exist already, 
	 * and if yes do not adds it again
	 * @param workItemKey
	 * @param person
	 * @param raciRole
	 */
	void insertByWorkItemAndPersonAndRaciRole(Integer workItemKey, Integer person, String raciRole);
	
	/**
	 * Gets the watchers filtered by filterSelectsTO and raciBean
	 * @param filterSelectsTO
	 * @param raciBean
	 * @param personID
	 * @return
	 */
	List<TNotifyBean> loadTreeFilterWatchers(FilterUpperTO filterSelectsTO, RACIBean raciBean, Integer personID);
	
	/**
	 * Get the watchers for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @return
	 */
	List<TNotifyBean> loadTQLFilterWatchers(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors);
	
	/**
	 * Loads the list of TNotifyBeans associated with items
	 * @param workItemIDs
	 * @param raciRole
	 * @return
	 */
	List<TNotifyBean> loadWatcherByItems(List<Integer> workItemIDs, String raciRole);
	
	/**
	 * Loads the list of TNotifyBeans associated with an array of workItems
	 * @param workItemIDs
	 * @return
	 */
	List<TNotifyBean> loadLuceneConsInf(int[] workItemIDs);
	
	/**
	 * Gets the list of TNotifyBeans by a list of workItemIDs 
	 * for which the person (or a group it belongs to) has consultant or informant role 
	 * @param workItemIDs
	 * @param personIDs
	 * @return
	 */
	List<TNotifyBean> getByWorkItemsAndPersons(List<Integer> workItemIDs, List<Integer> personIDs);

	/**
	 * Counts the direct watcher persons for an item
	 * @param workItemID
	 * @return
	 */
	int countDirectWatcherPersons(Integer workItemID);
}
