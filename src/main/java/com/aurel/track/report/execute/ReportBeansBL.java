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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TStateBean;

public class ReportBeansBL {
	private static final Logger LOGGER = LogManager.getLogger(ReportBeansBL.class);
	//the maximum levels are limited in order to avoid infinite circles in the hierarchy
	private static int MAX_LEVELS = 20;

	/**
	 * Creates a flat from the hierarchical list:
	 * adds the first level reportBeans to the flat list and
	 * recursively add the children lists depending on addAllChildren.
	 * Set the counter and the expanded flag
	 * @param flatList
	 * @param hierarchicalList
	 * @param addAllChildren:	false - add the children only if they are expanded
	 * 						(used by preparing the data for report overview JSP)
	 * 				true - add the children anyway (used by exporting the data to external files)
	 */
	public static List<ReportBean> getFlatList(List<ReportBean> hierarchicalList) {
		List<ReportBean> flatList = new LinkedList<ReportBean>();
		for (ReportBean reportBean : hierarchicalList) {
			flatList.add(reportBean);
			// check if we need to add the children
			if (reportBean.isHasSons()) {
				flatList.addAll(getFlatList(reportBean.getChildren()));
			}
		}
		return flatList;
	}

	/**
	 * Build the item-tree up to a given level
	 * @param level
	 */
	private static List<ReportBean> setTree(List<ReportBean> reportBeansFlat,
			Map<Integer, ReportBean> reportBeansMap) {
		if (reportBeansMap == null) {
			return null;
		}
		// add the root level
		List<ReportBean> reportBeansFirstLevel = addRootItems(reportBeansFlat, reportBeansMap);;
		int i = 0;
		while (MAX_LEVELS >= i && !reportBeansFlat.isEmpty()) {
			addItemsToLevel(reportBeansFlat, ++i, reportBeansMap);
		}
		// add all the rest
		addRestItems(reportBeansFlat, reportBeansFirstLevel);
		for (ReportBean reportBean : reportBeansFirstLevel) {
			reportBean.setNotClosedConflictingAncestor(hasNotClosedConflictingDescendant(reportBean));
		}
		return reportBeansFirstLevel;
	}

	/**
	 * Whether the report bean has a nor closed conflicting descendant
	 * @param reportBean
	 * @return
	 */
	private static boolean hasNotClosedConflictingDescendant(ReportBean reportBean) {
		boolean notClosedConflictingAncestor = false;
		List<ReportBean> children = reportBean.getChildren();
		Integer closedStateFlag = Integer.valueOf(TStateBean.STATEFLAGS.CLOSED);
		if (children==null || children.isEmpty()) {
			notClosedConflictingAncestor = !closedStateFlag.equals(reportBean.getStateFlag()) &&
					(reportBean.isDateConflict() || reportBean.isBudgetOrPlanConflict());
		} else {
			for (ReportBean childReportBean : children) {
				notClosedConflictingAncestor = !closedStateFlag.equals(childReportBean.getStateFlag()) &&
						hasNotClosedConflictingDescendant(childReportBean);
				if (notClosedConflictingAncestor) {
					break;
				}
			}
		}
		reportBean.setNotClosedConflictingAncestor(notClosedConflictingAncestor);
		return notClosedConflictingAncestor;
	}

	/**
	 * Add all root-items (with no parents or parents not present in the result set)
	 * to reportBeansFirstLevel
	 */
	private static List<ReportBean> addRootItems(List<ReportBean> reportBeansFlat,
			 Map<Integer, ReportBean> reportBeansMap) {
		List<ReportBean> reportBeansFirstLevel = new ArrayList<ReportBean>();
		for (Iterator<ReportBean> iterator = reportBeansFlat.iterator(); iterator.hasNext();) {
			ReportBean reportBean = iterator.next();
			if (!hasParentInList(reportBean, reportBeansMap)) {
				// item has no parents in the hashtable -> add it
				reportBeansFirstLevel.add(reportBean);
				reportBean.setInReportList(true); // mark as added
				reportBean.setLevel(0); // in level 0
				reportBean.setHasSons(false);
				iterator.remove();
				LOGGER.debug("Build tree: add item "
						+ reportBean.getWorkItemBean().getObjectID()
						+ " to level 0");
			}
		}
		return reportBeansFirstLevel;
	}

