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

package com.aurel.track.vc;

import com.aurel.track.plugin.VersionControlDescriptor.BrowserDescriptor;

public class RepositoryFileViewer {
	private String repository;
	private String baseURL;
	private BrowserDescriptor browser;
	
	private String changesetLink;
	private String addedLink;
	private String modifiedLink;
	private String replacedLink;
	private String deletedLink;
	
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public BrowserDescriptor getBrowser() {
		return browser;
	}
	public void setBrowser(BrowserDescriptor browser) {
		this.browser = browser;
	}
	public String getBaseURL() {
		return baseURL;
	}
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	public String getChangesetLink() {
		return changesetLink;
	}
	public void setChangesetLink(String changesetLink) {
		this.changesetLink = changesetLink;
	}
	public String getAddedLink() {
		return addedLink;
	}
	public void setAddedLink(String addedLink) {
		this.addedLink = addedLink;
	}
	public String getModifiedLink() {
		return modifiedLink;
	}
	public void setModifiedLink(String modifiedLink) {
		this.modifiedLink = modifiedLink;
	}
	public String getReplacedLink() {
		return replacedLink;
	}
	public void setReplacedLink(String replacedLink) {
		this.replacedLink = replacedLink;
	}
	public String getDeletedLink() {
		return deletedLink;
	}
	public void setDeletedLink(String deletedLink) {
		this.deletedLink = deletedLink;
	}
}
