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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.json.JSONUtility;

public class DateMatcherDT extends AbstractMatcherDT {

	
	public DateMatcherDT(Integer fieldID) {
		super(fieldID);
	}

	
	/**
	 * The name of the jsp fragment which contains 
	 * the control for rendering the matcher value
	 * @return
	 */
	@Override
	public String getMatchValueControlClass() {
		switch (relation) {
		case MatchRelations.MORE_THAN_DAYS_AGO:
		case MatchRelations.MORE_THAN_EQUAL_DAYS_AGO:
		case MatchRelations.LESS_THAN_DAYS_AGO:
		case MatchRelations.LESS_THAN_EQUAL_DAYS_AGO:
		case MatchRelations.IN_MORE_THAN_DAYS:
		case MatchRelations.IN_MORE_THAN_EQUAL_DAYS:
		case MatchRelations.IN_LESS_THAN_DAYS:
		case MatchRelations.IN_LESS_THAN_EQUAL_DAYS:
			return MatchValueJSPNames.NUMBER_MATCHER_CLASS;
		case MatchRelations.EQUAL_DATE:
		case MatchRelations.NOT_EQUAL_DATE:
		case MatchRelations.GREATHER_THAN_DATE:
		case MatchRelations.GREATHER_THAN_EQUAL_DATE:
		case MatchRelations.LESS_THAN_DATE:
		case MatchRelations.LESS_THAN_EQUAL_DATE:
			return MatchValueJSPNames.DATE_MATCHER_CLASS;
		}
		return MatchValueJSPNames.NONE_MATCHER_CLASS;
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
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		if (!fieldTypeRT.isCustom() || inTree) {
			relations.add(Integer.valueOf(MatchRelations.IS_NULL));
		}
		relations.add(Integer.valueOf(MatchRelations.NOT_IS_NULL));
		relations.add(Integer.valueOf(MatchRelations.EQUAL_DATE));
		relations.add(Integer.valueOf(MatchRelations.NOT_EQUAL_DATE));
		
		relations.add(Integer.valueOf(MatchRelations.THIS_WEEK));
		relations.add(Integer.valueOf(MatchRelations.LAST_WEEK));
		relations.add(Integer.valueOf(MatchRelations.THIS_MONTH));
		relations.add(Integer.valueOf(MatchRelations.LAST_MONTH));
		
		relations.add(Integer.valueOf(MatchRelations.MORE_THAN_DAYS_AGO));
		relations.add(Integer.valueOf(MatchRelations.LESS_THAN_DAYS_AGO));
		
		relations.add(Integer.valueOf(MatchRelations.IN_MORE_THAN_DAYS));
		relations.add(Integer.valueOf(MatchRelations.IN_LESS_THAN_DAYS));
		
		relations.add(Integer.valueOf(MatchRelations.GREATHER_THAN_DATE));
		relations.add(Integer.valueOf(MatchRelations.LESS_THAN_DATE));
	
		relations.add(Integer.valueOf(MatchRelations.LATER_AS_LASTLOGIN));
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
		case MatchRelations.EQUAL_DATE:	
			return MatchRelations.NOT_EQUAL_DATE;
		case MatchRelations.NOT_EQUAL_DATE:
			return MatchRelations.EQUAL_DATE;
		case MatchRelations.MORE_THAN_DAYS_AGO:
			return MatchRelations.LESS_THAN_DAYS_AGO;
		case MatchRelations.LESS_THAN_DAYS_AGO:
			return MatchRelations.MORE_THAN_DAYS_AGO;
		case MatchRelations.IN_MORE_THAN_DAYS:
			return MatchRelations.IN_LESS_THAN_DAYS;
		case MatchRelations.IN_LESS_THAN_DAYS:
			return MatchRelations.IN_MORE_THAN_DAYS;
		case MatchRelations.GREATHER_THAN_DATE:
			return MatchRelations.LESS_THAN_DATE;
		case MatchRelations.LESS_THAN_DATE:
			return MatchRelations.GREATHER_THAN_DATE;
		default:
			//for all others there is no backward compatibility for leaf negation 
			return matcher;
		}
	}
	
	/**
	 * Deprecated relations with equals
	 * @param relation the relation to set
	 */
	@Override
	public void setRelation(int relation) {
		switch (relation) {
			case MatchRelations.MORE_THAN_EQUAL_DAYS_AGO:
				this.relation = MatchRelations.MORE_THAN_DAYS_AGO;
				break;
			case MatchRelations.LESS_THAN_EQUAL_DAYS_AGO:
				this.relation = MatchRelations.LESS_THAN_DAYS_AGO;
				break;
			case MatchRelations.IN_MORE_THAN_EQUAL_DAYS:
				this.relation = MatchRelations.IN_MORE_THAN_DAYS;
				break;
			case MatchRelations.IN_LESS_THAN_EQUAL_DAYS:
				this.relation = MatchRelations.IN_LESS_THAN_DAYS;
				break;
			case MatchRelations.GREATHER_THAN_EQUAL_DATE:
				this.relation = MatchRelations.GREATHER_THAN_DATE;
				break;
			case MatchRelations.LESS_THAN_EQUAL_DATE:
				this.relation = MatchRelations.LESS_THAN_DATE;
				break;
		default:
			this.relation = relation;
			break;
		}
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
			switch (relation) {
			case MatchRelations.MORE_THAN_DAYS_AGO:
			case MatchRelations.MORE_THAN_EQUAL_DAYS_AGO:
			case MatchRelations.LESS_THAN_DAYS_AGO:
			case MatchRelations.LESS_THAN_EQUAL_DAYS_AGO:
			case MatchRelations.IN_MORE_THAN_DAYS:
			case MatchRelations.IN_MORE_THAN_EQUAL_DAYS:
			case MatchRelations.IN_LESS_THAN_DAYS:
			case MatchRelations.IN_LESS_THAN_EQUAL_DAYS:
				JSONUtility.appendBooleanValue(stringBuilder, "allowDecimals", false);
				if (value!=null) {
					try {
						JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (Integer)value);
					} catch (Exception e) {
					}
				}
				break;
			case MatchRelations.EQUAL_DATE:
			case MatchRelations.NOT_EQUAL_DATE:
			case MatchRelations.GREATHER_THAN_DATE:
			case MatchRelations.GREATHER_THAN_EQUAL_DATE:
			case MatchRelations.LESS_THAN_DATE:
			case MatchRelations.LESS_THAN_EQUAL_DATE:
				if (value!=null) {
					try {
						JSONUtility.appendDateValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (Date)value);
					} catch (Exception e) {
					}
				}
				/*JSONUtility.appendStringValue(stringBuilder,
						"format", DateTimeUtils.getInstance().getExtJSDateFormat(locale));
				JSONUtility.appendStringValue(stringBuilder,
						"submitFormat", "Y-m-d H:i:s.u");*/
				break;
			}
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

}
