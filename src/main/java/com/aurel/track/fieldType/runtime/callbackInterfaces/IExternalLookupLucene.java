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

package com.aurel.track.fieldType.runtime.callbackInterfaces;

import java.util.List;
import java.util.Map;

/**
 * Fields whose datasource is remotely filtered
 * @author Tamas Ruff
 *
 */
public interface IExternalLookupLucene {
	
	/**
	 * Gets all external lookup objects to index
	 * @return
	 */
	List getAllExternalLookups();
	
	/**
	 * Gets the searchable lucene (sub)field names
	 */
	String[] getSearchableFieldNames();
	
	/**
	 * Gets the field names to values map for an external lookup instance 
	 * @param externalLookupObject
	 * @return
	 */
	Map<String, String> getUnfoldedSearchFields(Object externalLookupObject);

	/**
	 * Gets the lookup object ID
	 * @param externalLookupObject
	 * @return
	 */
	Integer getLookupObjectID(Object externalLookupObject);
}
