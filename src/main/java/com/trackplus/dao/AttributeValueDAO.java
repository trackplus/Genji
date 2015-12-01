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

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.trackplus.model.Tattributevalue;

/**
 * This defines the Data Access Object (DAO) interface for custom fields.
 * 
 */
public interface AttributeValueDAO {

	/**
	 * Loads an attributeValueBean from the Tattributevalue table by primary key
	 * 
	 * @param objetcID
	 * @return
	 */
	Tattributevalue loadByPrimaryKey(Integer objetcID);

	/**
	 * Loads an attributeValueBean from the Tattributevalue table by field,
	 * parameter and workitem
	 * 
	 * @param field
	 * @param parameterCode
	 * @param workItem
	 * @return
	 */
	Tattributevalue loadBeanByFieldAndWorkItemAndParameter(Integer field,
			Integer workItem, Integer parameterCode);

	/**
	 * Loads an Integer[] of optin lists from the Tattributevalue table by
	 * field, parameter and workitem Typically used for multiple selects
	 * 
	 * @param field
	 * @param parameterCode
	 * @param workItem
	 * @param customOption
	 *            whether the customoptionid or the systemoptionid will be
	 *            gathered
	 * @return
	 */
	// Integer[] loadIDsByFieldAndWorkItemAndParameter(Integer field, Integer

	/**
	 * Loads a list of attributeValueBeans from the Tattributevalue table by
	 * workitem
	 * 
	 * @param workItem
	 * @return
	 */
	List<Tattributevalue> loadByWorkItem(Integer workItemID);

	/**
	 * Loads a list of user picker attributes by workItem
	 * 
	 * @param workItem
	 * @return
	 */
	List<Tattributevalue> loadUserPickerByWorkItem(Integer workItemID);

	/**
	 * Gets the user picker attributes by projects/releases
	 * 
	 * @param projectReleaseIDList
	 * @param isProject
	 * @return
	 */
	List<Tattributevalue> getProjectIDsReleaseIDsUserPickerAttributes(
			List<Integer> projectReleaseIDList, boolean isProject);

	/**
	 * Gets the user picker attributes by FilterSelectsTo
	 * 
	 * @param filterSelectsTo
	 * @return
	 */
	List<Tattributevalue> getCustomFilterUserPickerAttributes(
			FilterUpperTO filterselectsto);

	/**
	 * Gets the user picker attributes by an array of workItemIDs
	 * 
	 * @param allWorkItemIDs
	 * @return
	 */
	List<Tattributevalue> getLuceneUserPickerAttributes(int[] allWorkItemIDs);

	/**
	 * Loads a list of attributeValueBeans from the Tattributevalue table by an
	 * array of workitemids
	 * 
	 * @param workItemIDs
	 * @return
	 */
	List<Tattributevalue> loadByWorkItemKeys(int[] workItemIDs);

	/**
	 * Get the attributeValueBeans associated with workItems the person is
	 * manager for
	 * 
	 * @param personID
	 * @return
	 */
	List<Tattributevalue> loadManagerAttributeValues(Integer personID);

	/**
	 * Get the attributeValueBeans associated with workItems the person is
	 * responsible for
	 * 
	 * @param personID
	 * @return
	 */
	List<Tattributevalue> loadResponsibleAttributeValues(Integer personID);

	/**
	 * Get the attributeValueBeans associated with workItems the person is
	 * originator for
	 * 
	 * @param personID
	 * @return
	 */
	List<Tattributevalue> loadReporterAttributeValues(Integer personID);

	/**
	 * Get the attributeValueBeans associated with workItems the person is
	 * manager or responsible or owner for
	 * 
	 * @param personID
	 * @return
	 */
	List<Tattributevalue> loadMyAttributeValues(Integer personID);

	/**
	 * Get the attributeValueBeans filtered by the FilterSelectsTo
	 * 
	 * @param filterSelectsTo
	 * @return
	 */
	List<Tattributevalue> loadCustomReportAttributeValues(
			FilterUpperTO filterselectsto);

	/**
	 * Get the custom option type attributeValueBeans for an array of
	 * workItemIDs
	 * 
	 * @param workItemIDs
	 * @return
	 */
	List<Tattributevalue> loadLuceneCustomOptionAttributeValues(
			int[] workItemIDs);

	/**
	 * Saves an attributeValueBean in the Tattributevalue table
	 * 
	 * @param Tattributevalue
	 * @return
	 */
	Integer save(Tattributevalue attributevalue);

	/**
	 * Deletes an attributeValueBean(s) from the Tattributevalue table by field
	 * and workitem
	 * 
	 * @param fieldID
	 * @param parameterCode
	 * @param workItemID
	 */
	void delete(Integer fieldID, Integer parameterCode, Integer workItemID);

}
