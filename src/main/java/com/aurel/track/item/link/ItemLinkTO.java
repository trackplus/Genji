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

/**
 * Transfer object for editing an item link
 * @author Tamas
 *
 */
public class ItemLinkTO {
	//private String linkTypeWithDirection;
	private Integer linkType;
	private Integer linkDirection;
	private Integer linkedWorkItemID;
	private String linkedWorkItemTitle;
	private String description;
	
	/*public String getLinkTypeWithDirection() {
		return linkTypeWithDirection;
	}
	public void setLinkTypeWithDirection(String linkTypeWithDirection) {
		this.linkTypeWithDirection = linkTypeWithDirection;
	}*/
	public Integer getLinkedWorkItemID() {
		return linkedWorkItemID;
	}
	public void setLinkedWorkItemID(Integer linkedWorkItemID) {
		this.linkedWorkItemID = linkedWorkItemID;
	}
	public String getLinkedWorkItemTitle() {
		return linkedWorkItemTitle;
	}
	public void setLinkedWorkItemTitle(String linkedWorkItemTitle) {
		this.linkedWorkItemTitle = linkedWorkItemTitle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getLinkType() {
		return linkType;
	}
	public void setLinkType(Integer linkType) {
		this.linkType = linkType;
	}
	public Integer getLinkDirection() {
		return linkDirection;
	}
	public void setLinkDirection(Integer linkDirection) {
		this.linkDirection = linkDirection;
	}
	
}
