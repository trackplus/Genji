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


package com.aurel.track.admin.customize.lists;

import java.util.Locale;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;

/**
 * Base business logic interface for list or list options
 * Contains the CRUD methods for lists and list entries (load, save, delete etc.)
 * @author Tamas Ruff
 *
 */
public interface ListOrOptionBaseBL {
	
	public interface ENTRY_TYPE {
		public static int SYSTEM_OPTION = 1;
		public static int CUSTOM_OPTION = 2;
		public static int GLOBAL_LIST = 3;
		public static int PROJECT_LIST = 4;
	}
	
	/**
	 * Gets the type of the entry
	 * @param repository
	 * @return
	 */
	int getEntityType(Integer repository);	
	
	/**
	 * Creates a detail transfer object
	 * @param optionID
	 * @param listID 
	 * @param add
	 * @param copy
	 * @param personBean
	 * @param locale
	 * @return
	 */
	DetailBaseTO loadDetailTO(Integer optionID, Integer listID,
			boolean add, boolean copy, TPersonBean personBean, Locale locale);
	
	/**
	 * Gets the bean by optionID and list or creates a new bean
	 * @param optionID
	 * @param listID
	 * @return
	 */
	ILabelBean getLabelBean(Integer optionID, Integer listID);
	
	/**
	 * Gets the localized bean by optionID and list or creates a new bean
	 * @param optionID
	 * @param listID
	 * @param locale
	 * @return
	 */
	ILabelBean getLocalizedLabelBean(Integer optionID, Integer listID, Locale locale);
	
	
	/**
	 * Saves a new/modified list entry
	 * If sortOrder is not set it will be set to be the next available sortOrder
	 * @param labelBean
	 * @param copy
	 * @param personID
	 * @param locale
	 * @return
	 */
	Integer save(ILabelBean labelBean, boolean copy, Integer personID, Locale locale);
	
	/**
	 * Whether the list entry has dependent data
	 * @param objectID
	 * @return
	 */
	boolean hasDependentData(Integer objectID);
	
	/**
	 * Deletes an list entry
	 * @param objectID
	 */
	void delete(Integer objectID);
}
