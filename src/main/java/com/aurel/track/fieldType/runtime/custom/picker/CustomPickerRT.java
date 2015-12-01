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


package com.aurel.track.fieldType.runtime.custom.picker;

import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISortedBean;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.CustomSelectUtil;
import com.aurel.track.fieldType.runtime.custom.select.CustomSelectBaseRT;
import com.aurel.track.fieldType.runtime.matchers.run.CustomSelectMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;

public abstract class CustomPickerRT extends CustomSelectBaseRT {
	private static final Logger LOGGER = LogManager.getLogger(CustomPickerRT.class);
	
	
	/**
	 * The value for the picker should be an entry of system option
	 */
	@Override
	public int getValueType() {
		return ValueType.SYSTEMOPTION;
	}
	
	/**
	 * Whether the values are based on custom lists or system lists
	 * @return
	 */
	@Override
	public boolean isCustomPicker() {
		return true;
	}
	
	/**
	 * Whether the list entries have dynamic icons
	 * @return
	 */
	@Override
	public boolean hasDynamicIcons() {
		return false;
	}
	
	/**
	 * The field key for getting the corresponding map from dropdown.
	 * The SystemSelects and the corresponding Pickers are stored under 
	 * the same key in the dropdownMap (the key of the SystemSelect field)
	 * @return
	 */
	@Override
	public Integer getDropDownMapFieldKey(Integer fieldID) {
		return getSystemOptionType();
	}
	
	/**
	 * Get the value to be shown from the database
	 * @param value
	 * @param locale
	 * @return
	 */	
	@Override
	public String getShowValue(Object value, Locale locale) {
		StringBuffer showValue = new StringBuffer();
		Integer[] optionIDs = CustomSelectUtil.getSelectedOptions(value);
		if (optionIDs==null || optionIDs.length==0) {
			LOGGER.debug("No option seleced by getting the show value from database for picker field " + getSystemOptionType());
			return showValue.toString();
		}
		for (int i = 0; i < optionIDs.length; i++) {
			ILabelBean labelBean = lookupLabelBean(optionIDs[i], locale);
			if (labelBean!=null) {
				showValue.append(labelBean.getLabel());
				if (i<optionIDs.length-1) {
					showValue.append(OPTION_SPLITTER_STRING); 
				}
			}
		}
		return showValue.toString().trim();
	}
	
	/**
	 * Get the value to be shown for a matcher
	 * Typically same as for the getShowValue(), except the selects
	 * (the value object's type differs for matchers compared to issue field values in getShowValue)  
	 * @param fieldID
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getMatcherShowValue(Integer fieldID, Object value, Locale locale) {
		Integer[] optionIDs = null;
		if (value!=null) {
			try {
				optionIDs = (Integer[])value;
			} catch (Exception e) {
				LOGGER.warn("Casting the value type " + value.getClass().getName() +
						" to Integer[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				return "";
			}
			return LookupContainer.getNotLocalizedLookupCommaSepatatedString(getSystemOptionType(), optionIDs, locale);
		}
		return "";
	}
	
	/**
	 * Get the show value called when an item result set is implied
	 * @param fieldID
	 * @param parameterCode
	 * @param value
	 * @param workItemID
	 * @param localLookupContainer
	 * @param locale 
	 * @return
	 */
	@Override
	public String getShowValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		StringBuffer showValue = new StringBuffer();
		Integer[] optionIDs = CustomSelectUtil.getSelectedOptions(value);
		if (optionIDs==null || optionIDs.length==0) {
			LOGGER.debug("No option seleced by getting the show value");
			return showValue.toString();
		}
		for (int i = 0; i < optionIDs.length; i++) {
			ILabelBean labelBean = lookupLabelBean(optionIDs[i], locale);
			if (labelBean!=null) {
				showValue.append(labelBean.getLabel());
			}
			if (i<optionIDs.length-1) {
				showValue.append(OPTION_SPLITTER_STRING); 
			}
		}
		String showValueString = showValue.toString();
		if (!"".equals(showValueString)) {
			return showValueString.trim(); 
		}
		//try from the database
		LOGGER.debug("Getting the show value for field " + fieldID + " and value " + optionIDs[0] + "  from database instead of droprdown container");
		return getShowValue(optionIDs, locale);
	}
	
	/**
	 * Gets the label bean for an objectID
	 * @param objectID
	 * @param locale
	 * @return
	 */
	protected abstract ILabelBean lookupLabelBean(Integer objectID, Locale locale);
	
	/**
	 * Get the sort order related to the value
	 * By default the value is directly used as sortOrder
	 * the select fields has extra sortOrder columns 
	 * @param fieldID
	 * @param parameterCode
	 * @param value the value the sortorder is looked for
	 * @param workItemID
	 * @param localLookupContainer 
	 * @return
	 */
	@Override
	public Comparable getSortOrderValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer) {
		Integer[] optionIDs = CustomSelectUtil.getSelectedOptions(value);
		if (optionIDs==null || optionIDs.length==0) {
			LOGGER.debug("No option seleced by getting the sort order value");
			return null;
		} else {
			Comparable minSortOrderValue = null;
			//select the option with the smallest sortOrder
			for (int i = 0; i < optionIDs.length; i++) {
				ISortedBean sortedBean = (ISortedBean)LookupContainer.getNotLocalizedLabelBean(getDropDownMapFieldKey(fieldID), optionIDs[i]); //beansMap.get(optionIDs[i]);
				if (sortedBean!=null && sortedBean.getSortOrderValue()!=null) {
					Comparable currentSortOrder = sortedBean.getSortOrderValue();
					if (minSortOrderValue==null || currentSortOrder.compareTo(minSortOrderValue)<0) {
						minSortOrderValue = currentSortOrder;
					}
				}
			}
			return minSortOrderValue;
		}
	}
	
	/**
	 * Parses the string value into the corresponding object value
	 * @param isoStrValue
	 * @return
	 */
	@Override
	public Object parseISOValue(Object isoStrValue) {
		if (isoStrValue!=null && !"".equals(isoStrValue)) {
			return new Integer(isoStrValue.toString());
		}
		return null;
	}
	
	/**
	 * Loads the matcher for the field 
	 */
	@Override
	public IMatcherRT getMatcherRT(Integer fieldID, int relation, Object matchValue, 
			MatcherContext matcherContext){
		IMatcherRT matcherRT = new CustomSelectMatcherRT(fieldID, relation, matchValue, matcherContext);
		Integer[] integerArr = null;
		if (matchValue!=null) {
			try {
				//it is Integer[] because IntegerArrayMatcherConverter is used
				integerArr = (Integer[])matchValue;
			} catch (Exception e) {
				LOGGER.warn("Converting the " + matchValue +  " to Integer[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			if (integerArr!=null) {
				Integer[] actualMatchValue = getMatchValue(integerArr, matcherContext);
				matcherRT.setMatchValue(actualMatchValue);
			}
		}
		return matcherRT;
	}
	
	/**
	 * Replace the symbolic value with the actual value (when it is the case)
	 * @param integerArr
	 * @param matcherContext
	 * @return
	 */
	protected Integer[] getMatchValue(Integer[] integerArr, MatcherContext matcherContext) {
		return integerArr;
	}
}
