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

package com.aurel.track.admin.customize.lists;


/**
 * Transfer object for a list option tree node
 * @author Tamas
 *
 */
public class ListOptionTreeNodeTO {
	private String label = null;
	private String id = null;
	//the list ID of the custom cascading list's main listID:
	//drag and drop of optionIDs is possible only within the same cascading list (but not outside of a cascading list)
	//in this case  the list options within a composite select can be reorganized 
	private Integer topParentListID;	
	//whether this node can be edited by the current user
	private boolean canEdit=false;
	//whether any child of this node might be edited
	private boolean mightEditChild=false;
	//whether this node can be deleted by the current user
	private boolean canDelete=false;
	//whether any child of this node might be deleted
	private boolean mightDeleteChild=false;
	
	//whether a new child can be added to this node by the current user
	private boolean canAddChild=false;
	//whether this node can be copied: only custom lists can be copied 
	private boolean canCopy=false;
	//whether any child of this node can be copied: only custom lists can be copied
	private boolean mightCopyChild=false;
	
	//CRUD label list for the node (edit/delete the node)  
	private Integer nodeListForLabel;
	//CRUD label list for the node's children (add child, edit/delete child, copy)
	private Integer nodeChildrenListForLabel;	
	private String icon = null;
	private String iconCls = null;
	private boolean leaf = false;
	//system or custom node (list or option)
	private Integer type;
	//the listID of this node's children 
	private Integer childListID;
	//the optionID of a list entry. For system lists and parent custom list this is null
	private Integer optionID;
	//whether this node has a typeflag
	private boolean hasTypeflag;
	//whether the list has a typeflag/statusflag (all system lists, but no custom lists or any options)
	private boolean childrenHaveTypeflag;
	//if childrenHaveTypeFlag, whether is should be disabled (for issue type it is disabled)
	private boolean disableTypeflag;
	//whether the node has an icon
	private boolean hasIcon;
	//whether the child list has an icon column
	private boolean childrenHaveIcon;
	//whether this node has background color
	private boolean hasBgrColor;
	//whether the children nodes can have background color 
	private boolean childrenHaveCssStyle;
	//whether the filtering by children is defined (only for issue types)
	private boolean childrenHaveIssueChildFilter;
	//whether the node has percent complete value (only for states)
	private boolean hasPercentComplete;
	//whether the children nodes have percent complete value (only for states)
	private boolean childrenHavePercentComplete;
	//whether this node has default value
	private boolean hasDefaultOption;
	//whether the children nodes can have default value
	private boolean childrenHaveDefaultOption;		
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public Integer getTopParentListID() {
		return topParentListID;
	}
	public void setTopParentListID(Integer topParentListID) {
		this.topParentListID = topParentListID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	public boolean isCanEdit() {
		return canEdit;
	}
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	public boolean isCanDelete() {
		return canDelete;
	}
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
	public boolean isCanAddChild() {
		return canAddChild;
	}
	public void setCanAddChild(boolean canAddChild) {
		this.canAddChild = canAddChild;
	}	
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}		
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getChildListID() {
		return childListID;
	}
	public void setChildListID(Integer childListID) {
		this.childListID = childListID;
	}
	public Integer getOptionID() {
		return optionID;
	}
	public void setOptionID(Integer optionID) {
		this.optionID = optionID;
	}
	
	public boolean isChildrenHaveTypeflag() {
		return childrenHaveTypeflag;
	}
	public void setChildrenHaveTypeflag(boolean childrenHaveTypeflag) {
		this.childrenHaveTypeflag = childrenHaveTypeflag;
	}
	public boolean isChildrenHaveIcon() {
		return childrenHaveIcon;
	}
	public void setChildrenHaveIcon(boolean childrenHaveIcon) {
		this.childrenHaveIcon = childrenHaveIcon;
	}
	public boolean isChildrenHaveCssStyle() {
		return childrenHaveCssStyle;
	}
	public void setChildrenHaveCssStyle(boolean childrenHaveCssStyle) {
		this.childrenHaveCssStyle = childrenHaveCssStyle;
	}	
	public boolean isChildrenHaveIssueChildFilter() {
		return childrenHaveIssueChildFilter;
	}
	public void setChildrenHaveIssueChildFilter(boolean childrenHaveIssueChildFilter) {
		this.childrenHaveIssueChildFilter = childrenHaveIssueChildFilter;
	}	
	public boolean isChildrenHavePercentComplete() {
		return childrenHavePercentComplete;
	}
	public void setChildrenHavePercentComplete(boolean childrenHavePercentComplete) {
		this.childrenHavePercentComplete = childrenHavePercentComplete;
	}	
	public boolean isDisableTypeflag() {
		return disableTypeflag;
	}
	public void setDisableTypeflag(boolean disableTypeflag) {
		this.disableTypeflag = disableTypeflag;
	}
	public boolean isCanCopy() {
		return canCopy;
	}
	public void setCanCopy(boolean canCopy) {
		this.canCopy = canCopy;
	}
	public boolean isMightEditChild() {
		return mightEditChild;
	}
	public void setMightEditChild(boolean mightEditChild) {
		this.mightEditChild = mightEditChild;
	}
	public boolean isMightDeleteChild() {
		return mightDeleteChild;
	}
	public void setMightDeleteChild(boolean mightDeleteChild) {
		this.mightDeleteChild = mightDeleteChild;
	}
	public Integer getNodeListForLabel() {
		return nodeListForLabel;
	}
	public void setNodeListForLabel(Integer nodeListForLabel) {
		this.nodeListForLabel = nodeListForLabel;
	}
	public Integer getNodeChildrenListForLabel() {
		return nodeChildrenListForLabel;
	}
	public void setNodeChildrenListForLabel(Integer nodeChildrenListForLabel) {
		this.nodeChildrenListForLabel = nodeChildrenListForLabel;
	}
	public boolean isHasTypeflag() {
		return hasTypeflag;
	}
	public void setHasTypeflag(boolean hasTypeflag) {
		this.hasTypeflag = hasTypeflag;
	}
	public boolean isHasBgrColor() {
		return hasBgrColor;
	}
	public void setHasBgrColor(boolean hasBgrColor) {
		this.hasBgrColor = hasBgrColor;
	}
	public boolean isHasPercentComplete() {
		return hasPercentComplete;
	}
	public void setHasPercentComplete(boolean hasPercentComplete) {
		this.hasPercentComplete = hasPercentComplete;
	}
	public boolean isHasDefaultOption() {
		return hasDefaultOption;
	}
	public void setHasDefaultOption(boolean hasDefaultOption) {
		this.hasDefaultOption = hasDefaultOption;
	}
	public boolean isChildrenHaveDefaultOption() {
		return childrenHaveDefaultOption;
	}
	public void setChildrenHaveDefaultOption(boolean childrenHaveDefaultOption) {
		this.childrenHaveDefaultOption = childrenHaveDefaultOption;
	}
	public boolean isMightCopyChild() {
		return mightCopyChild;
	}
	public void setMightCopyChild(boolean mightCopyChild) {
		this.mightCopyChild = mightCopyChild;
	}
	public boolean isHasIcon() {
		return hasIcon;
	}
	public void setHasIcon(boolean hasIcon) {
		this.hasIcon = hasIcon;
	}
	
}
