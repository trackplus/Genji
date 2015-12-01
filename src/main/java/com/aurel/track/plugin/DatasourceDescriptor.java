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

package com.aurel.track.plugin;

/**
 * descriptor for datasource plugin
 */
public class DatasourceDescriptor extends BasePluginDescriptor {
	
	private static final long serialVersionUID = 1L;
	private String listing;
	private String previewImg;
	private Integer personID;
	private Integer projectID;	
	
	public Integer getPersonID() {
		return personID;
	}
	public void setPersonID(Integer personID) {
		this.personID = personID;
	}
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public String getListing() {
		return listing;
	}
	public void setListing(String listing) {
		this.listing = listing;
	}
	public String getPreviewImg() {
		return previewImg;
	}
	public void setPreviewImg(String previewImg) {
		this.previewImg = previewImg;
	}
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		if (id!=null) {
			stringBuffer.append("id = " + id);
		}
		if (name!=null) {
			stringBuffer.append(" name = " + name);
		}
		if (description!=null) {
			stringBuffer.append(" description = " + description);
		}
		if (listing!=null) {
			stringBuffer.append(" listing = " + listing);
		}
		if (previewImg!=null) {
			stringBuffer.append(" previewImg = " + previewImg);
		}
		if (jsClass!=null) {
			stringBuffer.append(" jsClass = " + jsClass);
		}
		if (jsConfigClass !=null) {
			stringBuffer.append(" jsConfigClass = " + jsConfigClass);
		}
		if (bundleName!=null) {
			stringBuffer.append(" bundleName = " + bundleName);
		}		
		if (theClassName!=null) {
			stringBuffer.append(" theClassName = " + theClassName);
		}		
		if (personID!=null) {
			stringBuffer.append(" personID = " + personID);
		}
		if (projectID!=null) {
			stringBuffer.append(" projectID = " + projectID);
		}
		return stringBuffer.toString();
	}
}
