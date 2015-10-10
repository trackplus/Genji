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

package com.aurel.track.admin.customize.lists;

/**
 * Transfer object for a grid row
 * @author Tamas
 *
 */
public class ListOptionGridRowTO extends ListBaseTO {
	private boolean hasTypeflag;
	private boolean disableTypeflag;
	private String typeflagLabel;
	private boolean hasCssStyle;
	private String cssStyle;
	//whether has icon and icon is modifiable
	//a system list might also have an icon but it is hardcoded
	private boolean hasIcon;
	private String iconName = null;
	private boolean hasChildFilter = false;
	private boolean hasPercentComplete;
	private Integer percentComplete;
	private boolean hasDefaultOption;
	private boolean defaultOption;
	private boolean leaf;
	
	public String getTypeflagLabel() {
		return typeflagLabel;
	}
	public void setTypeflagLabel(String typeflagLabel) {
		this.typeflagLabel = typeflagLabel;
	}
	public boolean isHasTypeflag() {
		return hasTypeflag;
	}
	public void setHasTypeflag(boolean hasTypeflag) {
		this.hasTypeflag = hasTypeflag;
	}
		
	public boolean isHasCssStyle() {
		return hasCssStyle;
	}
	public void setHasCssStyle(boolean hasCssStyle) {
		this.hasCssStyle = hasCssStyle;
	}
	
	public String getCssStyle() {
		return cssStyle;
	}
	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}
	public boolean isHasIcon() {
		return hasIcon;
	}
	public void setHasIcon(boolean hasIcon) {
		this.hasIcon = hasIcon;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public boolean isHasPercentComplete() {
		return hasPercentComplete;
	}
	public void setHasPercentComplete(boolean hasPercentComplete) {
		this.hasPercentComplete = hasPercentComplete;
	}
	public Integer getPercentComplete() {
		return percentComplete;
	}
	public void setPercentComplete(Integer percentComplete) {
		this.percentComplete = percentComplete;
	}
	public boolean isDisableTypeflag() {
		return disableTypeflag;
	}
	public void setDisableTypeflag(boolean disableTypeflag) {
		this.disableTypeflag = disableTypeflag;
	}
	public boolean isHasDefaultOption() {
		return hasDefaultOption;
	}
	public void setHasDefaultOption(boolean hasDefaultOption) {
		this.hasDefaultOption = hasDefaultOption;
	}
	public boolean isDefaultOption() {
		return defaultOption;
	}
	public void setDefaultOption(boolean defaultOption) {
		this.defaultOption = defaultOption;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public boolean isHasChildFilter() {
		return hasChildFilter;
	}
	public void setHasChildFilter(boolean hasChildFilter) {
		this.hasChildFilter = hasChildFilter;
	}
	
	
}
