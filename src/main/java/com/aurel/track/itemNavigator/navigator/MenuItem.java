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

package com.aurel.track.itemNavigator.navigator;

import java.io.Serializable;
import java.util.List;

public class MenuItem implements Serializable{
	
	private static final long serialVersionUID = 3519164659154276065L;
	
	private String id;
	private Integer objectID;
	private Integer projectID;
	private String type;
	private String name;
	private String icon;

	private String iconCls;
	private String url;
	private boolean useAJAX;

	private boolean allowDrop;
	private boolean verifyAllowDropAJAX;
	private boolean useFilter;
	private String filterViewID;
	private boolean lazyChildren;
	private boolean expanded;

	private String dropHandlerCls;

	private List<MenuItem> children;
	private int level=1;
	private boolean maySaveFilterLayout;
	private String cls;

	public MenuItem(){
	}
	public MenuItem(String type,Integer objectID,String name, String iconCls){
		this(type,objectID,name,iconCls,true,true);
	}
	public MenuItem(String type,Integer objectID,String name, String iconCls,boolean useFilter,boolean allowDrop){
		this(type,objectID, name, iconCls,useFilter,allowDrop,null);
	}
	public MenuItem(String type,Integer objectID,String name, String iconCls,boolean useFilter,boolean allowDrop,String id){
		this.type=type;
		this.objectID=objectID;
		this.name=name;
		this.iconCls=iconCls;
		this.allowDrop=allowDrop;
		this.useFilter=useFilter;
		this.id=id;
	}

	public Integer getObjectID() {
		return objectID;
	}
	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<MenuItem> getChildren() {
		return children;
	}
	public void setChildren(List<MenuItem> children) {
		this.children = children;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isUseAJAX() {
		return useAJAX;
	}
	public void setUseAJAX(boolean useAJAX) {
		this.useAJAX = useAJAX;
	}
	public boolean isAllowDrop() {
		return allowDrop;
	}

	public void setAllowDrop(boolean allowDrop) {
		this.allowDrop = allowDrop;
	}
	public boolean isUseFilter() {
		return useFilter;
	}
	public void setUseFilter(boolean useFilter) {
		this.useFilter = useFilter;
	}
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public boolean isLazyChildren() {
		return lazyChildren;
	}

	public void setLazyChildren(boolean lazyChildren) {
		this.lazyChildren = lazyChildren;
	}

	public String getDropHandlerCls() {
		return dropHandlerCls;
	}

	public void setDropHandlerCls(String dropHandlerCls) {
		this.dropHandlerCls = dropHandlerCls;
	}

	public boolean isVerifyAllowDropAJAX() {
		return verifyAllowDropAJAX;
	}

	public void setVerifyAllowDropAJAX(boolean verifyAllowDropAJAX) {
		this.verifyAllowDropAJAX = verifyAllowDropAJAX;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilterViewID() {
		return filterViewID;
	}

	public void setFilterViewID(String filterViewID) {
		this.filterViewID = filterViewID;
	}

	public boolean isMaySaveFilterLayout() {
		return maySaveFilterLayout;
	}

	public void setMaySaveFilterLayout(boolean maySaveFilterLayout) {
		this.maySaveFilterLayout = maySaveFilterLayout;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
}