	/**
	 * Add all currently not added items as flat items
	 * They are in the hierarchy lower as the maximal allowed level
	 * or they have circular dependences in which case
	 * the lowest level should be limited to avoid infinite circles
	 */
	private static void addRestItems(List<ReportBean> reportBeansFlat,
			List<ReportBean> reportBeansFirstLevel) {
		// check all items from the hashtable
		for (Iterator<ReportBean> iterator = reportBeansFlat.iterator(); iterator.hasNext();) {
			ReportBean reportBean = iterator.next();
			if (!reportBean.isInReportList()) {
				// item has no parents in the hastable -> add it
				reportBeansFirstLevel.add(reportBean);
				reportBean.setInReportList(true); // mark as added
				reportBean.setLevel(0); // in level 0
				reportBean.setHasSons(false);
				iterator.remove();
				LOGGER.debug("Build tree: add item "
						+ reportBean.getWorkItemBean().getObjectID()
						+ " to level 0 (rest)");
			}
		}
	}

	
	
	/**
	 * Add all the items which belong to a specific level
	 * @param level
	 */
	private static void addItemsToLevel(List<ReportBean> reportBeansFlat, int level,
			Map<Integer, ReportBean> reportBeansMap) {
		Integer closedStateFlag = Integer.valueOf(TStateBean.STATEFLAGS.CLOSED);
		for (Iterator<ReportBean> iterator = reportBeansFlat.iterator(); iterator.hasNext();) {
			ReportBean reportBean = iterator.next();
			if (!reportBean.isInReportList() && hasParentInList(reportBean, reportBeansMap)) {
				// check if the parent is one level below
				ReportBean parentItem = reportBeansMap.get(reportBean.getWorkItemBean().getSuperiorworkitem());
				if (parentItem.isInReportList()
						&& (parentItem.getLevel() == level - 1)) {
					// add item to the parent's list
					parentItem.addChild(reportBean);
					parentItem.setHasSons(true);
					reportBean.setInReportList(true); // mark as added
					reportBean.setLevel(level);
					if (reportBean.isCommittedDateConflict() && !closedStateFlag.equals(reportBean.getStateFlag())) {
						//propagate the overdue flag to the nearest non-overdue ancestor
						ReportBean parentItemCommittedDateConflict = parentItem;
						while (parentItemCommittedDateConflict!=null && !parentItemCommittedDateConflict.isCommittedDateConflict()) {
							parentItemCommittedDateConflict.setCommittedDateConflict(true);
							Integer parentID = parentItemCommittedDateConflict.getWorkItemBean().getSuperiorworkitem();
							if (parentID!=null) {
								parentItemCommittedDateConflict = reportBeansMap.get(parentID);
							} else {
								break;
							}
						}
					}
					if (reportBean.isTargetDateConflict() && !closedStateFlag.equals(reportBean.getStateFlag())) {
						//propagate the overdue flag to the nearest non-overdue ancestor
						ReportBean parentItemTargetDateConflict = parentItem;
						while (parentItemTargetDateConflict!=null && !parentItemTargetDateConflict.isTargetDateConflict()) {
							parentItemTargetDateConflict.setTargetDateConflict(true);
							Integer parentID = parentItemTargetDateConflict.getWorkItemBean().getSuperiorworkitem();
							if (parentID!=null) {
								parentItemTargetDateConflict = reportBeansMap.get(parentID);
							} else {
								break;
							}
						}
					}
					if (reportBean.isPlannedValueConflict() && !closedStateFlag.equals(reportBean.getStateFlag())) {
						//propagate the over budget flag to the nearest non over budget ancestor
						ReportBean parentItemPlannedValueConflict = parentItem;
						while (parentItemPlannedValueConflict!=null && !parentItemPlannedValueConflict.isPlannedValueConflict()) {
							parentItemPlannedValueConflict.setPlannedValueConflict(true);
							Integer parentID = parentItemPlannedValueConflict.getWorkItemBean().getSuperiorworkitem();
							if (parentID!=null) {
								parentItemPlannedValueConflict = reportBeansMap.get(parentID);
							} else {
								break;
							}
						}
					}
					if (reportBean.isBudgetConflict() && !closedStateFlag.equals(reportBean.getStateFlag())) {
						//propagate the over budget flag to the nearest non over budget ancestor
						ReportBean parentItemBudgetConflict = parentItem;
						while (parentItemBudgetConflict!=null && !parentItemBudgetConflict.isBudgetConflict()) {
							parentItemBudgetConflict.setBudgetConflict(true);
							Integer parentID = parentItemBudgetConflict.getWorkItemBean().getSuperiorworkitem();
							if (parentID!=null) {
								parentItemBudgetConflict = reportBeansMap.get(parentID);
							} else {
								break;
							}
						}
					}
					iterator.remove();
					LOGGER.debug("Build tree: add item "
							+ reportBean.getWorkItemBean().getObjectID()
							+ " to level " + level);
				}
			}
		}
	}

