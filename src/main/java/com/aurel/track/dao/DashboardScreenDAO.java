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

package com.aurel.track.dao;

import java.util.List;

import com.aurel.track.beans.TDashboardScreenBean;

/**
 *
 */
public interface DashboardScreenDAO {
    /**
	 * Gets a screenBean from the TDashboardScreen table
	 * @param objectID
	 * @return
	 */
	TDashboardScreenBean loadByPrimaryKey(Integer objectID);
	
    /**
	 * Gets a screenBean from the TDashboardScreen table
	 * @param objectID
	 * @return
	 */
	TDashboardScreenBean tryToLoadByPrimaryKey(Integer objectID);

    /**
	 * Gets the screenBean from the TDashboardScreen table by person
	 * @param objectID
	 * @return
	 */
	TDashboardScreenBean loadByPersonKey(Integer objectID);
	
	/**
	 * Gets the screenBean from the TDashboardScreen table by project and entityType
	 * @param projectID
	 * @param entityType
	 * @return
	 */
	TDashboardScreenBean loadByProject(Integer projectID,Integer entityType);
	
	List<TDashboardScreenBean> loadByProjects(int[] projects);
	
	
	/**
	 * Gets the default screenBean from the TDashboardScreen table
	 * (that with personID null) 
	 * @return
	 */
	TDashboardScreenBean loadDefault();

	/**
	 * Load all cockpit templates (not assigned to user or project/release)
	 * @return
	 */
	List<TDashboardScreenBean> loadAllTemplates();

	/**
	 * Load the public and user owned cockpit templates (not assigned to user or project/release)
	 * @param personIDs
	 * @return
	 */
	List<TDashboardScreenBean> loadMyAndPublicTemplates(List<Integer> personIDs);
	
    /**
	 * Save  screen in the TDashboardScreenBean
	 * @param screen
	 * @return
	 */
	Integer save(TDashboardScreenBean screen);

	/**
	 * Deletes a screen from the TDashboardScreen table 
	 * @param objectID
	 */
	void delete(Integer objectID);
}
