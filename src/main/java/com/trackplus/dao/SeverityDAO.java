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

import com.trackplus.model.Tseverity;

/**
 * DAO for Severity
 * 
 * @author Tamas Ruff
 * 
 */
public interface SeverityDAO {
	/**
	 * Loads a severityBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tseverity loadByPrimaryKey(Integer objectID);

	/**
	 * Gets an severityBean by label
	 * 
	 * @param label
	 * @return
	 */
	List<Tseverity> loadByLabel(String label);

	/**
	 * Loads all priorityBeans
	 * 
	 * @return
	 */
	List<Tseverity> loadAll();

	/**
	 * Loads the severities by IDs
	 * 
	 * @param severityIDs
	 */
	List<Tseverity> loadBySeverityIDs(List<Integer> severityIDs);

	/**
	 * Loads the severityBeans for a project and issueType
	 * 
	 * @param projectID
	 * @param issueType
	 * @return
	 */
	List<Tseverity> loadByProjectAndIssueType(Integer projectID,
			Integer issueType);

	/**
	 * Gets the next available sortorder
	 * 
	 * @return
	 */
	Integer getNextSortOrder();

	/**
	 * Saves a severityBean in the TSeverity table
	 * 
	 * @param severityBean
	 * @return
	 */
	Integer save(Tseverity severityBean);

	/**
	 * Whether the severity is used as foreign key
	 * 
	 * @param objectID
	 * @return
	 */
	boolean hasDependentData(Integer objectID);

	/**
	 * Deletes a severityBean from the TSeverity table
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Replaces the dependences with a newSeverityID and deletes the
	 * oldSeverityID from the TSeverity table
	 * 
	 * @param oldSeverityID
	 * @param newSeverityID
	 */
	void replace(Integer oldSeverityID, Integer newSeverityID);

	/**
	 * Returns the sort order column name
	 * 
	 * @return
	 */
	String getSortOrderColumn();

	/**
	 * Returns the table name
	 * 
	 * @return
	 */
	String getTableName();
}
