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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.json.JSONUtility;

public class CompositeSelectMatcherDT extends AbstractMatcherDT {

	
	
	public CompositeSelectMatcherDT(Integer fieldID) {
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
		relations.add(Integer.valueOf(MatchRelations.PARTIAL_MATCH));
		relations.add(Integer.valueOf(MatchRelations.PARTIAL_NOTMATCH));
		if (inTree) {
			relations.add(Integer.valueOf(MatchRelations.IS_NULL));
		}
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
		case MatchRelations.EQUAL:	
			return MatchRelations.NOT_EQUAL;
		case MatchRelations.NOT_EQUAL:			
			return MatchRelations.EQUAL;
		case MatchRelations.IS_NULL:
			return MatchRelations.NOT_IS_NULL;			
		case MatchRelations.NOT_IS_NULL:
			return MatchRelations.IS_NULL;
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
		case MatchRelations.PARTIAL_MATCH:
		case MatchRelations.PARTIAL_NOTMATCH:
			return MatchValueJSPNames.COMPOSITE_SELECT_MATCHER_CLASS;
		}
		return MatchValueJSPNames.NONE_MATCHER_CLASS;		
	}
	
	/**
	 * Builds the name of the client side controls for submit
	 * @param baseName
	 * @param index
	 * @return
	 */
	protected static String getNameWithMergedKey(String baseName, Integer index, Integer parameterCode) {
		StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder.append(baseName).append("['").append(MergeUtil.mergeKey(index, parameterCode)).append("']").toString();		
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
		//it will be the itemId of the panel, needed for removing the panel after matcher or field change 
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName, index));		
		CustomCompositeBaseRT compositeBaseRT = (CustomCompositeBaseRT)FieldTypeManager.getFieldTypeRT(fieldID);		
		if (compositeBaseRT!=null) {
			Integer numberOfParts = compositeBaseRT.getNumberOfParts();
			JSONUtility.appendIntegerValue(stringBuilder, "numberOfParts", numberOfParts);
			JSONUtility.appendIntegerValue(stringBuilder, "fieldID", fieldID);
			JSONUtility.appendFieldName(stringBuilder, "partNames").append(":{");
			for (int i=1; i<=numberOfParts; i++) {				
				JSONUtility.appendStringValue(stringBuilder, Integer.valueOf(i).toString(),
						getNameWithMergedKey(baseName, index, Integer.valueOf(i)), i==compositeBaseRT.getNumberOfParts());
			}
			stringBuilder.append("},");		
		if (value!=null) {
			Map<Integer, Integer[]> valueMap = (Map<Integer, Integer[]>)value;			
			JSONUtility.appendFieldName(stringBuilder, "partValues").append(":{");
			if (valueMap!=null && !valueMap.isEmpty()) {				
				for (Iterator<Integer> iterator = valueMap.keySet().iterator(); iterator.hasNext();) {
					Integer parameterID = iterator.next();
					Integer[] partValue = valueMap.get(parameterID);
					if (partValue!=null && partValue.length>0) {
						JSONUtility.appendIntegerValue(stringBuilder, parameterID.toString(),
								partValue[0], !iterator.hasNext());
					}					
				}				
			}
			stringBuilder.append("},");
		}
		Map<Integer, List<ILabelBean>> labelBeansMap = (Map<Integer, List<ILabelBean>>)dataSource;		
		if (labelBeansMap!=null && !labelBeansMap.isEmpty()) {
			stringBuilder.append(JSONUtility.JSON_FIELDS.DATA_SOURCE).append(":{");			
			for (Iterator<Integer> iterator = labelBeansMap.keySet().iterator(); iterator.hasNext();) {
				Integer parameterID = iterator.next();
				List<ILabelBean> labelBeansList = labelBeansMap.get(parameterID);
				JSONUtility.appendILabelBeanList(stringBuilder, parameterID.toString(), labelBeansList, !iterator.hasNext());								
			}
			stringBuilder.append("},");
		}
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);		
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
}
