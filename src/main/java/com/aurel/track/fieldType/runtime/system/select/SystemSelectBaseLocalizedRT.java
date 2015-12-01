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


package com.aurel.track.fieldType.runtime.system.select;

import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ILocalizedLabelBean;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.resources.LocalizeUtil;


public abstract class SystemSelectBaseLocalizedRT extends SystemSelectBaseRT {
	
	private static final Logger LOGGER = LogManager.getLogger(SystemSelectBaseLocalizedRT.class);
	
	/**
	 * Whether the list entries have dynamic icons
	 * @return
	 */
	public boolean hasDynamicIcons() {
		return true;
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
		Integer optionID = null;
		if (value!=null) {
			try {
				optionID = (Integer)value;
			} catch (Exception e) {
				LOGGER.error("Casting the value type " + value.getClass().getName() +
						" to Integer in getShowValue() failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			if (optionID!=null) {
				ILabelBean labelBean = LookupContainer.getLocalizedLabelBean(getDropDownMapFieldKey(fieldID), optionID, locale);
				if (labelBean!=null ) {
					return labelBean.getLabel();
				}
			}
		}
		//try from the database although quite superfluous when the dropdown container is loaded correcly
		LOGGER.debug("The field number " + fieldID + " and parametercode "+ parameterCode + 
				" was not found in the dropdown container for getting the show value ");
		return getShowValue(value, locale);
	}
	
	/**
	 * Get the value to be shown from the database and then try to localize  
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getShowValue(Object value, Locale locale) {
		ILabelBean labelBean =  getLabelBeanForShowValue(value, locale);
		if (labelBean!=null) {
			return LocalizeUtil.localizeDropDownEntry((ILocalizedLabelBean)labelBean, locale);
		}
		return "";
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
			return LookupContainer.getLocalizedLookupCommaSepatatedString(fieldID, optionIDs, locale);
		}
		return "";
	}
}
