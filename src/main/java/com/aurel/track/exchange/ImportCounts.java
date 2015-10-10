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


package com.aurel.track.exchange;

public class ImportCounts {
	private int noOfCreatedIssues = 0;
	private int noOfUpdatedIssues = 0;
	private int noOfDeletedIssues = 0;
	private int noOfPlannedWorks = 0;
	private int noOfCreatedLinks = 0;
	private int noOfUpdatedLinks = 0;
	private int noOfDeletedLinks = 0;
	
	public int getNoOfCreatedIssues() {
		return noOfCreatedIssues;
	}
	public void setNoOfCreatedIssues(int noOfCreatedIssues) {
		this.noOfCreatedIssues = noOfCreatedIssues;
	}
	public int getNoOfUpdatedIssues() {
		return noOfUpdatedIssues;
	}
	public void setNoOfUpdatedIssues(int noOfUpdatedIssues) {
		this.noOfUpdatedIssues = noOfUpdatedIssues;
	}
	public int getNoOfDeletedIssues() {
		return noOfDeletedIssues;
	}
	public void setNoOfDeletedIssues(int noOfDeletedIssues) {
		this.noOfDeletedIssues = noOfDeletedIssues;
	}
	public int getNoOfPlannedWorks() {
		return noOfPlannedWorks;
	}
	public void setNoOfPlannedWorks(int noOfPlannedWorks) {
		this.noOfPlannedWorks = noOfPlannedWorks;
	}
	public int getNoOfCreatedLinks() {
		return noOfCreatedLinks;
	}
	public void setNoOfCreatedLinks(int noOfCreatedLinks) {
		this.noOfCreatedLinks = noOfCreatedLinks;
	}
	public int getNoOfUpdatedLinks() {
		return noOfUpdatedLinks;
	}
	public void setNoOfUpdatedLinks(int noOfUpdatedLinks) {
		this.noOfUpdatedLinks = noOfUpdatedLinks;
	}
	public int getNoOfDeletedLinks() {
		return noOfDeletedLinks;
	}
	public void setNoOfDeletedLinks(int noOfDeletedLinks) {
		this.noOfDeletedLinks = noOfDeletedLinks;
	}
	
	
}
