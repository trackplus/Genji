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

package com.aurel.track.admin.customize.category;

/**
 * Transfer object for a category or leaf grid row
 * @author Tamas
 *
 */
public class CategoryGridRowTO extends CategoryTO {	
	private String typeLabel;
	//meaningful only for filter leaf nodes
	private boolean includeInMenu;
	private String styleFieldLabel;
	private String viewName;
	//meaningful only for report leaf nodes
	private String reportIcon;
	
	public CategoryGridRowTO(String nodeID, String categoryType, String label,  
			String typeLabel, boolean modifiable, boolean leaf) {
		super(nodeID, categoryType, label, modifiable, leaf);
		this.typeLabel = typeLabel;
	}
		
	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

	public boolean isIncludeInMenu() {
		return includeInMenu;
	}

	public void setIncludeInMenu(boolean includeInMenu) {
		this.includeInMenu = includeInMenu;
	}

	public String getStyleFieldLabel() {
		return styleFieldLabel;
	}

	public void setStyleFieldLabel(String backgroundFieldLabel) {
		this.styleFieldLabel = backgroundFieldLabel;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getReportIcon() {
		return reportIcon;
	}

	public void setReportIcon(String reportIcon) {
		this.reportIcon = reportIcon;
	}
	
	
}
