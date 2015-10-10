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

package com.aurel.track.item.link;

import java.util.Date;

import com.aurel.track.util.SortOrderUtil;

/**
 * Represents an entry from an item links list
 * @author Tamas
 *
 */
public class ItemLinkListEntry extends ItemLinkTO implements Comparable<ItemLinkListEntry> {
	private Integer linkID;
	private String linkTypeName;
	private String stateLabel;
	private String responsibleLabel;
	private Date lastEdit;
	private String parameters;
	private boolean editable;
	private Integer sortOrder;

	public String getLinkTypeName() {
		return linkTypeName;
	}
	public void setLinkTypeName(String linkTypeName) {
		this.linkTypeName = linkTypeName;
	}
	public String getStateLabel() {
		return stateLabel;
	}
	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}
	public String getResponsibleLabel() {
		return responsibleLabel;
	}
	public void setResponsibleLabel(String responsibleLabel) {
		this.responsibleLabel = responsibleLabel;
	}
	public Integer getLinkID() {
		return linkID;
	}
	public void setLinkID(Integer linkID) {
		this.linkID = linkID;
	}
	public Date getLastEdit() {
		return lastEdit;
	}
	public void setLastEdit(Date lastEdit) {
		this.lastEdit = lastEdit;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public int compareTo(ItemLinkListEntry itemLinkListEntry) {
		if (itemLinkListEntry==null) {
			return 1;
		}
		int compareResult;
		compareResult = SortOrderUtil.compareValue(this.getSortOrder(), itemLinkListEntry.getSortOrder());
		if (compareResult!=0) {
			return compareResult;
		}
		return SortOrderUtil.compareValue(this.getLinkID(), itemLinkListEntry.getLinkID());
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof ItemLinkListEntry)) {
			return false;
		}
		if(compareTo((ItemLinkListEntry)obj) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getSortOrder() == null) ? 0 : getSortOrder().hashCode());
		return result;
	}




}
