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

package com.aurel.track.item;

import com.aurel.track.util.IntegerStringBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bojani Adrian on 11/27/2015.
 */
public class ItemPath {
	private List<IntegerStringBean> projectPath;
	private List<IntegerStringBean> releasePath;
	private List<IntegerStringBean> itemPath;

	public ItemPath(){
	}
	public List<ItemPathElement> getFullPath(){
		List<ItemPathElement> fullPath=new ArrayList<ItemPathElement>();
		for(IntegerStringBean bean:projectPath){
			fullPath.add(new ItemPathElement(bean.getValue(),ItemPathElement.TYPE_PROJECT,bean.getLabel()));
		}
		if(releasePath!=null&&!releasePath.isEmpty()){
			for(IntegerStringBean bean:releasePath){
				fullPath.add(new ItemPathElement(bean.getValue(),ItemPathElement.TYPE_RELEASE,bean.getLabel()));
			}
		}
		for(IntegerStringBean bean:itemPath){
			fullPath.add(new ItemPathElement(bean.getValue(),ItemPathElement.TYPE_WORKITEM,bean.getLabel()));
		}
		return fullPath;
	}

	public List<IntegerStringBean> getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(List<IntegerStringBean> projectPath) {
		this.projectPath = projectPath;
	}

	public List<IntegerStringBean> getReleasePath() {
		return releasePath;
	}

	public void setReleasePath(List<IntegerStringBean> releasePath) {
		this.releasePath = releasePath;
	}

	public List<IntegerStringBean> getItemPath() {
		return itemPath;
	}

	public void setItemPath(List<IntegerStringBean> itemPath) {
		this.itemPath = itemPath;
	}
}
