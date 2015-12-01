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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.linkType.ItemLinkSpecificData;

public class ReportBeanLink implements Comparable<ReportBeanLink>, Serializable {
		
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LogManager.getLogger(ReportBeanLink.class);
	private Integer objectID;
	private Integer workItemID;
	private String linkedItemTitle;
	private boolean linkedItemIncluded;
	private String projectSpecificIssueNo;
	private Integer linkTypeID;
	private Integer linkDirection;
	private String linkTypeName;
	//further link details specific for MSProject
	private transient ItemLinkSpecificData linkSpecificData;
	
	public ReportBeanLink() {
		super();
	}
		
	public ReportBeanLink(Integer objectID, Integer workItemID, String linkedItemTitle, boolean linkedItemIncluded, Integer linkTypeID, Integer linkDirection, String linkTypeName) {
		super();
		this.objectID = objectID;
		this.workItemID = workItemID;
		this.linkedItemTitle = linkedItemTitle;
		this.linkedItemIncluded = linkedItemIncluded;
		this.linkTypeID = linkTypeID;
		this.linkDirection = linkDirection;
		this.linkTypeName = linkTypeName;
	}

	public Integer getWorkItemID() {
		return workItemID;
	}
	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
	
	public String getLinkedItemTitle() {
		return linkedItemTitle;
	}

	public void setLinkedItemTitle(String linkedItemTitle) {
		this.linkedItemTitle = linkedItemTitle;
	}

	public boolean isLinkedItemIncluded() {
		return linkedItemIncluded;
	}

	public void setLinkedItemIncluded(boolean linkedItemIncluded) {
		this.linkedItemIncluded = linkedItemIncluded;
	}

	public Integer getLinkTypeID() {
		return linkTypeID;
	}
	public void setLinkTypeID(Integer linkTypeID) {
		this.linkTypeID = linkTypeID;
	}
	public Integer getLinkDirection() {
		return linkDirection;
	}
	public void setLinkDirection(Integer linkDirection) {
		this.linkDirection = linkDirection;
	}
	
	public String getLinkTypeName() {
		return linkTypeName;
	}

	public void setLinkTypeName(String linkTypeName) {
		this.linkTypeName = linkTypeName;
	}

	public String getProjectSpecificIssueNo() {
		return projectSpecificIssueNo;
	}

	public void setProjectSpecificIssueNo(String projectSpecificIssueNo) {
		this.projectSpecificIssueNo = projectSpecificIssueNo;
	}

	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public ItemLinkSpecificData getLinkSpecificData() {
		return linkSpecificData;
	}

	public void setLinkSpecificData(ItemLinkSpecificData linkSpecificData) {
		this.linkSpecificData = linkSpecificData;
	}

	@Override
	public boolean equals(Object reportBeanLink) {
		if (reportBeanLink==null) {
			return false;
		}
		int compareResult = compareValue(this.getLinkTypeID(), ((ReportBeanLink)reportBeanLink).getLinkTypeID());
		if (compareResult == 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.objectID;
	}

	@Override
	public int compareTo(ReportBeanLink reportBeanLink) {
		if (reportBeanLink==null) {
			return 1;
		}
		int compareResult;
		compareResult = compareValue(this.getLinkTypeID(), reportBeanLink.getLinkTypeID());
		if (compareResult!=0) {
			return compareResult;
		}
		compareResult = compareValue(this.getLinkDirection(), reportBeanLink.getLinkDirection()); 
		if (compareResult!=0) {
			return compareResult;
		}
		return compareValue(this.getWorkItemID(), reportBeanLink.getWorkItemID());
	}

	/**
	 * Compare two "atomic" values
	 * @param value0
	 * @param value1
	 * @return
	 */
	private int compareValue(Integer value0, Integer value1) {
		if ((value0==null) && (value1==null)) {
			return 0;
		}
		if (value0==null) {
			return -1;
		}
		if (value1==null) {
			return 1;
		}
		try {
			return value0.compareTo(value1);
		} catch (Exception e) {
			LOGGER.warn("Sorting the values " + value0 + " of class " + value0.getClass().getName() + 
					" and " + value1 + " of class " + value1.getClass().getName() + " failed with " + e.getMessage(), e);
			return 0; // can't sort
		}
	}
}
