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

import com.aurel.track.beans.TFieldBean;

public interface FieldDAO {
	
	/**
	 * Gets a fieldBean from the TField table
	 * @param objetcID
	 * @return
	 */
	TFieldBean loadByPrimaryKey(Integer objetcID);
	
	/**
	 * Loads a field by name 
	 * @param name
	 * @return
	 */
	List<TFieldBean> loadByName(String name);
	
	/**
	 * Loads the fields by names
	 * @param names
	 * @return
	 */
	List<TFieldBean> loadByNames(List<String> names);
		
	/**
	 * Loads all Fields from table
	 * @return 
	 */
	List<TFieldBean> loadAll();

	/**
	 * Loads all active fields from TField table 
	 * @return 
	 */
	List<TFieldBean> loadActive();
	
	/**
	 * Loads all custom fields from TField table
	 * @return
	 */
	List<TFieldBean> loadCustom();
	
	/**
	 * Loads all system fields from TField table
	 * @return
	 */
	List<TFieldBean> loadSystem();
	
	/**
	 * Loads all filter fields from TField table (present in the upper part filter)
	 * @return
	 */
	List<TFieldBean> loadFilterFields();
	
	/**
	 * Loads all custom fieldBeans which are specified for a workItem 
	 * @param workItemID
	 * @return
	 */
	List<TFieldBean> loadSpecifiedCustomFields(Integer workItemID);
	
	/**
	 * Returns whether the name is unique for the field
	 * @param name
	 * @param fieldID
	 * @return
	 */
	boolean isNameUnique(String name, Integer fieldID);
	
	/**
	 * saves a field in the TField table
	 * @param fieldBean
	 * @return
	 */
	Integer save(TFieldBean fieldBean);
	
	/**
	 * Deletes a field from the TField table
	 * @param fieldId
	 */
	void delete(Integer fieldId);
	
	/**
	 * Verifies whether a field is deletable
	 * @param fieldId
	 */
	boolean isDeletable(Integer fieldId);

	/**
	 * Sets the deprecated flag
	 * @param fieldId
	 */
	void setDeprecated(Integer fieldID, boolean deactivate);
	
	/**
	 * Load all fields from screen
	 * @param screenID
	 * @return
	 */
	List<TFieldBean> loadAllFields(Integer screenID);

	List<TFieldBean> loadAllFieldsOnCard(Integer cardPanelID);

	/**
	 * Load all custom fields from screen
	 * @param screenID
	 * @return
	 */
	List<TFieldBean> loadAllCustomFields(Integer screenID);
	
	/**
	 * Load the custom fields from tab
	 * @param screenTabID
	 * @return
	 */
	List<TFieldBean> loadCustomFieldsFromTab(Integer screenTabID);
	
	/**
	 * Load all fields from screens
	 * @param screenIDs
	 * @return
	 */
	List<TFieldBean> loadByScreens(Object[] screenIDs);
	
	/**
	 * Load fields by fieldID 
	 * @param fieldIDs
	 * @return
	 */
	List<TFieldBean> loadByFieldIDs(Object[] fieldIDs);
	
	/**
	 * Load the fields defined only in a project type
	 * @return
	 */
	List<TFieldBean> loadByProjectType(Integer projectTypeID);
	
	/**
	 * Load the fields defined only in a project
	 * @return
	 */
	List<TFieldBean> loadByProject(Integer projectID);
}
