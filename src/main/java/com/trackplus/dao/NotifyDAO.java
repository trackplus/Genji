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


package com.trackplus.dao;

import java.util.List;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.trackplus.model.Tnotify;

public interface NotifyDAO {

	/**
	 * Whether the person has a specific raci Role for a workItem (direct or
	 * through a group)
	 * 
	 * @param workItemKey
	 * @param personKey
	 * @param raciRole
	 *            if null it doesn't matter
	 * @return
	 * @throws Exception
	 */
	boolean hasRaciRole(Integer workItemKey, Integer personKey, String raciRole);

	/**
	 * Whether the person has a specific raci Role for a workItem directly (not
	 * through group)
	 * 
	 * @param workItemKey
	 * @param personKey
	 * @param raciRole
	 *            if null it doesn't matter
	 * @return
	 * @throws Exception
	 */
	boolean hasDirectRaciRole(Integer workItemKey, Integer personKey,
			String raciRole);

	/**
	 * Deletes a raci role for a list of persons for a workItem
	 * 
	 * @param workItemKey
	 * @param selectedConsultants
	 * @param raciRole
	 */
	void deleteByWorkItemAndPersonsAndRaciRole(Integer workItemKey,
			Integer[] selectedPersons, String raciRole);

	/**
	 * Deletes all persons from a raci role for a workItem
	 * 
	 * @param workItemKey
	 * @param raciRole
	 */
	void deleteByWorkItemAndRaciRole(Integer workItemKey, String raciRole);

	/**
	 * Inserts a raci role for a person in a workItem First verifies whether the
	 * role does exist already, and if yes do not adds it again
	 * 
	 * @param workItemKey
	 * @param person
	 * @param raciRole
	 */
	void insertByWorkItemAndPersonAndRaciRole1(Integer workItemKey,
			Integer person, String raciRole);

	/**
	 * Loads the list of TNotifyBeans for all the workItems the person is
	 * manager for
	 * 
	 * @param personID
	 */
	List<Tnotify> loadManagerConsInf(Integer personID);

	/**
	 * Loads the list of TNotifyBeans for all the workItems the person is
	 * responsible for
	 * 
	 * @param personID
	 */
	List<Tnotify> loadResponsibleConsInf(Integer personID);

	/**
	 * Loads the list of TNotifyBeans for all the workItems the person is
	 * originator for
	 * 
	 * @param personID
	 */
	List<Tnotify> loadReporterConsInf(Integer personID);

	/**
	 * Loads the list of TNotifyBeans for all the workItems the person is
	 * originator or manager or responsible for
	 * 
	 * @param personID
	 */
	List<Tnotify> loadMyConsInf(Integer personID);

	/**
	 * Loads the list of TNotifyBeans filtered by the FilterSelectsTO
	 * 
	 * @param filterSelectsTO
	 * @return
	 */
	List<Tnotify> loadCustomReportConsInf(FilterUpperTO filterSelectsTO);

	/**
	 * Loads the list of TNotifyBeans associated with an array of workItems
	 * 
	 * @param workItemIDs
	 * @return
	 */
	List<Tnotify> loadLuceneConsInf(int[] workItemIDs);
}
