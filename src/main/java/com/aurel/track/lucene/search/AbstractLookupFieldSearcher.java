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

package com.aurel.track.lucene.search;

import java.util.Locale;

import org.apache.lucene.analysis.Analyzer;

/**
 * Abstract base class for searching the lookup fields
 * @author Tamas Ruff
 *
 */
public abstract class AbstractLookupFieldSearcher implements ILookupFieldSearcher {
	/**
	 * Get the OR separated IDs which match the specified field's user entered string
	 * @param analyzer
	 * @param fieldName
	 * @param fieldValue
	 * @param fieldID
	 * @param locale
	 * @return
	 */
	protected abstract String searchExplicitField(Analyzer analyzer,
			String fieldName, String fieldValue, Integer fieldID, Locale locale);
	/**
	 * Get the OR separated IDs which match the user entered string
	 * @param analyzer
	 * @param fieldValue
	 * @param locale
	 * @return
	 */
	protected abstract String searchNoExplicitField(Analyzer analyzer,
			String fieldValue, Locale locale);
}
