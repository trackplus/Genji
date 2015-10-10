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

package com.aurel.track.itemNavigator.filterInMenu;

import java.io.Serializable;

/**
 * Structure for the filter hierarchy present in the user menu
 * @author Tamas Ruff
 *
 */
public class FilterInMenuTO implements Serializable {
	
	private static final long serialVersionUID = 400L;
	
	private Integer objectID;
	private String label;
	private String tooltip;
	private Integer type;
	private Integer lastExecutedQueryID;
	private String iconCls;
	private String icon;
	private String viewID;
	private boolean maySaveFilterLayout;

	public Integer getObjectID() {
		return objectID;
	}
	public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getLastExecutedQueryID() {
		return lastExecutedQueryID;
	}
	public void setLastExecutedQueryID(Integer lastExecutedQueryID) {
		this.lastExecutedQueryID = lastExecutedQueryID;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getViewID() {
		return viewID;
	}
	public void setViewID(String viewID) {
		this.viewID = viewID;
	}
	public boolean isMaySaveFilterLayout() {
		return maySaveFilterLayout;
	}
	public void setMaySaveFilterLayout(boolean maySaveFilterLayout) {
		this.maySaveFilterLayout = maySaveFilterLayout;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
	
}
