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


package com.aurel.track.report.group;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.itemNavigator.layout.group.GroupFieldTO;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.util.IntegerStringBean;


public class ReportGroupBL {

	private static final Logger LOGGER = LogManager.getLogger(ReportGroupBL.class);
	private static String groupSeparatorChar = "gs";
	private static String attributeSeparatorChar = "as";

	public final static int MAXIMUM_GROUPING_LEVEL = 4;
	private final static String GROUPSEPARATOR = ": ";
	private final static String VALUE_SEPARATOR = "__";
	private final static String FIELD_SEPARATOR = "|";



	/**
	 * Populates the grouping limits map for showing the grouping on the UI
	 * @param reportBeans
	 * @param groupByList
	 * @param treeFieldsChildToParentMap
	 * @param treeFieldsIDToLabelMap
	 * @param treeFieldsToSortOrderMap
	 * @param groupOtherExpandCollapse
	 * @param locale
	 * @return map:
	 * 	- key: workItemID
	 * 	- value: list of GroupLimitBeans (a workItem can be a grouping limit for one or more groups)
	 */
	public static Map<Integer, List<GroupLimitBean>> populateGroupingLimits(ReportBeans reportBeans,
			List<GroupFieldTO> groupByList, Map<Integer, Map<Integer, Integer>> treeFieldsChildToParentMap,
			Map<Integer, Map<Integer, String>> treeFieldsIDToLabelMap,
			Map<Integer, Map<Integer, Integer>> treeFieldsToSortOrderMap,
			Set<String> groupOtherExpandCollapse, Locale locale) {
		Map<Integer, List<GroupLimitBean>> groupingLimitsMapForWorkItem = new HashMap<Integer, List<GroupLimitBean>>();
		if (reportBeans==null ||
				reportBeans.getReportBeansFlat()==null || reportBeans.getReportBeansFlat().isEmpty() ||
				groupByList==null || groupByList.isEmpty()) {
			return groupingLimitsMapForWorkItem;
		}
		if (groupOtherExpandCollapse==null) {
			groupOtherExpandCollapse = new HashSet<String>();
		}
		List<Integer> groupByFieldIDs = getGroupByFieldIDs(groupByList);
		Map<Integer, String> localizedLabels = FieldRuntimeBL.getLocalizedDefaultFieldLabels(groupByFieldIDs, locale) ;
		Iterator<ReportBean> reportBeansIterator = reportBeans.getReportBeansFlat().iterator();
		//get the first reportBean: this is surely the grouping limit for all grouping fields
		ReportBean reportBean = reportBeansIterator.next();
		TWorkItemBean workItemBean = reportBean.getWorkItemBean();
		Integer workItemID = workItemBean.getObjectID();
		List<GroupLimitBean> groupingLimitsListForWorkItem = new LinkedList<GroupLimitBean>();
		//the first workItem is grouping limit
		groupingLimitsMapForWorkItem.put(workItemID, groupingLimitsListForWorkItem);
		//contains the actual groupLimitBean for each grouping level (for both flat and tree field grouping)
		Map<Integer, GroupLimitBean> groupLimitBeansMap = new HashMap<Integer, GroupLimitBean>();
		//contains the actual ordinal number for each level
		List<IntegerStringBean> groupIdentifierList = new LinkedList<IntegerStringBean>();
		//tree field management variables
		Map<Integer, Stack<Integer>> treeStacksMap = new HashMap<Integer, Stack<Integer>>();
		//gather the tree type fields
		Map<Integer, Boolean> fieldIsTreeMap = new HashMap<Integer, Boolean>();
		for (int i = 0; i < groupByList.size(); i++) {
			GroupFieldTO groupFieldTO = groupByList.get(i);
			Integer fieldID = groupFieldTO.getFieldID();
			Map<Integer, Integer> childToParentMap = null;
			if  (treeFieldsChildToParentMap!=null) {
				childToParentMap = treeFieldsChildToParentMap.get(fieldID);
				if (childToParentMap!=null) {
					fieldIsTreeMap.put(fieldID, Boolean.TRUE);
				} else {
					fieldIsTreeMap.put(fieldID, Boolean.FALSE);
				}
			} else {
				fieldIsTreeMap.put(fieldID, Boolean.FALSE);
			}
		}
		Map<Integer, Map<Integer, GroupLimitBean>> treeLimitBeansMapForField = new HashMap<Integer, Map<Integer,GroupLimitBean>>();
		//previous values and next values to observe the group changes
		Map<Integer, Object> previousValuesMap = new HashMap<Integer, Object>();
		Map<Integer, Object> nextValuesMap = new HashMap<Integer, Object>();
		//start with the first ReportBean: it is limit for all grouping levels
		for (int i = 0; i < groupByList.size(); i++) {
			GroupFieldTO groupFieldTO = groupByList.get(i);
			Integer fieldID = groupFieldTO.getFieldID();
			Object firstValue = workItemBean.getAttribute(fieldID);
			previousValuesMap.put(fieldID, firstValue);
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			String showValue = null;
			if (fieldTypeRT!=null && fieldTypeRT.groupLabelDiffersFromShowValue()) {
				showValue = fieldTypeRT.getGroupLabel(firstValue, locale);
			} else {
				showValue = reportBean.getShowValue(fieldID) + "";
			}
			String groupValue = "";
			if (firstValue!=null) {
				groupValue = fieldTypeRT.getStringGroupValue(firstValue);
			}
			//by default get the "global" expanding for the field
			groupIdentifierList.add(new IntegerStringBean(groupValue, fieldID));
			String groupID = encodeGroupIdentifierString(groupIdentifierList);
			boolean expanding = !groupFieldTO.isCollapsed();
			if (groupOtherExpandCollapse.contains(groupID)) {
				expanding = !expanding;
			}
			boolean isTreeField = fieldIsTreeMap.get(fieldID).booleanValue();
			GroupLimitBean groupLimitBean = new GroupLimitBean(fieldID, localizedLabels.get(fieldID) +
					GROUPSEPARATOR + showValue, firstValue, reportBean.getSortOrder(fieldID), i, expanding,
					workItemID, groupID, isTreeField, showValue);
			if (isTreeField) {
				groupLimitBean.setTreeSortOrder(getTreeSortOrder(treeFieldsToSortOrderMap, fieldID, (Integer)firstValue));
			}
			groupingLimitsListForWorkItem.add(groupLimitBean);
			groupLimitBeansMap.put(Integer.valueOf(i), groupLimitBean);
			if (isTreeField) {
				//put the tree parents in stack, start a new branch
				Stack<Integer> treeStack = new Stack<Integer>();
				treeStacksMap.put(fieldID, treeStack);
				treeStack.push((Integer)firstValue);
				Map<Integer, GroupLimitBean> treeLimitBeansMap = new HashMap<Integer, GroupLimitBean>();
				treeLimitBeansMapForField.put(fieldID, treeLimitBeansMap);
				treeLimitBeansMap.put((Integer)firstValue, groupLimitBean);
			}
			if (i>0) {
				GroupLimitBean parentGroupFieldBean = groupLimitBeansMap.get(Integer.valueOf(i-1));
				if (parentGroupFieldBean!=null) {
					//set the parent-child relationship between succeeding levels
					groupLimitBean.setParent(parentGroupFieldBean);
				}
			}
			groupLimitBean.setNumberOfWorkItems(1);
		}
		//the other ReportBeans
		while (reportBeansIterator.hasNext()) {
			reportBean = reportBeansIterator.next();
			workItemBean = reportBean.getWorkItemBean();
			workItemID = workItemBean.getObjectID();
			//whether the current field or any previous field was changed
			Set<Integer> changeFlags = new HashSet<Integer>();
			for (int i = 0; i < groupByList.size(); i++) {
				GroupFieldTO groupFieldTO = groupByList.get(i);
				Integer fieldID = groupFieldTO.getFieldID();
				Map<Integer, GroupLimitBean> treeLimitBeansMap = treeLimitBeansMapForField.get(fieldID);
				boolean isTreeField = fieldIsTreeMap.get(fieldID).booleanValue();
				Object nextValue = workItemBean.getAttribute(fieldID);
				Object previousValue = previousValuesMap.get(fieldID);
				nextValuesMap.put(fieldID, nextValue);
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				boolean changeOnThisField = fieldTypeRT.valueModified(nextValue, previousValue);
				boolean changeOnPreviousField = i>0 && changeFlags.contains(groupByList.get(i-1).getFieldID());
				//the actual grouping field value modified or a previous grouping field modified: then there is a new grouping limit
				if (changeOnThisField || changeOnPreviousField) {
					previousValuesMap.put(fieldID, nextValue);
					groupingLimitsListForWorkItem = groupingLimitsMapForWorkItem.get(workItemID);
					if (groupingLimitsListForWorkItem==null) {
						groupingLimitsListForWorkItem = new LinkedList<GroupLimitBean>();
						groupingLimitsMapForWorkItem.put(workItemID, groupingLimitsListForWorkItem);
					}
					//by default get the "global" expanding for the field
					String showValue = null;
					if (fieldTypeRT!=null && fieldTypeRT.groupLabelDiffersFromShowValue()) {
						showValue = fieldTypeRT.getGroupLabel(nextValue, locale);
					} else {
						showValue = reportBean.getShowValue(fieldID) + "";
					}
					String groupValue = "";
					if (nextValue!=null) {
						groupValue = fieldTypeRT.getStringGroupValue(nextValue);
					}
					IntegerStringBean groupIdentifierValue = null;
					if (groupIdentifierList.size()>i) {
						groupIdentifierValue = groupIdentifierList.get(Integer.valueOf(i));
					}
					if (groupIdentifierValue==null) {
						//it was removed by a supergroup change in a previous iteration
						groupIdentifierValue = new IntegerStringBean(groupValue, fieldID);
						groupIdentifierList.add(groupIdentifierValue);
					} else {
						//actualized the old label to the new label, field is unchanged
						groupIdentifierValue.setLabel(groupValue);
					}
					//reset the subgroups of this group because a new group means automatically new subgroup
					for (int j = i+1; j<groupByList.size(); j++) {
						if (groupIdentifierList.size()>i+1) {
							groupIdentifierList.remove(i+1);
						}
					}
					String groupID = encodeGroupIdentifierString(groupIdentifierList);
					boolean expanding = !groupFieldTO.isCollapsed();
					//modify it if it is contained in the otherExpandCollapse set
					if (groupOtherExpandCollapse.contains(groupID)) {
						expanding = !expanding;
					}
					//will be added only by group change (flat field change or tree field change at first level)
					GroupLimitBean nextLevelGroupLimitBean = null;
					boolean setGroupLimitParent = true;
					if (isTreeField) {
						//a tree field changed
						Map<Integer, String> idToLabelMap = treeFieldsIDToLabelMap.get(fieldID);
						//is tree field
						Integer nextTreeValue = (Integer)nextValue;
						Map<Integer, Integer> childToParentMap = null;
						if  (treeFieldsChildToParentMap!=null) {
							childToParentMap = treeFieldsChildToParentMap.get(fieldID);
						}
						Stack<Integer> treeStack = treeStacksMap.get(fieldID);

						if (changeOnPreviousField) {
							//the value for the previous grouping level changed:
							//start a new stack, the tree field value is on the highest level (no ancestor hierarchy is implied)
							//new group is started
							treeStack.clear();
							treeLimitBeansMap.clear();
							nextLevelGroupLimitBean = new GroupLimitBean(fieldID, localizedLabels.get(fieldID) +
									GROUPSEPARATOR + showValue, nextValue, reportBean.getSortOrder(fieldID), i, expanding,
									workItemID, groupID, isTreeField, showValue);
							nextLevelGroupLimitBean.setNumberOfWorkItems(1);
							groupingLimitsListForWorkItem.add(nextLevelGroupLimitBean);
							//put the tree parents in stack as root of a branch
							treeStack.push((Integer)nextTreeValue);
							treeLimitBeansMap.put((Integer)nextTreeValue, nextLevelGroupLimitBean);
							nextLevelGroupLimitBean.setTreeSortOrder(getTreeSortOrder(treeFieldsToSortOrderMap, fieldID, nextTreeValue));
						} else {
							//the tree field branch changed, a new group might start but not necessarily
							//empty the stack and treeLimitBeansMap till the first ancestor
							Integer ancestorValue = popStackToAncestor(treeStack, childToParentMap, nextTreeValue, treeLimitBeansMap);
							for (GroupLimitBean groupLimitBean : treeLimitBeansMap.values()) {
								//increment each existing ancestor found
								groupLimitBean.setNumberOfWorkItems(groupLimitBean.getNumberOfWorkItems()+1);
							}
							//gets the tree hierarchy between the first ancestor till the actual value including the actual value
							//(fill the eventual holes in tree : parent - <missingChild> - existing grand child )
							List<Integer> ancestorListTopToBottom = getAncestorList(childToParentMap, ancestorValue, nextTreeValue);
							Integer peek = null;
							GroupLimitBean peekTreeLimitBean = null;
							if (!treeStack.isEmpty()) {
								//used ancestor found
								peek = treeStack.peek();
								peekTreeLimitBean = treeLimitBeansMap.get(peek);
							}
							for (Integer ancestorTreeValue : ancestorListTopToBottom) {
								//add the tree branches from the ancestor down
								groupValue = fieldTypeRT.getStringGroupValue(ancestorTreeValue);
								groupIdentifierValue.setLabel(groupValue);
								groupID = encodeGroupIdentifierString(groupIdentifierList);
								Comparable<Object> branchSortOrder = fieldTypeRT.getSortOrderValue(fieldID, null, ancestorTreeValue, workItemID, null);
								nextLevelGroupLimitBean = new GroupLimitBean(fieldID, localizedLabels.get(fieldID) +
										GROUPSEPARATOR + idToLabelMap.get(ancestorTreeValue), ancestorTreeValue, branchSortOrder, i, expanding,
										workItemID, groupID, isTreeField, showValue);
								nextLevelGroupLimitBean.setTreeSortOrder(getTreeSortOrder(treeFieldsToSortOrderMap, fieldID, ancestorTreeValue));
								groupingLimitsListForWorkItem.add(nextLevelGroupLimitBean);
								treeLimitBeansMap.put(ancestorTreeValue, nextLevelGroupLimitBean);
								nextLevelGroupLimitBean.setNumberOfWorkItems(1);
								treeStack.push(ancestorTreeValue);
								if (peekTreeLimitBean!=null) {
									//add to the parent tree value, but same grouping level
									nextLevelGroupLimitBean.setTreeParent(peekTreeLimitBean);
									setGroupLimitParent = false;
								}
								peekTreeLimitBean = nextLevelGroupLimitBean;
							}
						}
					} else {
						//non tree field changed
						nextLevelGroupLimitBean = new GroupLimitBean(fieldID, localizedLabels.get(fieldID) +
								GROUPSEPARATOR + showValue, nextValue, reportBean.getSortOrder(fieldID), i, expanding,
								workItemID, groupID, isTreeField, showValue);
						groupingLimitsListForWorkItem.add(nextLevelGroupLimitBean);
						nextLevelGroupLimitBean.setNumberOfWorkItems(1);
					}
					if (nextLevelGroupLimitBean!=null) {
						groupLimitBeansMap.put(Integer.valueOf(i), nextLevelGroupLimitBean);
						if (i>0 && setGroupLimitParent) {
							GroupLimitBean parentGroupLimitBean = groupLimitBeansMap.get(Integer.valueOf(i-1));
							if (parentGroupLimitBean!=null) {
								nextLevelGroupLimitBean.setParent(parentGroupLimitBean);
							}
						}
					}
					changeFlags.add(fieldID);
				} else {
					if (isTreeField) {
						//increment the count for the actual node and also for each ancestor node from the tree
						for (GroupLimitBean groupLimitBean : treeLimitBeansMap.values()) {
							groupLimitBean.setNumberOfWorkItems(groupLimitBean.getNumberOfWorkItems()+1);
						}
					} else {
						//increment the count for the current group
						GroupLimitBean groupLimitBean = groupLimitBeansMap.get(Integer.valueOf(i));
						if (groupLimitBean!=null) {
							groupLimitBean.setNumberOfWorkItems(groupLimitBean.getNumberOfWorkItems()+1);
						}
					}
				}
			}
		}
		return groupingLimitsMapForWorkItem;
	}

