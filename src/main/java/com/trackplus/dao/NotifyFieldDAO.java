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

import java.util.List;

import com.trackplus.model.Tnotifyfield;

/**
 * The notificaton fields for different RACI roles
 * 
 * @author Tamas Ruff
 * 
 */
public interface NotifyFieldDAO {

	/**
	 * Get the notifyFieldBeans, the person requests notification for, for a
	 * project, actionType and fieldType
	 * 
	 * @param person
	 * @param projectIDs
	 *            typically one single project but if the project itself is
	 *            modified then both the old and the new project
	 * @param actionType
	 * @param fieldType
	 * @return
	 */
	List<Tnotifyfield> getTriggerFieldsForRaciRole(Integer person,
			List<Integer> projectIDs, Integer actionType, Integer fieldType);

	/**
	 * Saves a Tnotifyfield * @param notifyFieldBean
	 * 
	 * @return
	 */
	void save(Tnotifyfield notifyFieldBean);

	/**
	 * Gets the list of edit notifyFieldBeans for a trigger
	 * 
	 * @param triggerID
	 * @return
	 */
	List<Tnotifyfield> getNotifyFieldsForTrigger(Integer triggerID);

	/**
	 * Gets the list of edit notifyFieldBeans by IDs
	 * 
	 * @param notifyTriggerFieldIDs
	 * @return
	 */
	List<Tnotifyfield> getNotifyFieldsByKeys(List<Integer> notifyTriggerFieldIDs);

	/**
	 * Deletes all entries assigned to a triggerID
	 * 
	 * @param triggerID
	 * @return
	 */
	public void deleteByTrigger(Integer triggerID);

	/**
	 * Deletes the field assigned to triggers
	 * 
	 * @param fieldID
	 * @param action
	 *            probably ACTIONTYPE.EDIT_ISSUE
	 * @param fieldType
	 *            probably FIELDTYPE.ISSUE_FIELD
	 */
	public void deleteByField(Integer fieldID, int action, int fieldType);

	/**
	 * Deletes a trigger field by objectID
	 * 
	 * @param objectID
	 */
	public void delete(Integer objectID);
}
