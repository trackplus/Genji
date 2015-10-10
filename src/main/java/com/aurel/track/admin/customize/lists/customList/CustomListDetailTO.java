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

package com.aurel.track.admin.customize.lists.customList;

import com.aurel.track.admin.customize.lists.DetailBaseTO;

/**
 * Transfer object for a system list option
 * @author Tamas
 *
 */
public class CustomListDetailTO extends DetailBaseTO {	
	//private List<IntegerStringBean> customListTypesList;	
	//private Integer cascadingType;
	//private List<IntegerStringBean> cascadingTypesList;	
	//private Integer repositoryType;
	//private List<IntegerStringBean> repositoryTypesList;
	//private Integer project;
	//private List<ILabelBean> projectsList;
	private String description;
	
	/*public List<IntegerStringBean> getCustomListTypesList() {
		return customListTypesList;
	}
	public void setCustomListTypesList(List<IntegerStringBean> customListTypesList) {
		this.customListTypesList = customListTypesList;
	}
	public List<IntegerStringBean> getCascadingTypesList() {
		return cascadingTypesList;
	}
	public void setCascadingTypesList(List<IntegerStringBean> cascadingTypesList) {
		this.cascadingTypesList = cascadingTypesList;
	}*/
	/*public List<IntegerStringBean> getRepositoryTypesList() {
		return repositoryTypesList;
	}
	public void setRepositoryTypesList(List<IntegerStringBean> repositoryTypesList) {
		this.repositoryTypesList = repositoryTypesList;
	}*/
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
	
}
