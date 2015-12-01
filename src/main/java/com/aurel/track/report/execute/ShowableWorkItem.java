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


package com.aurel.track.report.execute;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;

/**
 * This class represents a workItem with the (eventually localized) show values
 * Used by printItem and base class for Reportbean
 * @author Tamas Ruff
 */
public class ShowableWorkItem implements Serializable {
	private static final long serialVersionUID = -1332508961665754857L;
	private static final Logger LOGGER = LogManager.getLogger(ShowableWorkItem.class);  
	protected TWorkItemBean workItemBean;
	
	/**
	 * Map with show values: typically the show value is the value itself
	 * This map is typically used for dropdown and formattable (dates) fields
	 * -	key: fieldID (Integer)
	 * -	value: localized label (String)
	 */
	protected Map<Integer, String> showValuesMap = new HashMap<Integer, String>();
	
	/**
	 * Map with sort order values: typically the sortOrder value is the value itself, and in this case the value doesn't appear in this map
	 * This map is typically used for ordered select fields
	 * -	key: fieldID (Integer)
	 * -	value: sortOrder value (Integer) 
	 */
	protected Map<Integer, Comparable> sortOrderValuesMap = new HashMap<Integer, Comparable>();
	
	protected Integer stateFlag = null;
	
	/**
	 * @return the workItemBean
	 */
	public TWorkItemBean getWorkItemBean() {
		return workItemBean;
	}

	/**
	 * @param workItemBean the workItemBean to set
	 */
	public void setWorkItemBean(TWorkItemBean workItemBean) {
		this.workItemBean = workItemBean;
	}

	/**
	 * @return the showValues
	 */
	public Map<Integer, String> getShowValuesMap() {
		return showValuesMap;
	}
	
	/**
	 * @return the sortOrder
	 */
	public Map<Integer, Comparable> getSortOrderValuesMap() {
		return sortOrderValuesMap;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrderValuesMap(Map<Integer, Comparable> sortOrder) {
		this.sortOrderValuesMap = sortOrder;
	}

	/**
	 * @param showValues the showValues to set
	 */
	public void setShowValuesMap(Map<Integer, String> showValues) {
		this.showValuesMap = showValues;
	}
	/**
	 * @return the stateFlag
	 */
	public Integer getStateFlag() {
		return stateFlag;
	}

	/**
	 * @param stateFlag the stateFlag to set
	 */
	public void setStateFlag(Integer stateFlag) {
		this.stateFlag = stateFlag;
	}
	public Comparable getSortOrder(Integer fieldID) {
		Object sortOrderValue = sortOrderValuesMap.get(fieldID);
		if (sortOrderValue==null) {
			//try the direct value
			sortOrderValue = workItemBean.getAttribute(fieldID);
		}
		Comparable comparableValue = null;
		if (sortOrderValue!=null) {
			try {
				comparableValue = (Comparable)sortOrderValue;
			} catch (Exception e) {
				LOGGER.info("The type of the sortOrder attribute for fieldID " + 
						fieldID  + " is " + sortOrderValue.getClass().getName() + 
						". Casting it to Comparable failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return comparableValue;
	}

	public String getShowValue(Integer fieldID) {
		String explicitShowValue = showValuesMap.get(fieldID);
		String showValue = null;
		if (explicitShowValue != null) {
			showValue = explicitShowValue;
		} else {
			Object attributeValue =  workItemBean.getAttribute(fieldID);
			if (attributeValue!=null) {
				showValue = attributeValue.toString();
			}
		}
		if (showValue != null) {
			return showValue;
		} else {
			return "";
		}
	}


	
}
