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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ITreeSelect;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.itemNavigator.ItemTreeNode;
import com.aurel.track.itemNavigator.layout.group.GroupFieldTO;
import com.aurel.track.itemNavigator.layout.group.SortFieldTO;
import com.aurel.track.report.group.GroupLimitBean;
import com.aurel.track.report.group.GroupLimitComparator;
import com.aurel.track.report.group.ReportGroupBL;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.TreeNode;

/**
 * This bean contains a collection of ReportBean, each representing the
 * attributes of one row of the result set obtained by executing an 
 * SQL query against the database using the queryString.
 */
public class ReportBeans implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(ReportBeans.class);
	
	/**
	 * A flat list containing all ReportBeans to be shown in the right order
	 */
	private List<ReportBean> reportBeansFlat = new LinkedList<ReportBean>();

	/**
	 * A list containing the ReportBeans from the first level: root beans and 
	 * child beans whose parents are not in the result set
	 * The other child beans are contained in the children collection of the parent beans
	 */
	private List<ReportBean> reportBeansFirstLevel = new LinkedList<ReportBean>();

	/**
	 * A map containing all ReportBeans: this is filled after querying the database
	 * and remains an invariant for the rest of the live of this ReportBean
	 * It is not affected by layout change or expand/collapse operations
	 * 	-	key: workItemID
	 * 	-	value: ReportBean
	 */
	private Map<Integer, ReportBean> reportBeansMap = new Hashtable<Integer, ReportBean>(10);

	/**
	 * A map containing the grouping limits: used mainly at the UI
	 * 	-	key: workItemID
	 * 	-	value: list of localized group headers
	 */
	private Map<Integer, List<GroupLimitBean>> groupingLimits;
	
	/**
	 * The actual group by list
	 */
	private List<GroupFieldTO> groupByList;
	
	/**
	 * Whether the sorting by the user profile settings are needed
	 * In case of TQL with ORDER_BY no sorting is needed because 
	 * the issues are eventually sorted at the database level  
	 */
	private boolean sortNeeded = true;
	private Integer sortField;
	private Boolean sortOrder;
	private boolean plainData=false;

	/**
	 * Initializes the collections according to rights and found reportBeans: used when the ReportBeans are newly loaded from the database 
	 * @param reportBeanList
	 * @param personID
	 * @param reportBeanExpandContext
	 * @param sortNeeded
	 */
	public ReportBeans(List<ReportBean> reportBeanList, Locale locale, ReportBeanExpandContext reportBeanExpandContext, boolean sortNeeded) {
		this(reportBeanList, locale, reportBeanExpandContext, sortNeeded, false);
	}
	
	/**
	 * Sort the report beans by wbs
	 * @param reportBeanList
	 * @param locale
	 * @param reportBeanExpandContext
	 */
	public ReportBeans(List<ReportBean> reportBeanList, Locale locale) {
		init(reportBeanList, locale, null, true, SystemFields.INTEGER_WBS, Boolean.FALSE, false);
	}
	
	public ReportBeans(List<ReportBean> reportBeanList, Locale locale, ReportBeanExpandContext reportBeanExpandContext, boolean sortNeeded, boolean plainData) {
		Integer sortField = null;
		Boolean sortOrder = null;
		if (reportBeanExpandContext!=null) {
			SortFieldTO sortFieldTO = reportBeanExpandContext.getSortFieldTO();
			if (sortFieldTO!=null) {
				sortField = sortFieldTO.getFieldID();
				sortOrder = sortFieldTO.isDescending();
			}
		}
		init(reportBeanList, locale, reportBeanExpandContext, sortNeeded, sortField, sortOrder, plainData);
	}
	
	/**
	 * Create report beans with project (mock a project as report bean for moving the entire project in Gantt diagram)
	 * @param originalReportBeans
	 * @param projectBean
	 * @return
	 */
	public ReportBeans getReportBeans(ReportBeans originalReportBeans, TProjectBean projectBean) {
		ReportBean projectReportBean = new ReportBean(projectBean);
		List<ReportBean> originalFirstLevelItems = originalReportBeans.getReportBeansFirstLevel();
		List<ReportBean> originalReportBeansFlat = originalReportBeans.getReportBeansFlat();
		for (ReportBean reportBean : originalFirstLevelItems) {
			projectReportBean.addChild(reportBean);
			projectReportBean.setHasSons(true);
		}
		List<ReportBean> firstLevelItems = new LinkedList<ReportBean>();
		List<ReportBean> reportBeansFlat = new LinkedList<ReportBean>();
		firstLevelItems.add(projectReportBean);
		reportBeansFlat.add(projectReportBean);
		reportBeansFlat.addAll(originalReportBeansFlat);
		Map<Integer, ReportBean> reportBeansMap = new HashMap<Integer, ReportBean>();
		reportBeansMap.putAll(originalReportBeans.getReportBeansMap());
		//projectID might overwrite itemID!!!
		ReportBeans reportBeansResult =  new ReportBeans();
		reportBeansResult.setReportBeansFirstLevel(firstLevelItems);
		reportBeansResult.setReportBeansFlat(reportBeansFlat);
		reportBeansResult.setReportBeansMap(reportBeansMap);
		return reportBeansResult;
	}

	
	/**
	 * Only to be used in previous constructor
	 */
	private ReportBeans() {
		super();
	}
	
	private void init(List<ReportBean> reportBeanList, Locale locale, ReportBeanExpandContext reportBeanExpandContext,
			boolean sortNeeded, Integer sortField, Boolean sortOrder, boolean plainData){
		this.sortNeeded = sortNeeded;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
		this.plainData = plainData;
		// add all items
		if(reportBeanList!=null){
			for (ReportBean reportBean : reportBeanList) {
				addItem(reportBean);
			}
		}
		cleanup();
		Boolean allItemsExpanded = Boolean.TRUE;
		Set<Integer> otherItemsSet = null;
		Set<String> groupOtherExpandCollapse = null;
		if (reportBeanExpandContext!=null) {
			allItemsExpanded = reportBeanExpandContext.getAllItemsExpanded();
			otherItemsSet = reportBeanExpandContext.getOtherItemsSet();
			groupOtherExpandCollapse = reportBeanExpandContext.getOtherGroupsSet();
			this.groupByList = reportBeanExpandContext.getGroupBy();
		}
		prepareStructure(allItemsExpanded, otherItemsSet, groupOtherExpandCollapse, sortField, sortOrder, locale);
	}
	
	private void prepareStructure(Boolean allItemsExpanded, Set<Integer> otherItemsSet, Set<String> groupOtherExpandCollapse,
			Integer sortField, Boolean sortOrder, Locale locale) {
		ReportBeansBL.expandCollapseParents(allItemsExpanded, otherItemsSet, reportBeansMap);				
		if (groupByList==null || groupByList.isEmpty()) {
			//no grouping configured			
			reportBeansFirstLevel = ReportBeansBL.setHierarchieAndSort(reportBeansFlat, reportBeansMap, sortNeeded,  sortField, sortOrder, plainData);
			if (!plainData && reportBeansFlat!=null &&  !reportBeansFlat.isEmpty()) {
				LOGGER.warn("The reportBeansFlat is not empty after setHierarchieAndSort");
			}
			reportBeansFlat = ReportBeansBL.getFlatList(reportBeansFirstLevel);
		} else {
			group(locale, groupByList, groupOtherExpandCollapse, sortNeeded, plainData);
		}
	}
	
	public List<ReportBean>  getReportBeansFirstLevel(){
		return reportBeansFirstLevel;
	}
	
	public void setReportBeansFirstLevel(List<ReportBean> reportBeansFirstLevel) {
		this.reportBeansFirstLevel = reportBeansFirstLevel;
	}

	/**
	 * Adds an item to each collection
	 * @param reportBean
	 */
	private void addItem(ReportBean reportBean) {
		reportBeansFlat.add(reportBean);
		reportBeansMap.put(reportBean.getWorkItemBean().getObjectID(), reportBean);
	}
	
	/**
	 * Gets a list of all visible items for showing it on the JSP 
	 * @return 
	 */
	public List<ReportBean> getItems() {
			//all ReportBeans are included but only those with inGroupedList set to true will be shown:
			//needed because the grouping limit rows will be shown in the iteration for the ReportBeans
			//and if the ReportBeans are collapsed (not shown) for a group then the grouping limit rows would be also absent
			return reportBeansFlat;
	}
	
	/**
	 * Gets the item rows (with grouping rows if grouped) 
	 * @param plainData
	 * @return
	 */
	public List<ItemTreeNode> getReportRows(){
		List<ItemTreeNode> reportRows = new LinkedList<ItemTreeNode>();
		if (this.groupingLimits==null) {
			for (ReportBean reportBean : reportBeansFirstLevel) {
				reportRows.add(getTreeNodeFromReportBean(reportBean));
			}
		} else {
			List<GroupLimitBean> groupLimitBeansOnFirstLevel = new LinkedList<GroupLimitBean>();
			GroupFieldTO firstLevelGroupFieldBean = this.groupByList.get(0);
			Integer firstLevelGroupByField = firstLevelGroupFieldBean.getFieldID();
			boolean firstLevelGroupIsDescending = firstLevelGroupFieldBean.isDescending();
			for (Integer workItemID : this.groupingLimits.keySet()) {
				List<GroupLimitBean> groupLimitBeans = this.groupingLimits.get(workItemID);
				if (groupLimitBeans!=null && !groupLimitBeans.isEmpty()) {
					//workItem marks a group limit
					GroupLimitBean groupLimitBean = groupLimitBeans.get(0);
					//get the groups from the first level 
					if (firstLevelGroupByField.equals(groupLimitBean.getGroupField())) {
						if (groupLimitBean.isTreeField()) {
							//get only if on the first level in tree
							Integer depthInTree = groupLimitBean.getDepthInTree();
							if (depthInTree!=null && depthInTree.intValue()==GroupLimitBean.FIRST_LEVEL_DEPTH) {
								groupLimitBeansOnFirstLevel.add(groupLimitBean);
							}
						} else {
							groupLimitBeansOnFirstLevel.add(groupLimitBean);
						}
					}
				}
			}
			Collections.sort(groupLimitBeansOnFirstLevel, new GroupLimitComparator(firstLevelGroupIsDescending));
			for (GroupLimitBean groupLimitBean : groupLimitBeansOnFirstLevel) {
				reportRows.add(getTreeNodeFromGroup(groupLimitBean));
			}
		}
		return reportRows;
	}
	
	/**
	 * Get the tree node from a group limit bean
	 * @param groupLimitBean
	 * @return
	 */
	private ItemTreeNode getTreeNodeFromGroup(GroupLimitBean groupLimitBean) {
		ItemTreeNode itemTreeNode = new ItemTreeNode(groupLimitBean.getOrdinalNumberString(), groupLimitBean.getGroupLabel());
		itemTreeNode.setGroupLimitBean(groupLimitBean);
		List<TreeNode> childrenNodes = new LinkedList<TreeNode>();
		itemTreeNode.setChildren(childrenNodes);
		List<GroupLimitBean> subgroupLimitBeanList = groupLimitBean.getSubgroups();
		if (subgroupLimitBeanList!=null && !subgroupLimitBeanList.isEmpty()) {
			List<GroupLimitBean> childGroupLimitBeans = new LinkedList<GroupLimitBean>();
			for (GroupLimitBean childGroupLimitBean : subgroupLimitBeanList) {
				childGroupLimitBeans.add(childGroupLimitBean);
			}
			Collections.sort(childGroupLimitBeans, new GroupLimitComparator(this.groupByList.get(groupLimitBean.getGroupLevel()+1).isDescending()));
			for (GroupLimitBean chilsGroupLimitBean : childGroupLimitBeans) {
				childrenNodes.add(getTreeNodeFromGroup(chilsGroupLimitBean));
			}
		} else {
			List<ReportBean> groupReportBeans = groupLimitBean.getReportBeans();
			if (groupReportBeans!=null) {
				for (ReportBean reportBean : groupReportBeans) {
					childrenNodes.add(getTreeNodeFromReportBean(reportBean));
				}
			}
		}
		//a "flat" group might include directly either subgroups from lower level or items, but not both
		//a "tree" group might include tree branches (sub-folders) and either subgroups from lower level or items
		//(the sub-folders are here added at the end an it is expected to appear at the end but the real rendering oreder depends on client side ordering)
		List<GroupLimitBean> treeLimitBeans = groupLimitBean.getTreeSubgroups();
		if (treeLimitBeans!=null && !treeLimitBeans.isEmpty()) {
			for (GroupLimitBean treeLimitBean : treeLimitBeans) {
				childrenNodes.add(getTreeNodeFromGroup(treeLimitBean));
			}
		}
		return itemTreeNode;
	}
	
	private static ItemTreeNode getTreeNodeFromReportBean(ReportBean reportBean) {
		ItemTreeNode itemTreeNode = new ItemTreeNode(reportBean.getWorkItemBean().getObjectID().toString(), reportBean.getWorkItemBean().getSynopsis());
		itemTreeNode.setReportBean(reportBean);
		List<ReportBean> children = reportBean.getChildren(); 
		if (children!=null && !children.isEmpty()) {
			itemTreeNode.setLeaf(Boolean.FALSE);
			List<TreeNode> childrenNodes = new LinkedList<TreeNode>();
			itemTreeNode.setChildren(childrenNodes);
			for (ReportBean childReportBean : children) {
				childrenNodes.add(getTreeNodeFromReportBean(childReportBean));
			}
		} else {
			itemTreeNode.setLeaf(Boolean.TRUE);
		}
		return itemTreeNode;
	}
		
	
	/**
	 * Cleanup the internal structure of the ReportBean's  
	 */
	private void cleanup() {
		 // get all items from the hashtable
		for (ReportBean reportBean : reportBeansMap.values()) {
			 // mark as not in list yet
			 reportBean.setInReportList(false);
			 // marks as not expanded
			 reportBean.setExpanded(false);
			 // remove all children
			 reportBean.removeChildren();
			 //reset the hasSons flag 
			 reportBean.setHasSons(false);
			 //reset the level
			 reportBean.setLevel(0);
			 reportBean.setParent(null);			 
		 }		
	 }

	

	/**
	 * Get a reportItem from the report
	 * @param workItemKey, key of thr reportItem
	 * @return
	 */
	public ReportBean getItem(Integer workItemKey) {
		return reportBeansMap.get(workItemKey);
	}
	
	/**
	 * Gets the previous sibling of the current item in case of indent
	 * @param workItemKey
	 * @return
	 */
	public ReportBean getPreviousSibling(Integer workItemKey) {
		ReportBean reportBean = getItem(workItemKey);
		if (reportBean!=null) {
			List<ReportBean> children;
			ReportBean parentReportBean = reportBean.getParent();
			if (parentReportBean==null) {
				children = reportBeansFirstLevel; 
			} else {
				children = parentReportBean.getChildren();
			}
			if (children!=null) {
				ReportBean previousSibling = null;
				for (Iterator<ReportBean> iterator = children.iterator(); iterator.hasNext();) {						
					ReportBean childReportBean = iterator.next();
					if (childReportBean.getWorkItemBean().getObjectID().equals(workItemKey)) {
						return previousSibling; 
					}
					previousSibling = childReportBean;
				}
			}			
		}
		return null;
	}
	
	/**
	 * Gets the previous sibling of the current item
	 * @param workItemKey
	 * @return
	 */
	public List<ReportBean> getNextSiblings(Integer workItemKey) {
		List<ReportBean> nextSiblings = new LinkedList<ReportBean>();
		ReportBean reportBean = getItem(workItemKey);
		if (reportBean!=null) {
			List<ReportBean> children;
			ReportBean parentReportBean = reportBean.getParent();
			if (parentReportBean==null) {
				children = reportBeansFirstLevel; 
			} else {
				children = parentReportBean.getChildren();
			}
			if (children!=null) {
				int indexOfElement = children.indexOf(reportBean);
				if (indexOfElement!=-1) {
					nextSiblings = children.subList(indexOfElement+1, children.size());
				}				
			}
		}		
		return nextSiblings;
	}
	
	/**
	 * set all report-items to expanded/collapsed
	 * @param expanded, true or false
	 */
	public void setExpandedAll(boolean expanded) {
		// get all items from the hashtable
		Iterator<ReportBean> itemIter = reportBeansMap.values().iterator();
		while (itemIter.hasNext()) {
			ReportBean reportBean = itemIter.next();
			reportBean.setExpanded(expanded);
		}
	}
	
	/**
	 * Get the number ow items retrieved by the current query string.
	 */
	public int getCount() {
		if (this.reportBeansMap != null) {
			return this.reportBeansMap.size();
		} else {
			return 0;
		}
	}
	
	/**
	 * Groups the reportBeans independently of the parent child hierarchy
	 */
	private void group(Locale locale, List<GroupFieldTO> groupByList, Set<String> groupOtherExpandCollapse, boolean sortNeeded, boolean plainData) {		
		//search for tree based grouping fields
		Map<Integer, Map<Integer, Integer>> treeFieldsToSortOrderMap = null;
		Map<Integer, Map<Integer, Integer>> treeFieldsToChildToParentMap = null;
		Map<Integer, Map<Integer, String>> treeFieldsToIDToLabelMap = null;
		for (GroupFieldTO groupFieldTO : groupByList) {
			Integer groupFieldID = groupFieldTO.getFieldID();
			boolean groupFieldIsDescending = groupFieldTO.isDescending();
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(groupFieldID);
			if (fieldTypeRT!=null && fieldTypeRT.isLookup()) {
				ILookup lookup = (ILookup)fieldTypeRT;
				if (lookup.isTree() && fieldTypeRT instanceof ITreeSelect) {
					//to do implement ITreeSelect also for release
					ITreeSelect treeSelect = (ITreeSelect)fieldTypeRT;
					//tree based grouping field found
					Set<Integer> treeFieldValues = new HashSet<Integer>();
					for (ReportBean reportBean : reportBeansFlat) {
						Integer fieldValue = (Integer)reportBean.getWorkItemBean().getAttribute(groupFieldID);
						if (fieldValue!=null) {
							treeFieldValues.add(fieldValue);
						}
					}
					//gets the involved tree branches without holes  
					List<SimpleTreeNode> simpleTreeNodes = treeSelect.getSimpleTreeNodesWithCompletePath(GeneralUtils.createIntegerListFromCollection(treeFieldValues));
					//build the child to parent map
					if (treeFieldsToChildToParentMap==null) {
						treeFieldsToChildToParentMap = new HashMap<Integer, Map<Integer,Integer>>();
					}
					treeFieldsToChildToParentMap.put(groupFieldID, ReportBeansBL.getChildToParentMap(simpleTreeNodes));
					if (treeFieldsToIDToLabelMap==null) {
						treeFieldsToIDToLabelMap = new HashMap<Integer, Map<Integer,String>>();
					}
					treeFieldsToIDToLabelMap.put(groupFieldID, ReportBeansBL.getIDToLabelMap(simpleTreeNodes));
					
					//builds the sort order map
					Map<Integer, Integer> treeFieldSortOrderMap = ReportBeansBL.getTreeFieldOrderMap(simpleTreeNodes, groupFieldIsDescending);
					if (treeFieldsToSortOrderMap==null) {
						treeFieldsToSortOrderMap = new HashMap<Integer, Map<Integer,Integer>>();
					}
					treeFieldsToSortOrderMap.put(groupFieldID, treeFieldSortOrderMap);
				}
			}
		}
		//do not sort by sortField/sortOrder because it might not be needed at all (for TQL queries)
		//and if it is needed then it will be made in processInsideGroup 
		ReportBeanComparator comparator = new ReportBeanComparator(null, null, groupByList, false);
		if (treeFieldsToSortOrderMap!=null) {
			comparator.setTreeFieldToSortOrderMap(treeFieldsToSortOrderMap);
		}
		//sort reportBeans by groups		
		Collections.sort(reportBeansFlat, comparator);
		
		//get the limits but just when not already set
		//uncomment the if when groupingLimits of the new ReportBean is given as parameter
		//from the old ReportBean to avoid recalculating of it in case of manual expand/collapse 
		this.groupingLimits = ReportGroupBL.populateGroupingLimits(this, groupByList, treeFieldsToChildToParentMap, treeFieldsToIDToLabelMap, treeFieldsToSortOrderMap, groupOtherExpandCollapse, locale);
		
		List<ReportBean> newReportBeansFlat = new LinkedList<ReportBean>();
		//gather the report beans form the same group
		List<ReportBean> reporBeansInTheSameGroup = new LinkedList<ReportBean>();
		Integer previousGroupLimitWorkItemID = null;
		for (ReportBean reportBean : reportBeansFlat) {
			Integer workItemID = reportBean.getWorkItemBean().getObjectID();			
			if (groupingLimits.containsKey(workItemID)) {
				if (!reporBeansInTheSameGroup.isEmpty()) {
					//process the previous group with the parent-child hierarchy logic
					List<ReportBean> flatList = processInsideGroup(reporBeansInTheSameGroup, 
							reportBeansMap, previousGroupLimitWorkItemID, 
							groupingLimits, sortNeeded, sortField, sortOrder, plainData);
					newReportBeansFlat.addAll(flatList);					
				}
				previousGroupLimitWorkItemID = workItemID;
				reporBeansInTheSameGroup = new LinkedList<ReportBean>();		
			}
			reporBeansInTheSameGroup.add(reportBean);
		}
		//process the last group with the parent-child hierarchy logic 
		if (!reporBeansInTheSameGroup.isEmpty()) {
			List<ReportBean> flatList = processInsideGroup(reporBeansInTheSameGroup, 
					reportBeansMap, previousGroupLimitWorkItemID, 
					groupingLimits, sortNeeded, sortField, sortOrder, plainData);		
			newReportBeansFlat.addAll(flatList);			
		}
		reportBeansFlat = newReportBeansFlat;
	}
	
	/**
	 * Process the ReportBeans from a group according to the parent-child hierarchy
	 * @param reportBeansInTheSameGroup
	 * @param reportBeansMap
	 * @param previousGroupLimitWorkItemID
	 * @param groupingLimits
	 * @param sortNeeded
	 * @param sortField
	 * @param sortOrder
	 * @param plainData
	 * @return
	 */
	private List<ReportBean> processInsideGroup(
			List<ReportBean> reportBeansInTheSameGroup, 
			Map<Integer, ReportBean> reportBeansMap,
			Integer previousGroupLimitWorkItemID, 
			Map<Integer, List<GroupLimitBean>> groupingLimits, 
			boolean sortNeeded,  Integer sortField, Boolean sortOrder,
			boolean plainData) {
		//set the parent-child hierarchies and then sort at each level 					 
		List<ReportBean> reportBeansFirstLevel = ReportBeansBL.setHierarchieAndSort(
				reportBeansInTheSameGroup, filterMap(reportBeansInTheSameGroup, reportBeansMap), sortNeeded,  sortField, sortOrder, plainData);
		//set the number of children for the last grouping level: the number of items from the first level
		if (groupingLimits!=null) {		
			List<GroupLimitBean> groupingLimitsList = groupingLimits.get(previousGroupLimitWorkItemID);
			if (groupingLimitsList!=null && !groupingLimitsList.isEmpty()) {
				GroupLimitBean groupLimitBean = groupingLimitsList.get(groupingLimitsList.size()-1);
				groupLimitBean.setReportBeans(reportBeansFirstLevel);
			}
		}		
		//build the flat list from the hierarchical list
		List<ReportBean> flatList = ReportBeansBL.getFlatList(reportBeansFirstLevel);
		if (previousGroupLimitWorkItemID!=null && !flatList.isEmpty()) {			
			ReportBean firstReportBeanFromFlatList = flatList.get(0);
			//if after creating the hierarchy, sorting and creating the flat list, the first item from the resulting list 
			//is not the same with the previousGroupLimitWorkItemID then the corresponding key from groupingLimits map should be actualized
			//but the value should remain the same. This can be the case when by sorting before calculating the grouping limits
			//a child entry gets the first place in the group but the parent entry is also in this group. 
			//In this case after creating the hierarchy, sorting and creating the flat list the parent gets on the first place 
			//but the group separator would remain at the child workItem leading to incorrect rendering
			Integer newGroupLimitWorkItemID = firstReportBeanFromFlatList.getWorkItemBean().getObjectID();			
			if (!previousGroupLimitWorkItemID.equals(newGroupLimitWorkItemID) && groupingLimits != null) {
				List<GroupLimitBean> groupingLimitList = groupingLimits.get(previousGroupLimitWorkItemID);
				Iterator<GroupLimitBean> iterator = groupingLimitList.iterator();
				while (iterator.hasNext()) {
					GroupLimitBean groupLimitBean = iterator.next();
					groupLimitBean.setWorkItemID(newGroupLimitWorkItemID);
				}
				groupingLimits.remove(previousGroupLimitWorkItemID);
				groupingLimits.put(newGroupLimitWorkItemID, groupingLimitList);						
			}
		}
		return flatList;
	}
	
	/**
	 * Create a new map containing only the ReportBeans which are contained in the reportBeansList
	 * Important in creating the hierarchical list (hasParentInList())
	 * @param reportBeansList
	 * @param reportBeansMap
	 * @return
	 */
	private static Map<Integer, ReportBean> filterMap(
			List<ReportBean> reportBeansList, Map<Integer, ReportBean> reportBeansMap) {
		Map<Integer, ReportBean> filteredMap = new HashMap<Integer, ReportBean>();
		for (ReportBean reportBean : reportBeansList) {
			Integer workItemID = reportBean.getWorkItemBean().getObjectID();
			filteredMap.put(workItemID, reportBeansMap.get(workItemID));
		}
		return filteredMap;
	}
	

	/**
	 * @return Returns the reportHash.
	 */
	public Map<Integer, ReportBean> getReportBeansMap() {
		return reportBeansMap;
	}
	
	
	public void setReportBeansMap(Map<Integer, ReportBean> reportBeansMap) {
		this.reportBeansMap = reportBeansMap;
	}

	public Map<Integer, List<GroupLimitBean>> getGroupingLimits() {
		return groupingLimits;
	}	

	public List<ReportBean> getReportBeansFlat() {
		return reportBeansFlat;
	}

	public void setReportBeansFlat(List<ReportBean> reportBeansFlat) {
		this.reportBeansFlat = reportBeansFlat;
	}

	public boolean isSortNeeded() {
		return sortNeeded;
	}

	public void setSortNeeded(boolean sortNeeded) {
		this.sortNeeded = sortNeeded;
	}
	
	public List<TWorkItemBean> getWorkItems(){
		List<ReportBean> reportBeans=getItems();
		List<TWorkItemBean> result=new LinkedList<TWorkItemBean>();
		if(reportBeans!=null){
			for(int  i=0;i<reportBeans.size();i++){
				result.add(reportBeans.get(i).getWorkItemBean());
			}
		}
		return result;
	}

	public boolean isPlainData() {
		return plainData;
	}
}
