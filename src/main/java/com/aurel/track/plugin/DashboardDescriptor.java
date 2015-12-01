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
 * descriptor for DashboardPlugin
 */
public class DashboardDescriptor extends BasePluginDescriptor {

	private static final long serialVersionUID = 340L;
    //private String name;
    private String thumbnail;
    private String thumbnailProject;
    private String thumbnailRelease;
    private String label;
    
    private String pageDescription;    
    private String tooltip;


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getPageDescription() {
        return pageDescription;
    }

    public void setPageDescription(String pageDescription) {
        this.pageDescription = pageDescription;
    }

    @Override
	public String toString() {
        return "DasboardDescriptor:["+getId()+":"+getName()+":"+getJsClass()+"]";
    }



	public String getThumbnailProject() {
		return thumbnailProject;
	}

	public void setThumbnailProject(String thumbnailProject) {
		this.thumbnailProject = thumbnailProject;
	}

	public String getThumbnailRelease() {
		return thumbnailRelease;
	}

	public void setThumbnailRelease(String thumbnailRelease) {
		this.thumbnailRelease = thumbnailRelease;
	}
}
