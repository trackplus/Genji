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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.SystemInputBaseRT;
import com.aurel.track.fieldType.runtime.validators.DoubleValidator;
import com.aurel.track.fieldType.runtime.validators.Validator;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

/**
 * Behavior for Duration field
 * @author Tamas
 *
 */
public class SystemDurationRT extends SystemInputBaseRT {
	private static final Logger LOGGER = LogManager.getLogger(SystemDurationRT.class);
	@Override
	public int getValueType() {
		return ValueType.DOUBLE;
	}

	/**
	 * Whether this field is a custom label field
	 * @param fieldID
	 * @param parameterCode
	 */
	@Override
	public boolean isComputed(Integer fieldID, Integer parameterCode) {
		return true;
	}
	
	/**
	 * Sets the  default values for a field on the workItem from the config settings
	 * @param fieldID
	 * @param parameterCode
	 * @param validConfig
	 * @param fieldSettings
	 * @param workItemBean
	 * @return
	 */
	@Override
	public void setDefaultAttribute(Integer fieldID, Integer parameterCode, 
			Integer validConfig, Map<String, Object> fieldSettings, TWorkItemBean workItemBean) {
		if (workItemBean!=null) {
			Date startDate = getStartDate(workItemBean);
			Date endDate = getEndDate(workItemBean);
			if (startDate!=null && endDate!=null) {
				Integer differenceInDay = DateTimeUtils.getDurationBetweenDates(startDate, endDate, true);
				workItemBean.setAttribute(fieldID, Double.valueOf(differenceInDay));
			}
		}
	}
	
	/**
	 * Get the value to be shown 
	 */
	@Override
	public String getShowValue(Object value, Locale locale) {
		if (value!=null) {
			try {
				Double doubleValue = (Double)value;
				DoubleNumberFormatUtil doubleNumberFormatUtil = DoubleNumberFormatUtil.getInstance();								
				return doubleNumberFormatUtil.formatGUI(doubleValue, locale);
			} catch (Exception e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Getting the showValue for double " + value + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
				return value.toString();
			}
		}
		return "";
	}
	
	@Override
	public String getShowValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		return getShowValue(value, locale);
	}
	
	/**
	 ** Parses the string value into the corresponding object value
	 * @param isoStrValue
	 * @return
	 */
	@Override
	public Object parseISOValue(Object isoStrValue) {
		if (isoStrValue!=null) {
			return DoubleNumberFormatUtil.parseISO(isoStrValue.toString());
		}
		return null;
	}
	
	/**
	 * Gets the start date of the workitem
	 * @param workItemBean
	 * @param fieldID
	 * @return
	 */
	protected Date getStartDate(TWorkItemBean workItemBean) {
		if (workItemBean!=null) {
			return workItemBean.getStartDate();
		}
		return null;
	}
	
	/**
	 * Gets the start date of the workitem
	 * @param workItemBean
	 * @param fieldID
	 * @return
	 */
	/*protected void setStartDate(TWorkItemBean workItemBean, Date startDate) {
		if (workItemBean!=null) {
			workItemBean.setStartDate(startDate);
		}
	}*/

	/**
	 * Gets the start date of the workitem
	 * @param workItemBean
	 * @param fieldID
	 * @return
	 */
	protected Date getEndDate(TWorkItemBean workItemBean) {
		if (workItemBean!=null) {
			return workItemBean.getEndDate();
		}
		return null;
	}
	
	/**
	 * Gets the start date of the workitem
	 * @param workItemBean
	 * @param fieldID
	 * @return
	 */
	/*protected void setEndDate(TWorkItemBean workItemBean, Date endDate) {
		if (workItemBean!=null) {
			workItemBean.setEndDate(endDate);
		}
	}*/
	
	/**
	 * Gets the start date of the workitem
	 * @param workItemBean
	 * @param fieldID
	 * @return
	 */
	/*protected Double getDuration(TWorkItemBean workItemBean) {
		if (workItemBean!=null) {
			return workItemBean.getDuration();
		}
		return null;
	}*/
	
	
	/**
	 * Duration can't be negative
	 */
	@Override
	public Map<Integer, List<Validator>> getValidators(Integer fieldID, 
			TFieldConfigBean fieldConfigBean,Integer parameterCode,
			Object settingsObject, TWorkItemBean workItemBean) {
		Map<Integer, List<Validator>> validatorsMap = super.getValidators(fieldID,fieldConfigBean,parameterCode,
				settingsObject, workItemBean); 
		if (parameterCode==null) {
			parameterCode = Integer.valueOf(0);
		}
		List<Validator> validatorsList = new ArrayList<Validator>();
		if(validatorsMap.get(parameterCode)!=null){
			validatorsList=validatorsMap.get(parameterCode);
		}
		DoubleValidator floatValidator=new DoubleValidator();
		Double minValue = 0.0;
		floatValidator.setMinDouble(minValue);
		validatorsList.add(floatValidator);
		validatorsMap.put(parameterCode, validatorsList);
		return  validatorsMap;
	}
	
	/**
	 * Preprocess a custom attribute before save:
	 * implemented when some extra save is needed for ex. by extensible select
	 * or a system field should be calculated before save (wbs)
	 * @param fieldID
	 * @param parameterCode
	 * @param validConfig
	 * @param fieldSettings
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param contextInformation
	 */
	/*@Override
	public void processBeforeSave(Integer fieldID, Integer parameterCode, Integer validConfig, Map<String, Object> fieldSettings,
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, Map<String, Object> contextInformation) {
		Double newDuration = getDuration(workItemBean);
		Double oldDuration = null;
		if (workItemBeanOriginal!=null) {
			oldDuration = getDuration(workItemBeanOriginal);
		}
		if (EqualUtils.notEqual(newDuration, oldDuration)) {
			Date startDate = getStartDate(workItemBean);
			Date endDate = getEndDate(workItemBean);
			if (newDuration!=null) {
				if (startDate!=null) {
					endDate = DateTimeUtils.moveByDays(startDate, newDuration.intValue(), true, true);
					setEndDate(workItemBean, endDate);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("EndDate " + endDate + " calculated from StartDate " + startDate + " and duration " + newDuration);
					}
				} else {
					if (endDate!=null) {
						startDate = DateTimeUtils.moveByDays(endDate, newDuration.intValue(), false, true);
						setStartDate(workItemBean, startDate);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("StartDate " + startDate + " calculated from EndDate " + endDate + " and duration " + newDuration);
						}
					} else {
						startDate = CalendarUtil.getToday();
						setStartDate(workItemBean, startDate);
						endDate = DateTimeUtils.moveByDays(startDate, newDuration.intValue(), true, true);
						setEndDate(workItemBean, endDate);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("StartDate " + startDate + "  and EndDate " + endDate + " calculated from duration " + newDuration);
						}
					}
				}
			}
		}
	}*/
	
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
	 * Whether the field might be matched in for an excel column
	 * @return
	 */
	@Override
	public boolean mightMatchExcelColumn() {
		return false;
	}
	
	@Override
	public int getLuceneStored() {
		return LuceneUtil.STORE.YES;
	}

	@Override
	public int getLuceneTokenized() {
		return LuceneUtil.TOKENIZE.NO;
	}

	@Override
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.DIRECTINTEGER;
	}

}