	/**
	 * Check if the item has a parent in the hashtable
	 * @param reportBean
	 * @return
	 */
	private static boolean hasParentInList(ReportBean reportBean, Map<Integer, ReportBean> reportBeansMap) {
		Integer parentID = reportBean.getWorkItemBean().getSuperiorworkitem();
		if (parentID==null) {
			return false;
		}
		return reportBeansMap.containsKey(parentID);
	}


	/**
	 * Creates the hierarchical list from the flat list
	 * @param reportBeansFlat
	 * @param reportBeansMap
	 * @param sortNeeded
	 * @param sortField
	 * @param sortOrder
	 * @param plainData
	 * @return
	 */
	static List<ReportBean> setHierarchieAndSort(
			List<ReportBean> reportBeansFlat, Map<Integer, ReportBean> reportBeansMap,
			boolean sortNeeded, Integer sortField, Boolean sortOrder,
			boolean plainData) {
		List<ReportBean> reportBeansFirstLevel = null;
		if (plainData) {
			reportBeansFirstLevel = reportBeansFlat;
		} else {
			reportBeansFirstLevel = setTree(reportBeansFlat, reportBeansMap);
		}
		if (sortNeeded) {
			//sort the result at each level
			sort(reportBeansFirstLevel, sortField, sortOrder, plainData);
		}
		return reportBeansFirstLevel;
	}


	/**
	 * Sorting when no grouping is active or inside a grouping
	 * It should be taken into account the hierarchical (parent child)
	 * structure and sort separately on each level
	 * @param reportBeansFirstLevel
	 * @param sortField
	 * @param sortOrder
	 * @param plainData
	 */
	public static void sort(List<ReportBean> reportBeansFirstLevel, Integer sortField, Boolean sortOrder, boolean plainData) {
		//configure the comparator
		Comparator<ReportBean> comparator = new ReportBeanComparator(sortOrder,	sortField, null, true);
		//sort reportBeans by comparator on each level recursively
		sortBeans(comparator, reportBeansFirstLevel, plainData);
	}

	/**
	 * Helper function to make a recursive sort of workItems and children
	 * @param comp
	 * @param reportBeans
	 */
	private static void sortBeans(Comparator<ReportBean> comp, List<ReportBean> reportBeans, boolean plainData) {
		if (reportBeans == null) {
			return;
		}
		// sort the list
		Collections.sort(reportBeans, comp);
		if (!plainData) {
			// now check for children and sort these too
			for (ReportBean reportBean : reportBeans) {
				if (reportBean.isHasSons()) {
					sortBeans(comp, reportBean.getChildren(), plainData);
				}
			}
		}
	}

	/**
	 * Expand/collapse the parent workItems
	 * @param allItemsExpanded
	 * @param otherItemsSet
	 * @param reportBeansMap
	 */
	static void expandCollapseParents(Boolean allItemsExpanded, Set<Integer> otherItemsSet, Map<Integer, ReportBean> reportBeansMap) {
		boolean allExpandedInEffect = false;
		//expand nodes based on previous expand/collapse operations
		if (allItemsExpanded!=null && allItemsExpanded.booleanValue()) {
			//all is expanded...
			for (ReportBean reportBean : reportBeansMap.values()) {
				reportBean.setExpanded(allItemsExpanded.booleanValue());
			}
			allExpandedInEffect=true;
		}
		//set the expanded flag for the other nodes (those node which does not correspond to the actual allItemsExpanded flag)
		if (otherItemsSet!=null && !otherItemsSet.isEmpty()) {
			for (Integer workItemID : otherItemsSet) {
				ReportBean reportBean = reportBeansMap.get(workItemID);
				if (reportBean!=null) {
					reportBean.setExpanded(!allExpandedInEffect);
				}
			}
		}
	}

	public static Set<Integer> getProjectIDs(List<ReportBean> list){
		Set<Integer> result=new HashSet<Integer>();
		if(list!=null){
			for(ReportBean reportBean:list){
				result.add(reportBean.getWorkItemBean().getProjectID());
			}
		}
		return result;
	}

