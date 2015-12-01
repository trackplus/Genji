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

import com.aurel.track.beans.TWorkItemBean;


/**
 * Interface for creating the lucene index for a custom field
 * @author Tamas Ruff
 *
 */
public interface ILucene {
	
	/**
	 * Gets the string value to be stored by lucene
	 * @param value the value of the field
	 * @param workItemBean the lucene value might depend on other fields of the workItem
	 * @return
	 */
	String getLuceneValue(Object value, TWorkItemBean workItemBean);
	
	/**
	 * Whether the field should be stored
	 * Part of lucene indexing
	 * @return
	 */
	int getLuceneStored();
	
	/**
	 * Whether the field should be tokenized
	 * Used by lucene indexing
	 * @return
	 */
	int getLuceneTokenized();
	
	/**
	 * Returns the lookup entity type related to the fieldType
	 * Depending on this type different preprocessing method
	 * of the user entered query string is needed
	 * Used by lucene search
	 * @return
	 */
	int getLookupEntityType();
	
	/**
	 * Whether the field will be used in lucene highlighter
	 */
	boolean isHighlightedField();
}
