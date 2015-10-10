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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.json.JSONUtility;


public class IntegerMatcherDT extends AbstractMatcherDT {
		
	public IntegerMatcherDT(Integer fieldID) {
		super(fieldID);
	}

	/**
	 * Gets the possible relations for a field type
	 * @param withParameter
	 * @param inTree
	 * @return
	 */
	public List<Integer> getPossibleRelations(boolean withParameter, boolean inTree) {
		List <Integer> relations = new ArrayList<Integer>();
		relations.add(Integer.valueOf(MatchRelations.EQUAL));
		relations.add(Integer.valueOf(MatchRelations.NOT_EQUAL));
		if (inTree) {
			relations.add(Integer.valueOf(MatchRelations.IS_NULL));
		}
		relations.add(Integer.valueOf(MatchRelations.NOT_IS_NULL));
		relations.add(Integer.valueOf(MatchRelations.GREATHER_THAN));
		relations.add(Integer.valueOf(MatchRelations.GREATHER_THAN_EQUAL));
		relations.add(Integer.valueOf(MatchRelations.LESS_THAN));
		relations.add(Integer.valueOf(MatchRelations.LESS_THAN_EQUAL));
		if (withParameter) {
			relations.add(MatcherContext.PARAMETER);
		}
		return relations;
	}
	
	/**
	 * Gets the negated matcher to maintain the backward compatibility with the older
	 * filters where the negation of the leaf node was possible 
	 * @param matcher
	 * @return
	 */
	public int getNegatedMatcher(int matcher) {
		switch (matcher) {
		case MatchRelations.IS_NULL:
			return MatchRelations.NOT_IS_NULL;			
		case MatchRelations.NOT_IS_NULL:
			return MatchRelations.IS_NULL;
		case MatchRelations.EQUAL:	
			return MatchRelations.NOT_EQUAL;
		case MatchRelations.NOT_EQUAL:			
			return MatchRelations.EQUAL;			
		case MatchRelations.GREATHER_THAN:			
			return MatchRelations.LESS_THAN_EQUAL;
		case MatchRelations.LESS_THAN_EQUAL:			
			return MatchRelations.GREATHER_THAN;		
		case MatchRelations.LESS_THAN:
			return MatchRelations.GREATHER_THAN_EQUAL;
		case MatchRelations.GREATHER_THAN_EQUAL:	
			return MatchRelations.LESS_THAN;				
		default: 
			return matcher;
		}
	}
	
	/**
	 * The name of the jsp fragment which contains 
	 * the control for rendering the matcher value
	 * @return
	 */
	public String getMatchValueControlClass() {
		switch (relation) {
		case MatchRelations.EQUAL:
		case MatchRelations.NOT_EQUAL:
		case MatchRelations.GREATHER_THAN:
		case MatchRelations.GREATHER_THAN_EQUAL:
		case MatchRelations.LESS_THAN:
		case MatchRelations.LESS_THAN_EQUAL:		
			return MatchValueJSPNames.NUMBER_MATCHER_CLASS;
		}
		return MatchValueJSPNames.NONE_MATCHER_CLASS;		
	}
	
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
	@Override
	public String getMatchValueJsonConfig(Integer fieldID, String baseName, Integer index, Object value,
			boolean disabled, Object dataSource, Integer[] projectIDs, Integer matcherRelation, Locale locale, Integer personID) {
		StringBuilder stringBuilder = new StringBuilder("{");
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName, index));
		if (value!=null) {
			JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (Integer)value);
		}		
		JSONUtility.appendBooleanValue(stringBuilder, "allowDecimals", false);
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
}