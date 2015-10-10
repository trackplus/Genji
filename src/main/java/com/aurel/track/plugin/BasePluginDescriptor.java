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

package com.aurel.track.plugin;
/**
 * Base class for plugin descriptors 
 * @author Tamas
 *
 */
public class BasePluginDescriptor implements PluginDescriptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String id;
	protected String name;
	protected String description;
	protected String jsClass;
	protected String jsConfigClass;
	protected String bundleName;
	protected String theClassName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getJsClass() {
		return jsClass;
	}

	public void setJsClass(String jsClass) {
		this.jsClass = jsClass;
	}

	public String getJsConfigClass() {
		return jsConfigClass;
	}
	public void setJsConfigClass(String pageConfig) {
		this.jsConfigClass = pageConfig;
	}
	public String getBundleName() {
		return bundleName;
	}
	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
	public String getTheClassName() {
		return theClassName;
	}
	public void setTheClassName(String theClassName) {
		this.theClassName = theClassName;
	}

}