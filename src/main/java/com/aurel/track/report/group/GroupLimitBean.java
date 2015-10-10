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


package com.aurel.track.report.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.aurel.track.report.execute.ReportBean;

public class GroupLimitBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//the fieldID
	private Integer groupField;
	//the first workItemID in the group
	private Integer workItemID;
	//the localized name: field label and actual value
	private String groupLabel;
	//the groups to be sorted at "grouping" level: either non tree fields or tree fields on first level (not in tree depth) 
	private Comparable<Object> groupSortOrder;
	//the value for the group
	private Object groupValue;
	//the level of the current grouping (0 to 3)
	private int groupLevel;
	//whether it is expanded or collapsed
	private boolean expanding;
	//total number of workItems in this (sub)group
	private int numberOfWorkItems;
	//gathers the ordinal numbers up to this group level as a string with separator between the numbers:
	//"global" ordinal number for the uppermost group and the ordinal numbers of the lower group inside the upper group
	private String ordinalNumberString;
	//either reportBeans or subgroup should be null
	//the direct ReportBean descendants of the last level group
	private List<ReportBean> reportBeans;
	//the direct subgroups
	private List<GroupLimitBean> subgroups;
	//the tree subgroups
	private List<GroupLimitBean> treeSubgroups;
	private boolean isTreeField;
	//the depth in tree: makes sense only if isTreeField is true
	private Integer depthInTree;
	//makes sense only if isTreeField is true. Used to sort in tree at a certain depth.
	//It is an "artificial" integer calculated based on the relative sort order of the tree field (which originally can be also a Comparable<String>)
	private Integer treeSortOrder = null;
	
	public static int FIRST_LEVEL_DEPTH = 0;
	//private GroupLimitBean parent; 
	
	public GroupLimitBean() {
		super();
	}	

	public GroupLimitBean(Integer groupField, String groupLabel, Object groupValue, Comparable<Object> groupSortOrder, int groupLevel, 
			boolean expanding, Integer workItemID, String ordinalNumberString, boolean isTreeField) {
		super();
		this.groupField = groupField;
		this.groupLabel = groupLabel;
		this.groupValue = groupValue;
		this.groupSortOrder = groupSortOrder;
		this.groupLevel = groupLevel;
		this.expanding = expanding;
		this.workItemID = workItemID;
		this.ordinalNumberString = ordinalNumberString;
		this.isTreeField = isTreeField;
		if (isTreeField) {
			this.depthInTree = Integer.valueOf(FIRST_LEVEL_DEPTH);
		}
	}

	public boolean isExpanding() {
		return expanding;
	}
	
	public void setExpanding(boolean expanding) {
		this.expanding = expanding;
	}

	public Integer getGroupField() {
		return groupField;
	}
	
	public String getGroupLabel() {
		return groupLabel;
	}
	
	public Comparable<Object> getGroupSortOrder() {
		return groupSortOrder;
	}

	public int getGroupLevel() {
		return groupLevel;
	}
	
	/**
	 * @return the numberOfWorkItems
	 */
	public int getNumberOfWorkItems() {
		return numberOfWorkItems;
	}
	/**
	 * @param numberOfWorkItems the numberOfWorkItems to set
	 */
	public void setNumberOfWorkItems(int numberOfWorkItems) {
		this.numberOfWorkItems = numberOfWorkItems;
	}	
	/**
	 * @return the ordinalNumberString
	 */
	public String getOrdinalNumberString() {
		return ordinalNumberString;
	}
	/**
	 * @param ordinalNumberString the ordinalNumberString to set
	 */
	public void setOrdinalNumberString(String ordinalNumberString) {
		this.ordinalNumberString = ordinalNumberString;
	}

	/**
	 * Add a new child limit bean to parent
	 * @param parent
	 */
	public void setParent(GroupLimitBean parent) {
		if (parent!=null) {
			//this.parent = parent;		
			List<GroupLimitBean> subgroups = parent.getSubgroups();
			if (subgroups==null) {
				subgroups = new ArrayList<GroupLimitBean>();
				parent.setSubgroups(subgroups);
			}
			subgroups.add(this);
		}
	}

	/**
	 * Add a new tree child limit bean to parent
	 * @param treeParent
	 */
	public void setTreeParent(GroupLimitBean treeParent) {
		if (treeParent!=null) {
			//this.parent = parent;		
			List<GroupLimitBean> treeSubgroups = treeParent.getTreeSubgroups();
			if (treeSubgroups==null) {
				treeSubgroups = new ArrayList<GroupLimitBean>();
				treeParent.setTreeSubgroups(treeSubgroups);
			}
			this.setDepthInTree(treeParent.getDepthInTree()+1);
			treeSubgroups.add(this);
		}
	}
	
	public Integer getWorkItemID() {
		return workItemID;
	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}

	public List<ReportBean> getReportBeans() {
		return reportBeans;
	}

	public void setReportBeans(List<ReportBean> reportBeans) {
		this.reportBeans = reportBeans;
	}

	public List<GroupLimitBean> getSubgroups() {
		return subgroups;
	}

	public void setSubgroups(List<GroupLimitBean> subgroups) {
		this.subgroups = subgroups;
	}

	public List<GroupLimitBean> getTreeSubgroups() {
		return treeSubgroups;
	}

	public void setTreeSubgroups(List<GroupLimitBean> treeSubgroups) {
		this.treeSubgroups = treeSubgroups;
	}

	public boolean isTreeField() {
		return isTreeField;
	}

	public void setTreeField(boolean isTreeField) {
		this.isTreeField = isTreeField;
	}

	public Integer getDepthInTree() {
		return depthInTree;
	}

	public void setDepthInTree(Integer depthInTree) {
		this.depthInTree = depthInTree;
	}


	public Integer getTreeSortOrder() {
		return treeSortOrder;
	}

	public void setTreeSortOrder(Integer treeSortOrder) {
		this.treeSortOrder = treeSortOrder;
	}

	public Object getGroupValue() {
		return groupValue;
	}	
	
	
}
