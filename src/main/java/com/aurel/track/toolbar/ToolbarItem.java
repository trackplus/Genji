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


package com.aurel.track.toolbar;


/**
 * Bean for toolbar item
 * @author Tamas Ruff
 *
 */
public class ToolbarItem {

	public static final int SIBLING=1;
	public static final int CHOOSE_PARENT=2;
	public static final int PRINT=3;
	public static final int PRINT_WITH_CHILDREN=4;
	public static final int ACCESS_LEVEL=5;
	public static final int ARCHIVE=6;
	public static final int DELETE =7;
	public static final int MAIL=8;
	public static final int BACK=9;
	public static final int NAVIGATION_NEXT=10;
	public static final int NAVIGATION_PREV=11;

	public static final int ITEM_ACTION=12;

	public static final int SAVE=13;
	public static final int RESET=14;
	public static final int CANCEL=15;
	public static final int PRINT_ITEM=16;
	public static final int EXPORT=17;


	private int id;

	private String jsonData;

	private boolean condition;

	// whether the action needs also a submit or is just an url without submit
	//private boolean submit;


	// the css class of the active toolbar item
	private String cssClass;

	// the resource key name for the tooltip
	private String tooltipKey;

	// the resource key name for the label
	private String labelKey;

	// the name of the inactive image
	private String imageInactive;
	//whether to appear directly in the toolbar or in a more actions split button
	private boolean more = false;
	// whether to show this item or not
	//private boolean showItem = true;
	
	// whether this item is just a placeholder, not a real toolbar item
	//private boolean placeholder;
	
	//private List<ToolbarItem> children;
	
	/**
	 * @return the condition
	 */
	public boolean isCondition() {
		return condition;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(boolean condition) {
		this.condition = condition;
	}

	/**
	 * @return the cssClass
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * @param cssClass
	 *            the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * @return the imageInactive
	 */
	public String getImageInactive() {
		return imageInactive;
	}

	/**
	 * @param imageInactive
	 *            the imageInactive to set
	 */
	public void setImageInactive(String imageInactive) {
		this.imageInactive = imageInactive;
	}

	/**
	 * @return the labelKey
	 */
	public String getLabelKey() {
		return labelKey;
	}

	/**
	 * @param labelKey
	 *            the labelKey to set
	 */
	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}

	/**
	 * @return the tooltipKey
	 */
	public String getTooltipKey() {
		return tooltipKey;
	}

	/**
	 * @param tooltipKey
	 *            the tooltipKey to set
	 */
	public void setTooltipKey(String tooltipKey) {
		this.tooltipKey = tooltipKey;
	}
	/**
	 * @return the submit
	 */
	/*public boolean isSubmit() {
		return submit;
	}*/

	/**
	 * @param submit
	 *            the submit to set
	 */
	/*public void setSubmit(boolean submit) {
		this.submit = submit;
	}*/


	/**
	 * @return the showItem
	 */
	/*public boolean isShowItem() {
		return showItem;
	}*/

	/**
	 * @param showItem the showItem to set
	 */
	/*public void setShowItem(boolean showItem) {
		this.showItem = showItem;
	}*/

	/**
	 * @return the placeholder
	 */
	/*public boolean isPlaceholder() {
		return placeholder;
	}*/

	/**
	 * @param placeholder the placeholder to set
	 */
	/*public void setPlaceholder(boolean placeholder) {
		this.placeholder = placeholder;
	}*/

	/*public List<ToolbarItem> getChildren() {
		return children;
	}

	public void setChildren(List<ToolbarItem> children) {
		this.children = children;
	}*/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public boolean isMore() {
		return more;
	}

	public void setMore(boolean more) {
		this.more = more;
	}
	
	
}
