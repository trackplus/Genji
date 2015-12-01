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


package com.aurel.track.screen.dashboard.assign;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TDashboardScreenBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.DashboardScreenDAO;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.screen.dashboard.bl.design.DashboardScreenDesignBL;
import com.aurel.track.screen.item.bl.design.ScreenDesignBL;

public class DashboardAssignBL {
	private static final Logger LOGGER = LogManager.getLogger(DashboardAssignBL.class);
	private static DashboardScreenDAO dashboardScreenDAO = DAOFactory.getFactory().getDashboardScreenDAO();
	public static String resetProjectDashboard(Integer projectID,Integer entityType,Integer resetID){
		if(resetID!=null){
			TDashboardScreenBean dashboardScreenBean=DashboardAssignBL.loadByID(resetID);
			if(dashboardScreenBean!=null){
				TDashboardScreenBean originalScreen=dashboardScreenDAO.loadByProject(projectID, entityType);
				if(originalScreen!=null){
					DashboardScreenDesignBL.getInstance().deleteScreen(originalScreen.getObjectID());
				}
				DashboardScreenDesignBL.getInstance().copyProjectScreen(resetID,dashboardScreenBean.getName(),dashboardScreenBean.getDescription(),projectID,entityType);
			}
		}
		return null;
	}
	public static String resetDashboard(TPersonBean user,Integer resetID){
		LOGGER.debug("reset dashboard for user "+user.getObjectID()+":"+user.getFullName()+" with:"+resetID);
		if(resetID!=null){
			TDashboardScreenBean dashboardScreenBean=DashboardAssignBL.loadByID(resetID);
			if(dashboardScreenBean!=null){
				LOGGER.debug("rest dashboard found:"+dashboardScreenBean.getLabel());
				TDashboardScreenBean originalScreen= DashboardScreenDesignBL.getInstance().loadByPersonNotCreateDefault(user.getObjectID());
				if(originalScreen!=null){
					DashboardScreenDesignBL.getInstance().deleteScreen(originalScreen.getObjectID());
				}
				DashboardScreenDesignBL.getInstance().copyScreen(resetID,dashboardScreenBean.getName()+" ("+user.getUsername()+")",dashboardScreenBean.getDescription(),null,user.getObjectID());
			}
		}
		return null;
	}

	public static List<TPersonBean> getSettablePersons(Integer personID) {
		TPersonBean personBean = LookupContainer.getPersonBean(personID);
		if (personBean!=null && personBean.isSys()) {
			return PersonBL.loadPersons(); 
		} else {
			List personsList = new ArrayList();
			List projectList = ProjectBL.getAllNotClosedAdminProjectBeansFlat(personBean, true);
			if (projectList==null || projectList.isEmpty()) {
				//no project admin
				return personsList;
			}
			Integer[] projectIDs = new Integer[projectList.size()];
			for (int i = 0; i < projectIDs.length; i++) {
				TProjectBean projectBean = (TProjectBean)projectList.get(i);
				projectIDs[i] = projectBean.getObjectID();
			}
			int[] arrRights = new int[] { AccessFlagIndexes.READANYTASK, AccessFlagIndexes.PROJECTADMIN };
			Set<Integer> personIDs = AccessBeans.getPersonSetByProjectsRights(projectIDs, arrRights);
			return PersonBL.getPersonsByCategory(personIDs, TPersonBean.PERSON_CATEGORY.ALL_PERSONS, false, false, null);
		}
	}
	
	public static TPersonBean loadPersonByKey(Integer personID) {
		return PersonBL.loadByPrimaryKey(personID);
	}
	
	/**
	 * Load all cockpit templates (not assigned to user or project/release)
	 * @return
	 */
	public static List<TDashboardScreenBean> loadNotAssigned(){
		return dashboardScreenDAO.loadAllTemplates();
	}
	
	/**
	 * Load the public and user owned cockpit templates (not assigned to user or project/release)
	 * @param personIDs
	 * @return
	 */
	public static List<TDashboardScreenBean> loadMyAndPublicTemplates(List<Integer> personIDs) {
		return dashboardScreenDAO.loadMyAndPublicTemplates(personIDs);
	}
	
	public static Integer assign(Integer screenID,Integer personID){
		return ScreenDesignBL.getInstance().copyScreen(screenID,null,null,null,personID);
	}
	public static TDashboardScreenBean loadByID(Integer objectID){
		return dashboardScreenDAO.loadByPrimaryKey(objectID);
	}
}
