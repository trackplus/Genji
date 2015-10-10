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

package com.aurel.track.fieldType.bulkSetters;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.TWorkItemBean;

/**
 * A transormation context for transforming the bulk values to workItemBean attributes
 * Typically they are the same (SET_TO) but for some  relations a transformation is needed
 * (SET_EARLIEST_STARTING_FROM, MOVE_BY_DAYS, ADD_IF_SET)
 * @author Tamas
 *
 */
public class BulkTranformContext {
	//if a transformation factor should be calculated based on the selected issues
	//for example SET_EARLIEST_STARTING_FROM
	private List<TWorkItemBean> selectedWorkItems;
	//to store the transformation factor for a field in a cache to avoid calculating 
	//it for each call of getWorkItemAttribute()
	private Map<Integer, Object> valueCache;		
	
	/**
	 * The map containing the most appropriate list in each context (project/issueType) for the custom list fields 
	 */
	private Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldToProjectToIssueTypeToListMap;
	
	//the set of selected fields
	private Set<Integer> selectedFieldsSet;
	
	public List<TWorkItemBean> getSelectedWorkItems() {
		return selectedWorkItems;
	}
	public void setSelectedWorkItems(List<TWorkItemBean> selectedWorkItems) {
		this.selectedWorkItems = selectedWorkItems;
	}
	public Map<Integer, Object> getValueCache() {
		return valueCache;
	}
	public void setValueCache(Map<Integer, Object> valueCache) {
		this.valueCache = valueCache;
	}
	public Set<Integer> getSelectedFieldsSet() {
		return selectedFieldsSet;
	}
	public void setSelectedFieldsSet(Set<Integer> selectedFieldsSet) {
		this.selectedFieldsSet = selectedFieldsSet;
	}
	public Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldToProjectToIssueTypeToListMap() {
		return fieldToProjectToIssueTypeToListMap;
	}
	public void setFieldToProjectToIssueTypeToListMap(
			Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldToProjectToIssueTypeToListMap) {
		this.fieldToProjectToIssueTypeToListMap = fieldToProjectToIssueTypeToListMap;
	}	

	
}
