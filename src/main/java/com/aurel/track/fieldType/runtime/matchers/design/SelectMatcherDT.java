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


package com.aurel.track.fieldType.runtime.matchers.design;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.json.JSONUtility;

public class SelectMatcherDT extends AbstractMatcherDT {
	
	public SelectMatcherDT(Integer fieldID) {
		super(fieldID);
	}

	/**
	 * Gets the possible relations for a field type
	 * @param withParameter
	 * @param inTree
	 * @return
	 */
	@Override
	public List<Integer> getPossibleRelations(boolean withParameter, boolean inTree) {
		List <Integer> relations = new ArrayList<Integer>();
		relations.add(Integer.valueOf(MatchRelations.EQUAL));
		relations.add(Integer.valueOf(MatchRelations.NOT_EQUAL));
		relations.add(Integer.valueOf(MatchRelations.IS_NULL));
		relations.add(Integer.valueOf(MatchRelations.NOT_IS_NULL));
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
	@Override
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
		default:
			return matcher;
		}
	}
	
	/**
	 * The name of the jsp fragment which contains 
	 * the control for rendering the matcher value
	 * @return
	 */
	@Override
	public String getMatchValueControlClass() {
		switch (relation) {
		case MatchRelations.EQUAL:
		case MatchRelations.NOT_EQUAL:
			return MatchValueJSPNames.SELECT_MATCHER_CLASS;
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
			Integer intValue = null;
			try {
				Integer[] intArr = (Integer[])value;
				if (intArr!=null && intArr.length>0) {
					intValue = intArr[0];
				}
			} catch (Exception e) {
			}
			JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, intValue);
		}
		List<ILabelBean> labelBeanList = (List<ILabelBean>)dataSource;
		JSONUtility.appendILabelBeanList(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE, labelBeanList);
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
}