	/**
	 * Gets the "artificial" sort order for a tree field value
	 * @param treeFieldsToSortOrderMap
	 * @param fieldID
	 * @param fieldValue
	 * @return
	 */
	private static Integer getTreeSortOrder(Map<Integer, Map<Integer, Integer>> treeFieldsToSortOrderMap, Integer fieldID, Integer fieldValue) {
		if (treeFieldsToSortOrderMap!=null) {
			Map<Integer, Integer> sortOrderMap = treeFieldsToSortOrderMap.get(fieldID);
			if (sortOrderMap!=null) {
				return sortOrderMap.get(fieldValue);
			}
		}
		return null;
	}

	/**
	 * Whether the field value for for a tree field is descendant of the previous items' value
	 * @param childToParentMap
	 * @param previousValue
	 * @param nextValue
	 * @return
	 */
	private static boolean isAncestor(Map<Integer, Integer> childToParentMap, Integer previousValue, Integer nextValue) {
		Integer parentID = childToParentMap.get(nextValue);
		if (parentID!=null) {
			if (parentID.equals(previousValue)) {
				return true;
			} else {
				return isAncestor(childToParentMap, previousValue, parentID);
			}
		}
		return false;
	}

	/**
	 * Gets the ancestor field values for a tree field
	 * @param childToParentMap
	 * @param previousValue
	 * @param nextValue
	 * @return
	 */
	private static List<Integer> getAncestorList(Map<Integer, Integer> childToParentMap, Integer previousValue, Integer nextValue) {
		List<Integer> ancestorList = new LinkedList<Integer>();
		ancestorList.add(nextValue);
		Integer parentID = childToParentMap.get(nextValue);
		while (parentID!=null) {
			if (previousValue!=null && previousValue.equals(parentID)) {
				break;
			}
			ancestorList.add(0, parentID);
			parentID = childToParentMap.get(parentID);
		}
		return ancestorList;
	}