	/************************************/
	/** tree type field utility methods**/
	/************************************/

	/**
	 * Gets the sort order map for the tree based grouping
	 * Although the sortOrder field can be any comparable (string, integer etc.)
	 * the tree will be linearized based on sort order and the resulting map
	 * is based on the order in the resulting list
	 * @param reportBeansFlat
	 * @param fieldID
	 * @param groupFieldOrder
	 * @return
	 */
	static Map<Integer, Integer> getTreeFieldOrderMap(List<SimpleTreeNode> simpleTreeNodes, boolean isDescending) {
		Comparator<SimpleTreeNode> comparator = new SimpleTreeNodedComparator(isDescending);
		sortBeans(simpleTreeNodes, comparator);
		List<Integer> orderedObjectIDList = new ArrayList<Integer>();
		traverseInPreorder(simpleTreeNodes, orderedObjectIDList);
		Map<Integer, Integer> orderedObjectsMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < orderedObjectIDList.size(); i++) {
			orderedObjectsMap.put(orderedObjectIDList.get(i), Integer.valueOf(i));
		}
		return orderedObjectsMap;

	}

	/**
	 * Recursive sort of a tree
	 * @param simpleTreeNodes
	 */
	private static void sortBeans(List<SimpleTreeNode> simpleTreeNodes, Comparator<SimpleTreeNode> comparator) {
		if (simpleTreeNodes == null) {
			return;
		}
		Collections.sort(simpleTreeNodes, comparator);
		//check for children and sort these too
		for (SimpleTreeNode reportBean : simpleTreeNodes) {
			List<SimpleTreeNode> children = reportBean.getChildren();
			if (children!=null) {
				sortBeans(children, comparator);
			}
		}
	}

	/**
	 * Fill the list by traversing the tree in pre-order
	 * @param simpleTreeNodes
	 * @param preorderList
	 */
	private static void traverseInPreorder(List<SimpleTreeNode> simpleTreeNodes, List<Integer> preorderList) {
		if (simpleTreeNodes!=null) {
			for (SimpleTreeNode simpleTreeNode : simpleTreeNodes) {
				preorderList.add(simpleTreeNode.getObjectID());
				traverseInPreorder(simpleTreeNode.getChildren(), preorderList);
			}
		}
	}

	/**
	 * Gets the child to parent map
	 * @param simpleTreeNodes
	 * @return
	 */
	static  Map<Integer, Integer> getChildToParentMap(List<SimpleTreeNode> simpleTreeNodes) {
		Map<Integer, Integer> childToParentMap = new HashMap<Integer, Integer>();
		getChildToParentMap(simpleTreeNodes, null, childToParentMap);
		return childToParentMap;
	}

	/**
	 * Fill the list by traversing the tree in preorder
	 * @param simpleTreeNodes
	 * @param preorderList
	 */
	private static void getChildToParentMap(List<SimpleTreeNode> simpleTreeNodes, Integer parentID, Map<Integer, Integer> childToParentMap) {
		if (simpleTreeNodes!=null) {
			for (SimpleTreeNode simpleTreeNode : simpleTreeNodes) {
				Integer childID = simpleTreeNode.getObjectID();
				if (parentID!=null) {
					childToParentMap.put(childID, parentID);
				}
				getChildToParentMap(simpleTreeNode.getChildren(), childID, childToParentMap);
			}
		}
	}


	/**
	 * Gets the id to label map
	 * @param simpleTreeNodes
	 * @return
	 */
	static Map<Integer, String> getIDToLabelMap(List<SimpleTreeNode> simpleTreeNodes) {
		Map<Integer, String> idToLabelMap = new HashMap<Integer, String>();
		getIDToLabelMap(simpleTreeNodes, idToLabelMap);
		return idToLabelMap;
	}

	/**
	 * Fills the id to label map
	 * @param simpleTreeNodes
	 * @param preorderList
	 */
	private static void getIDToLabelMap(List<SimpleTreeNode> simpleTreeNodes, Map<Integer, String> idToLabelMap) {
		if (simpleTreeNodes!=null) {
			for (SimpleTreeNode simpleTreeNode : simpleTreeNodes) {
				Integer objectID = simpleTreeNode.getObjectID();
				String label = simpleTreeNode.getLabel();
				idToLabelMap.put(objectID, label);
				getIDToLabelMap(simpleTreeNode.getChildren(), idToLabelMap);
			}
		}
	}
}
