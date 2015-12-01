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

package com.aurel.track.admin.customize.lists;

/**
 * Transfer object for a grid row
 * @author Tamas
 *
 */
public class ListGridRowTO extends ListBaseTO {
	private boolean canCopy=false;
	private String customListType;
	private String cascadingType;
	//whether the filtering by children is defined (only for issue types)
	
	private String iconCls;
	
	public String getCustomListType() {
		return customListType;
	}
	public void setCustomListType(String customListType) {
		this.customListType = customListType;
	}
	public String getCascadingType() {
		return cascadingType;
	}
	public void setCascadingType(String cascadingType) {
		this.cascadingType = cascadingType;
	}
	public boolean isCanCopy() {
		return canCopy;
	}
	public void setCanCopy(boolean canCopy) {
		this.canCopy = canCopy;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	
}
