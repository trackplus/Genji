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

import com.trackplus.model.Tfield;

public interface FieldDAO {

	/**
	 * Gets a fieldBean from the TField table
	 * 
	 * @param objetcID
	 * @return
	 */
	Tfield loadByPrimaryKey(Integer objetcID);

	/**
	 * Loads a field by name
	 * 
	 * @return
	 */
	List<Tfield> loadByName(String name);

	/**
	 * Loads all Fields from table
	 * 
	 * @return
	 */
	List<Tfield> loadAll();

	/**
	 * Loads all active fields from TField table
	 * 
	 * @return
	 */
	List<Tfield> loadActive();

	/**
	 * Loads all custom fields from TField table
	 * 
	 * @return
	 */
	List<Tfield> loadCustom();

	/**
	 * Loads all system fields from TField table
	 * 
	 * @return
	 */
	List<Tfield> loadSystem();

	/**
	 * Loads all filter fields from TField table (present in the upper part
	 * filter)
	 * 
	 * @return
	 */
	List<Tfield> loadFilterFields();

	/**
	 * Loads all custom fieldBeans which are specified for a workItem
	 * 
	 * @param workItemID
	 * @return
	 */
	List<Tfield> loadSpecifiedCustomFields(Integer workItemID);

	/**
	 * Returns whether the name is unique for the field
	 * 
	 * @param name
	 * @param fieldID
	 * @return
	 */
	boolean isNameUnique(String name, Integer fieldID);

	/**
	 * saves a field in the TField table
	 * 
	 * @param fieldBean
	 * @return
	 */
	Integer save(Tfield fieldBean);

	/**
	 * Deletes a field from the TField table
	 * 
	 * @param fieldId
	 */
	void delete(Integer fieldId);

	/**
	 * Verifies whether a field is deletable
	 * 
	 * @param fieldId
	 */
	boolean isDeletable(Integer fieldId);

	/**
	 * Sets the deprecated flag
	 * 
	 * @param fieldId
	 */
	void setDeprecated(Integer fieldID, boolean deactivate);

	/**
	 * Load all fields from screen
	 * 
	 * @param screenID
	 * @return
	 */
	public List<Tfield> loadAllFields(Integer screenID);

	/**
	 * Load all custom fields from screen
	 * 
	 * @param screenID
	 * @return
	 */
	public List<Tfield> loadAllCustomFields(Integer screenID);

	/**
	 * Load the custom fields from tab
	 * 
	 * @param screenTabID
	 * @return
	 */
	public List<Tfield> loadCustomFieldsFromTab(Integer screenTabID);

	/**
	 * Load all fields from screens
	 * 
	 * @param screenIDs
	 * @return
	 */
	public List<Tfield> loadByScreens(Object[] screenIDs);

	/**
	 * Load fields by fieldID
	 * 
	 * @param fieldIDs
	 * @return
	 */
	public List<Tfield> loadByFieldIDs(Object[] fieldIDs);
}
