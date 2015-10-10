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


package com.aurel.track.fieldType.runtime.matchers.design;

import java.util.List;
import java.util.Locale;

import com.aurel.track.fieldType.runtime.matchers.IMatcherBase;


/**
 * Base interface for configuring a matcher 
 * @author Tamas Ruff
 */
public interface IMatcherDT extends IMatcherBase {
	
	/**
	 * Gets the possible relations for a field type
	 * @param withParameter
	 * @param inTree
	 * @return
	 */
	List<Integer> getPossibleRelations(boolean withParameter, boolean inTree);
	
	/**
	 * Gets the negated matcher to maintain the backward compatibility with the older
	 * filters where the negation of the leaf node was possible 
	 * @param matcher
	 * @return
	 */
	int getNegatedMatcher(int matcher);
	
	/**
	 * The name of the js class which creates 
	 * the control for rendering the matcher value
	 * @return
	 */
	String getMatchValueControlClass();
	
	/**
	 * The json configuration object for configuring the js control containing the value
	 * @param fieldID
	 * @param baseName: the name of the control: important by submit
	 * @param index
	 * @param value: the value to be set by render
	 * @param disabled: whether the control is disabled
	 * @param dataSource: for combos, lists etc. the datasource is loaded in getMatcherDataSource()
	 * @param projectIDs: the selected projects for getting the datasource for project dependent pickers/trees (releases)
	 * @param matcherRelation: the value might depend on matcherRelation
	 * @param locale
	 * @param personID
	 * @return
	 */
	String getMatchValueJsonConfig(Integer fieldID, String baseName, Integer index, Object value,
			boolean disabled, Object dataSource, Integer[] projectIDs, Integer matcherRelation, Locale locale, Integer personID);
}