	/**
	 * Pop the values from the stack till a direct ancestor is found on the top of the stack
	 * @param treeStack
	 * @param childToParentMap
	 * @param actualValue
	 * @param treeLimitBeansMap
	 * @return
	 */
	private static Integer popStackToAncestor(Stack<Integer> treeStack, Map<Integer, Integer> childToParentMap,
			Integer actualValue, Map<Integer, GroupLimitBean> treeLimitBeansMap) {
		if (treeStack.isEmpty()) {
			return null;
		}
		Integer peekValue = treeStack.peek();
		while (!isAncestor(childToParentMap, peekValue, actualValue)) {
			treeStack.pop();
			treeLimitBeansMap.remove(peekValue);
			if (treeStack.isEmpty()) {
				return null;
			} else {
				peekValue = treeStack.peek();
			}
		}
		return peekValue;
	}

	/**
	 * Create a unique identifier for each group
	 * @param groupPathList
	 * @return
	 */
	private static String encodeGroupIdentifierString(List<IntegerStringBean> groupPathList) {
		if (groupPathList==null || groupPathList.isEmpty()) {
			return null;
		}
		StringBuffer stringBuffer = new StringBuffer();
		for (Iterator<IntegerStringBean> iterator = groupPathList.iterator(); iterator.hasNext();) {
			IntegerStringBean integerStringBean = iterator.next();
			stringBuffer.append(integerStringBean.getValue());
			stringBuffer.append(VALUE_SEPARATOR);
			stringBuffer.append(integerStringBean.getLabel());
			if (iterator.hasNext()) {
				stringBuffer.append(FIELD_SEPARATOR);
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * Decode a node
	 * @param groupID
	 * @return
	 */
	public static Integer decodeGroupField(String groupID){
		String[] tokens = groupID.split("\\" + FIELD_SEPARATOR);
		if (tokens!=null && tokens.length>0) {
			//the last token specifies the group field. The parent groups are included for uniqueness of the groupID
			tokens = tokens[tokens.length-1].split(VALUE_SEPARATOR);
			if (tokens!=null && tokens.length>0) {
				return Integer.valueOf(tokens[0]);
			}
		}
		return null;
	}

	/**
	 * Gets the list of fieldIDs from a list of GroupFields
	 * @param groupByList
	 * @return
	 */
	private static List<Integer> getGroupByFieldIDs(List<GroupFieldTO> groupByList) {
		List<Integer> groupFieldIDs = new LinkedList<Integer>();
		if (groupByList!=null) {
			for (GroupFieldTO groupField : groupByList) {
				Integer fieldID = groupField.getFieldID();
				if (fieldID!=null) {
					groupFieldIDs.add(fieldID);
				}
			}
		}
		return groupFieldIDs;
	}
}
