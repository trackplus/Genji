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

package com.aurel.track.browseProjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TDashboardScreenBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.util.IntegerStringBean;

public class CopyProjectDashboardBL {
	public static List<IntegerStringBean> getCopyFrom(Integer userID,Integer projectID,Integer entityType){
		List<IntegerStringBean>  result=new ArrayList<IntegerStringBean>();
		
		List<TProjectBean> projects = ProjectBL.loadUsedProjectsFlat(userID);
		if(projects!=null){
			int[] projectsIntArray=new int[projects.size()];
			for (int i = 0; i < projectsIntArray.length; i++) {
				projectsIntArray[i]=projects.get(i).getObjectID().intValue();
			}
			
			List<TDashboardScreenBean> screens =DAOFactory.getFactory().getDashboardScreenDAO().loadByProjects(projectsIntArray);
			
			if(screens!=null){
				IntegerStringBean integerStringBean;
				TDashboardScreenBean dashboardScreenBean;
				Integer projectIdIndex;
				Integer entityTypeIndex;
				TProjectBean projectIndex;
				for (int i = 0; i < screens.size(); i++) {
					dashboardScreenBean=screens.get(i);
					projectIdIndex=dashboardScreenBean.getProject();
					if(projectIdIndex.intValue()==projectID.intValue()){
						continue;
					}
					projectIndex=findProject(projectIdIndex, projects);
					entityTypeIndex=dashboardScreenBean.getEntityType();
					integerStringBean=new IntegerStringBean();
					integerStringBean.setValue(dashboardScreenBean.getObjectID());
					if(entityTypeIndex!=null&&entityTypeIndex.intValue()==SystemFields.RELEASESCHEDULED){
						//release
						integerStringBean.setLabel(projectIndex.getLabel()+" Release");
					}else{
						integerStringBean.setLabel(projectIndex.getLabel());
					}
					result.add(integerStringBean);
				}
			}
		}
		
		Collections.sort(result);
		return result;
	}
	private static TProjectBean findProject(Integer projectID,List<TProjectBean> projects){
		for (int i = 0; i < projects.size(); i++) {
			if(projects.get(i).getObjectID().intValue()==projectID.intValue()){
				return projects.get(i);
			}
		}
		return null;
	}
}
