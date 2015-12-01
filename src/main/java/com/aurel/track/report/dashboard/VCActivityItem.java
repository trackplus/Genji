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

package com.aurel.track.report.dashboard;

import java.util.Date;
import java.util.List;

import com.aurel.track.item.FileDiffTO;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.vc.Revision;

public class VCActivityItem implements Comparable<VCActivityItem>{
	private String revisionNo;
	private String revisionDate;
	private Date date;
	private String revisionAuthor;
	private String revisionComment;
	private String repository;
	List<FileDiffTO> changedPaths;
	public VCActivityItem(){
	}
	public VCActivityItem(Revision r){
		this.revisionNo=r.getRevisionNo();
		this.revisionDate=r.getRevisionDate();
		this.date=DateTimeUtils.getInstance().parseISODateTime(r.getRevisionDate());
		this.revisionAuthor=r.getRevisionAuthor();
		this.revisionComment=r.getRevisionComment();
		this.repository=r.getRepository();
	}

	@Override
	public int compareTo(VCActivityItem vcCactivityItem) {
		return vcCactivityItem.date.compareTo(this.date);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof VCActivityItem)) {
			return false;
		}
		if (compareTo((VCActivityItem)obj) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((date == null) ? 0 : date.hashCode());
		return result;
	}

	public String getRevisionNo() {
		return revisionNo;
	}
	public void setRevisionNo(String revisionNo) {
		this.revisionNo = revisionNo;
	}
	public String getRevisionDate() {
		return revisionDate;
	}
	public void setRevisionDate(String revisionDate) {
		this.revisionDate = revisionDate;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getRevisionAuthor() {
		return revisionAuthor;
	}
	public void setRevisionAuthor(String revisionAuthor) {
		this.revisionAuthor = revisionAuthor;
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
}
