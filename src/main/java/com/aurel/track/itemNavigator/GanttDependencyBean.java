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

package com.aurel.track.itemNavigator;

public class GanttDependencyBean {
	private String description;
	private String linkTypeWithDirection;
	private Integer linkType;
	private Integer linkLag;
	private Integer linkLagUnit;
	private Integer sourceID;
	private Integer targetID;

	public GanttDependencyBean() {
	}

	public GanttDependencyBean(String description,
			String linkTypeWithDirection, Integer linkType, Integer linkLag,
			Integer linkLagUnit, Integer sourceID, Integer targetID) {
		super();
		this.description = description;
		this.linkTypeWithDirection = linkTypeWithDirection;
		this.linkType = linkType;
		this.linkLag = linkLag;
		this.linkLagUnit = linkLagUnit;
		this.sourceID = sourceID;
		this.targetID = targetID;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLinkTypeWithDirection() {
		return linkTypeWithDirection;
	}
	public void setLinkTypeWithDirection(String linkTypeWithDirection) {
		this.linkTypeWithDirection = linkTypeWithDirection;
	}
	public Integer getLinkType() {
		return linkType;
	}
	public void setLinkType(Integer linkType) {
		this.linkType = linkType;
	}
	public Integer getLinkLag() {
		return linkLag;
	}
	public void setLinkLag(Integer linkLag) {
		this.linkLag = linkLag;
	}
	public Integer getSourceID() {
		return sourceID;
	}
	public void setSourceID(Integer sourceID) {
		this.sourceID = sourceID;
	}
	public Integer getTargetID() {
		return targetID;
	}
	public void setTargetID(Integer targetID) {
		this.targetID = targetID;
	}
	public Integer getLinkLagUnit() {
		return linkLagUnit;
	}
	public void setLinkLagUnit(Integer linkLagUnit) {
		this.linkLagUnit = linkLagUnit;
	}

}
