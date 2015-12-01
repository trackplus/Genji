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


package com.aurel.track.fieldType.runtime.system.text;

import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.SystemOutputBaseRT;
import com.aurel.track.fieldType.runtime.matchers.converter.IntegerMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.ItemPickerMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.IntegerMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.prop.ApplicationBean;

/**
 * Runtime class for IssueNo
 * @author Tamas Ruff
 *
 */
public class SystemIssueNoRT  extends SystemOutputBaseRT {

	private static final Logger LOGGER = LogManager.getLogger(SystemIssueNoRT.class);
	/** 
	 * Defines the data type of the field attribute
	 * Used by saving field attributes for custom fields
	 * and saving explicit history for both system and custom fields
	 * Should be a @ValueType constant
	 * Here not really meaningful because this field will be never historized
	 * @return
	 */
	@Override
	public int getValueType() {
		//should be the same as for SystemProjectSpecificIssueNoRT (to avoid Integer parse errors)
		//not custom and no history for this field so "faking" the type to Text should not be a problem
		return ValueType.TEXT;
	}
	
	/**
	 * In case of a custom picker or system selects select the list type
	 * Used by saving custom pickers and 
	 * explicit history for both system and custom fields
	 * @return
	 */
	@Override
	public Integer getSystemOptionType() {
		return SystemFields.INTEGER_ISSUENO;
	}
	
	/**
	 * Whether the value of this field can be changed
	 * @return
	 */
	@Override
	public boolean isReadOnly() {
		return true;
	}
	
	/**
	 * Creates the matcher object for configuring the matcher
	 * @param fieldID 
	 */
	@Override
	public IMatcherDT getMatcherDT(Integer fieldID) {
		return new ItemPickerMatcherDT(fieldID, false);
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
		return new IntegerMatcherRT(fieldID, relation, matchValue, matcherContext);
	}
	
	@Override
	public MatcherConverter getMatcherConverter() {
        return IntegerMatcherConverter.getInstance();
    }
	
	/**
	 * Get the value to be shown
	 * For text fields typically the field value itself
	 * For selects the (eventually localized) label corresponding to the value 	
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getShowValue(Object value, Locale locale) {
		Integer optionID = null;
		try {
			optionID = (Integer)value;
		} catch (Exception e) {
			LOGGER.error("Converting the value to integer failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (optionID!=null) {
			if (ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn()) {
				TWorkItemBean parentWorkItemBean = null;
				try {
					parentWorkItemBean = ItemBL.loadWorkItem(optionID);
					if (parentWorkItemBean!=null) {
						return SystemProjectSpecificIssueNoRT.getShowValue(parentWorkItemBean.getIDNumber(), parentWorkItemBean);
					}
				} catch (ItemLoaderException e) {	
				}
			} else {
				return optionID.toString();
			}
		} else {
			LOGGER.debug("The parentID " + optionID + " was not found");
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
			return Integer.valueOf(isoStrValue.toString());
		}
		return null; 
	}
	
	/**
	 * Whether the field should appear in the groupable fields list
	 * Typically fields which are typically unique should not be groupable   
	 * @return
	 */
	@Override
	public boolean isGroupable() {
		return false;
	}
	
	/**
	 * Whether the field should be stored
	 * @return
	 */
	@Override
	public int getLuceneStored() {
		return LuceneUtil.STORE.YES;
	}
	
	/**
	 * Whether the field should be tokenized
	 * @return
	 */
	@Override
	public int getLuceneTokenized() {
		return LuceneUtil.TOKENIZE.NO;
	}
	
	@Override
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.DIRECTINTEGER;
	}
}
