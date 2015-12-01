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

package com.aurel.track.vc;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.aurel.track.plugin.VersionControlDescriptor;

/**
 * @author  Adrian Bojani
 */
public class VersionControlTO implements Serializable{

	public static final long serialVersionUID = 400L;
	
	private boolean useVersionControl;
	private String versionControlType;
	private List<VersionControlDescriptor.BrowserDescriptor> browsers;
	private String browserID="-1";
	private String baseURL;
	private String changesetLink;
	private String addedLink;
	private String modifiedLink;
	private String replacedLink;
	private String deletedLink;
	private Map<String,String> parameters;
	private boolean missing;
	
	@Override
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append("{\n");
		sb.append("versionControlType:").append(getVersionControlType()).append(",\n");
		sb.append("browserID:").append(getBrowserID()).append(",\n");;
		sb.append("baseURL:").append(getBaseURL()).append(",\n");;
		sb.append("changesetLink:").append(getChangesetLink()).append(",\n");;
		sb.append("addedLink:").append(getAddedLink()).append(",\n");;
		sb.append("modifiedLink:").append(getModifiedLink()).append(",\n");;
		sb.append("replacedLink:").append(getReplacedLink()).append(",\n");;
		sb.append("deletedLink:").append(getDeletedLink());
		sb.append("\n}");
		return sb.toString();
	}
	public boolean isUseVersionControl() {
		return useVersionControl;
	}

	public void setUseVersionControl(boolean useVersionControl) {
		this.useVersionControl = useVersionControl;
	}

	public String getVersionControlType() {
		return versionControlType;
	}

	public void setVersionControlType(String versionControlType) {
		this.versionControlType = versionControlType;
	}

	public List<VersionControlDescriptor.BrowserDescriptor> getBrowsers() {
		return browsers;
	}

	public void setBrowsers(List<VersionControlDescriptor.BrowserDescriptor> browsers) {
		this.browsers = browsers;
	}

	public String getBrowserID() {
		return browserID;
	}

	public void setBrowserID(String browserID) {
		this.browserID = browserID;
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

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public boolean isMissing() {
		return missing;
	}

	public void setMissing(boolean missing) {
		this.missing = missing;
	}
}
