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

package com.aurel.track.item;

import java.util.List;

/**
 */
public class RevisionTO {
	private String revisionNo;
	private String revisionURL;
	private String revisionDate;
	private String revisionAuthor;
	private String revisionComment;

	private String repository;

	List<FileDiffTO> changedPaths;

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

	public String getRevisionURL() {
		return revisionURL;
	}

	public void setRevisionURL(String revisionURL) {
		this.revisionURL = revisionURL;
	}
}
