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

package com.aurel.track.item.history;

import java.util.Date;
import java.util.List;

import com.aurel.track.item.FileDiffTO;

/**
 * Contains the data for a transaction
 */
public class FlatHistoryBean implements IChronologicalField, Comparable<FlatHistoryBean> {
	public interface RENDER_TYPE {
		static int HISTORY_VALUES = 1; //both new and old values
		static int ACTUAL_VALUES = 2; //only actual values (new values)
		static int CHILD_DETAIL = 3; //the detail data for a child
		static int ATTACHMENT_DETAIL = 4; //the detail data for a child
		static int VERSION_CONTROL = 5; //the detail data for a child
	}
	//workItemID, title and renderType used only when the history of children is also shown
	//and when more workItems are implied (activity stream)
	private Integer workItemID;
	private String itemID;//either workItemID or the project specific one
    private String title;
    private Integer renderType;

	private List<HistoryEntry> historyEntries;
	private List<HistoryEntry> historyLongEntries;
	private Date lastEdit;
	private String dateFormatted;
	private String changedByName;
	private Integer personID;
	private int type;
	private String iconName;

	private Integer projectID;
	private String project;
	private String revisionNo;
	private String revisionComment;
	private String repository;
	private List<FileDiffTO> changedPaths;



	public List<HistoryEntry> getHistoryEntries() {
		return historyEntries;
	}
	public void setHistoryEntries(List<HistoryEntry> historyEntries) {
		this.historyEntries = historyEntries;
	}
	@Override
	public Date getLastEdit() {
		return lastEdit;
	}
	public void setLastEdit(Date lastEdit) {
		this.lastEdit = lastEdit;
	}
	public String getChangedByName() {
		return changedByName;
	}
	public void setChangedByName(String changedByName) {
		this.changedByName = changedByName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public List<HistoryEntry> getHistoryLongEntries() {
		return historyLongEntries;
	}
	public void setHistoryLongEntries(List<HistoryEntry> historyLongEntries) {
		this.historyLongEntries = historyLongEntries;
	}
	public Integer getWorkItemID() {
		return workItemID;
	}
	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getRenderType() {
		return renderType;
	}
	public void setRenderType(Integer renderType) {
		this.renderType = renderType;
	}

	public int compareTo(FlatHistoryBean object) {
		int sortDir = -1;   //  1 sorts in ascending order,
        //  -1 sorts in descending order (newest date comes first)
		FlatHistoryBean flatHistoryBean = object;
		Date left =  getLastEdit();
		Date right = flatHistoryBean.getLastEdit();
		if(left==null && right==null) {
			Integer leftRenderType = getRenderType();
			Integer rightRenderType = flatHistoryBean.getRenderType();
			return leftRenderType.compareTo(rightRenderType);
		}

		if(left == null) {
			return sortDir * -1;
		}

		if(right == null) {
			return sortDir * 1;
		}

		int result = sortDir * left.compareTo(right);
		if (result==0) {
			Integer leftRenderType = getRenderType();
			Integer rightRenderType = flatHistoryBean.getRenderType();
			return sortDir * leftRenderType.compareTo(rightRenderType);
		} else {
			return result;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof FlatHistoryBean)) {
			return false;
		}
		if (compareTo((FlatHistoryBean)obj) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getRenderType() == null) ? 0 : getRenderType().hashCode());
		return result;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getDateFormatted() {
		return dateFormatted;
	}

	public void setDateFormatted(String dateFormatted) {
		this.dateFormatted = dateFormatted;
	}

	public String getRevisionNo() {
		return revisionNo;
	}

	public void setRevisionNo(String revisionNo) {
		this.revisionNo = revisionNo;
	}

	public String getRevisionComment() {
		return revisionComment;
	}

	public void setRevisionComment(String revisionComment) {
		this.revisionComment = revisionComment;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public List<FileDiffTO> getChangedPaths() {
		return changedPaths;
	}

	public void setChangedPaths(List<FileDiffTO> changedPaths) {
		this.changedPaths = changedPaths;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}
}
