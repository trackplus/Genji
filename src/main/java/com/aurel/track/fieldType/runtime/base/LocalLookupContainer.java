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


package com.aurel.track.fieldType.runtime.base;

import java.util.HashMap;
import java.util.Map;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.util.IntegerStringBean;

/**
 * Contains lookups specific for the item result set (should be loaded for each result set)
 * The global lookups (lists) are accessible through the LookupContainer 
 * Used in getShowValue() for getting the project specific itemID, calculating the WBS
 * @author Tamas
 */
public class LocalLookupContainer {
	
	/*
	 * Map containing the workItems from result set and the ancestor workItems of those 
	 */
	Map<Integer, TWorkItemBean> workItemsMap = new HashMap<Integer, TWorkItemBean>();
	
	/*
	 * Map containing the workItem associations to their parents
	 * If a workItem has no parent (or has parent but the parent is not included in the resulting workItem set)
	 */
	protected Map<Integer, Integer> workItemToParentMap = new HashMap<Integer, Integer>();
	
	/*
	 * Map containing the the custom options
	 */
	protected Map<Integer, ILabelBean> customOptionsMap = new HashMap<Integer, ILabelBean>();
	
	/*
	 * Map containing the the external lookup options
	 * - key: fieldID
	 * - value: map: - key: externalOptionID
	 * 				 - value: labelalueBean
	 */
	protected Map<Integer, Map<Integer, IntegerStringBean>> externalOptionsMap = new HashMap<Integer, Map<Integer, IntegerStringBean>>();
	
	public Map<Integer, TWorkItemBean> getWorkItemsMap() {
		return workItemsMap;
	}

	public void setWorkItemsMap(Map<Integer, TWorkItemBean> workItemsMap) {
		this.workItemsMap = workItemsMap;
	}

	public Map<Integer, Integer> getWorkItemToParentMap() {
		return workItemToParentMap;
	}

	public void setWorkItemToParentMap(Map<Integer, Integer> workItemToParentMap) {
		this.workItemToParentMap = workItemToParentMap;
	}

	public Map<Integer, ILabelBean> getCustomOptionsMap() {
		return customOptionsMap;
	}

	public void setCustomOptionsMap(Map<Integer, ILabelBean> customOptionsMap) {
		this.customOptionsMap = customOptionsMap;
	}

	public Map<Integer, Map<Integer, IntegerStringBean>> getExternalOptionsMap() {
		return externalOptionsMap;
	}

	public void setExternalOptionsMap(
			Map<Integer, Map<Integer, IntegerStringBean>> externalOptionsMap) {
		this.externalOptionsMap = externalOptionsMap;
	}

	
	
}
