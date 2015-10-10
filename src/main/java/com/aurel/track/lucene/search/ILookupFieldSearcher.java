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

package com.aurel.track.lucene.search;

import java.util.Locale;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;

/**
 * Base interface for searching strings not directly contained in the workItems
 * (list labels and strings in associated entities) 
 * @author Tamas Ruff
 *
 */
public interface ILookupFieldSearcher {
	/**
	 * Preprocess an explicit field
	 * @param analyzer 
	 * @param toBeProcessedString a part of the user entered query string
	 * @param fieldName the name of the user entered field
	 * @param fieldID
	 * @param locale
	 * @param indexStart the index to start looking for fieldName
	 * @return
	 */
	String preprocessExplicitField(Analyzer analyzer, 
			String toBeProcessedString, Locale locale, int indexStart);
	/**
	 * Preprocess the toBeProcessedString when no field is specified
	 * @param analyzer
	 * @param toBeProcessedString
	 * @param locale
	 * @return
	 */
	Query getNoExplicitFieldQuery(Analyzer analyzer, String toBeProcessedString, Locale locale);
}
