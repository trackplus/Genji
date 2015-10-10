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


package com.aurel.track.fieldType.runtime.validators;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorParameter;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.util.GeneralUtils;

/**
 * Validate the date values in item hierarchy: the child date should nor exceed the parent date
 * @author Tamas
 *
 */
public class DateHierarchyValidator implements Validator {
	
	/**
	 * The fieldID the validator is applied for
	 */
	private Integer fieldID;
	
	/**
	 * The ID of the workItem to verify
	 */
	private Integer workItemID;
	
	/**
	 * The parentID of the workItem to verify
	 */
	private Integer parentID;
	
	/**
	 * Implements the date validator
	 * Validates an attribute for a field 
	 * and returns a map in case of validation error
	 * - key: fieldID
	 * - value: ValidatorData  
	 * @return
	 */
	public ErrorData validateField(Object value){
		if(value==null){
			return null;
		}
		Date dateValue=(Date)value;
		ErrorData errorData = null;
		//whether it is an end pr start date
		boolean end = SystemFields.INTEGER_ENDDATE.equals(fieldID) || SystemFields.INTEGER_TOP_DOWN_END_DATE.equals(fieldID);
		//to avoid infinite cycle when there is a circular dependency in the parent child hierarchy. Should never happen but... 
		Set<Integer> visistedAscendentsSet = new HashSet<Integer>();
		while (parentID!=null) {
			//is start date too early or end date to late for any ascendent?
			TWorkItemBean parentWorkItemBean = null;
			try {
				parentWorkItemBean = ItemBL.loadWorkItem(parentID);
			} catch (ItemLoaderException e) {
			} 
			parentID = null;
			if (parentWorkItemBean!=null) {
				Date parentReferenceDatum = (Date)parentWorkItemBean.getAttribute(fieldID);
				if (parentReferenceDatum!=null) {
					//first datum found in the ancestor hierarchy: stop climbing the hierarchy (do not assign parentID)
					//return either error or do not search upwards if it conforms 
					//(we suppose that if till the first ancestor it conforms, then it conforms also from this ancestor upwards) 
					//Calendar parentCalendar = Calendar.getInstance();
					//parentCalendar.setTime(parentReferenceDatum);
					if (end) {
						//Date parentCalendarEnd = parentCalendar.getTime();
						if (parentReferenceDatum.before(dateValue)) {
							List<ErrorParameter> errorParameters = new LinkedList<ErrorParameter>();
							errorParameters.add(new ErrorParameter(parentReferenceDatum));
							errorParameters.add(new ErrorParameter(parentWorkItemBean.getSynopsis()));
							errorData =new ErrorData("common.err.interval.date.endAfterParentTarget", errorParameters);
							return errorData;
						}
					} else {
						//Date parentCalendarStart = parentCalendar.getTime();
						if (parentReferenceDatum.after(dateValue)) {
							List<ErrorParameter> errorParameters = new LinkedList<ErrorParameter>();
							errorParameters.add(new ErrorParameter(parentReferenceDatum));
							errorParameters.add(new ErrorParameter(parentWorkItemBean.getSynopsis()));
							errorData = new ErrorData("common.err.interval.date.startBeforeParentTarget", errorParameters);
							return errorData;
						}
					}
				} else {
					//no topDown datum found, look further up in the hierarchy
					parentID = parentWorkItemBean.getSuperiorworkitem();
					if (parentID!=null) {
						if (!visistedAscendentsSet.contains(parentID)) {
							visistedAscendentsSet.add(parentID); 
						} else {
							//circular reference 
							parentID = null;
						}
					}
				}
			}
		}
		List<TWorkItemBean> childrenWorkItemBeans = ItemBL.getChildren(workItemID);
		//to avoid infinite cycle when there is a circular dependency in the parent child hierarchy. Should never happen but...
		Set<Integer> visistedDescendentsSet = new HashSet<Integer>();
		visistedDescendentsSet.add(workItemID);
		while (childrenWorkItemBeans!=null && !childrenWorkItemBeans.isEmpty()) {
			//is start date too late or end date to early for any descendant?
			Date largestGapChildDate = null;
			String title  = null;
			for (TWorkItemBean childWorkItemBean : childrenWorkItemBeans) {
				Date childDate = (Date)childWorkItemBean.getAttribute(fieldID);
				if (childDate!=null) {
					if (end) {
						if (childDate.after(dateValue)) {
							//find the latest target end date of all children
							if (largestGapChildDate==null) {
								largestGapChildDate = childDate;
							} else {
								if (largestGapChildDate.before(childDate)) {
									largestGapChildDate = childDate;
								}
							}
							title = childWorkItemBean.getSynopsis();
						}
					} else {
						if (childDate.before(dateValue)) {
							//find the earliest target start date of all children
							if (errorData==null) {
								largestGapChildDate = childDate;
							} else {
								if (largestGapChildDate.after(childDate)) {
									largestGapChildDate = childDate;
								}
							}
							title = childWorkItemBean.getSynopsis();
						}
					}
				}
			}
			if (largestGapChildDate!=null) {
				List<ErrorParameter> errorParameters = new LinkedList<ErrorParameter>();
				errorParameters.add(new ErrorParameter(largestGapChildDate));
				errorParameters.add(new ErrorParameter(title));
				String errorKey = null;
				if (end) {
					errorKey = "common.err.interval.date.endBeforeChildTarget";
				} else {
					errorKey = "common.err.interval.date.startAfterChildTarget";
				}
				return new ErrorData(errorKey, errorParameters);
			} else {
				//try one level lower
				List<Integer> childIDs = GeneralUtils.createIntegerListFromBeanList(childrenWorkItemBeans);
				childIDs.removeAll(visistedDescendentsSet);
				childrenWorkItemBeans = ItemBL.getChildren(GeneralUtils.createIntArrFromIntegerList(childIDs), true, null, null, null);
				visistedDescendentsSet.addAll(childIDs);
			}
		}
		return errorData;
	}

	public Integer getFieldID() {
		return fieldID;
	}

	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}

	public Integer getWorkItemID() {
		return workItemID;
	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}

	public Integer getParentID() {
		return parentID;
	}

	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}
	
}
