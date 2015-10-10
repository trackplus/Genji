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


package com.aurel.track.fieldType.runtime.system.text;

import java.util.Date;
import java.util.Locale;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.DropDownContainer;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.SystemOutputBaseRT;
import com.aurel.track.fieldType.runtime.matchers.converter.DateMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.DateMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.DateMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;

/**
 * Runtime class for CreateDate and LastEditDate
 * @author Tamas Ruff
 *
 */
public class SystemLabelDateRT extends SystemOutputBaseRT {
	private static final Logger LOGGER = LogManager.getLogger(SystemLabelDateRT.class);	
	/** 
	 * Defines the data type of the field attribute
	 * Used by saving field attributes for custom fields
	 * and saving explicit history for both system and custom fields
	 * Should be a @ValueType constant
	 * Here not really meaningful because this field will be never historized
	 * @return
	 */
	public int getValueType() {
		return ValueType.DATETIME;
	}
	
	/**
	 * Whether the value of this field can be changed
	 * @return
	 */
	public boolean isReadOnly() {
		return true;
	}
	
	/**
	 * Whether the value have been modified
	 * Considered by assembling the history and mail
	 * @param newValue
	 * @param oldValue
	 * @return
	 */
	@Override
	public boolean valueModified(Object newValue, Object oldValue) {
		Date dateNew = null;
		if (newValue!=null) {
			try {
				dateNew = (Date) newValue;
			} catch (Exception e) {
				LOGGER.warn("Converting the new value of type " + 
						newValue.getClass().getName() +  
						" to Date failed with " + e.getMessage(), e);
			}
		}
		Date dateOld = null;
		if (oldValue!=null) {
			try {
				dateOld = (Date) oldValue;
			} catch (Exception e) {
				LOGGER.warn("Converting the old value of type " + 
						newValue.getClass().getName() +  
						" to Boolean failed with " + e.getMessage(), e);
			}
		}
		//CreateDate and LastEditDate contain also time value near the date value
		//that's why we do not allow to group by them
		//return EqualUtils.notEqual(dateNew, dateOld);
		return EqualUtils.notEqualDateNeglectTime(dateNew, dateOld);
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
	public String getShowValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		return getShowValue(value, locale);
	}
	
	/**
	 * Get the value to be shown 
	 */
	@Override
	public String getShowValue(Object value, Locale locale) {
		if (value!=null) {
			try {
				Date dateValue = (Date)value;
				return DateTimeUtils.getInstance().formatGUIDateTime(dateValue, locale);
			} catch (Exception e) {
				//it can happen when the value is an Integer (days relative in treeQueries)
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Getting the showValue for date " + value + " failed with " + e.getMessage(), e);
				}
				return value.toString();
			}
		}
		return "";
	}
	
	/**
	 * Get the ISO show value for locale independent exporting to xml
	 * typically same as show value, date and number values are formatted by iso format 
	 * @param fieldID
	 * @param parameterCode
	 * @param value
	 * @param workItemID
	 * @param localLookupContainer
	 * @param locale
	 * @return
	 */
	public String getShowISOValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		if (value!=null) {
			try {
				Date dateValue = (Date)value;
				return DateTimeUtils.getInstance().formatISODateTime(dateValue);
			} catch (Exception e) {
				return value.toString();
			}
		}
		return "";
	}
	
	/**
	 * Parses the string value into the corresponding object value
	 * @param isoStrValue
	 * @return
	 */
	@Override
	public Object parseISOValue(Object isoStrValue) {
		if (isoStrValue!=null) {
			return DateTimeUtils.getInstance().parseISODateTime(isoStrValue.toString());
		}
		return null; 
	}
	
	/**
	 * Returns whether the iso show value differs from 
	 * the locale specific show value for this field type.
	 * This is needed to avoid loading the iso values 
	 * by repeating the load for locale specific show value
	 * (which takes surprisingly long time)   
	 * @return
	 */
	@Override
	public boolean isoDiffersFromLocaleSpecific() {
		return true;
	}
	
	/**
	 * Creates the matcher object for configuring the matcher
	 * @param fieldID 
	 */
	@Override
	public IMatcherDT getMatcherDT(Integer fieldID) {		
		return new DateMatcherDT(fieldID);
	}
	
	/**
	 * Creates the  matcher object for executing the matcher
	 * @param fieldID
	 * @param relation
	 * @param matchValue
	 */
	@Override
	public IMatcherRT getMatcherRT(Integer fieldID, int relation, Object matchValue, 
			MatcherContext matcherContext){		
		return new DateMatcherRT(fieldID, relation, matchValue, matcherContext);
	}
	
	@Override
	public MatcherConverter getMatcherConverter() {
		return DateMatcherConverter.getInstance();
	}
	
	/**
	 * Whether the field should appear in the groupable fields list
	 * Typically fields which are typically unique should not be groupable   
	 * @return
	 */
	/*public boolean isGroupable() {
		return false;
	}*/
	/**
	 * Gets the label for the group. It is typically the same
	 * as the showValue() of the workItems from the same group,
	 * but it will be called only for fields for which groupLabelDiffersFromShowValue() returns true
	 * (For create/last modification date it should differ from the show showValue() 
	 * because the time part of the date should be removed from the label for group)
	 * @return
	 */
	@Override
	public String getGroupLabel(Object value, Locale locale) {
		if (value!=null) {
			try {
				Date dateValue = (Date)value;
				return DateTimeUtils.getInstance().formatGUIDate(dateValue, locale);
			} catch (Exception e) {
			}
		}
		return "";
	}
	
	/**
	 * Whether the group label differs from the show value of the workItems from the same group
	 * It is needed to differentiate because for getting the label for group either  
	 * the getGroupLabel() will be called or the reportBean.getShowValue(fieldID) method   
	 * (not the fieldTypeRT's getShowValue(Object value, Locale locale) which is the  
	 * default implementation for getGroupLabel() because the reportBean
	 * contains the showValue already, and calling the getShowValue(Object value, Locale locale)
	 * might result in unnecessary database access: see. SystemSelectBase getLabelBean())
	 * @return
	 */
	@Override
	public boolean groupLabelDiffersFromShowValue() {
		return true;
	}
	
	
	/**
	 * Gets the string value to be stored by lucene
	 * @param value the value of the field
	 * @param workItemBean the lucene value might depend on other fields of the workItem
	 * @return
	 */
	public String getLuceneValue(Object value, TWorkItemBean workItemBean) {
		return LuceneUtil.getLuceneDateValue(value);
	}
	
	/**
	 * Whether the field should be stored
	 * @return
	 */
	public int getLuceneStored() {
		return LuceneUtil.STORE.NO;
	}
	
	/**
	 * Whether the field should be tokenized
	 * @return
	 */
	public int getLuceneTokenized() {
		return LuceneUtil.TOKENIZE.NO;
	}
	
	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.DIRECTDATE;
	}
}
